/* ════════════════════════════════════════════════════
   pago.js  —  Lógica de la pantalla de pago
   ════════════════════════════════════════════════════ */

let carrito = JSON.parse(localStorage.getItem('javaFoodCart')) || [];
let metodoPagoSeleccionado = 'EFECTIVO';

document.addEventListener('DOMContentLoaded', () => {
    renderizarResumen();
});

function renderizarResumen() {
    const contenedor = document.getElementById('resumen-contenido');
    if (carrito.length === 0) {
        contenedor.innerHTML = `
            <div class="carrito-vacio">
                <span>🛒</span>
                <p>Tu carrito está vacío.</p>
                <p style="font-size:0.8rem;margin-top:8px;">Vuelve al menú y añade productos.</p>
            </div>`;
        document.getElementById('btn-pagar').disabled = true;
        return;
    }
    const total = carrito.reduce((acc, item) => acc + item.total, 0);
    let filasHTML = carrito.map(item => `
        <tr>
            <td>${item.cantidad}×</td>
            <td>${item.nombre}</td>
            <td>${item.total.toFixed(2)}€</td>
        </tr>
    `).join('');
    contenedor.innerHTML = `
        <table class="resumen-tabla">
            <thead>
                <tr>
                    <th>CANT</th>
                    <th>PRODUCTO</th>
                    <th style="text-align:right;">PRECIO</th>
                </tr>
            </thead>
            <tbody>${filasHTML}</tbody>
        </table>
        <div class="resumen-total">
            <span>TOTAL</span>
            <span>${total.toFixed(2)}€</span>
        </div>
    `;
}

function seleccionarMetodo(metodo) {
    metodoPagoSeleccionado = metodo;
    document.getElementById('btn-efectivo').classList.toggle('activo', metodo === 'EFECTIVO');
    document.getElementById('btn-tarjeta').classList.toggle('activo', metodo === 'TARJETA');
    const seccion = document.getElementById('seccion-tarjeta');
    if (metodo === 'TARJETA') {
        seccion.classList.add('visible');
    } else {
        seccion.classList.remove('visible');
        limpiarCamposTarjeta();
    }
}

function validarFormulario() {
    const nombre = document.getElementById('input-nombre').value.trim();
    if (nombre.length < 2) {
        mostrarError('Por favor, introduce tu nombre.');
        return false;
    }
    if (metodoPagoSeleccionado === 'TARJETA') {
        const numTarjeta = document.getElementById('input-numero-tarjeta').value.replace(/\s/g, '');
        const caducidad  = document.getElementById('input-caducidad').value.trim();
        const cvv        = document.getElementById('input-cvv').value.trim();
        if (numTarjeta.length < 16) { mostrarError('El número de tarjeta debe tener 16 dígitos.'); return false; }
        if (!/^\d{2}\/\d{2}$/.test(caducidad)) { mostrarError('Introduce la caducidad con formato MM/AA.'); return false; }
        if (cvv.length < 3) { mostrarError('El CVV debe tener al menos 3 dígitos.'); return false; }
    }
    return true;
}

async function confirmarPago() {
    if (carrito.length === 0) return;
    if (!validarFormulario()) return;

    const nombreCliente = document.getElementById('input-nombre').value.trim();
    const btnPagar      = document.getElementById('btn-pagar');
    btnPagar.disabled    = true;
    btnPagar.textContent = 'PROCESANDO...';

    try {
        const clienteSession = JSON.parse(sessionStorage.getItem('jfCliente') || 'null');

        const response = await fetch('/api/pedidos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                nombreCliente,
                metodoPago: metodoPagoSeleccionado,
                idCliente:  clienteSession ? clienteSession.id_cliente : null,
                items: carrito
            })
        });

        if (!response.ok) {
            const textoError = await response.text();
            throw new Error('Error del servidor: ' + textoError);
        }

        const data = await response.json();

        // Guardar items para mostrarlos en gracias.html (reseñas)
        localStorage.setItem('itemsPedido', JSON.stringify(carrito));

        // Guardar datos del pedido para mostrar en la pantalla de gracias
        sessionStorage.setItem('ultimoPedido', JSON.stringify(data));

        // Vaciar carrito
        localStorage.removeItem('javaFoodCart');
        carrito = [];

        // Redirigir a pantalla de confirmación
        window.location.href = 'gracias.html';

    } catch (error) {
        console.error('Error al procesar el pago:', error);
        mostrarError('No se pudo conectar con el servidor.\n' + error.message);
        btnPagar.disabled    = false;
        btnPagar.textContent = 'CONFIRMAR PAGO';
    }
}

function mostrarConfirmacion(titulo, mensaje) {
    document.getElementById('modal-titulo').textContent  = titulo;
    document.getElementById('modal-titulo').style.color  = '#00e5ff';
    document.getElementById('modal-mensaje').textContent = mensaje;
    document.getElementById('modal-resultado').classList.add('visible');
}

function mostrarError(mensaje) {
    document.getElementById('modal-titulo').textContent  = '❌ ERROR';
    document.getElementById('modal-titulo').style.color  = '#ff4444';
    document.getElementById('modal-mensaje').textContent = mensaje;
    document.getElementById('modal-resultado').classList.add('visible');
}

function cerrarModal() {
    document.getElementById('modal-resultado').classList.remove('visible');
}

function limpiarCamposTarjeta() {
    ['input-numero-tarjeta', 'input-nombre-tarjeta', 'input-caducidad', 'input-cvv']
        .forEach(id => { const el = document.getElementById(id); if (el) el.value = ''; });
}

function formatearTarjeta(input) {
    let v = input.value.replace(/\D/g, '').substring(0, 16);
    input.value = v.replace(/(.{4})/g, '$1 ').trim();
}

function formatearCaducidad(input) {
    let v = input.value.replace(/\D/g, '').substring(0, 4);
    if (v.length > 2) v = v.substring(0, 2) + '/' + v.substring(2);
    input.value = v;
}