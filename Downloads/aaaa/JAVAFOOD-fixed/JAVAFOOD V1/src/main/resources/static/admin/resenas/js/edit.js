/* admin/resenas/js/edit.js */

const id = new URLSearchParams(location.search).get('id');
let puntuacionSeleccionada = 0;

const LABELS = ['', 'Muy malo 😞', 'Malo 😐', 'Normal 🙂', 'Bueno 😊', 'Excelente 🤩'];

async function cargar() {
    if (!id) {
        document.getElementById('loading').innerHTML =
            "<p style='color:#e05555;text-align:center'>❌ No se indicó id de reseña.</p>";
        return;
    }
    try {
        const res = await fetch(`/api/admin/resenas/${id}`);
        if (!res.ok) throw new Error('HTTP ' + res.status);
        const r = await res.json();

        document.getElementById('titulo-id').textContent    = '#' + r.id_resena;
        document.getElementById('info-producto').textContent = r.nombre_producto;
        document.getElementById('info-cliente').textContent  = r.nombre_cliente;
        document.getElementById('inp-comentario').value      = r.comentario || '';

        seleccionarEstrella(r.puntuacion);

        document.getElementById('loading').style.display   = 'none';
        document.getElementById('contenido').style.display = 'block';
    } catch (err) {
        document.getElementById('loading').innerHTML =
            `<p style="color:#e05555;text-align:center">❌ ${err.message}</p>`;
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

async function guardar() {
    if (puntuacionSeleccionada < 1) {
        mostrarMsg('err', 'Selecciona una puntuación (1-5 estrellas)');
        return;
    }
    const btn = document.getElementById('btn-save');
    btn.disabled = true;
    btn.textContent = 'Guardando…';
    limpiarMsg();

    try {
        const res = await fetch(`/api/admin/resenas/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                puntuacion: puntuacionSeleccionada,
                comentario: document.getElementById('inp-comentario').value.trim() || null
            })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.detalle || data.message || 'Error del servidor');
        mostrarMsg('ok', '✅ Reseña actualizada correctamente');
        setTimeout(() => location.href = `show.html?id=${id}`, 900);
    } catch (err) {
        mostrarMsg('err', '❌ ' + err.message);
        btn.disabled = false;
        btn.textContent = '💾 Guardar cambios';
    }
}

function mostrarMsg(tipo, texto) {
    const el = document.getElementById('msg');
    el.className = 'msg ' + tipo;
    el.textContent = texto;
    el.style.display = 'block';
}
function limpiarMsg() {
    const el = document.getElementById('msg');
    el.style.display = 'none';
}

cargar();
