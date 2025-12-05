
document.querySelectorAll('tr').forEach(row => {
    const previewCanvas = row.querySelector('.drawsPreview');
    const hiddenInput = row.querySelector('.shapesPreview');

    if (!previewCanvas || !hiddenInput) return;

    let shapes;
    try {
        shapes = JSON.parse(hiddenInput.value);
    } catch (e) {
        console.error("Error parsing shapesPreview JSON", e, hiddenInput.value);
        return;
    }

    drawPreview(shapes, previewCanvas);
});



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

    const originalWidth = maxX - minX || 1;
    const originalHeight = maxY - minY || 1;


    const scaleX = previewCanvas.width / originalWidth;
    const scaleY = previewCanvas.height / originalHeight;
    const scale = Math.min(scaleX, scaleY);

    shapes.forEach(shape => {
        const drawX = (shape.x - minX) * scale;
        const drawY = (shape.y - minY) * scale;
        const drawSize = (shape.size || 0) * scale;
        const drawStroke = (shape.stroke || 2) * scale;

        switch (shape.tipo) {
            case 'square':
                if (shape.filled) {
                    ctx.fillStyle = shape.color;
                    ctx.fillRect(drawX - drawSize / 2, drawY - drawSize / 2, drawSize, drawSize);
                }
                ctx.strokeStyle = shape.color;
                ctx.lineWidth = drawStroke;
                ctx.strokeRect(drawX - drawSize / 2, drawY - drawSize / 2, drawSize, drawSize);
                break;
            case 'circle':
                ctx.beginPath();
                ctx.arc(drawX, drawY, drawSize, 0, Math.PI * 2);
                if (shape.filled) ctx.fillStyle = shape.color, ctx.fill();
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
                if (shape.filled) ctx.fillStyle = shape.color, ctx.fill();
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
                if (shape.filled) ctx.fillStyle = shape.color, ctx.fill();
                ctx.strokeStyle = shape.color;
                ctx.lineWidth = drawStroke;
                ctx.stroke();
                break;
            case 'pencil':
                if (!shape.drawLine || shape.drawLine.length < 2) break;
                ctx.beginPath();
                ctx.strokeStyle = shape.color;
                ctx.lineWidth = drawStroke / 2;
                ctx.moveTo((shape.drawLine[0].x - minX) * scale, (shape.drawLine[0].y - minY) * scale);
                for (let i = 1; i < shape.drawLine.length; i++) {
                    ctx.lineTo((shape.drawLine[i].x - minX) * scale, (shape.drawLine[i].y - minY) * scale);
                }
                ctx.stroke();
                break;
        }
    });
}
