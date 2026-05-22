/* admin/pagos/js/edit.js — edición completa del pedido */

const id = new URLSearchParams(location.search).get("id");
let productosDB = [];
let lineaCount  = 0;

async function cargar() {
    if (!id) {
        document.getElementById("loading").innerHTML =
            "<p style='color:#e05555;text-align:center'>❌ No se indicó id de pedido.</p>";
        return;
    }
    try {
        const [resPedido, resProd] = await Promise.all([
            fetch(`/api/admin/pedidos/${id}`),
            fetch(`/api/productos`)
        ]);
        if (!resPedido.ok) throw new Error("HTTP " + resPedido.status);
        productosDB = resProd.ok ? await resProd.json() : [];

        const p = await resPedido.json();

        document.getElementById("titulo-id").textContent  = "Pedido #" + p.id_pedido;
        document.getElementById("info-fecha").textContent = formatFecha(p.fecha);
        document.getElementById("inp-nombre").value       = p.nombreCliente || "";
        document.getElementById("sel-metodo").value       = p.metodoPago    || "EFECTIVO";
        document.getElementById("sel-estado").value       = p.estado        || "PAGADO";

        // Cargar líneas existentes
        if (p.lineas && p.lineas.length > 0) {
            p.lineas.forEach(l => addLinea(l.nombre_producto, l.cantidad, l.precio_unitario));
        } else {
            addLinea();
        }

        document.getElementById("loading").style.display   = "none";
        document.getElementById("contenido").style.display = "block";

    } catch (err) {
        document.getElementById("loading").innerHTML =
            `<p style="color:#e05555;text-align:center">❌ ${err.message}</p>`;
    }
}

function addLinea(nombrePre, cantidadPre, precioPre) {
    lineaCount++;
    const n    = lineaCount;
    const wrap = document.getElementById("lineas-wrap");
    const div  = document.createElement("div");
    div.className = "linea-item";
    div.id = "linea-" + n;

    const opciones = productosDB.map(p =>
        `<option value="${p.id_producto}" data-precio="${p.precio}" data-nombre="${p.nombre}"
            ${p.nombre === nombrePre ? "selected" : ""}>
            ${p.nombre} — ${parseFloat(p.precio).toFixed(2)} €
        </option>`
    ).join('');

    div.innerHTML = `
        <select id="sel-${n}" onchange="onProductoChange(${n})">
            <option value="">— Producto —</option>
            ${opciones}
        </select>
        <input type="number" id="cantidad-${n}" value="${cantidadPre || 1}" min="1" oninput="recalcular()">
        <input type="number" id="precio-${n}"   value="${precioPre  ? parseFloat(precioPre).toFixed(2) : ''}" step="0.01" min="0" oninput="recalcular()" readonly>
        <button class="btn-rm-linea" onclick="removeLinea(${n})">✕</button>
    `;
    wrap.appendChild(div);
    recalcular();
}

function onProductoChange(n) {
    const sel = document.getElementById("sel-" + n);
    const opt = sel.options[sel.selectedIndex];
    document.getElementById("precio-" + n).value =
        opt?.dataset?.precio ? parseFloat(opt.dataset.precio).toFixed(2) : "";
    recalcular();
}

function removeLinea(n) {
    document.getElementById("linea-" + n)?.remove();
    recalcular();
}

function recalcular() {
    let total = 0;
    document.querySelectorAll(".linea-item").forEach(div => {
        const n   = div.id.split("-")[1];
        const qty = parseFloat(document.getElementById("cantidad-" + n)?.value) || 0;
        const prc = parseFloat(document.getElementById("precio-"   + n)?.value) || 0;
        total += qty * prc;
    });
    document.getElementById("total-preview").textContent = total.toFixed(2) + " €";
}

async function guardar() {
    const estado        = document.getElementById("sel-estado").value;
    const nombreCliente = document.getElementById("inp-nombre").value.trim();
    const metodoPago    = document.getElementById("sel-metodo").value;
    const btn           = document.getElementById("btn-guardar");
    const msg           = document.getElementById("msg");

    const items = [];
    let valido  = true;
    document.querySelectorAll(".linea-item").forEach(div => {
        const n      = div.id.split("-")[1];
        const sel    = document.getElementById("sel-" + n);
        const opt    = sel?.options[sel.selectedIndex];
        const nombre = opt?.dataset?.nombre || "";
        const cant   = parseInt(document.getElementById("cantidad-" + n)?.value);
        const prec   = parseFloat(document.getElementById("precio-"   + n)?.value);
        if (!nombre || !cant || !prec) { valido = false; return; }
        items.push({ nombre, cantidad: cant, precio: prec, total: +(cant * prec).toFixed(2) });
    });

    if (!valido || items.length === 0) {
        mostrarMsg("err", "❌ Selecciona un producto en cada línea.");
        return;
    }

    btn.disabled = true; btn.textContent = "Guardando…";
    msg.style.display = "none";

    try {
        const res = await fetch(`/api/admin/pedidos/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ estado, nombreCliente, metodoPago, items })
        });
        if (!res.ok) throw new Error("HTTP " + res.status);
        mostrarMsg("ok", "✅ Pedido actualizado correctamente");
        setTimeout(() => location.href = "index.html", 1500);
    } catch (err) {
        mostrarMsg("err", "❌ " + err.message);
        btn.disabled = false; btn.textContent = "💾 Guardar cambios";
    }
}

function mostrarMsg(tipo, texto) {
    const msg = document.getElementById("msg");
    msg.className = "msg " + tipo;
    msg.textContent = texto;
    msg.style.display = "block";
}

function formatFecha(f) {
    if (!f) return "—";
    try {
        const d = new Date(f.replace(" ", "T"));
        return d.toLocaleDateString("es-ES") + " " +
               d.toLocaleTimeString("es-ES", { hour:"2-digit", minute:"2-digit" });
    } catch { return f; }
}

cargar();
