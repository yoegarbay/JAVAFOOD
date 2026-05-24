document.addEventListener("DOMContentLoaded", () => {

  let deleteId = null;
  let editId   = null;
  let roleId   = null;

  const API = "/api/usuarios";

  const modals = {
    add:  document.getElementById("modalAdd"),
    edit: document.getElementById("modalEdit"),
    role: document.getElementById("modalRole"),
    del:  document.getElementById("modalDelete"),
    view: document.getElementById("modalView")
  };

  // ================= CARGAR USUARIOS =================
  function cargarUsuarios() {
    fetch(API)
      .then(r => r.json())
      .then(data => {
        const tbody = document.getElementById("tablaUsuarios");
        if (!tbody) return;
        tbody.innerHTML = "";
        data.forEach(u => {
          const miRol = (u.tipo || "CLIENTE").toUpperCase();
          tbody.innerHTML += `
            <tr>
              <td>
                <strong>${u.nom ?? u.nombre ?? ""} ${u.apellidos ?? ""}</strong>
                <span class="rol">(${miRol})</span>
              </td>
              <td class="actions-cell">
                <button type="button" class="btn-gray-view" onclick="ver(${u.id})">Ver</button>
                <button type="button" class="btn-blue"      onclick="rol(${u.id})">Cambiar rol</button>
                <button type="button" class="btn-yellow"    onclick="editar(${u.id})">Modificar</button>
                <button type="button" class="btn-red"       onclick="eliminar(${u.id})">Eliminar</button>
              </td>
            </tr>
          `;
        });
      })
      .catch(e => console.error("Error cargando usuarios:", e));
  }

  // ================= VER =================
  window.ver = (id) => {
    fetch(`${API}/${id}`)
      .then(r => r.json())
      .then(u => {
        const miRol = (u.tipo || "").toUpperCase();
        document.getElementById("infoUsuario").innerHTML = `
          <p><strong>ID:</strong> ${u.id}</p>
          <p><strong>Nombre:</strong> ${u.nom ?? u.nombre ?? ""}</p>
          <p><strong>Apellidos:</strong> ${u.apellidos ?? "-"}</p>
          <p><strong>Rol:</strong> ${miRol}</p>
          <p><strong>Dirección:</strong> ${u.direccion ?? "-"}</p>
          <p><strong>Teléfono:</strong> ${u.telefono ?? "-"}</p>
          <p><strong>Email:</strong> ${u.email}</p>
          ${miRol === "CLIENTE" ? `<p><strong>Puntos:</strong> ${u.puntos ?? 0}</p>` : ""}
        `;
        modals.view.classList.add("active");
      })
      .catch(e => console.error(e));
  };

  // ================= ABRIR MODAL ADD =================
  window.abrirModalAdd = () => modals.add.classList.add("active");

  // ================= CREAR USUARIO =================
  window.crearUsuario = () => {
    const body = {
      nom:       document.getElementById("nombre").value,
      apellidos: document.getElementById("apellidos").value,
      direccion: document.getElementById("direccion").value,
      telefono:  parseInt(document.getElementById("telefono").value) || 0,
      email:     document.getElementById("email").value,
      contrasena: document.getElementById("password").value,
      tipo: "CLIENTE"
    };

    fetch(API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    })
    .then(res => {
      if (!res.ok) throw new Error("Error creando usuario");
      ["nombre","apellidos","direccion","telefono","email","password"].forEach(id => {
        document.getElementById(id).value = "";
      });
      modals.add.classList.remove("active");
      cargarUsuarios();
    })
    .catch(e => { console.error(e); alert("Error al crear usuario"); });
  };

  // ================= ELIMINAR =================
  window.eliminar = (id) => { deleteId = id; modals.del.classList.add("active"); };

  document.getElementById("confirmDelete").onclick = () => {
    fetch(`${API}/${deleteId}`, { method: "DELETE" })
      .then(() => { modals.del.classList.remove("active"); deleteId = null; cargarUsuarios(); })
      .catch(e => console.error(e));
  };

  // ================= EDITAR =================
  window.editar = (id) => {
    editId = id;
    fetch(`${API}/${id}`)
      .then(r => r.json())
      .then(u => {
        document.getElementById("editNombre").value    = u.nom ?? u.nombre ?? "";
        document.getElementById("editApellidos").value = u.apellidos ?? "";
        document.getElementById("editDireccion").value = u.direccion ?? "";
        document.getElementById("editTelefono").value  = u.telefono ?? "";
        document.getElementById("editEmail").value     = u.email;
        modals.edit.classList.add("active");
      })
      .catch(e => console.error(e));
  };

  document.getElementById("saveEdit").onclick = () => {
    const body = {
      nom:       document.getElementById("editNombre").value,
      apellidos: document.getElementById("editApellidos").value,
      direccion: document.getElementById("editDireccion").value,
      telefono:  parseInt(document.getElementById("editTelefono").value) || 0,
      email:     document.getElementById("editEmail").value
    };
    fetch(`${API}/${editId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    })
    .then(() => { modals.edit.classList.remove("active"); cargarUsuarios(); })
    .catch(e => console.error(e));
  };

  // ================= CAMBIAR ROL =================
  window.rol = (id) => { roleId = id; modals.role.classList.add("active"); };

  document.getElementById("saveRole").onclick = () => {
    const r = document.querySelector('input[name="rol"]:checked');
    if (!r) { alert("Selecciona un rol"); return; }
    fetch(`${API}/${roleId}/rol`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ rol: r.value })
    })
    .then(() => { modals.role.classList.remove("active"); cargarUsuarios(); })
    .catch(e => console.error(e));
  };

  // ================= CERRAR MODALES =================
  document.getElementById("cancelAdd").onclick    = () => modals.add.classList.remove("active");
  document.getElementById("cancelEdit").onclick   = () => modals.edit.classList.remove("active");
  document.getElementById("cancelRole").onclick   = () => modals.role.classList.remove("active");
  document.getElementById("cancelDelete").onclick = () => modals.del.classList.remove("active");
  document.getElementById("closeView").onclick    = () => modals.view.classList.remove("active");

  // ================= INIT =================
  cargarUsuarios();
});
