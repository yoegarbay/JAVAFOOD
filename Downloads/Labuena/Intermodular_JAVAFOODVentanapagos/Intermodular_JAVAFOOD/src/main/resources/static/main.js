let carrito = JSON.parse(localStorage.getItem('javaFoodCart')) || [];

document.addEventListener('DOMContentLoaded', () => {
    crearEstructuraModal();
    actualizarVistaCarrito();
    vincularBotones();
    vincularPagar();
});

/* ── MODALES ──────────────────────────────────────────── */
function crearEstructuraModal() {
    const html = `
        <!-- Modal añadir producto -->
        <div id="custom-modal" class="modal-overlay">
            <div class="modal-content">
                <h2 id="modal-titulo">PRODUCTO</h2>
                <div class="modal-body">
                    <div id="seccion-extras" style="display:none;">
                        <label>AÑADIR EXTRAS (+0.50€):</label>
                        <select id="select-extras">
                            <option value="">Ninguno</option>
                            <option value="Extra Queso">Extra Queso Fundido</option>
                            <option value="Bacon bits">Bacon Crispy bits</option>
                            <option value="Cebolla">Cebolla Crujiente</option>
                        </select>
                    </div>
                    <label>CANTIDAD:</label>
                    <input type="number" id="input-cantidad" value="1" min="1">
                    <button class="confirm-btn" onclick="confirmarCompra()">AÑADIR AL PEDIDO</button>
                    <button class="confirm-btn" onclick="cerrarModal()"
                        style="background:transparent;border:1px solid #ff4444;color:#ff4444;margin-top:10px;">
                        CANCELAR
                    </button>
                </div>
            </div>
        </div>

        <!-- Modal confirmación pago -->
        <div id="modal-pago" class="modal-overlay" style="display:none;">
            <div class="modal-content">
                <h2 id="modal-pago-titulo">✅ PEDIDO ENVIADO</h2>
                <div class="modal-body">
                    <p id="modal-pago-msg" style="line-height:1.6;white-space:pre-line;"></p>
                    <button class="confirm-btn" onclick="cerrarModalPago()">ACEPTAR</button>
                </div>
            </div>
        </div>
    `;
    document.body.insertAdjacentHTML('beforeend', html);
}

/* ── AÑADIR PRODUCTO ──────────────────────────────────── */
let itemActual = null;

function vincularBotones() {
    document.addEventListener('click', (e) => {
        const btn = e.target;
        // Botón "+" en una card
        if (btn.tagName === 'BUTTON' && btn.innerText.trim() === '+') {
            const card = btn.closest('.card, .product-card');
            if (!card) return;

            const nombreElem = card.querySelector('h4, .product-name');
            const precioElem = card.querySelector('.card-price span, .price');

            if (nombreElem && precioElem) {
                itemActual = {
                    nombre: nombreElem.innerText.trim(),
                    precio: parseFloat(precioElem.innerText.replace('€', '').trim())
                };
                document.getElementById('modal-titulo').innerText = itemActual.nombre;

                const esComp = window.location.pathname.includes('Complementos.html');
                document.getElementById('seccion-extras').style.display = esComp ? 'block' : 'none';

                document.getElementById('input-cantidad').value = 1;
                document.getElementById('custom-modal').style.display = 'flex';
            }
        }
    });
}

function confirmarCompra() {
    const cantidad = parseInt(document.getElementById('input-cantidad').value);
    const extra = document.getElementById('select-extras').value;
    if (cantidad <= 0 || isNaN(cantidad)) return;

    let pFinal = itemActual.precio;
    let nFinal = itemActual.nombre;

    if (extra && window.location.pathname.includes('Complementos.html')) {
        pFinal += 0.50;
        nFinal += ` + ${extra}`;
    }

    const nuevo = {
        nombre: nFinal,
        precio: pFinal,
        cantidad: cantidad,
        total: parseFloat((pFinal * cantidad).toFixed(2))
    };

    carrito.push(nuevo);
    guardarCarrito();
    cerrarModal();
}

function cerrarModal() {
    document.getElementById('custom-modal').style.display = 'none';
}

/* ── BORRAR ÍTEM DEL CARRITO ─────────────────────────── */
function borrarItem(index) {
    carrito.splice(index, 1);
    guardarCarrito();
}

/* ── RENDERIZAR CARRITO ──────────────────────────────── */
function actualizarVistaCarrito() {
    // Puede haber varios contenedores (una por página)
    const contenedores = document.querySelectorAll('.cart-items-list, .cart-items-scroll');
    const totales = document.querySelectorAll('.total-price');

    let total = 0;

    contenedores.forEach(contenedor => {
        contenedor.innerHTML = '';

        if (carrito.length === 0) {
            contenedor.innerHTML = '<p style="color:#888;font-size:0.8rem;text-align:center;padding:8px 0;">Sin productos</p>';
        } else {
            carrito.forEach((item, i) => {
                const div = document.createElement('div');
                div.className = 'cart-item';
                div.innerHTML = `
                    <div class="item-info">
                        <span class="item-nombre">${item.cantidad}× ${item.nombre}</span>
                        <span class="item-precio">${item.total.toFixed(2)}€</span>
                    </div>
                    <button class="btn-borrar" title="Eliminar" onclick="borrarItem(${i})">×</button>
                `;
                contenedor.appendChild(div);
            });
        }
    });

    total = carrito.reduce((acc, item) => acc + item.total, 0);
    totales.forEach(el => el.innerText = `${total.toFixed(2)}€`);
}

function guardarCarrito() {
    localStorage.setItem('javaFoodCart', JSON.stringify(carrito));
    actualizarVistaCarrito();
}

/* ── PAGAR ───────────────────────────────────────────── */
/* ── PAGAR — ahora redirige a la pantalla de pago ───── */
function vincularPagar() {
    document.addEventListener('click', (e) => {
        if (e.target.closest('.pay-btn')) {
            if (carrito.length === 0) {
                alert('Tu carrito está vacío. Añade productos antes de pagar.');
                return;
            }
            // Redirigir a la pantalla de pago (el carrito ya está en localStorage)
            window.location.href = 'pago.html';
        }
    });
}

async function pagarPedido() {
    if (carrito.length === 0) {
        alert('Tu carrito está vacío. Añade productos antes de pagar.');
        return;
    }

    try {
        const response = await fetch('/api/pedidos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ items: carrito })
        });

        if (!response.ok) throw new Error('Error del servidor: ' + await response.text());

        const data = await response.json();

        // Vaciar carrito
        carrito = [];
        guardarCarrito();

        document.getElementById('modal-pago-titulo').innerText = '✅ PEDIDO CONFIRMADO';
        document.getElementById('modal-pago-msg').innerText =
            `Pedido #${data.id_pedido} registrado.\nTotal: ${parseFloat(data.total).toFixed(2)}€\nEstado: ${data.estado}`;

    } catch (error) {
        console.error('Error al pagar:', error);
        document.getElementById('modal-pago-titulo').innerText = '❌ ERROR AL PAGAR';
        document.getElementById('modal-pago-msg').innerText =
            'No se pudo conectar con el servidor.\n\n' + error.message;
    }

    document.getElementById('modal-pago').style.display = 'flex';
}

function cerrarModalPago() {
    document.getElementById('modal-pago').style.display = 'none';
}
