/**
 * resenas.js — Sistema de reseñas con estrellas para JAVAFOOD
 * Incluir DESPUÉS de main.js en las páginas de productos.
 */
(function () {

  /* ── CSS ─────────────────────────────────────────────────────── */
  const CSS = `
    /* Estrellas en las cards */
    .card-stars {
      display: flex; align-items: center; gap: 4px;
      margin-top: 6px; cursor: pointer;
      font-size: 0.72rem; color: #a0a0a0;
      padding: 2px 0;
    }
    .card-stars .stars-row { display:inline-flex; gap:1px; }
    .card-stars .star      { font-size: 1rem; line-height:1; }
    .card-stars .star.full  { color: #f5a623; }
    .card-stars .star.half  { color: #f5a623; }
    .card-stars .star.empty { color: #444; }
    .card-stars .stars-count { font-size:0.7rem; color:#777; margin-left:3px; }

    /* Modal de reseña */
    #modal-resena {
      display: none; position: fixed; inset: 0; z-index: 9999;
      background: rgba(0,0,0,0.75); justify-content: center; align-items: center;
    }
    #modal-resena.visible { display: flex; }
    .resena-card {
      background: #1e1209; border: 1.5px solid rgba(209,122,34,0.35);
      border-radius: 22px; padding: 36px 32px; max-width: 440px; width: 90%;
      box-shadow: 0 0 60px rgba(209,122,34,0.2);
      animation: resenaIn .25s ease;
    }
    @keyframes resenaIn {
      from { opacity:0; transform:translateY(20px) scale(.97); }
      to   { opacity:1; transform:none; }
    }
    .resena-card h3 {
      color: #f5e8d3; font-size: 1.2rem; font-weight: 700;
      margin-bottom: 4px;
    }
    .resena-card .resena-prod {
      color: #d17a22; font-size: 0.9rem; margin-bottom: 20px;
    }
    /* Estrellas interactivas en el modal */
    .resena-stars-input {
      display: flex; gap: 6px; margin-bottom: 18px;
    }
    .resena-stars-input .rstar {
      font-size: 2.2rem; cursor: pointer; transition: transform .12s;
      color: #444; user-select: none;
    }
    .resena-stars-input .rstar.on { color: #f5a623; }
    .resena-stars-input .rstar:hover { transform: scale(1.15); }

    .resena-card textarea {
      width: 100%; min-height: 90px; border-radius: 12px;
      background: #120c05; border: 1px solid rgba(209,122,34,0.3);
      color: #f5e8d3; padding: 10px 14px; font-size: 0.9rem;
      resize: vertical; margin-bottom: 18px; font-family: inherit;
    }
    .resena-card textarea:focus {
      outline: none; border-color: rgba(209,122,34,0.7);
    }
    .resena-btns { display: flex; gap: 10px; }
    .btn-enviar-resena {
      flex:1; padding: 12px; border-radius: 12px;
      background: linear-gradient(45deg,#d17a22,#f5e8d3);
      color: #1e1209; font-weight: 700; border: none;
      cursor: pointer; font-size: 0.95rem; transition: .2s;
    }
    .btn-enviar-resena:hover { opacity: .9; transform: translateY(-1px); }
    .btn-enviar-resena:disabled { opacity:.5; cursor:not-allowed; }
    .btn-cerrar-resena {
      padding: 12px 20px; border-radius: 12px;
      background: transparent; border: 1px solid rgba(209,122,34,0.3);
      color: #f5e8d3; cursor: pointer; font-size: 0.9rem; transition: .2s;
    }
    .btn-cerrar-resena:hover { border-color:#d17a22; }
    .resena-msg {
      font-size: 0.85rem; margin-bottom: 12px; padding: 8px 12px;
      border-radius: 8px; display: none;
    }
    .resena-msg.err { display:block; background:rgba(255,60,60,.1); color:#ff6b6b; }
    .resena-msg.ok  { display:block; background:rgba(80,200,80,.1); color:#6bff8a; }

    /* Lista de reseñas dentro del modal */
    .resenas-lista {
      max-height: 220px; overflow-y: auto; margin-top: 18px;
      border-top: 1px solid rgba(209,122,34,0.15); padding-top: 14px;
    }
    .resena-item {
      margin-bottom: 14px; padding-bottom: 14px;
      border-bottom: 1px solid rgba(255,255,255,0.05);
    }
    .resena-item:last-child { border-bottom: none; }
    .resena-item .ri-header {
      display: flex; justify-content: space-between; align-items:center;
      margin-bottom: 4px;
    }
    .resena-item .ri-nombre { color: #d17a22; font-size: 0.85rem; font-weight:600; }
    .resena-item .ri-fecha  { color: #555; font-size: 0.72rem; }
    .resena-item .ri-stars  { font-size: 0.85rem; letter-spacing:1px; }
    .resena-item .ri-texto  { color: #ccc; font-size: 0.85rem; line-height:1.5; }
    .no-resenas { color:#555; font-size:0.85rem; text-align:center; padding:10px 0; }
  `;

  const styleEl = document.createElement('style');
  styleEl.textContent = CSS;
  document.head.appendChild(styleEl);

  /* ── Helpers de estrellas ────────────────────────────────────── */
  function starsHtml(promedio) {
    let html = '<span class="stars-row">';
    for (let i = 1; i <= 5; i++) {
      if (promedio >= i)           html += '<span class="star full">★</span>';
      else if (promedio >= i - 0.5) html += '<span class="star half">★</span>';
      else                          html += '<span class="star empty">★</span>';
    }
    html += '</span>';
    return html;
  }

  /* ── CSS ya inyectado arriba ─────────────────────────────────── */

  /* ── Cargar estrellas en un grid ─────────────────────────────── */
  window.cargarEstrellasGrid = async function (gridId) {
    const grid = document.getElementById(gridId);
    if (!grid) return;

    const cards = [...grid.querySelectorAll('[data-id]')];
    if (!cards.length) return;

    const ids = [...new Set(cards.map(c => c.dataset.id))];

    // Fetch paralelo
    const resultados = await Promise.all(
      ids.map(id =>
        fetch(`/api/resenas/producto/${id}`)
          .then(r => r.ok ? r.json() : { promedio: 0, total: 0, resenas: [] })
          .catch(() => ({ promedio: 0, total: 0, resenas: [] }))
      )
    );

    const mapa = {};
    ids.forEach((id, i) => { mapa[id] = resultados[i]; });

    cards.forEach(card => {
      const id   = card.dataset.id;
      const data = mapa[id] || { promedio: 0, total: 0 };
      const div  = card.querySelector('.card-stars');
      if (!div) return;

      const labelCount = data.total > 0
        ? `<span class="stars-count">${data.promedio.toFixed(1)} (${data.total})</span>`
        : `<span class="stars-count">Sin reseñas</span>`;

      div.innerHTML = starsHtml(data.promedio) + labelCount;
      div.title     = data.total > 0
        ? `${data.promedio.toFixed(1)} de 5 · ${data.total} reseña(s). Haz clic para ver o reseñar.`
        : 'Sé el primero en reseñar este producto';
    });
  };

  /* ── Modal de reseña ─────────────────────────────────────────── */
  let _idProducto   = null;
  let _puntuacion   = 0;

  function inyectarModal() {
    const html = `
      <div id="modal-resena">
        <div class="resena-card">
          <h3>⭐ Deja tu reseña</h3>
          <p class="resena-prod" id="resena-prod-nombre"></p>

          <div class="resena-stars-input" id="resena-stars-input">
            <span class="rstar" data-v="1">★</span>
            <span class="rstar" data-v="2">★</span>
            <span class="rstar" data-v="3">★</span>
            <span class="rstar" data-v="4">★</span>
            <span class="rstar" data-v="5">★</span>
          </div>

          <textarea id="resena-comentario" placeholder="Cuenta tu experiencia (opcional)..."></textarea>

          <div class="resena-msg" id="resena-msg"></div>

          <div class="resena-btns">
            <button class="btn-enviar-resena" id="btn-enviar-resena" onclick="submitResena()">
              Enviar reseña
            </button>
            <button class="btn-cerrar-resena" onclick="cerrarModalResena()">Cancelar</button>
          </div>

          <!-- Lista de reseñas existentes -->
          <div class="resenas-lista" id="resenas-lista"></div>
        </div>
      </div>
    `;
    document.body.insertAdjacentHTML('beforeend', html);

    // Estrellas interactivas
    document.querySelectorAll('.rstar').forEach(star => {
      star.addEventListener('click', () => seleccionarEstrella(parseInt(star.dataset.v)));
      star.addEventListener('mouseenter', () => resaltarEstrellas(parseInt(star.dataset.v)));
      star.addEventListener('mouseleave', () => resaltarEstrellas(_puntuacion));
    });
  }

  function seleccionarEstrella(n) {
    _puntuacion = n;
    resaltarEstrellas(n);
  }

  function resaltarEstrellas(n) {
    document.querySelectorAll('.rstar').forEach(s => {
      s.classList.toggle('on', parseInt(s.dataset.v) <= n);
    });
  }

  function cerrarModalResena() {
    const modal = document.getElementById('modal-resena');
    if (modal) modal.classList.remove('visible');
    _idProducto = null;
    _puntuacion = 0;
  }
  window.cerrarModalResena = cerrarModalResena;

  /* ── Abrir modal ─────────────────────────────────────────────── */
  window.abrirModalResena = async function (idProducto, nombreProducto) {
    _idProducto = idProducto;
    _puntuacion = 0;

    const modal  = document.getElementById('modal-resena');
    const msg    = document.getElementById('resena-msg');
    const btn    = document.getElementById('btn-enviar-resena');
    const lista  = document.getElementById('resenas-lista');
    const textarea = document.getElementById('resena-comentario');

    // Reset UI
    document.getElementById('resena-prod-nombre').textContent = nombreProducto;
    resaltarEstrellas(0);
    textarea.value = '';
    msg.className  = 'resena-msg';
    msg.textContent = '';
    btn.disabled    = false;
    btn.style.display = 'block';
    lista.innerHTML = '<p class="no-resenas">Cargando reseñas…</p>';

    modal.classList.add('visible');

    // Cargar reseñas existentes
    const data = await fetch(`/api/resenas/producto/${idProducto}`)
      .then(r => r.ok ? r.json() : null)
      .catch(() => null);

    if (data && data.resenas.length > 0) {
      lista.innerHTML = data.resenas.map(r => `
        <div class="resena-item">
          <div class="ri-header">
            <span class="ri-nombre">👤 ${r.nombre_cliente}</span>
            <span class="ri-fecha">${r.fecha.replace('T', ' ').substring(0, 16)}</span>
          </div>
          <div class="ri-stars">${'★'.repeat(r.puntuacion)}${'☆'.repeat(5 - r.puntuacion)}</div>
          ${r.comentario ? `<p class="ri-texto">${r.comentario}</p>` : ''}
        </div>
      `).join('');
    } else {
      lista.innerHTML = '<p class="no-resenas">Aún no hay reseñas. ¡Sé el primero!</p>';
    }

    // Verificar si el usuario puede reseñar
    const cliente = JSON.parse(sessionStorage.getItem('javafoodUser') || 'null');
    if (!cliente) {
      mostrarMsg('err', '🔒 Inicia sesión para dejar una reseña.');
      btn.style.display = 'none';
      return;
    }

    const check = await fetch(`/api/resenas/puede/${idProducto}?idCliente=${cliente.id_cliente}`)
      .then(r => r.ok ? r.json() : null)
      .catch(() => null);

    if (!check) return;

    if (check.yaReseno) {
      mostrarMsg('ok', '✅ Ya has reseñado este producto. ¡Gracias!');
      btn.style.display = 'none';
    } else if (!check.puede) {
      mostrarMsg('err', '🛒 Debes haber pedido este producto para poder reseñarlo.');
      btn.style.display = 'none';
    }
  };

  /* ── Enviar reseña ───────────────────────────────────────────── */
  window.submitResena = async function () {
    const cliente = JSON.parse(sessionStorage.getItem('javafoodUser') || 'null');
    if (!cliente) { mostrarMsg('err', 'Inicia sesión primero'); return; }
    if (_puntuacion < 1) { mostrarMsg('err', 'Selecciona una puntuación (1-5 estrellas)'); return; }

    const btn       = document.getElementById('btn-enviar-resena');
    const comentario = document.getElementById('resena-comentario').value.trim();

    btn.disabled    = true;
    btn.textContent = 'Enviando…';

    try {
      const res = await fetch('/api/resenas', {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          id_cliente:  cliente.id_cliente,
          id_producto: _idProducto,
          puntuacion:  _puntuacion,
          comentario:  comentario || null
        })
      });

      const data = await res.json();
      if (!res.ok) throw new Error(data.detalle || data.message || 'Error del servidor');

      mostrarMsg('ok', '✅ ¡Reseña enviada! Gracias por tu opinión.');
      btn.style.display = 'none';

      // Refrescar estrellas en todas las cards del producto
      document.querySelectorAll(`[data-id="${_idProducto}"] .card-stars`).forEach(div => {
        div.innerHTML = '⭐ Actualizando…';
      });
      // Recargar estrellas para ese producto
      const updated = await fetch(`/api/resenas/producto/${_idProducto}`)
        .then(r => r.ok ? r.json() : null).catch(() => null);
      if (updated) {
        document.querySelectorAll(`[data-id="${_idProducto}"] .card-stars`).forEach(div => {
          const labelCount = updated.total > 0
            ? `<span class="stars-count">${updated.promedio.toFixed(1)} (${updated.total})</span>`
            : '<span class="stars-count">Sin reseñas</span>';
          div.innerHTML = starsHtml(updated.promedio) + labelCount;
        });
      }

    } catch (e) {
      mostrarMsg('err', '❌ ' + e.message);
      btn.disabled    = false;
      btn.textContent = 'Enviar reseña';
    }
  };

  function mostrarMsg(tipo, texto) {
    const msg = document.getElementById('resena-msg');
    msg.className   = 'resena-msg ' + tipo;
    msg.textContent = texto;
  }

  /* ── Delegación de clic en .card-stars ──────────────────────── */
  document.addEventListener('click', e => {
    const starsDiv = e.target.closest('.card-stars');
    if (!starsDiv) return;
    const card = starsDiv.closest('[data-id]');
    if (!card) return;
    const id     = card.dataset.id;
    const nombre = card.querySelector('h4')?.textContent?.trim() || 'Producto';
    window.abrirModalResena(id, nombre);
  });

  // Cerrar al clicar fuera
  document.addEventListener('click', e => {
    const modal = document.getElementById('modal-resena');
    if (modal && e.target === modal) cerrarModalResena();
  });

  /* ── Init ────────────────────────────────────────────────────── */
  document.addEventListener('DOMContentLoaded', inyectarModal);

})();
