/* admin/resenas/js/index.js */

let resenasGlobal = [];

async function cargarResenas() {
    try {
        const res = await fetch('/api/admin/resenas');
        if (!res.ok) throw new Error('HTTP ' + res.status);
        resenasGlobal = await res.json();
    } catch (err) {
        console.error('Error al cargar reseñas:', err);
        resenasGlobal = [];
    }
    actualizarStats(resenasGlobal);
    renderizarTabla(resenasGlobal);
    document.getElementById('loading').style.display = 'none';
    document.getElementById('tabla').style.display = resenasGlobal.length ? 'table' : 'none';
}

function renderizarTabla(resenas) {
    const tbody = document.getElementById('tbody');
    const empty = document.getElementById('empty');
    tbody.innerHTML = '';

    if (resenas.length === 0) {
        empty.style.display = 'block';
        document.getElementById('tabla').style.display = 'none';
        return;
    }
    empty.style.display = 'none';
    document.getElementById('tabla').style.display = 'table';

    resenas.forEach(r => {
        const estrellas = '★'.repeat(r.puntuacion) + '<span class="stars-empty">' + '★'.repeat(5 - r.puntuacion) + '</span>';
        const comentario = r.comentario ? escHtml(r.comentario) : '<em style="opacity:.35">Sin comentario</em>';
        const fecha = formatearFecha(r.fecha);
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><strong>#${r.id_resena}</strong></td>
            <td>${escHtml(r.nombre_producto)}</td>
            <td>${escHtml(r.nombre_cliente)}</td>
            <td><span class="stars">${estrellas}</span></td>
            <td class="td-comment">${comentario}</td>
            <td class="td-fecha">${fecha}</td>
            <td>
              <div class="actions">
                <a href="show.html?id=${r.id_resena}" class="btn-ver">👁 Ver</a>
                <button class="btn-del" onclick="eliminar(${r.id_resena}, this)">🗑 Borrar</button>
              </div>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function filtrar() {
    const texto = document.getElementById('input-buscar').value.trim().toLowerCase();
    if (!texto) { renderizarTabla(resenasGlobal); return; }
    const filtradas = resenasGlobal.filter(r =>
        r.nombre_producto.toLowerCase().includes(texto) ||
        r.nombre_cliente.toLowerCase().includes(texto)  ||
        String(r.puntuacion).includes(texto)             ||
        (r.comentario || '').toLowerCase().includes(texto)
    );
    renderizarTabla(filtradas);
}

function limpiarFiltro() {
    document.getElementById('input-buscar').value = '';
    renderizarTabla(resenasGlobal);
}

async function eliminar(id, boton) {
    if (!confirm(`¿Eliminar la reseña #${id}? Esta acción no se puede deshacer.`)) return;
    boton.disabled = true;
    boton.textContent = '…';
    try {
        const res = await fetch(`/api/admin/resenas/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        resenasGlobal = resenasGlobal.filter(r => r.id_resena !== id);
        actualizarStats(resenasGlobal);
        renderizarTabla(resenasGlobal);
    } catch (err) {
        alert('No se pudo eliminar la reseña #' + id);
        boton.disabled = false;
        boton.textContent = '🗑 Borrar';
    }
}

function actualizarStats(resenas) {
    document.getElementById('stat-total').textContent = resenas.length;
    const promedio = resenas.length
        ? (resenas.reduce((acc, r) => acc + r.puntuacion, 0) / resenas.length).toFixed(1)
        : '—';
    document.getElementById('stat-promedio').textContent = resenas.length ? promedio + ' ★' : '—';
    const hoy = new Date().toISOString().slice(0, 10);
    document.getElementById('stat-hoy').textContent =
        resenas.filter(r => r.fecha && r.fecha.startsWith(hoy)).length;
}

function formatearFecha(f) {
    if (!f) return '—';
    try {
        const d = new Date(f.replace(' ', 'T'));
        return d.toLocaleDateString('es-ES') + ' ' +
               d.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
    } catch { return f; }
}

function escHtml(str) {
    return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

cargarResenas();
