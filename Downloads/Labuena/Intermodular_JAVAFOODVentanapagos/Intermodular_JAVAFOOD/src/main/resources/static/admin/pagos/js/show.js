/* admin/pagos/js/show.js
   Carga el detalle completo de un pedido desde GET /api/admin/pedidos/{id}
   y permite eliminarlo.
*/

// Leer el id de la URL: show.html?id=5
const id = new URLSearchParams(location.search).get("id");

// ── Carga del pedido ──────────────────────────────────────────────────────────
async function cargarDetalle() {
    if (!id) {
        document.getElementById("loading").innerHTML = "<p>❌ No se indicó ningún id de pedido.</p>";
        return;
    }

    try {
        const res = await fetch(`/api/admin/pedidos/${id}`);

        if (!res.ok) {
            throw new Error("Pedido no encontrado (HTTP " + res.status + ")");
        }

        // El servidor devuelve un PedidoConDetalle:
        // { id_pedido, fecha, total, estado, lineas: [ {id_linea, nombre_producto, cantidad, precio_unitario, subtotal}, ... ] }
        const pedido = await res.json();

        renderizarTicket(pedido);

    } catch (err) {
        console.error(err);
        document.getElementById("loading").innerHTML = `<p style="color:#e05555">❌ ${err.message}</p>`;
    }
}

// ── Renderizar ────────────────────────────────────────────────────────────────
function renderizarTicket(p) {
    // Ocultar spinner y mostrar ticket
    document.getElementById("loading").style.display = "none";
    document.getElementById("ticket").style.display  = "block";

    // Actualizar título de la pestaña
    document.title = "JAVAFOOD · Pedido #" + p.id_pedido;

    // Cabecera
    document.getElementById("t-id").textContent     = "Pedido #" + p.id_pedido;
    document.getElementById("t-total").textContent  = parseFloat(p.total).toFixed(2);
    document.getElementById("t-estado").textContent = p.estado;
    document.getElementById("t-fecha").textContent  = formatearFecha(p.fecha);
    document.getElementById("t-nlineas").textContent = (p.lineas ? p.lineas.length : 0) + " productos";

    // Líneas de productos
    const contenedor = document.getElementById("lineas-lista");
    contenedor.innerHTML = "";

    if (!p.lineas || p.lineas.length === 0) {
        contenedor.innerHTML = `<p style="opacity:.4;text-align:center;padding:16px;">Sin líneas de detalle</p>`;
        return;
    }

    p.lineas.forEach(l => {
        const div = document.createElement("div");
        div.className = "linea";
        div.innerHTML = `
            <div>
                <div class="linea-nombre">${l.nombre_producto}</div>
                <div class="linea-qty">× ${l.cantidad} ud · ${parseFloat(l.precio_unitario).toFixed(2)} € / ud</div>
            </div>
            <div class="linea-sub">${parseFloat(l.subtotal).toFixed(2)} €</div>
        `;
        contenedor.appendChild(div);
    });
}

// ── Eliminar pedido ───────────────────────────────────────────────────────────
async function eliminar() {
    if (!confirm(`¿Eliminar el pedido #${id}? Esta acción no se puede deshacer.`)) return;

    const boton = document.getElementById("btn-eliminar");
    boton.disabled = true;
    boton.textContent = "Eliminando…";

    try {
        const res = await fetch(`/api/admin/pedidos/${id}`, { method: "DELETE" });

        if (!res.ok) {
            throw new Error("Error HTTP " + res.status);
        }

        // Redirigir al listado con mensaje de éxito
        location.href = "index.html";

    } catch (err) {
        console.error("Error al eliminar:", err);
        alert("No se pudo eliminar el pedido. Inténtalo de nuevo.");
        boton.disabled = false;
        boton.textContent = "🗑 Eliminar pedido";
    }
}

// ── Utilidad de fecha ─────────────────────────────────────────────────────────
function formatearFecha(fechaStr) {
    if (!fechaStr) return "—";
    try {
        const d = new Date(fechaStr.replace(" ", "T"));
        return d.toLocaleDateString("es-ES", { year:"numeric", month:"long", day:"numeric" })
             + " · " + d.toLocaleTimeString("es-ES", { hour:"2-digit", minute:"2-digit" });
    } catch {
        return fechaStr;
    }
}

// ── Arranque ──────────────────────────────────────────────────────────────────
cargarDetalle();
