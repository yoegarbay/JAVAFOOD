/* admin/pagos/js/index.js
   Carga la lista de pedidos desde GET /api/admin/pedidos
   y permite filtrar y eliminar directamente desde la tabla.
*/

// Variable global para almacenar todos los pedidos
let pedidosGlobal = [];

// ── Carga inicial ─────────────────────────────────────────────────────────────
async function cargarPedidos() {
    mostrarSpinner(true);

    try {
        const res = await fetch("/api/admin/pedidos");

        if (!res.ok) {
            throw new Error("Error HTTP " + res.status);
        }

        pedidosGlobal = await res.json();

    } catch (err) {
        console.error("Error al cargar pedidos:", err);
        pedidosGlobal = [];
    }

    actualizarStats(pedidosGlobal);
    renderizarTabla(pedidosGlobal);
    mostrarSpinner(false);
}

// ── Renderizar tabla ──────────────────────────────────────────────────────────
function renderizarTabla(pedidos) {
    const tbody  = document.getElementById("tbody");
    const empty  = document.getElementById("empty");

    tbody.innerHTML = "";

    if (pedidos.length === 0) {
        empty.style.display = "block";
        return;
    }
    empty.style.display = "none";

    pedidos.forEach(p => {
        // Formatear fecha: "2025-05-12T14:30:00" → "12/05/2025 14:30"
        const fecha = formatearFecha(p.fecha);

        // Badge según estado
        const badgeClass = p.estado === "PAGADO" ? "badge-pagado" : "badge-pending";

        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td><strong>#${p.id_pedido}</strong></td>
            <td>${fecha}</td>
            <td><span class="badge ${badgeClass}">${p.estado}</span></td>
            <td class="total-cell">${parseFloat(p.total).toFixed(2)} €</td>
            <td style="display:flex;gap:8px;flex-wrap:wrap;">
                <a href="show.html?id=${p.id_pedido}" class="btn-ver">👁 Ver</a>
                <button class="btn-del" onclick="eliminar(${p.id_pedido}, this)">🗑 Borrar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// ── Filtro de búsqueda ────────────────────────────────────────────────────────
function filtrar() {
    const texto = document.getElementById("input-buscar").value.trim().toLowerCase();

    if (!texto) {
        renderizarTabla(pedidosGlobal);
        return;
    }

    const filtrados = pedidosGlobal.filter(p =>
        String(p.id_pedido).includes(texto) ||
        p.estado.toLowerCase().includes(texto) ||
        String(p.total).includes(texto)
    );

    renderizarTabla(filtrados);
}

function limpiarFiltro() {
    document.getElementById("input-buscar").value = "";
    renderizarTabla(pedidosGlobal);
}

// ── Eliminar pedido ───────────────────────────────────────────────────────────
async function eliminar(id, boton) {
    if (!confirm(`¿Eliminar el pedido #${id}? Esta acción no se puede deshacer.`)) return;

    // Deshabilitar el botón mientras procesa
    boton.disabled = true;
    boton.textContent = "…";

    try {
        const res = await fetch(`/api/admin/pedidos/${id}`, { method: "DELETE" });

        if (!res.ok) {
            throw new Error("Error HTTP " + res.status);
        }

        // Quitar el pedido del array global y re-renderizar sin recargar la página
        pedidosGlobal = pedidosGlobal.filter(p => p.id_pedido !== id);
        actualizarStats(pedidosGlobal);
        renderizarTabla(pedidosGlobal);

    } catch (err) {
        console.error("Error al eliminar:", err);
        alert("No se pudo eliminar el pedido #" + id);
        boton.disabled = false;
        boton.textContent = "🗑 Borrar";
    }
}

// ── Stats ─────────────────────────────────────────────────────────────────────
function actualizarStats(pedidos) {
    // Total pedidos
    document.getElementById("stat-total").textContent = pedidos.length;

    // Suma total recaudado
    const total = pedidos.reduce((acc, p) => acc + parseFloat(p.total), 0);
    document.getElementById("stat-importe").textContent = total.toFixed(2);

    // Pedidos de hoy
    const hoy = new Date().toISOString().slice(0, 10); // "2025-05-12"
    const hoyCount = pedidos.filter(p => p.fecha && p.fecha.startsWith(hoy)).length;
    document.getElementById("stat-hoy").textContent = hoyCount;
}

// ── Utilidades ────────────────────────────────────────────────────────────────
function formatearFecha(fechaStr) {
    if (!fechaStr) return "—";
    try {
        // La fecha viene como "2025-05-12T14:30:00" o "2025-05-12 14:30:00"
        const d = new Date(fechaStr.replace(" ", "T"));
        return d.toLocaleDateString("es-ES") + " " + d.toLocaleTimeString("es-ES", { hour:"2-digit", minute:"2-digit" });
    } catch {
        return fechaStr;
    }
}

function mostrarSpinner(mostrar) {
    document.getElementById("loading").style.display = mostrar ? "block" : "none";
}

// ── Arranque ──────────────────────────────────────────────────────────────────
cargarPedidos();
