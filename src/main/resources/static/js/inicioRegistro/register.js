function registrarUsuario(event) {
  event.preventDefault();

  const usuario = {
    nombre: document.getElementById("nombre").value,
    apellidos: document.getElementById("apellidos").value,
    direccion: document.getElementById("direccion").value,
    telefono: document.getElementById("telefono").value,
    email: document.getElementById("email").value,
    contrasenya: document.getElementById("password").value, 
  };

  fetch("/api/clientes", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(usuario),
  })
    .then((res) => {
      if (res.ok) {
        document.getElementById("mensaje").innerText =
          "Usuario registrado correctamente";
      } else {
        document.getElementById("mensaje").innerText =
          "Error al registrar. Puede ser que el email ya haya sido usado con anterioridad o que la contraseña tenga menos de 7 caracteres.";
      }
    })
    .catch(() => {
      document.getElementById("mensaje").innerText =
        "Error de conexión";
    });
}    

