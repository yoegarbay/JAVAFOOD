/**
 * register.js — Registro de clientes en JAVAFOOD
 * Ruta: /js/inicioRegistro/register.js
 *
 * Llamado desde register.html.
 * Envía al endpoint /api/clientes/registro de Spring Boot.
 */
async function registrarUsuario(event) {
  event.preventDefault();

  const nombre    = document.getElementById("nombre")?.value.trim()    || document.getElementById("r-nombre")?.value.trim()    || "";
  const apellidos = document.getElementById("apellidos")?.value.trim() || document.getElementById("r-apellidos")?.value.trim() || "";
  const direccion = document.getElementById("direccion")?.value.trim() || document.getElementById("r-direccion")?.value.trim() || "";
  const telefono  = document.getElementById("telefono")?.value.trim()  || document.getElementById("r-telefono")?.value.trim()  || "";
  const email     = document.getElementById("email")?.value.trim()     || document.getElementById("r-email")?.value.trim()     || "";
  const password  = document.getElementById("password")?.value         || document.getElementById("r-pass")?.value             || "";

  const msgEl = document.getElementById("mensaje") || document.getElementById("msg-reg");
  const btnEl = document.getElementById("btn-reg");

  if (!nombre || !email || !password) {
    mostrarMensaje(msgEl, "❌ Nombre, email y contraseña son obligatorios", "red");
    return;
  }
  if (password.length < 4) {
    mostrarMensaje(msgEl, "❌ La contraseña debe tener mínimo 4 caracteres", "red");
    return;
  }

  if (btnEl) { btnEl.disabled = true; btnEl.textContent = "Creando cuenta…"; }

  try {
    const res = await fetch("/api/clientes/registro", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ nombre, apellidos, direccion, telefono, email, password })
    });

    const data = await res.json().catch(() => ({}));

    if (!res.ok) {
      throw new Error(data.detalle || data.mensaje || "Error al registrar");
    }

    // Guardar sesión automáticamente tras el registro
    sessionStorage.setItem("javafoodUser", JSON.stringify(data));

    mostrarMensaje(msgEl, "✅ ¡Cuenta creada! Bienvenido, " + (data.nombre || nombre), "green");

    const next = new URLSearchParams(location.search).get("next") || "index.html";
    setTimeout(() => location.href = next, 1000);

  } catch (e) {
    mostrarMensaje(msgEl, "❌ " + e.message, "red");
    if (btnEl) { btnEl.disabled = false; btnEl.textContent = "Crear cuenta →"; }
  }
}

function mostrarMensaje(el, txt, color) {
  if (!el) return;
  el.textContent  = txt;
  el.style.color  = color;
  el.style.display = "block";
}
