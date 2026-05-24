/* admin/pagos/js/create.js */

let productosDB = [];
let lineaCount  = 0;

async function cargarDatos() {
    try {
        const res = await fetch("/api/productos");
        if (res.ok) productosDB = await res.json();
    } catch (err) { console.error("Error productos:", err); }

    try {
        const res = await fetch("/api/clientes");
        if (res.ok) {
            const clientes = await res.json();
            const dl = document.getElementById("dl-clientes");
            clientes.forEach(c => {
                const opt = document.createElement("option");
                opt.value = c.nombre;
                dl.appendChild(opt);
            });
        }
    } catch (err) { console.error("Error clientes:", err); }

    addLinea();
}

function addLinea() {
    lineaCount++;
    const n    = lineaCount;
    const wrap = document.getElementById("lineas-wrap");
    const div  = document.createElement("div");
    div.className = "linea-item";
    div.id = "linea-" + n;

    const opciones = productosDB.map(p =>
        `<option value="${p.id_producto}" data-precio="${p.precio}" data-nombre="${p.nombre}">
            ${p.nombre} — ${parseFloat(p.precio).toFixed(2)} €
        </option>`
    ).join('');

    div.innerHTML = `
        <select id="sel-${n}" onchange="onProductoChange(${n})">
            <option value="">— Selecciona producto —</option>
            ${opciones}
        </select>
        <input type="number" id="cantidad-${n}" value="1" min="1" step="1"
               onkeydown="if(event.key==='-'||event.key==='e'||event.key==='+')event.preventDefault()"
               oninput="sanitizarCantidad(${n}); recalcular()">
        <input type="number" id="precio-${n}" step="0.01" min="0" placeholder="€"
               oninput="recalcular()" readonly>
        <button class="btn-rm-linea" onclick="removeLinea(${n})">✕</button>
    `;
    wrap.appendChild(div);
    recalcular();
}

// FIX: evitar cantidades negativas o cero — fuerza mínimo 1
function sanitizarCantidad(n) {
    const inp = document.getElementById("cantidad-" + n);
    if (!inp) return;
    const val = parseInt(inp.value);
    if (isNaN(val) || val < 1) inp.value = 1;
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
        const qty = Math.max(1, parseInt(document.getElementById("cantidad-" + n)?.value) || 1);
        const prc = parseFloat(document.getElementById("precio-"   + n)?.value) || 0;
        total += qty * prc;
    });
    document.getElementById("total-preview").textContent = total.toFixed(2) + " €";
}

async function crear() {
    const nombreCliente = document.getElementById("nombre-cliente").value.trim();
    const metodoPago    = document.getElementById("metodo-pago").value;
    const btn = document.getElementById("btn-crear");
    const msg = document.getElementById("msg");

    const items = [];
    let valido = true;
    document.querySelectorAll(".linea-item").forEach(div => {
        const n   = div.id.split("-")[1];
        const sel = document.getElementById("sel-" + n);
        const opt = sel?.options[sel.selectedIndex];
        const nombre = opt?.dataset?.nombre || "";
        const cant   = parseInt(document.getElementById("cantidad-" + n)?.value);
        const prec   = parseFloat(document.getElementById("precio-"  + n)?.value);

        // FIX: validación — cantidad debe ser >= 1
        if (!nombre || isNaN(cant) || cant < 1 || isNaN(prec) || prec <= 0) {
            valido = false; return;
        }
        items.push({ nombre, cantidad: cant, precio: prec, total: +(cant * prec).toFixed(2) });
    });

    if (items.length === 0 || !valido) {
        mostrarMsg("err", "❌ Selecciona productos válidos. La cantidad mínima es 1.");
        return;
    }

    btn.disabled = true; btn.textContent = "Creando…"; msg.style.display = "none";

    try {
        const res = await fetch("/api/admin/pedidos", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nombreCliente: nombreCliente || "Admin", metodoPago, items })
        });
        if (!res.ok) {
            const e = await res.json().catch(() => ({}));
            throw new Error(e.detalle || "HTTP " + res.status);
        }
        const data = await res.json();
        mostrarMsg("ok", `✅ Pedido #${data.id_pedido} creado — ${parseFloat(data.total).toFixed(2)} €`);
        setTimeout(() => location.href = "index.html", 1800);
    } catch (err) {
        mostrarMsg("err", "❌ " + err.message);
        btn.disabled = false; btn.textContent = "✅ Crear pedido";
    }
}

function mostrarMsg(tipo, texto) {
    const msg = document.getElementById("msg");
    msg.className = "msg " + tipo;
    msg.textContent = texto;
    msg.style.display = "block";
}

cargarDatos();