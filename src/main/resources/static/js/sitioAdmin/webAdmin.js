document.addEventListener("DOMContentLoaded", () => {

  let deleteId = null;
  let editId = null;
  let roleId = null;
  let viewId = null;

  const modalDelete = document.getElementById("modalDelete");
  const modalEdit = document.getElementById("modalEdit");
  const modalRole = document.getElementById("modalRole");
  const modalView = document.getElementById("modalView");
  const modalAdd = document.getElementById("modalAdd");

  // ================= CARGAR USUARIOS =================
  function cargarUsuarios() {
    fetch("http://localhost:8080/api/clientes")
      .then(res => res.json())
      .then(data => {
        const tabla = document.getElementById("tablaUsuarios");
        if (!tabla) return;

        tabla.innerHTML = "";

        data.forEach(u => {
          tabla.innerHTML += `
            <tr>
              <td>
                <strong>${u.nombre} ${u.apellidos}</strong>
                <span class="rol">(${u.rol})</span>
              </td>
              <td class="actions-cell">
                <button type="button" class="btn-gray-view" data-id="${u.id}">Ver</button>
                <button type="button" class="btn-blue" data-id="${u.id}">Cambiar rol</button>
                <button type="button" class="btn-yellow" data-id="${u.id}">Modificar</button>
                <button type="button" class="btn-red" data-id="${u.id}">Eliminar</button>
              </td>
            </tr>
          `;
        });
      });
  }

  // ================= EVENTOS =================
  document.addEventListener("click", (e) => {

    if (e.target.classList.contains("btn-gray-view")) {
      viewId = e.target.dataset.id;

      fetch("http://localhost:8080/api/clientes/" + viewId)
        .then(res => res.json())
        .then(u => {
          document.getElementById("infoUsuario").innerHTML = `
            <p><strong>ID:</strong> ${u.id}</p>
            <p><strong>Nombre:</strong> ${u.nombre}</p>
            <p><strong>Apellidos:</strong> ${u.apellidos}</p>
            <p><strong>Rol:</strong> ${u.rol}</p>
            <p><strong>Dirección:</strong> ${u.direccion}</p>
            <p><strong>Teléfono:</strong> ${u.telefono}</p>
            <p><strong>Correo:</strong> ${u.email}</p>
          `;
          modalView.classList.add("active");
        });
    }

    if (e.target.classList.contains("btn-red")) {
      deleteId = e.target.dataset.id;
      modalDelete.classList.add("active");
    }

    if (e.target.classList.contains("btn-yellow")) {
      editId = e.target.dataset.id;

      fetch("http://localhost:8080/api/clientes/" + editId)
        .then(res => res.json())
        .then(u => {
          document.getElementById("editNombre").value = u.nombre;
          document.getElementById("editApellidos").value = u.apellidos;
          document.getElementById("editDireccion").value = u.direccion;
          document.getElementById("editTelefono").value = u.telefono;
          document.getElementById("editEmail").value = u.email;

          modalEdit.classList.add("active");
        });
    }

    if (e.target.classList.contains("btn-blue")) {
      roleId = e.target.dataset.id;
      modalRole.classList.add("active");
    }

    if (e.target.id === "closeView") modalView.classList.remove("active");

    if (e.target.id === "cancelDelete") modalDelete.classList.remove("active");
    if (e.target.id === "cancelEdit") modalEdit.classList.remove("active");
    if (e.target.id === "cancelRole") modalRole.classList.remove("active");
    if (e.target.id === "cancelAdd") modalAdd.classList.remove("active");

  });

  // ================= DELETE =================
  document.getElementById("confirmDelete").onclick = () => {
    if (!deleteId) return;

    fetch("http://localhost:8080/api/clientes/" + deleteId, {
      method: "DELETE"
    }).then(() => {
      modalDelete.classList.remove("active");
      cargarUsuarios();
    });
  };

  // ================= EDIT =================
  document.getElementById("saveEdit").onclick = () => {

    const datos = {
      nombre: document.getElementById("editNombre").value,
      apellidos: document.getElementById("editApellidos").value,
      direccion: document.getElementById("editDireccion").value,
      telefono: document.getElementById("editTelefono").value,
      email: document.getElementById("editEmail").value
    };

    fetch("http://localhost:8080/api/clientes/" + editId, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(datos)
    }).then(() => {
      modalEdit.classList.remove("active");
      cargarUsuarios();
    });
  };

  // ================= ROLE =================
  document.getElementById("saveRole").onclick = () => {

    const rolInput = document.querySelector('input[name="rol"]:checked');
    if (!rolInput) return;

    fetch("http://localhost:8080/api/clientes/" + roleId + "/rol", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ rol: rolInput.value })
    }).then(() => {
      modalRole.classList.remove("active");
      cargarUsuarios();
    });
  };

  // ================= AÑADIR USUARIO =================
  window.abrirModalAdd = function () {
    const modal = document.getElementById("modalAdd");
    if (modal) modal.classList.add("active");
  };

  window.crearUsuario = async function () {

    const nombre = document.getElementById("nombre").value;
    const apellidos = document.getElementById("apellidos").value;
    const direccion = document.getElementById("direccion").value;
    const telefono = document.getElementById("telefono").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
      const response = await fetch("/api/clientes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nombre,
          apellidos,
          direccion,
          telefono,
          email,
          contrasenya: password
        })
      });

      if (response.ok) {

        alert("Usuario añadido correctamente");

        document.getElementById("nombre").value = "";
        document.getElementById("apellidos").value = "";
        document.getElementById("direccion").value = "";
        document.getElementById("telefono").value = "";
        document.getElementById("email").value = "";
        document.getElementById("password").value = "";

        const modal = document.getElementById("modalAdd");
        if (modal) modal.classList.remove("active");

        cargarUsuarios();
      }else{
		alert("Error al registrar usuario");
	  }

    } catch (e) {
      console.error(e);
    }
  };

  // INIT
  cargarUsuarios();
});