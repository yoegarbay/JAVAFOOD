/**
 * auth-rol.js — Rellena el #auth-area en el top-info de cada página.
 * - No logueado:  [Entrar/Registrarse]
 * - admin:        [⚙️ ADMIN] [📦 Pedidos] [↩ Salir]
 * - empleado:     [👷 TRABAJADOR] [↩ Salir]
 * - cliente:      [🪙 X pts] [📦 Pedidos] [↩ Salir]
 */
(function () {
    const CSS = `
    .auth-btn {
        display: inline-flex; align-items: center; gap: 5px;
        padding: 6px 14px; border-radius: 9px;
        font-family: 'Space Grotesk', sans-serif;
        font-size: 0.8rem; font-weight: 700;
        cursor: pointer; text-decoration: none;
        border: 1px solid rgba(209,122,34,0.35);
        color: var(--cream, #f5e8d3);
        background: transparent; transition: 0.2s;
        white-space: nowrap;
    }
    .auth-btn:hover { border-color: var(--primary-orange, #d17a22); color: var(--primary-orange, #d17a22); }
    .auth-btn.primary {
        background: var(--primary-orange, #d17a22);
        border-color: var(--primary-orange, #d17a22);
        color: #2b1b12; box-shadow: 0 0 10px rgba(209,122,34,0.3);
    }
    .auth-btn.primary:hover { background: #f0a040; box-shadow: 0 0 16px rgba(209,122,34,0.5); }
    .auth-btn.admin-btn { color: var(--primary-orange,#d17a22); border-color: rgba(209,122,34,0.5); }
    .auth-btn.admin-btn:hover { background: var(--primary-orange,#d17a22); color: #2b1b12; }
    .auth-btn.worker-btn { color: #6fbf6f; border-color: rgba(111,191,111,0.5); }
    .auth-btn.worker-btn:hover { background: #4caf50; color: white; border-color: #4caf50; }
    .auth-btn.danger { color: #ff8080; border-color: rgba(255,80,80,0.4); }
    .auth-btn.danger:hover { background: #ff4444; color: white; border-color: #ff4444; }
    .auth-btn.puntos-btn {
        background: rgba(209,122,34,0.12);
        border-color: rgba(209,122,34,0.5);
        color: var(--primary-orange, #d17a22);
        cursor: default;
    }
    .auth-btn.pedidos-btn {
        background: rgba(209,122,34,0.08);
        border-color: rgba(209,122,34,0.4);
        color: var(--cream, #f5e8d3);
    }
    .auth-btn.pedidos-btn:hover {
        background: rgba(209,122,34,0.25);
        border-color: var(--primary-orange, #d17a22);
        color: var(--primary-orange, #d17a22);
    }
    #auth-area { display: flex; align-items: center; gap: 7px; flex-shrink: 0; }
    `;

    function injectCss() {
        if (document.getElementById('auth-rol-css')) return;
        const s = document.createElement('style');
        s.id = 'auth-rol-css';
        s.textContent = CSS;
        document.head.appendChild(s);
    }

    function salir() {
        sessionStorage.removeItem('javafoodUser');
        localStorage.removeItem('javaFoodPuntos');
        location.reload();
    }
    window.jfSalir = salir;

    async function cargarPuntos(id) {
        try {
            const res = await fetch(`/api/clientes/${id}/puntos`);
            if (!res.ok) return null;
            const data = await res.json();
            const puntos = data.puntos ?? data;
            localStorage.setItem('javaFoodPuntos', puntos.toString());
            return puntos;
        } catch (e) {
            return parseInt(localStorage.getItem('javaFoodPuntos') || '0');
        }
    }

    async function render() {
        const area = document.getElementById('auth-area');
        if (!area) return;

        const raw  = sessionStorage.getItem('javafoodUser');
        const user = raw ? JSON.parse(raw) : null;
        const tipo = user ? (user.tipo || user.rol || '').toLowerCase() : '';

        if (!user) {
            area.innerHTML = `<a href="login.html" class="auth-btn primary">🔑 Entrar&nbsp;/&nbsp;Registrarse</a>`;
            return;
        }

        // Botón "Mis Pedidos" — visible para TODOS los usuarios registrados
        const pedidosBtn = `<a href="/pagos/index.html" class="auth-btn pedidos-btn">📦 Mis Pedidos</a>`;

        let rolBtn    = '';
        let puntosBtn = '';

        if (tipo === 'admin') {
            rolBtn = `<a href="webAdmin.html" class="auth-btn admin-btn">⚙️ ADMIN</a>`;
        } else if (tipo === 'empleado') {
            rolBtn = `<a href="fichar.html" class="auth-btn worker-btn">👷 TRABAJADOR</a>`;
        } else {
            // CLIENTE: mostrar puntos
            const id = user.id_cliente || user.id;
            const pts = await cargarPuntos(id);
            puntosBtn = `<span class="auth-btn puntos-btn">🪙 <span id="header-puntos">${pts ?? 0} pts</span></span>`;
        }

        area.innerHTML = `
            ${puntosBtn}
            ${rolBtn}
            ${pedidosBtn}
            <button class="auth-btn danger" onclick="jfSalir()">↩ Salir</button>`;
    }

    function init() {
        injectCss();
        render();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();