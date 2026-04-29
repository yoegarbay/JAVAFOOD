async function iniciarSesion(event) {
    event.preventDefault();

    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;
    const mensaje = document.getElementById("mensaje");

    try {
        const response = await fetch("/api/clientes/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: email,
                password: password
            }),
        });

        console.log("Status:", response.status);

        if (response.ok) {
            const usuario = await response.json();
            console.log(usuario);

            mensaje.innerText = "¡Bienvenido " + usuario.nombre + "!";
            mensaje.style.color = "green";

            setTimeout(() => {
                window.location.href = "Menus.html";
            }, 1000);
        } else {
            const error = await response.text();
            console.log("Error backend:", error);

            mensaje.innerText = "Email o contraseña incorrectos";
            mensaje.style.color = "red";
        }
    } catch (error) {
        console.error("Error en la petición:", error);
        mensaje.innerText = "Error: El servidor no responde";
    }
}
