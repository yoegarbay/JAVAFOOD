/* admin/resenas/js/show.js */

const id = new URLSearchParams(location.search).get('id');

function formatearFecha(f) {
    if (!f) return '—';
    try {
        const d = new Date(f.replace(' ', 'T'));
        return d.toLocaleDateString('es-ES') + ' ' +
               d.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
    } catch { return f; }
}

function starsHtml(p) {
    return '★'.repeat(p) + '<span class="empty">' + '★'.repeat(5 - p) + '</span>';
}

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

        document.getElementById('titulo-id').textContent   = '#' + r.id_resena;
        document.getElementById('badge-fecha').textContent = formatearFecha(r.fecha);
        document.getElementById('stars-big').innerHTML     = starsHtml(r.puntuacion);
        document.getElementById('info-producto').textContent  = r.nombre_producto;
        document.getElementById('info-cliente').textContent   = r.nombre_cliente;
        document.getElementById('info-puntuacion').textContent = r.puntuacion + ' / 5 ★';
        document.getElementById('info-fecha').textContent     = formatearFecha(r.fecha);

        const comentarioBox = document.getElementById('comentario-box');
        if (r.comentario) {
            comentarioBox.textContent = r.comentario;
        } else {
            comentarioBox.textContent = 'Sin comentario';
            comentarioBox.classList.add('empty');
        }

        document.getElementById('btn-edit-link').href = `edit.html?id=${r.id_resena}`;

        document.getElementById('loading').style.display   = 'none';
        document.getElementById('contenido').style.display = 'block';
    } catch (err) {
        document.getElementById('loading').innerHTML =
            `<p style="color:#e05555;text-align:center">❌ ${err.message}</p>`;
    }
}

async function eliminar() {
    if (!confirm(`¿Eliminar la reseña #${id}? Esta acción no se puede deshacer.`)) return;
    try {
        const res = await fetch(`/api/admin/resenas/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        location.href = 'index.html';
    } catch (err) {
        alert('Error al eliminar: ' + err.message);
    }
}

cargar();
