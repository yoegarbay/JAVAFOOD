/* admin/resenas/js/create.js */
/* IMPORTANTE SNAKE_CASE: mandar id_cliente e id_producto (no idCliente/idProducto) */

var puntuacionActual = 0;

async function cargarDatos() {
    try {
        var resClientes  = await fetch('/api/clientes');
        var resProductos = await fetch('/api/productos');

        if (resClientes.ok) {
            var clientes = await resClientes.json();
            var sel = document.getElementById('sel-cliente');
            clientes.forEach(function(c) {
                var opt = document.createElement('option');
                opt.value = c.id_cliente;
                opt.textContent = c.nombre + ' (' + c.email + ')';
                sel.appendChild(opt);
            });
        }

        if (resProductos.ok) {
            var productos = await resProductos.json();
            var sel2 = document.getElementById('sel-producto');
            productos.forEach(function(p) {
                var opt = document.createElement('option');
                opt.value = p.id_producto;
                opt.textContent = p.nombre + ' — ' + parseFloat(p.precio).toFixed(2) + ' EUR';
                sel2.appendChild(opt);
            });
        }

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
    cargarDatos();
});

async function crear() {
    var idCliente  = parseInt(document.getElementById('sel-cliente').value);
    var idProducto = parseInt(document.getElementById('sel-producto').value);
    var comentario = document.getElementById('inp-comentario').value.trim();
    var btn = document.getElementById('btn-save');
    var msg = document.getElementById('msg');

    if (!idCliente)       { mostrarMsg('err', 'Selecciona un cliente.');    return; }
    if (!idProducto)      { mostrarMsg('err', 'Selecciona un producto.');   return; }
    if (!puntuacionActual){ mostrarMsg('err', 'Selecciona una puntuacion.'); return; }

    btn.disabled = true; btn.textContent = 'Creando...'; msg.style.display = 'none';

    try {
        // SNAKE_CASE: id_cliente, id_producto (no idCliente, idProducto)
        var res = await fetch('/api/admin/resenas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                id_cliente:  idCliente,
                id_producto: idProducto,
                puntuacion:  puntuacionActual,
                comentario:  comentario || null
            })
        });
        if (!res.ok) {
            var e = await res.json().catch(function() { return {}; });
            throw new Error(e.detalle || 'HTTP ' + res.status);
        }
        mostrarMsg('ok', 'Resena creada correctamente');
        setTimeout(function() { location.href = 'index.html'; }, 1500);
    } catch (err) {
        mostrarMsg('err', err.message);
        btn.disabled = false; btn.textContent = 'Crear resena';
    }
}

function mostrarMsg(tipo, texto) {
    var msg = document.getElementById('msg');
    msg.className = 'msg ' + tipo;
    msg.textContent = texto;
    msg.style.display = 'block';
}