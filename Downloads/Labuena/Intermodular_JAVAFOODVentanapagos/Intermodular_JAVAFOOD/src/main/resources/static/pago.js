/* ════════════════════════════════════════════════════
   pago.js  —  Lógica de la pantalla de pago
   Depende de localStorage['javaFoodCart']
   ════════════════════════════════════════════════════ */

// ── Leer el carrito guardado en localStorage ──────────
let carrito = JSON.parse(localStorage.getItem('javaFoodCart')) || [];

// Método de pago seleccionado actualmente
let metodoPagoSeleccionado = 'EFECTIVO';

// ── Al cargar la página ───────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    renderizarResumen();
});

/* ────────────────────────────────────────────────────
   RENDERIZAR RESUMEN DEL PEDIDO (panel izquierdo)
   ──────────────────────────────────────────────────── */
function renderizarResumen() {
    const contenedor = document.getElementById('resumen-contenido');

    // Si el carrito está vacío mostramos aviso y desactivamos el botón de pagar
    if (carrito.length === 0) {
        contenedor.innerHTML = `
            <div class="carrito-vacio">
                <span>🛒</span>
                <p>Tu carrito está vacío.</p>
                <p style="font-size:0.8rem;margin-top:8px;">
                    Vuelve al menú y añade productos.
                </p>
            </div>`;
        document.getElementById('btn-pagar').disabled = true;
        return;
    }

    // Calcular total
    const total = carrito.reduce((acc, item) => acc + item.total, 0);

    // Construir tabla de ítems
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
            <tbody>
                ${filasHTML}
            </tbody>
        </table>
        <div class="resumen-total">
            <span>TOTAL</span>
            <span>${total.toFixed(2)}€</span>
        </div>
    `;
}

/* ────────────────────────────────────────────────────
   SELECCIÓN DE MÉTODO DE PAGO
   ──────────────────────────────────────────────────── */
function seleccionarMetodo(metodo) {
    metodoPagoSeleccionado = metodo;

    // Actualizar estilos de las tarjetas de selección
    document.getElementById('btn-efectivo').classList.toggle('activo', metodo === 'EFECTIVO');
    document.getElementById('btn-tarjeta').classList.toggle('activo', metodo === 'TARJETA');

    // Mostrar u ocultar la sección de datos de tarjeta
    const seccion = document.getElementById('seccion-tarjeta');
    if (metodo === 'TARJETA') {
        seccion.classList.add('visible');
    } else {
        seccion.classList.remove('visible');
        // Limpiar campos de tarjeta al cambiar a efectivo
        limpiarCamposTarjeta();
    }
}

/* ────────────────────────────────────────────────────
   VALIDACIONES DEL FORMULARIO
   ──────────────────────────────────────────────────── */

// Devuelve true si todos los campos obligatorios son correctos
function validarFormulario() {
    const nombre = document.getElementById('input-nombre').value.trim();

    if (nombre.length < 2) {
        mostrarError('Por favor, introduce tu nombre.');
        return false;
    }

    // Si pago con tarjeta validamos los campos adicionales
    if (metodoPagoSeleccionado === 'TARJETA') {
        const numTarjeta = document.getElementById('input-numero-tarjeta').value.replace(/\s/g, '');
        const caducidad  = document.getElementById('input-caducidad').value.trim();
        const cvv        = document.getElementById('input-cvv').value.trim();

        if (numTarjeta.length < 16) {
            mostrarError('El número de tarjeta debe tener 16 dígitos.');
            return false;
        }
        if (!/^\d{2}\/\d{2}$/.test(caducidad)) {
            mostrarError('Introduce la caducidad con formato MM/AA.');
            return false;
        }
        if (cvv.length < 3) {
            mostrarError('El CVV debe tener al menos 3 dígitos.');
            return false;
        }
    }

    return true;
}

/* ────────────────────────────────────────────────────
   ENVIAR PEDIDO AL BACKEND
   ──────────────────────────────────────────────────── */
async function confirmarPago() {
    if (carrito.length === 0) return;
    if (!validarFormulario()) return;

    const nombreCliente = document.getElementById('input-nombre').value.trim();
    const btnPagar      = document.getElementById('btn-pagar');

    // Deshabilitar botón mientras procesa
    btnPagar.disabled    = true;
    btnPagar.textContent = 'PROCESANDO...';

    try {
        const response = await fetch('/api/pedidos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                nombreCliente: nombreCliente,
                metodoPago:    metodoPagoSeleccionado,
                items:         carrito
            })
        });

        if (!response.ok) {
            const textoError = await response.text();
            throw new Error('Error del servidor: ' + textoError);
        }

        const data = await response.json();

        // ── Éxito: vaciar carrito y mostrar confirmación ──
        localStorage.removeItem('javaFoodCart');
        carrito = [];

        mostrarConfirmacion(
            '✅ PEDIDO CONFIRMADO',
            `Pedido #${data.id_pedido}\n` +
            `Cliente: ${data.nombreCliente}\n` +
            `Método: ${data.metodoPago}\n` +
            `Total: ${parseFloat(data.total).toFixed(2)}€\n` +
            `Estado: ${data.estado}`
        );

    } catch (error) {
        console.error('Error al procesar el pago:', error);
        mostrarError('No se pudo conectar con el servidor.\n' + error.message);

        // Reactivar botón para que pueda reintentar
        btnPagar.disabled    = false;
        btnPagar.textContent = 'CONFIRMAR PAGO';
    }
}

/* ────────────────────────────────────────────────────
   HELPERS — Modal, formateo, limpieza
   ──────────────────────────────────────────────────── */

// Muestra el modal con título verde y un mensaje
function mostrarConfirmacion(titulo, mensaje) {
    document.getElementById('modal-titulo').textContent  = titulo;
    document.getElementById('modal-titulo').style.color  = '#00e5ff';
    document.getElementById('modal-mensaje').textContent = mensaje;
    document.getElementById('modal-resultado').classList.add('visible');
}

// Muestra el modal con título rojo (error)
function mostrarError(mensaje) {
    document.getElementById('modal-titulo').textContent  = '❌ ERROR';
    document.getElementById('modal-titulo').style.color  = '#ff4444';
    document.getElementById('modal-mensaje').textContent = mensaje;
    document.getElementById('modal-resultado').classList.add('visible');
}

// Cierra el modal y si el pago fue correcto vuelve al inicio
function cerrarModal() {
    document.getElementById('modal-resultado').classList.remove('visible');
    // Si el carrito está vacío es que el pago fue correcto → redirigir al inicio
    if (carrito.length === 0) {
        window.location.href = 'index.html';
    }
}

// Limpiar campos de tarjeta al cambiar a efectivo
function limpiarCamposTarjeta() {
    ['input-numero-tarjeta', 'input-nombre-tarjeta', 'input-caducidad', 'input-cvv']
        .forEach(id => {
            const el = document.getElementById(id);
            if (el) el.value = '';
        });
}

// Formatear número de tarjeta: grupos de 4 separados por espacio
function formatearTarjeta(input) {
    let v = input.value.replace(/\D/g, '').substring(0, 16);
    input.value = v.replace(/(.{4})/g, '$1 ').trim();
}

// Formatear caducidad: insertar / después de 2 dígitos
function formatearCaducidad(input) {
    let v = input.value.replace(/\D/g, '').substring(0, 4);
    if (v.length > 2) v = v.substring(0, 2) + '/' + v.substring(2);
    input.value = v;
}