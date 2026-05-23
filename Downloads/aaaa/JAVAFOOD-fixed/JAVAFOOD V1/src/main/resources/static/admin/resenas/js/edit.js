/* admin/resenas/js/edit.js */

var id = new URLSearchParams(location.search).get('id');
var puntuacionActual = 0;

async function cargar() {
    if (!id) {
        document.getElementById('loading').innerHTML = "<p style='color:#e05555;text-align:center'>No se indico id.</p>";
        return;
    }
    try {
        var res = await fetch('/api/admin/resenas/' + id);
        if (!res.ok) throw new Error('HTTP ' + res.status);
        var r = await res.json();

        document.getElementById('titulo-id').textContent    = '#' + r.id_resena;
        document.getElementById('info-producto').textContent = r.nombre_producto || '—';
        document.getElementById('info-cliente').textContent  = r.nombre_cliente  || '—';
        document.getElementById('inp-comentario').value     = r.comentario || '';

        setEstrellas(r.puntuacion);

        document.getElementById('loading').style.display   = 'none';
        document.getElementById('contenido').style.display = 'block';
    } catch (err) {
        document.getElementById('loading').innerHTML =
            '<p style="color:#e05555;text-align:center">' + err.message + '</p>';
    }
}

function setEstrellas(n) {
    puntuacionActual = n;
    document.querySelectorAll('#star-picker .s').forEach(function(el) {
        el.classList.toggle('on', parseInt(el.dataset.v) <= n);
    });
    var labels = ['', 'Muy mala', 'Mala', 'Regular', 'Buena', 'Excelente'];
    document.getElementById('star-label').textContent = labels[n] || 'Selecciona una puntuacion';
}

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('#star-picker .s').forEach(function(el) {
        el.addEventListener('click',     function() { setEstrellas(parseInt(el.dataset.v)); });
        el.addEventListener('mouseover', function() {
            var v = parseInt(el.dataset.v);
            document.querySelectorAll('#star-picker .s').forEach(function(s) {
                s.classList.toggle('on', parseInt(s.dataset.v) <= v);
            });
        });
        el.addEventListener('mouseout', function() { setEstrellas(puntuacionActual); });
    });
});

async function guardar() {
    if (!puntuacionActual) { mostrarMsg('err', 'Selecciona una puntuacion.'); return; }
    var comentario = document.getElementById('inp-comentario').value.trim();
    var btn = document.getElementById('btn-save');
    btn.disabled = true; btn.textContent = 'Guardando...';

    try {
        var res = await fetch('/api/admin/resenas/' + id, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ puntuacion: puntuacionActual, comentario: comentario || null })
        });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        mostrarMsg('ok', 'Resena actualizada correctamente');
        setTimeout(function() { location.href = 'index.html'; }, 1500);
    } catch (err) {
        mostrarMsg('err', err.message);
        btn.disabled = false; btn.textContent = 'Guardar cambios';
    }
}

function mostrarMsg(tipo, texto) {
    var msg = document.getElementById('msg');
    msg.className = 'msg ' + tipo;
    msg.textContent = texto;
    msg.style.display = 'block';
}

cargar();