/* pagos/js/index.js — solo pedidos del usuario */

const cliente = JSON.parse(sessionStorage.getItem("javafoodUser") || "null");

// Si no hay sesión, redirigir a login
if (!cliente) {
    location.href = "/login.html?next=pagos/index.html";
}

// Mostrar nombre en navbar
document.getElementById("nav-nombre").textContent = cliente?.nom || cliente?.nombre || "";
document.getElementById("hero-sub").textContent = "Pedidos de " + (cliente?.nom || cliente?.nombre || "");
document.getElementById("nav-logout").addEventListener("click", e => {
    e.preventDefault();
    sessionStorage.removeItem("javafoodUser");
    location.href = "/index.html";
});

async function cargar() {
    try {
        const res = await fetch("/api/admin/pedidos");
        if (!res.ok) throw new Error("HTTP " + res.status);
        const todos = await res.json();

        // Filtrar solo los del cliente logueado (por nombre)
        const miNombre = (cliente.nom || cliente.nombre || "").toLowerCase().trim();
        const miId     = cliente.id_cliente || cliente.id || null;
        const misPedidos = todos.filter(p => {
            if (miId && p.id_cliente) return p.id_cliente === miId;
            return (p.nombre_cliente || p.nombreCliente || "").toLowerCase().trim() === miNombre;
        });

        renderizar(misPedidos);
    } catch (err) {
        document.getElementById("loading").innerHTML =
            `<p style="color:var(--red);text-align:center">❌ Error al cargar pedidos: ${err.message}</p>`;
    }
}

function renderizar(lista) {
    document.getElementById("loading").style.display = "none";
    const cont  = document.getElementById("lista");
    const empty = document.getElementById("empty");

    if (lista.length === 0) { empty.style.display = "block"; return; }
    cont.style.display = "flex";

    lista.forEach(p => {
        const card = document.createElement("div");
        card.className = "pedido-card";
        card.id = "card-" + p.id_pedido;
        const badge = p.estado === "PAGADO" ? "badge-pagado"
                    : p.estado === "CANCELADO" ? "badge-cancelado" : "badge-pending";
        const cancelado = p.estado === "CANCELADO";

        card.innerHTML = `
            <div class="card-top" onclick="toggle(${p.id_pedido})">
                <div class="izq">
                    <span class="card-id">#${p.id_pedido}</span>
                    <div>
                        <div class="card-meta">${fmt(p.fecha)}</div>
                        <span class="badge ${badge}">${p.estado}</span>
                    </div>
                </div>
                <div style="display:flex;align-items:center;gap:12px">
                    <span class="card-total">${parseFloat(p.total).toFixed(2)} €</span>
                    <span class="chevron" id="chev-${p.id_pedido}">▼</span>
                </div>
            </div>
            <div class="card-body" id="body-${p.id_pedido}">
                <div class="lineas-mini" id="lineas-${p.id_pedido}">
                    <p style="opacity:.4;font-size:.85rem">Cargando productos…</p>
                </div>
                <div class="acciones">
                    ${cancelado
                        ? `<span style="opacity:.4;font-size:.85rem">Pedido cancelado</span>`
                        : `<button class="btn-cancelar" onclick="cancelar(${p.id_pedido})">❌ Cancelar pedido</button>
                           <button class="btn-metodo"   onclick="cambiarMetodo(${p.id_pedido},'${p.metodoPago||'EFECTIVO'}')">💳 Cambiar método de pago</button>`
                    }
                </div>
                <div class="inline-msg" id="msg-${p.id_pedido}"></div>
            </div>`;
        cont.appendChild(card);
    });
}

async function toggle(id) {
    const body = document.getElementById("body-" + id);
    const chev = document.getElementById("chev-" + id);
    const open = body.classList.toggle("open");
    chev.textContent = open ? "▲" : "▼";
    if (open) await cargarLineas(id);
}

async function cargarLineas(id) {
    const cont = document.getElementById("lineas-" + id);
    try {
        const res = await fetch(`/api/admin/pedidos/${id}`);
        const p   = await res.json();
        if (!p.lineas?.length) { cont.innerHTML = `<p style="opacity:.4;font-size:.85rem">Sin productos</p>`; return; }
        cont.innerHTML = p.lineas.map(l =>
            `<div class="linea-mini">
                <span>${l.nombre_producto} × ${l.cantidad}</span>
                <span class="sub">${parseFloat(l.subtotal).toFixed(2)} €</span>
            </div>`
        ).join('');
    } catch { cont.innerHTML = `<p style="color:var(--red);font-size:.85rem">Error</p>`; }
}

async function cancelar(id) {
    if (!confirm(`¿Cancelar el pedido #${id}?`)) return;
    await accion(id, { estado: "CANCELADO" }, "Pedido cancelado");
}

async function cambiarMetodo(id, actual) {
    const nuevo = actual === "EFECTIVO" ? "TARJETA" : "EFECTIVO";
    if (!confirm(`¿Cambiar método de ${actual} a ${nuevo}?`)) return;
    await accion(id, { metodoPago: nuevo }, `Método cambiado a ${nuevo}`);
}

async function accion(id, cambios, ok) {
    try {
        const resPed = await fetch(`/api/admin/pedidos/${id}`);
        const ped    = await resPed.json();
        const payload = {
            estado:        cambios.estado        || ped.estado,
            nombreCliente: cambios.nombreCliente || ped.nombreCliente || cliente.nombre,
            metodoPago:    cambios.metodoPago    || ped.metodoPago    || "EFECTIVO",
            items: (ped.lineas || []).map(l => ({
                nombre: l.nombre_producto, cantidad: l.cantidad,
                precio: l.precio_unitario, total: l.subtotal
            }))
        };
        const res = await fetch(`/api/admin/pedidos/${id}`, {
            method:"PUT", headers:{"Content-Type":"application/json"},
            body: JSON.stringify(payload)
        });
        if (!res.ok) throw new Error("HTTP " + res.status);
        inlineMsg(id, "ok", "✅ " + ok);
        setTimeout(cargar, 1200);
    } catch(e) { inlineMsg(id, "err", "❌ " + e.message); }
}

function inlineMsg(id, tipo, txt) {
    const m = document.getElementById("msg-" + id);
    if (!m) return;
    m.className = "inline-msg " + tipo; m.textContent = txt; m.style.display = "block";
}

function fmt(f) {
    if (!f) return "—";
    try {
        const d = new Date(f.replace(" ","T"));
        return d.toLocaleDateString("es-ES") + " " + d.toLocaleTimeString("es-ES",{hour:"2-digit",minute:"2-digit"});
    } catch { return f; }
}

cargar();
