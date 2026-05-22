/* admin/resenas/js/create.js */

let puntuacionSeleccionada = 0;
const LABELS = ['', 'Muy malo 😞', 'Malo 😐', 'Normal 🙂', 'Bueno 😊', 'Excelente 🤩'];

async function cargarDatos() {
    try {
        const [resClientes, resProductos] = await Promise.all([
            fetch('/api/clientes'),
            fetch('/api/productos')
        ]);

        if (resClientes.ok) {
            const clientes = await resClientes.json();
            const sel = document.getElementById('sel-cliente');
            clientes.forEach(c => {
                const opt = document.createElement('option');
                opt.value = c.id_cliente;
                opt.textContent = `${c.nombre} (${c.email})`;
                sel.appendChild(opt);
            });
        }

        if (resProductos.ok) {
            const productos = await resProductos.json();
            const sel = document.getElementById('sel-producto');
            productos.forEach(p => {
                const opt = document.createElement('option');
                opt.value = p.id_producto;
                opt.textContent = `${p.nombre} — ${parseFloat(p.precio).toFixed(2)} €`;
                sel.appendChild(opt);
            });
        }

        document.getElementById('loading').style.display   = 'none';
        document.getElementById('contenido').style.display = 'block';
    } catch (err) {
        document.getElementById('loading').innerHTML =
            `<p style="color:#e05555;text-align:center">❌ Error al cargar datos: ${err.message}</p>`;
    }
}

function seleccionarEstrella(n) {
    puntuacionSeleccionada = n;
    resaltarEstrellas(n);
    document.getElementById('star-label').textContent = LABELS[n] || '';
}

function resaltarEstrellas(n) {
    document.querySelectorAll('#star-picker .s').forEach(s => {
        s.classList.toggle('on', parseInt(s.dataset.v) <= n);
    });
}

document.querySelectorAll('#star-picker .s').forEach(s => {
    s.addEventListener('click', () => seleccionarEstrella(parseInt(s.dataset.v)));
    s.addEventListener('mouseenter', () => resaltarEstrellas(parseInt(s.dataset.v)));
    s.addEventListener('mouseleave', () => resaltarEstrellas(puntuacionSeleccionada));
});

async function crear() {
    const idCliente  = parseInt(document.getElementById('sel-cliente').value);
    const idProducto = parseInt(document.getElementById('sel-producto').value);

    if (!idCliente)              { mostrarMsg('err', 'Selecciona un cliente'); return; }
    if (!idProducto)             { mostrarMsg('err', 'Selecciona un producto'); return; }
    if (puntuacionSeleccionada < 1) { mostrarMsg('err', 'Selecciona una puntuación'); return; }

    const btn = document.getElementById('btn-save');
    btn.disabled = true;
    btn.textContent = 'Creando…';
    limpiarMsg();

    try {
        const res = await fetch('/api/admin/resenas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                id_cliente:  idCliente,
                id_producto: idProducto,
                puntuacion:  puntuacionSeleccionada,
                comentario:  document.getElementById('inp-comentario').value.trim() || null
            })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.detalle || data.message || 'Error del servidor');
        mostrarMsg('ok', '✅ Reseña creada correctamente');
        setTimeout(() => location.href = `show.html?id=${data.id_resena}`, 900);
    } catch (err) {
        mostrarMsg('err', '❌ ' + err.message);
        btn.disabled = false;
        btn.textContent = '✅ Crear reseña';
    }
}

function mostrarMsg(tipo, texto) {
    const el = document.getElementById('msg');
    el.className = 'msg ' + tipo;
    el.textContent = texto;
    el.style.display = 'block';
}
function limpiarMsg() {
    document.getElementById('msg').style.display = 'none';
}

cargarDatos();
