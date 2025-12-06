function loadAllPreviews() {
    // Seleccionar todos los canvas que pueden contener previews
    const allPreviewCanvases = [
        ...document.querySelectorAll('.draw-preview'),     // sistema antiguo
        ...document.querySelectorAll('.version-canvas')    // sistema nuevo
    ];

    allPreviewCanvases.forEach(canvas => {
        let shapesData = null;

        //
        // 1) SISTEMA ANTIGUO
        //
        if (canvas.classList.contains('draw-preview')) {
            shapesData = canvas.dataset.shapes;

            if (!shapesData) {
                const hiddenInput = canvas.parentElement.querySelector('.shapesPreview');
                shapesData = hiddenInput?.dataset.shapes || null;
            }
        }

        //
        // 2) SISTEMA NUEVO: versión en <th class="version-item">
        //
        if (!shapesData && canvas.classList.contains('version-canvas')) {
            const parentItem = canvas.closest('.version-item');
            shapesData = parentItem?.dataset.shapes || null;
        }

        if (!shapesData) return;

        let shapes;
        try {
            shapes = JSON.parse(shapesData);
        } catch (e) {
            console.error("Error parsing shapes JSON", e, shapesData);
            return;
        }

        drawPreview(shapes, canvas);
    });
}

loadAllPreviews(); // Ejecutar al cargar



/*********************************************************************
 *              FUNCIÓN drawPreview (idéntica a la tuya)
 *********************************************************************/
function drawPreview(shapes, previewCanvas) {
    const ctx = previewCanvas.getContext('2d');
    ctx.clearRect(0, 0, previewCanvas.width, previewCanvas.height);

    if (!shapes || shapes.length === 0) return;

    let minX = Infinity, minY = Infinity, maxX = 0, maxY = 0;

    shapes.forEach(shape => {
        switch (shape.tipo) {
            case 'square':
            case 'circle':
            case 'triangle':
            case 'star':
                minX = Math.min(minX, shape.x - shape.size);
                minY = Math.min(minY, shape.y - shape.size);
                maxX = Math.max(maxX, shape.x + shape.size);
                maxY = Math.max(maxY, shape.y + shape.size);
                break;

            case 'pencil':
                shape.drawLine.forEach(p => {
                    minX = Math.min(minX, p.x);
                    minY = Math.min(minY, p.y);
                    maxX = Math.max(maxX, p.x);
                    maxY = Math.max(maxY, p.y);
                });
                break;
        }
    });

    const margin = 10;
    minX -= margin;
    minY -= margin;
    maxX += margin;
    maxY += margin;

    const originalWidth = maxX - minX || 1;
    const originalHeight = maxY - minY || 1;

    const scaleX = previewCanvas.width / originalWidth;
    const scaleY = previewCanvas.height / originalHeight;
    const scale = Math.min(scaleX, scaleY);

    ctx.lineCap = 'round';
    ctx.lineJoin = 'round';

    shapes.forEach(shape => {
        const drawX = (shape.x - minX) * scale;
        const drawY = (shape.y - minY) * scale;
        const drawSize = (shape.size || 0) * scale;
        const drawStroke = Math.max(1, (shape.stroke || 2) * scale);

        switch (shape.tipo) {
            case 'square':
                const half = drawSize / 2;

                if (shape.filled) {
                    ctx.fillStyle = shape.color;
                    ctx.fillRect(drawX - half, drawY - half, drawSize, drawSize);
                }

                ctx.strokeStyle = shape.color;
                ctx.lineWidth = drawStroke;
                ctx.beginPath();
                ctx.roundRect(
                    drawX - half,
                    drawY - half,
                    drawSize,
                    drawSize,
                    Math.min(drawStroke * 2, drawSize * 0.1)
                );
                ctx.stroke();
                break;

            case 'circle':
                ctx.beginPath();
                ctx.arc(drawX, drawY, drawSize, 0, Math.PI * 2);

                if (shape.filled) {
                    ctx.fillStyle = shape.color;
                    ctx.fill();
                }

                ctx.strokeStyle = shape.color;
                ctx.lineWidth = drawStroke;
                ctx.stroke();
                break;

            case 'triangle':
                const altura = drawSize * Math.sqrt(3) / 2;

                ctx.beginPath();
                ctx.moveTo(drawX, drawY - 2 * altura / 3);
                ctx.lineTo(drawX - drawSize / 2, drawY + altura / 3);
                ctx.lineTo(drawX + drawSize / 2, drawY + altura / 3);
                ctx.closePath();

                if (shape.filled) {
                    ctx.fillStyle = shape.color;
                    ctx.fill();
                }

                ctx.strokeStyle = shape.color;
                ctx.lineWidth = drawStroke;
                ctx.stroke();
                break;

            case 'star':
                const numPuntas = 7;
                const R = drawSize;
                const r = R * 0.4;

                ctx.beginPath();
                for (let i = 0; i < numPuntas; i++) {
                    let angleR = i * (2 * Math.PI / numPuntas) - Math.PI / 2;
                    let pxR = drawX + R * Math.cos(angleR);
                    let pyR = drawY + R * Math.sin(angleR);

                    if (i === 0) ctx.moveTo(pxR, pyR);
                    else ctx.lineTo(pxR, pyR);

                    let angle_r = (i + 0.5) * (2 * Math.PI / numPuntas) - Math.PI / 2;
                    let px_r = drawX + r * Math.cos(angle_r);
                    let py_r = drawY + r * Math.sin(angle_r);
                    ctx.lineTo(px_r, py_r);
                }

                ctx.closePath();

                if (shape.filled) {
                    ctx.fillStyle = shape.color;
                    ctx.fill();
                }

                ctx.strokeStyle = shape.color;
                ctx.lineWidth = drawStroke;
                ctx.stroke();
                break;

            case 'pencil':
                if (!shape.drawLine || shape.drawLine.length < 2) break;

                ctx.beginPath();
                ctx.strokeStyle = shape.color;
                ctx.lineWidth = Math.max(1, drawStroke / 2);

                ctx.moveTo(
                    (shape.drawLine[0].x - minX) * scale,
                    (shape.drawLine[0].y - minY) * scale
                );

                for (let i = 1; i < shape.drawLine.length; i++) {
                    ctx.lineTo(
                        (shape.drawLine[i].x - minX) * scale,
                        (shape.drawLine[i].y - minY) * scale
                    );
                }
                ctx.stroke();
                break;
        }
    });
}
