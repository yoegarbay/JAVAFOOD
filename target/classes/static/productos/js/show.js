function obtenerId() {

    const params = new URLSearchParams(window.location.search);

    return params.get("id");
}


async function cargarPelicula() {

    const id = obtenerId();

    const response = await fetch(`/api/productos/${id}`);

    const p = await response.json();

    document.getElementById("nombre").textContent = p.nombre;
    document.getElementById("precio").textContent = p.precio;
    document.getElementById("id_detalle").textContent = p.id_detalle;
}

cargarPelicula();
