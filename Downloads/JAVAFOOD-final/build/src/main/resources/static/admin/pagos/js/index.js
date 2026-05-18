/* admin/pagos/js/index.js */

let pedidosGlobal = [];

async function cargarPedidos() {
    mostrarSpinner(true);
    try {
        const res = await fetch("/api/admin/pedidos");
        if (!res.ok) throw new Error("HTTP " + res.status);
        pedidosGlobal = await res.json();
    } catch (err) {
        console.error("Error al cargar pedidos:", err);
        pedidosGlobal = [];
    }
    actualizarStats(pedidosGlobal);
    renderizarTabla(pedidosGlobal);
    mostrarSpinner(false);
}

function renderizarTabla(pedidos) {
    const tbody = document.getElementById("tbody");
    const empty = document.getElementById("empty");
    tbody.innerHTML = "";

    if (pedidos.length === 0) {
        empty.style.display = "block";
        return;
    }
    empty.style.display = "none";

    pedidos.forEach(p => {
        const fecha      = formatearFecha(p.fecha);
        const badgeClass = p.estado === "PAGADO"    ? "badge-pagado"
                         : p.estado === "CANCELADO" ? "badge-cancelado"
                         : "badge-pending";
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td><strong>#${p.id_pedido}</strong></td>
            <td>${fecha}</td>
            <td><span class="badge ${badgeClass}">${p.estado}</span></td>
            <td class="total-cell">${parseFloat(p.total).toFixed(2)} €</td>
            <td style="display:flex;gap:6px;flex-wrap:wrap;align-items:center;">
                <a href="show.html?id=${p.id_pedido}" class="btn-ver">👁 Ver</a>
                <a href="edit.html?id=${p.id_pedido}" class="btn-edit">✏️ Editar</a>
                <button class="btn-del" onclick="eliminar(${p.id_pedido}, this)">🗑 Borrar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function filtrar() {
    const texto = document.getElementById("input-buscar").value.trim().toLowerCase();
    if (!texto) { renderizarTabla(pedidosGlobal); return; }
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

async function eliminar(id, boton) {
    if (!confirm(`¿Eliminar el pedido #${id}? Esta acción no se puede deshacer.`)) return;
    boton.disabled = true;
    boton.textContent = "…";
    try {
        const res = await fetch(`/api/admin/pedidos/${id}`, { method: "DELETE" });
        if (!res.ok) throw new Error("HTTP " + res.status);
        pedidosGlobal = pedidosGlobal.filter(p => p.id_pedido !== id);
        actualizarStats(pedidosGlobal);
        renderizarTabla(pedidosGlobal);
    } catch (err) {
        alert("No se pudo eliminar el pedido #" + id);
        boton.disabled = false;
        boton.textContent = "🗑 Borrar";
    }
}

function actualizarStats(pedidos) {
    document.getElementById("stat-total").textContent = pedidos.length;
    const total = pedidos.reduce((acc, p) => acc + parseFloat(p.total), 0);
    document.getElementById("stat-importe").textContent = total.toFixed(2);
    const hoy = new Date().toISOString().slice(0, 10);
    document.getElementById("stat-hoy").textContent =
        pedidos.filter(p => p.fecha && p.fecha.startsWith(hoy)).length;
}

function formatearFecha(f) {
    if (!f) return "—";
    try {
        const d = new Date(f.replace(" ", "T"));
        return d.toLocaleDateString("es-ES") + " " +
               d.toLocaleTimeString("es-ES", { hour:"2-digit", minute:"2-digit" });
    } catch { return f; }
}

function mostrarSpinner(v) {
    document.getElementById("loading").style.display = v ? "block" : "none";
}

cargarPedidos();
