

const color = document.querySelector('#color');
const nameShape = document.getElementsByName('nameDraw');
const shape = document.querySelector('#shape');
const canvas = document.querySelector('#lienzo');
const ctx = canvas.getContext('2d');
const size = document.querySelector('#size');
const fill = document.querySelector('#fill');
const notForPencil = document.querySelectorAll('.notForPencil');
const clearDraw = document.querySelector('#clearDraw');
const strokeShape = document.querySelector('#stroke');
const user = document.querySelector('#user');
const log = document.querySelector('#log');
const logShape = document.querySelectorAll('.logsDraw');
const saveDrawButton = document.querySelector('#buttonSaveDraw');
const nameDraw = document.querySelector('#nameDraw');
const back = document.querySelector("#comeback");
const forward = document.querySelector("#forward");

const keyToShapeValue = {
    'a': 'touch',
    's': 'pencil',
    'd': 'square',
    'f': 'circle',
    'g': 'triangle',
    'h': 'star'
};

document.addEventListener('keydown', (event) => {
    const key = event.key.toLowerCase();
    if (!keyToShapeValue[key]) return;

    const newShape = keyToShapeValue[key];
    shape.value = newShape;
    changePen();
});
canvas.width = canvas.offsetWidth;
canvas.height = canvas.offsetHeight;
let drawId = null;
let shapes = [];
let isDrawing = false;
let drawLine = [];
let comeBackShapes = [];
let dateLocal = new Date();

color.addEventListener("input", e => {
    if (selectedShape) {
        selectedShape.color = e.target.value;
        redrawAll();
    }
});


size.addEventListener("input", e => {
    if (selectedShape) {
        selectedShape.size = parseInt(e.target.value*2);
        redrawAll();
    }
});


fill.addEventListener("change", e => {
    if (selectedShape) {
        selectedShape.filled = e.target.checked;
        redrawAll();
    }
});


strokeShape.addEventListener("input", e => {
    if (selectedShape) {
        selectedShape.stroke = parseInt(e.target.value);
        redrawAll();
    }
});

function loadShapeConfig(shape) {
    color.value = shape.color;
    size.value = shape.size;
    fill.checked = !!shape.filled;
    strokeShape.value = shape.stroke;
}

function configuration(color, shape, size, fill, stroke) {

    const configurationSave = {
        colorS: color,
        shapeS: shape,
        sizeS: size,
        fillS: fill,
        strokeS: stroke
    }
    return configurationSave;

}
//deshacer y rehacer inicio
forward.addEventListener('click', () => {
    if (comeBackShapes.length === 0) return;
    let lastComeBack = comeBackShapes.pop();
    console.log(lastComeBack);
    shapes.push(lastComeBack);
    redrawAll();
    saveShapes();
});
back.addEventListener('click', () => {

    if (shapes.length !== 0) {
        let lastElement = shapes.pop();
        comeBackShapes.push(lastElement);
        redrawAll();
        saveShapes();
    }

});

document.addEventListener('keydown', (event) => {
    if (event.ctrlKey && event.key.toLowerCase() === 'z') {
        event.preventDefault();
        back.click();
    }
    if (event.ctrlKey && event.key.toLowerCase() === 'y') {
        event.preventDefault();
        forward.click();
    }
});

//deshacer y rehacer final


log.addEventListener('click', (e) => {
    if (e.target.classList.contains('logsDraw') || e.target.closest('.logsDraw')) {
        e.stopPropagation();
        const logDiv = e.target.closest('.logsDraw');
        const id = logDiv.id.replace('shape', '');
        const index = parseInt(id);

        shapes.splice(index, 1);
        saveShapes();
        redrawAll();
    }
});

function saveShapes() {

    const keyBase = (drawId)
        ? `canvas-shapes-${user.textContent}-${drawId}`
        : `canvas-shapes-${user.textContent}-draft`;
    console.log(drawId);
    localStorage.setItem(`${keyBase}`, JSON.stringify(shapes));
    localStorage.setItem(`${keyBase}-returnShapes`, JSON.stringify(comeBackShapes));
    localStorage.setItem(`${keyBase}-returnConfiguration`,
        JSON.stringify(configuration(color.value, shape.value, size.value, fill.checked, strokeShape.value)));
    localStorage.setItem(`${keyBase}-returnDate`, JSON.stringify(dateLocal));
}


function getLog(id, typeShape, color) {
    log.innerHTML += `<div class="logsDraw" id="shape${id - 1}">Num: ${id} ${typeShape}<span class="color-box" style="background:${color};"></span></div>`
}
function loadShapes() {
    const keyBase = (drawId)
        ? `canvas-shapes-${user.textContent}-${drawId}`
        : `canvas-shapes-${user.textContent}-draft`;
    const saved = localStorage.getItem(`${keyBase}`);
    const savedReturn = localStorage.getItem(`${keyBase}-returnShapes`);
    const configurationSavedJson = localStorage.getItem(`${keyBase}-returnConfiguration`);
    const configurationSaved = JSON.parse(configurationSavedJson)
    if (saved) {
        shapes = JSON.parse(saved);
        comeBackShapes = JSON.parse(savedReturn);
        color.value = configurationSaved.colorS;
        shape.value = configurationSaved.shapeS;
        size.value = configurationSaved.sizeS;
        fill.checked = configurationSaved.fillS;
        strokeShape.value = configurationSaved.strokeS;
        redrawAll();

    }
}

function redrawAll() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    log.innerHTML = '';
    shapes.forEach((shape, index) => {
        switch (shape.tipo) {
            case 'square':
                drawSquare(shape.x, shape.y, shape.color, shape.size, shape.filled, shape.stroke);
                break;
            case 'circle':
                drawCircle(shape.x, shape.y, shape.color, shape.size, shape.filled, shape.stroke);
                break;
            case 'triangle':
                drawTriangle(shape.x, shape.y, shape.color, shape.size, shape.filled, shape.stroke);
                break;
            case 'pencil':
                drawPencil(shape.drawLine, shape.color, shape.stroke);
                break;
            case 'star':
                drawStar(shape.x, shape.y, shape.color, shape.size, shape.filled, shape.stroke);
                break;
        }

        if (shape.tipo !== 'touch') getLog(index + 1, shape.tipo, shape.color);
        if (selectedShape === shape) {
            drawSelectionOutline(shape);
        }
    });
}
window.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    drawId = params.get('id');
    const drawName = params.get('drawName');
    const dateServer = params.get('dateServer');
    const keyBase = (drawId)
        ? `canvas-shapes-${user.textContent}-${drawId}`
        : `canvas-shapes-${user.textContent}-draft`;
    const dateLocal = JSON.parse(localStorage.getItem(`${keyBase}-returnDate`));
    const dateLocalFormat = parseCustomDate(dateLocal);
    const widthServ = params.get('width');
    const heightServ = params.get('height');
    if (!drawId) {
        askCanvasSize();
    } else {
        if (widthServ) canvas.width = parseInt(widthServ);
        if (heightServ) canvas.height = parseInt(heightServ);
        log.style.height = heightServ + "px";

    }

    if (drawId && dateServer && (!dateLocalFormat || dateServer > dateLocalFormat)) {
        fetch(`/getdraw?id=${drawId}&drawName=${drawName}&dateServer=${dateServer}$width=${widthServ}$height=${heightServ}`)
            .then(resp => {
                if (!resp.ok) throw new Error(`Error loading the draw: ${resp.status}`);
                return resp.text();
            })
            .then(resp => {

                try {
                    shapes = JSON.parse(resp);
                    redrawAll();

                    const nameFromUrl = params.get('drawName');
                    nameDrawBack(nameFromUrl);
                } catch (e) {
                    console.error(" Error caching the shapes", e);
                }
            })
            .catch(err => {
                console.error(" Error loading the draw from the server:", err);
            });
    } else {
        const nameFromUrl = params.get('drawName');
        nameDrawBack(nameFromUrl);
        loadShapes();
    }
});

function nameDrawBack(nameDrawB) {
    if (nameDrawB) return nameDraw.value = nameDrawB;
}
function drawPencil(drawLine, color, stroke) {
    if (!drawLine || drawLine.length < 1) {
        return;
    }
    ctx.strokeStyle = color;
    ctx.lineWidth = stroke / 2 || 1;
    ctx.lineCap = 'round';
    ctx.lineJoin = 'round';

    ctx.beginPath();
    ctx.moveTo(drawLine[0].x, drawLine[0].y);

    for (let i = 1; i < drawLine.length; i++) {
        ctx.lineTo(drawLine[i].x, drawLine[i].y);
    }

    ctx.stroke();
    ctx.closePath();
}
window.addEventListener('DOMContentLoaded', () => changePen());
shape.addEventListener('change', () => changePen());
function changePen() {
    if (shape.value === 'pencil') {

        notForPencil.forEach(element => {
            element.style.display = 'none';

        });
    } else {
        notForPencil.forEach(element => {
            element.style.display = 'inline-block';
        });
    }
    const keyBase = (drawId)
        ? `canvas-shapes-${user.textContent}-${drawId}`
        : `canvas-shapes-${user.textContent}-draft`;
    localStorage.setItem(`${keyBase}-returnConfiguration`,
        JSON.stringify(configuration(color.value, shape.value, size.value, fill.checked, strokeShape.value)));
};


clearDraw.addEventListener('click', () => { clearAll() });

function clearAll() {
    const keyBase = (drawId)
        ? `canvas-shapes-${user.textContent}-${drawId}`
        : `canvas-shapes-${user.textContent}-draft`;
    shapes = [];
    localStorage.removeItem(`${keyBase}`);
    localStorage.removeItem(`${keyBase}-returnShapes`);
    localStorage.removeItem(`${keyBase}-returnConfiguration`);
    localStorage.removeItem(`${keyBase}-returnDate`);
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    log.innerHTML = '';
    comeBackShapes = [];

};

function drawStar(x, y, colorShape, sizeValue, isFilled, sizeStroke) {
    const numPuntas = 7;
    const R = sizeValue;
    const r = R * 0.4;

    ctx.beginPath();
    ctx.strokeStyle = colorShape;
    ctx.lineWidth = parseInt(sizeStroke) || 2;
    ctx.lineCap = 'round';
    ctx.lineJoin = 'round';

    for (let i = 0; i < numPuntas; i++) {

        let angleR = i * (2 * Math.PI / numPuntas) - (Math.PI / 2);
        let pxR = x + R * Math.cos(angleR);
        let pyR = y + R * Math.sin(angleR);

        if (i === 0) {
            ctx.moveTo(pxR, pyR);
        } else {
            ctx.lineTo(pxR, pyR);
        }
        let angle_r = (i + 0.5) * (2 * Math.PI / numPuntas) - (Math.PI / 2);
        let px_r = x + r * Math.cos(angle_r);
        let py_r = y + r * Math.sin(angle_r);

        ctx.lineTo(px_r, py_r);
    }

    ctx.closePath();

    if (isFilled) {
        ctx.fillStyle = colorShape;
        ctx.fill();
    }

    ctx.stroke();
}

function drawTriangle(x, y, colorShape, sizeValue, isFilled, stroke) {
    const altura = sizeValue * Math.sqrt(3) / 2;
    const x1 = x;
    const y1 = y - (2 * altura / 3);
    const x2 = x - sizeValue / 2;
    const y2 = y + (altura / 3);
    const x3 = x + sizeValue / 2;
    const y3 = y + (altura / 3);
    ctx.beginPath();
    ctx.lineCap = 'round';
    ctx.lineJoin = 'round';
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.lineTo(x3, y3);
    ctx.closePath();
    if (isFilled) {
        ctx.fillStyle = colorShape;
        ctx.fill();
    }
    ctx.strokeStyle = colorShape;
    ctx.lineWidth = parseInt(stroke) || 2;
    ctx.stroke();

}

function drawSquare(x, y, colorShape, sizeValue, isfilled, stroke) {
    const halfSize = sizeValue / 2;
    if (isfilled) {
        ctx.fillStyle = colorShape;
        ctx.fillRect(x - halfSize, y - halfSize, sizeValue, sizeValue);
    }
    ctx.lineCap = 'round';
    ctx.lineJoin = 'round';
    ctx.strokeStyle = colorShape;
    ctx.lineWidth = parseInt(stroke) || 2;
    ctx.strokeRect(x - halfSize, y - halfSize, sizeValue, sizeValue);

}

function drawCircle(x, y, colorShape, sizeValue, isFilled, stroke) {
    ctx.beginPath();
    ctx.arc(x, y, sizeValue, 0, 2 * Math.PI);
    if (isFilled) {
        ctx.fillStyle = colorShape;
        ctx.fill();
    }
    ctx.strokeStyle = colorShape;
    ctx.lineWidth = stroke;
    ctx.stroke();

}
// group of functions for manage the pencil "shape".
canvas.addEventListener('mousedown', (event) => {
    if (shape.value !== 'pencil') return;
    const typeShape = shape.value;
    drawLine = [];
    if (typeShape === 'pencil') {
        isDrawing = true;
        const rect = canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        const colorShape = color.value;
        const sizeStroke = parseInt(strokeShape.value);
        ctx.strokeStyle = colorShape;
        ctx.lineWidth = sizeStroke / 2 || 1;
        ctx.lineCap = 'round';
        ctx.lineJoin = 'round';
        ctx.beginPath();
        ctx.moveTo(x, y);
    }

});

canvas.addEventListener('mousemove', (event) => {
    if (shape.value !== 'pencil') return;
    if (!isDrawing) return;
    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    drawLine.push({ x, y });
    ctx.lineTo(x, y);
    ctx.stroke();

});

canvas.addEventListener('mouseup', () => {
    if (isDrawing) {
        isDrawing = false;
        ctx.closePath();
        const typeShape = shape.value;
        const colorShape = color.value;
        const sizeStroke = strokeShape.value || 2;
        shapes.push({
            tipo: typeShape,
            drawLine: drawLine,
            color: colorShape,
            stroke: sizeStroke,
            filled: false
        });
        comeBackShapes = [];
        drawLine = [];
        saveShapes();
        getLog(shapes.length, typeShape, colorShape);

    }
});
// end of the group of pencil's manage.

//Select type of the shape
canvas.addEventListener('click', (event) => {
    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    const typeShape = shape.value;
    const colorShape = color.value;
    const sizeValue = parseInt(size.value) * 2;
    const isFilled = fill.checked;
    const sizeStroke = strokeShape.value;

    if (typeShape !== 'pencil' && typeShape !== 'touch') {
        shapes.push({
            tipo: typeShape,
            x: x,
            y: y,
            size: sizeValue,
            color: colorShape,
            stroke: sizeStroke,
            filled: isFilled
        });
        comeBackShapes = [];
        saveShapes();
        getLog(shapes.length, typeShape, colorShape,);
    }
    switch (typeShape) {
        case 'square':
            drawSquare(x, y, colorShape, sizeValue, isFilled, sizeStroke);
            break;
        case 'circle':
            drawCircle(x, y, colorShape, sizeValue, isFilled, sizeStroke);
            break;
        case 'triangle':
            drawTriangle(x, y, colorShape, sizeValue, isFilled, sizeStroke);
            break;
        case 'star':
            drawStar(x, y, colorShape, sizeValue, isFilled, sizeStroke);
            break;

    }
});

if (saveDrawButton) {
    saveDrawButton.addEventListener('click', saveShapesToServer);
}

document.getElementById('buttonSaveDraw').addEventListener('click', saveShapesToServer);

function saveShapesToServer() {

    const bodyShapes = JSON.stringify(shapes);
    const drawName = nameDraw.value.trim() || nombreAleatorio();
    document.getElementById('bodyShapesInput').value = bodyShapes;
    document.getElementById('userinput').value = user.textContent.trim();
    const canvasSize = { width: canvas.width, height: canvas.height };
    document.getElementById('canvasSizeInput').value = JSON.stringify(canvasSize);
    nameDraw.value = drawName;
    nameDraw.value = drawName;
    clearAll();



}

function nombreAleatorio() {
    const fruta = [
        'banana', 'pera', 'manzana', 'kiwi', 'pomelo', 'sandia', 'melon', 'cereza',
        'naranja', 'uva', 'limón', 'fresa', 'mango', 'arandano',
        'coco', 'piña', 'aguacate', 'frambuesa', 'ciruela', 'mandarina'
    ];
    const adjetivo = [
        'fugaz', 'radiactivo', 'peludo', 'limpio', 'feroz', 'agresivo', 'tenaz', 'veloz',
        'brillante', 'misterioso', 'dulce', 'oscuro', 'silvestre', 'gigante',
        'diminuto', 'transparente', 'silencioso', 'soñador', 'antiguo', 'moderno',
        'fresco', 'salada', 'audaz', 'juguetón'
    ];
    const indexFruta = Math.floor(Math.random() * fruta.length);
    const indexadjetivo = Math.floor(Math.random() * adjetivo.length);
    let nameRandom = fruta[indexFruta] + ' ' + adjetivo[indexadjetivo];
    return nameRandom;
}


// ======== MODO TOUCH (selección y movimiento de figuras) ========

let selectedShape = null;
let offsetX = 0;
let offsetY = 0;

canvas.addEventListener('mousedown', (event) => {
    if (shape.value !== 'touch') return;

    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;

    for (let i = shapes.length - 1; i >= 0; i--) {
        const s = shapes[i];
        if (isPointInShape(s, x, y)) {
            selectedShape = s;
            loadShapeConfig(selectedShape);

            if (s.tipo === 'pencil') {
                offsetX = x;
                offsetY = y;
            } else {
                offsetX = x - s.x;
                offsetY = y - s.y;
            }

            isDragging = true;
            redrawAll();
            return;
        }
    }

    selectedShape = null;
    redrawAll();
});

canvas.addEventListener('mousemove', (event) => {
    if (shape.value !== 'touch' || !isDragging) return;

    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;

    if (selectedShape.tipo === 'pencil') {
        const dx = x - offsetX;
        const dy = y - offsetY;
        selectedShape.drawLine = selectedShape.drawLine.map(p => ({
            x: p.x + dx,
            y: p.y + dy
        }));
        offsetX = x;
        offsetY = y;
        redrawAll();
        return;
    }

    selectedShape.x = x - offsetX;
    selectedShape.y = y - offsetY;

    redrawAll();
});

canvas.addEventListener('mouseup', () => {
    if (shape.value !== 'touch') return;
    if (isDragging) {
        saveShapes();
        isDragging = false;
    }
});

// ======== Función de detección de punto dentro de figura ========

function isPointInShape(shape, x, y) {
    ctx.save();
    ctx.beginPath();

    switch (shape.tipo) {
        case 'square':
            ctx.rect(
                shape.x - shape.size / 2,
                shape.y - shape.size / 2,
                shape.size,
                shape.size
            );
            break;
        case 'circle':
            ctx.arc(shape.x, shape.y, shape.size, 0, 2 * Math.PI);
            break;
        case 'triangle': {
            const altura = shape.size * Math.sqrt(3) / 2;
            const x1 = shape.x;
            const y1 = shape.y - (2 * altura / 3);
            const x2 = shape.x - shape.size / 2;
            const y2 = shape.y + (altura / 3);
            const x3 = shape.x + shape.size / 2;
            const y3 = shape.y + (altura / 3);
            ctx.moveTo(x1, y1);
            ctx.lineTo(x2, y2);
            ctx.lineTo(x3, y3);
            ctx.closePath();
            break;
        }
        case 'star': {
            const numPuntas = 7;
            const R = shape.size;
            const r = R * 0.4;
            for (let i = 0; i < numPuntas; i++) {
                const angleR = i * (2 * Math.PI / numPuntas) - Math.PI / 2;
                const pxR = shape.x + R * Math.cos(angleR);
                const pyR = shape.y + R * Math.sin(angleR);
                if (i === 0) ctx.moveTo(pxR, pyR);
                else ctx.lineTo(pxR, pyR);
                const angle_r = (i + 0.5) * (2 * Math.PI / numPuntas) - Math.PI / 2;
                const px_r = shape.x + r * Math.cos(angle_r);
                const py_r = shape.y + r * Math.sin(angle_r);
                ctx.lineTo(px_r, py_r);
            }
            ctx.closePath();
            break;
        }
        case 'pencil': {
            const tolerance = (shape.stroke > 5) ? shape.stroke / 2 : 5;
            for (let p of shape.drawLine) {
                const dx = x - p.x;
                const dy = y - p.y;
                if (Math.sqrt(dx * dx + dy * dy) <= tolerance) {
                    return true;
                }
            }
            ctx.restore();
            return false;
        }
        default:
            ctx.restore();
            return false;
    }

    const inside = ctx.isPointInPath(x, y);
    ctx.restore();
    return inside;
}

function drawSelectionOutline(shape) {
    ctx.save();
    ctx.strokeStyle = "white";
    ctx.lineWidth = 2;
    ctx.setLineDash([6, 4]);

    ctx.beginPath();

    switch (shape.tipo) {
        case 'square':
            ctx.rect(
                shape.x - shape.size / 2 - 5,
                shape.y - shape.size / 2 - 5,
                shape.size + 10,
                shape.size + 10
            );
            break;

        case 'circle':
            ctx.arc(shape.x, shape.y, shape.size + 5, 0, 2 * Math.PI);
            break;

        case 'triangle': {
            const altura = shape.size * Math.sqrt(3) / 2;
            const x1 = shape.x;
            const y1 = shape.y - (2 * altura / 3);
            const x2 = shape.x - shape.size / 2;
            const y2 = shape.y + (altura / 3);
            const x3 = shape.x + shape.size / 2;
            const y3 = shape.y + (altura / 3);
            ctx.moveTo(x1, y1);
            ctx.lineTo(x2, y2);
            ctx.lineTo(x3, y3);
            ctx.closePath();
            break;
        }

        case 'star':
            const numPuntas = 7;
            const R = shape.size + 5;
            const r = R * 0.4;
            for (let i = 0; i < numPuntas; i++) {
                const angleR = i * (2 * Math.PI / numPuntas) - Math.PI / 2;
                const pxR = shape.x + R * Math.cos(angleR);
                const pyR = shape.y + R * Math.sin(angleR);
                if (i === 0) ctx.moveTo(pxR, pyR);
                else ctx.lineTo(pxR, pyR);
                const angle_r = (i + 0.5) * (2 * Math.PI / numPuntas) - Math.PI / 2;
                const px_r = shape.x + r * Math.cos(angle_r);
                const py_r = shape.y + r * Math.sin(angle_r);
                ctx.lineTo(px_r, py_r);
            }
            ctx.closePath();
            break;

        case 'pencil':

            let minX = Infinity, minY = Infinity, maxX = 0, maxY = 0;
            shape.drawLine.forEach(p => {
                minX = Math.min(minX, p.x);
                maxX = Math.max(maxX, p.x);
                minY = Math.min(minY, p.y);
                maxY = Math.max(maxY, p.y);
            });
            ctx.rect(minX - 5, minY - 5, (maxX - minX) + 10, (maxY - minY) + 10);
            break;
    }

    ctx.stroke();
    ctx.restore();
}


function parseCustomDate(dateStr) {

    if (!dateStr) return null;

    if (dateStr.includes("T")) {
        return new Date(dateStr);
    }
    const [datePart, timePart] = dateStr.split(" ");
    const [day, month, year] = datePart.split("/").map(Number);
    const [hours, minutes, seconds] = timePart.split(":").map(Number);
    return new Date(year, month - 1, day, hours, minutes, seconds);
}

function askCanvasSize() {
    const modal = document.getElementById("sizeModal");
    const widthInput = document.getElementById("modalWidth");
    const heightInput = document.getElementById("modalHeight");
    const applyBtn = document.getElementById("applySize");
    const errorWidth = document.getElementById("errorWidth");
    const errorHeight = document.getElementById("errorHeight");
    modal.style.display = "block";

    applyBtn.onclick = () => {
        let width = parseInt(widthInput.value);
        let height = parseInt(heightInput.value);

        if (width < 100 || width > 2000) {
            errorWidth.innerHTML = "Width must be between 100 and 2000 px.";
            return;
        } else {
            errorWidth.innerHTML = "";
        }

        if (height < 100 || height > 2000) {
            errorHeight.innerHTML = "Height must be between 100 and 2000 px.";
            return;
        } else {
            errorHeight.innerHTML = "";
        }

        canvas.width = width;
        canvas.height = height;
        log.style.height = height + "px";

        modal.style.display = "none";
    };
}