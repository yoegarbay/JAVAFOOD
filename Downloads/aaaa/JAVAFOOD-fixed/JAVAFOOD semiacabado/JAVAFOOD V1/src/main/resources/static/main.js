/**
 * main.js — JAVAFOOD
 * Versión con soporte completo de promociones/puntos + check de login.
 */
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

/* ── HELPERS ─────────────────────────────────────────── */
function esPromocionPage() {
    return window.location.pathname.includes('Promociones.html');
}

function getUser() {
    return JSON.parse(sessionStorage.getItem('javafoodUser') || 'null');
}

function getSaldoPuntos() {
    return parseInt(localStorage.getItem('javaFoodPuntos') || '0');
}

function setSaldoPuntos(valor) {
    localStorage.setItem('javaFoodPuntos', Math.max(0, valor).toString());
    // Actualizar displays de saldo en la página
    document.querySelectorAll('#saldo-puntos-usuario, #header-puntos').forEach(el => {
        el.textContent = Math.max(0, valor) + ' pts';
    });
}

/* ── SINCRONIZAR PUNTOS CON LA BD ────────────────────── */
async function sincronizarPuntos() {
    const user = getUser();
    if (!user) return;
    const id = user.id_cliente || user.id;
    if (!id) return;
    try {
        const res = await fetch(`/api/clientes/${id}/puntos`);
        if (!res.ok) return;
        const data = await res.json();
        const puntos = data.puntos ?? data;
        localStorage.setItem('javaFoodPuntos', puntos.toString());
        document.querySelectorAll('#saldo-puntos-usuario, #header-puntos').forEach(el => {
            el.textContent = puntos + ' pts';
        });
    } catch (e) {
        console.warn('No se pudo sincronizar puntos:', e);
    }
}

async function actualizarPuntosEnBD(delta) {
    const user = getUser();
    if (!user) return;
    const id = user.id_cliente || user.id;
    if (!id) return;
    try {
        await fetch(`/api/clientes/${id}/puntos`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ delta })
        });
    } catch (e) {
        console.warn('No se pudo actualizar puntos en BD:', e);
    }
}

/* ── AÑADIR PRODUCTO ──────────────────────────────────── */
let itemActual = null;

function vincularBotones() {
    document.addEventListener('click', (e) => {
        const btn = e.target;
        if (btn.tagName !== 'BUTTON' || btn.innerText.trim() !== '+') return;

        const esPromo = esPromocionPage();

        // ── CHECK LOGIN para promociones ───────────────────
        if (esPromo) {
            const user = getUser();
            if (!user) {
                alert('Debes iniciar sesión para canjear promociones.');
                return; // STOP — no se abre el modal
            }
        }

        const card = btn.closest('.card, .product-card');
        if (!card) return;
        if (card.classList.contains('bloqueado')) return;

        const nombreElem = card.querySelector('h4, .product-name');
        const precioElem = card.querySelector('.card-price span, .price');
        if (!nombreElem || !precioElem) return;

        const idProd = card.dataset.id ? parseInt(card.dataset.id) : null;

        // Para promos: precio en euros = 0, coste en pts del data-pts o del texto
        const costoPuntos = esPromo
            ? parseInt(precioElem.getAttribute('data-pts') || precioElem.innerText.replace(/[^0-9]/g, '') || '0')
            : 0;
        const precioEuros = esPromo
            ? 0.0
            : parseFloat(precioElem.innerText.replace('€', '').trim()) || 0;

        itemActual = {
            id_producto:  idProd,
            nombre:       nombreElem.innerText.trim(),
            precio:       precioEuros,
            costoPuntos:  costoPuntos,
            esPromocion:  esPromo
        };

        document.getElementById('modal-titulo').innerText = itemActual.nombre;

        const esComp = window.location.pathname.includes('Complementos.html');
        document.getElementById('seccion-extras').style.display = esComp ? 'block' : 'none';

        document.getElementById('input-cantidad').value = 1;
        document.getElementById('custom-modal').style.display = 'flex';
    });
}

function confirmarCompra() {
    const cantidad = parseInt(document.getElementById('input-cantidad').value);
    const extra    = document.getElementById('select-extras').value;
    if (cantidad <= 0 || isNaN(cantidad)) return;

    // ── LÓGICA DE PUNTOS ──────────────────────────────────
    if (itemActual.esPromocion) {
        const saldo     = getSaldoPuntos();
        const costeTotal = itemActual.costoPuntos * cantidad;

        if (costeTotal > saldo) {
            alert('❌ No tienes suficientes puntos para esta promoción.');
            cerrarModal();
            return;
        }

        // Descontar puntos localmente (preview UI — la BD se actualiza al pagar)
        setSaldoPuntos(saldo - costeTotal);
    }

    let pFinal = itemActual.precio; // 0 para promos
    let nFinal = itemActual.esPromocion
        ? `🎁 ${itemActual.nombre} (Promo)`
        : itemActual.nombre;

    if (extra && window.location.pathname.includes('Complementos.html')) {
        pFinal += 0.50;
        nFinal += ` + ${extra}`;
    }

    carrito.push({
        id_producto:  itemActual.id_producto || null,
        nombre:       nFinal,
        precio:       pFinal,
        cantidad:     cantidad,
        total:        parseFloat((pFinal * cantidad).toFixed(2)),
        esPromocion:  itemActual.esPromocion,
        costoPuntos:  itemActual.costoPuntos * cantidad
    });

    guardarCarrito();
    cerrarModal();

    // Refrescar grid de promociones para actualizar bloqueados
    if (itemActual.esPromocion && typeof cargarPromociones === 'function') {
        cargarPromociones();
    }
}

function cerrarModal() {
    document.getElementById('custom-modal').style.display = 'none';
}

/* ── BORRAR ÍTEM ─────────────────────────────────────── */
function borrarItem(index) {
    const item = carrito[index];

    // Devolver puntos al localStorage si se elimina una promo del carrito
    if (item.esPromocion && item.costoPuntos) {
        setSaldoPuntos(getSaldoPuntos() + item.costoPuntos);
    }

    carrito.splice(index, 1);
    guardarCarrito();

    if (esPromocionPage() && typeof cargarPromociones === 'function') {
        cargarPromociones();
    }
}

/* ── RENDERIZAR CARRITO ──────────────────────────────── */
function actualizarVistaCarrito() {
    const contenedores = document.querySelectorAll('.cart-items-list, .cart-items-scroll');
    const totales      = document.querySelectorAll('.total-price');

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

    const total = carrito.reduce((acc, item) => acc + item.total, 0);
    totales.forEach(el => el.innerText = `${total.toFixed(2)}€`);
}

function guardarCarrito() {
    localStorage.setItem('javaFoodCart', JSON.stringify(carrito));
    actualizarVistaCarrito();
}

/* ── PAGAR ───────────────────────────────────────────── */
function vincularPagar() {
    document.addEventListener('click', (e) => {
        if (!e.target.closest('.pay-btn')) return;
        if (carrito.length === 0) {
            alert('Tu carrito está vacío. Añade productos antes de pagar.');
            return;
        }
        window.location.href = 'pago.html';
    });
}

async function pagarPedido() {
    if (carrito.length === 0) {
        alert('Tu carrito está vacío.');
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
        carrito = [];
        guardarCarrito();
        document.getElementById('modal-pago-titulo').innerText = '✅ PEDIDO CONFIRMADO';
        document.getElementById('modal-pago-msg').innerText =
            `Pedido #${data.id_pedido} registrado.\nTotal: ${parseFloat(data.total).toFixed(2)}€\nEstado: ${data.estado}`;
    } catch (error) {
        document.getElementById('modal-pago-titulo').innerText = '❌ ERROR AL PAGAR';
        document.getElementById('modal-pago-msg').innerText = 'No se pudo conectar con el servidor.\n\n' + error.message;
    }
    document.getElementById('modal-pago').style.display = 'flex';
}

function cerrarModalPago() {
    document.getElementById('modal-pago').style.display = 'none';
}