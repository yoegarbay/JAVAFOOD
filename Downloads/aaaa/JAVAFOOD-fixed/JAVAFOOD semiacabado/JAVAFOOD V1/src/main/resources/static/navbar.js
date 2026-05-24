/**
 * JAVAFOOD — Navbar global
 * Se inyecta en todas las páginas.
 * Requiere: JAVAFOOD.css cargado antes, main.js después (para carrito).
 */
(function () {

    /* ── ESTILOS ─────────────────────────────────────────────────── */
    const CSS = `
:root {
    --nb-bg:     #0d0804;
    --nb-bg2:    #1a0d08;
    --nb-border: rgba(209,122,34,0.3);
    --nb-orange: #d17a22;
    --nb-cream:  #f5e8d3;
    --nb-h:      58px;
}

#jf-navbar {
    position: fixed;
    top: 0; left: 0; right: 0;
    z-index: 9000;
    background: var(--nb-bg);
    border-bottom: 2px solid var(--nb-orange);
    box-shadow: 0 0 20px rgba(209,122,34,0.25);
    font-family: 'Space Grotesk', sans-serif;
}

#jf-nav-main {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 0 5% 0 18px;
    height: var(--nb-h);
}

/* LOGO */
.jf-logo {
    text-decoration: none;
    white-space: nowrap;
    flex-shrink: 0;
}
.jf-logo-text {
    font-size: 1.5rem;
    font-weight: 900;
    color: #fff;
    letter-spacing: -1px;
}
.jf-logo-text span { color: var(--nb-orange); }

/* SEARCH */
.jf-search {
    display: flex;
    flex: 1;
    max-width: 700px;
    height: 40px;
    border-radius: 8px;
    overflow: hidden;
    border: 2px solid var(--nb-orange);
    box-shadow: 0 0 8px rgba(209,122,34,0.2);
}

.jf-search-cat {
    background: rgba(209,122,34,0.15);
    border: none;
    border-right: 1px solid var(--nb-border);
    color: var(--nb-cream);
    padding: 0 10px;
    font-size: 0.78rem;
    cursor: pointer;
    outline: none;
    font-family: inherit;
    min-width: 110px;
}
.jf-search-cat option { background: #1a0d08; }

.jf-search-input {
    flex: 1;
    background: rgba(255,255,255,0.06);
    border: none;
    color: #fff;
    padding: 0 14px;
    font-size: 0.95rem;
    outline: none;
    font-family: inherit;
}
.jf-search-input::placeholder { color: rgba(245,232,211,0.35); }

.jf-search-btn {
    background: var(--nb-orange);
    border: none;
    width: 46px;
    cursor: pointer;
    font-size: 1.1rem;
    display: flex; align-items: center; justify-content: center;
    transition: 0.2s;
    flex-shrink: 0;
}
.jf-search-btn:hover { background: #f0a040; }

/* CARRITO COUNT badge */
.jf-cart-badge {
    position: relative;
    display: inline-flex;
    align-items: center;
    gap: 6px;
    cursor: pointer;
    padding: 6px 10px;
    border-radius: 8px;
    border: 1px solid var(--nb-border);
    color: var(--nb-cream);
    font-size: 0.82rem;
    flex-shrink: 0;
    transition: 0.2s;
    text-decoration: none;
}
.jf-cart-badge:hover { border-color: var(--nb-orange); color: var(--nb-orange); }
.jf-cart-count {
    background: var(--nb-orange);
    color: #2b1b12;
    border-radius: 50%;
    width: 18px; height: 18px;
    font-size: 0.7rem;
    font-weight: 900;
    display: flex; align-items: center; justify-content: center;
}

/* AUTH */
.jf-auth {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-shrink: 0;
    margin-left: auto;
}

.jf-auth-greet {
    font-size: 0.75rem;
    color: rgba(245,232,211,0.5);
    white-space: nowrap;
}
.jf-auth-name {
    font-size: 0.85rem;
    font-weight: 700;
    color: var(--nb-orange);
    white-space: nowrap;
}

.jf-btn-nav {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    padding: 7px 13px;
    border-radius: 8px;
    border: 1px solid var(--nb-border);
    background: transparent;
    color: var(--nb-cream);
    font-size: 0.8rem;
    font-weight: 600;
    cursor: pointer;
    text-decoration: none;
    font-family: inherit;
    transition: 0.2s;
    white-space: nowrap;
}
.jf-btn-nav:hover { border-color: var(--nb-orange); color: var(--nb-orange); }
.jf-btn-nav.primary {
    background: var(--nb-orange);
    border-color: var(--nb-orange);
    color: #2b1b12;
    font-weight: 800;
}
.jf-btn-nav.primary:hover { background: #f0a040; border-color: #f0a040; }
.jf-btn-nav.danger { border-color: rgba(255,80,80,0.4); color: #ff8080; }
.jf-btn-nav.danger:hover { background: #ff4444; border-color: #ff4444; color: #fff; }

/* Ajuste del layout: empuja todo el contenido debajo del navbar */
body.has-jf-navbar {
    padding-top: var(--nb-h) !important;
    height: auto !important;
    min-height: 100vh;
    align-items: flex-start !important;
    overflow: auto !important;
}
body.has-jf-navbar .kiosk-container {
    height: calc(100vh - var(--nb-h) - 20px) !important;
    margin-top: 10px;
}

/* Animación entrada */
@keyframes nbSlideIn {
    from { transform: translateY(-100%); opacity: 0; }
    to   { transform: translateY(0);     opacity: 1; }
}
#jf-navbar { animation: nbSlideIn 0.25s ease; }
`;

    /* ── HTML ─────────────────────────────────────────────────────── */
    const CATEGORIAS = [
        { val: 'todo',           label: 'Todos' },
        { val: 'Hamburguesas',   label: '🍔 Hamburguesas' },
        { val: 'Pizzas',         label: '🍕 Pizzas' },
        { val: 'Bocatas',        label: '🥙 Bocatas & Kebabs' },
        { val: 'Complementos',   label: '🍟 Complementos' },
        { val: 'Salsas',         label: '🍯 Salsas' },
        { val: 'Bebidas',        label: '🥤 Bebidas' },
        { val: 'Postres',        label: '🎂 Postres' },
    ];

    function buildNavbar() {
        const catOptions = CATEGORIAS.map(c =>
            `<option value="${c.val}">${c.label}</option>`
        ).join('');

        return `
<div id="jf-nav-main">
    <a href="index.html" class="jf-logo">
        <span class="jf-logo-text">Java<span>Food</span></span>
    </a>

    <div class="jf-search">
        <select class="jf-search-cat" id="jf-cat-select">
            ${catOptions}
        </select>
        <input class="jf-search-input" id="jf-search-input"
               type="text" placeholder="Buscar productos en JavaFood..."
               autocomplete="off">
        <button class="jf-search-btn" id="jf-search-btn">🔍</button>
    </div>

    <a href="pago.html" class="jf-cart-badge" id="jf-cart-link" title="Ver pedido">
        🛒 <span id="jf-cart-count" class="jf-cart-count">0</span>
    </a>

    <div class="jf-auth" id="jf-auth-area"></div>
</div>`;
    }

    /* ── AUTH RENDER ─────────────────────────────────────────────── */
    function renderAuth() {
        const area = document.getElementById('jf-auth-area');
        if (!area) return;

        const raw = sessionStorage.getItem('javafoodUser');
        const user = raw ? JSON.parse(raw) : null;

        if (user) {
            const esAdmin = (user.tipo || '').toUpperCase() === 'ADMIN' || (user.rol || '').toUpperCase() === 'ADMIN';
            area.innerHTML = `
                <div style="line-height:1.2;text-align:right;">
                    <div class="jf-auth-greet">Hola,</div>
                    <div class="jf-auth-name">${user.nom || user.nombre || user.email}</div>
                </div>
                <a href="pagos/index.html" class="jf-btn-nav">📦 Pedidos</a>
                ${esAdmin ? `<a href="AdminComplementos.html" class="jf-btn-nav" style="border-color:rgba(209,122,34,0.5);color:var(--nb-orange);">⚙️ Admin</a>` : ''}
                <button class="jf-btn-nav danger" onclick="jfLogout()">↩ Salir</button>
            `;
        } else {
            area.innerHTML = `
                <a href="login.html" class="jf-btn-nav primary">🔑 Entrar / Registrarse</a>
            `;
        }
    }

    /* ── LOGOUT ──────────────────────────────────────────────────── */
    window.jfLogout = function () {
        sessionStorage.removeItem('javafoodUser');
        renderAuth();
        // También limpiar sesión servidor si existe
        fetch('/api/clientes/logout', { method: 'POST' }).catch(() => {});
    };

    /* ── CARRITO COUNT ───────────────────────────────────────────── */
    function updateCartCount() {
        const carrito = JSON.parse(localStorage.getItem('javaFoodCart') || '[]');
        const total = carrito.reduce((acc, i) => acc + i.cantidad, 0);
        const badge = document.getElementById('jf-cart-count');
        if (badge) {
            badge.textContent = total;
            badge.style.display = total > 0 ? 'flex' : 'flex';
            badge.style.background = total > 0 ? 'var(--nb-orange)' : 'rgba(209,122,34,0.3)';
        }
    }

    /* ── BUSCADOR ────────────────────────────────────────────────── */
    function setupSearch() {
        const btn   = document.getElementById('jf-search-btn');
        const input = document.getElementById('jf-search-input');
        const cat   = document.getElementById('jf-cat-select');

        const doSearch = () => {
            const q = input.value.trim();
            if (!q) return;
            const c = cat.value;
            window.location.href = `buscar.html?q=${encodeURIComponent(q)}&cat=${encodeURIComponent(c)}`;
        };

        btn.addEventListener('click', doSearch);
        input.addEventListener('keydown', e => { if (e.key === 'Enter') doSearch(); });

        // Pre-rellenar si venimos de buscar.html
        const params = new URLSearchParams(window.location.search);
        if (params.get('q')) input.value = params.get('q');
        if (params.get('cat')) cat.value = params.get('cat');
    }

    /* ── INIT ────────────────────────────────────────────────────── */
    function init() {
        // Inyectar CSS
        const style = document.createElement('style');
        style.textContent = CSS;
        document.head.appendChild(style);

        // Crear nav element
        const nav = document.createElement('nav');
        nav.id = 'jf-navbar';
        nav.innerHTML = buildNavbar();
        document.body.insertBefore(nav, document.body.firstChild);

        // Ajuste layout
        document.body.classList.add('has-jf-navbar');

        // Render
        renderAuth();
        updateCartCount();
        setupSearch();

        // Actualizar count cuando cambia carrito
        window.addEventListener('storage', updateCartCount);
        // Polling ligero para páginas en misma pestaña
        setInterval(updateCartCount, 1500);
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})();