let carrito = JSON.parse(localStorage.getItem("javaFoodCart")) || [];

document.addEventListener("DOMContentLoaded", () => {
  crearEstructuraModal();
  actualizarVistaCarrito();
  vincularBotones();

  const btnPagar = document.querySelector(".pay-btn");
  if (btnPagar) {
    btnPagar.onclick = enviarPedidoAlServidor;
  }
});

function crearEstructuraModal() {
  if (document.getElementById("custom-modal")) return;
  const modalHTML = `
        <div id="custom-modal" class="modal-overlay" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.8); z-index:1000; justify-content:center; align-items:center;">
            <div class="modal-content" style="background:#1a1a1a; padding:20px; border:1px solid #00ff41; border-radius:8px; width:300px; color:white;">
                <h2 id="modal-titulo" style="color:#00ff41;">PRODUCTO</h2>
                <div id="seccion-extras" style="display:none; margin-bottom:15px;">
                    <label>AÑADIR EXTRA (+0.50€):</label>
                    <select id="select-extras" style="width:100%; background:black; color:#00ff41; border:1px solid #00ff41;">
                        <option value="">Ninguno</option>
                        <option value="Extra Queso">Extra Queso</option>
                        <option value="Bacon bits">Bacon bits</option>
                    </select>
                </div>
                <label>CANTIDAD:</label>
                <input type="number" id="input-cantidad" value="1" min="1" style="width:90%; margin-bottom:20px; background:black; color:#00ff41; border:1px solid #00ff41;">
                <button id="btn-confirmar" style="width:100%; background:#00ff41; color:black; padding:10px; font-weight:bold; cursor:pointer;">AÑADIR AL PEDIDO</button>
                <button id="btn-cancelar" style="width:100%; background:transparent; border:1px solid #ff4444; color:#ff4444; padding:10px; margin-top:10px; cursor:pointer;">CANCELAR</button>
            </div>
        </div>`;
  document.body.insertAdjacentHTML("beforeend", modalHTML);
  document.getElementById("btn-confirmar").onclick = confirmarCompra;
  document.getElementById("btn-cancelar").onclick = cerrarModal;
}

let itemActual = null;

function vincularBotones() {
  document.addEventListener("click", (e) => {
    if (e.target.tagName === "BUTTON" && e.target.innerText === "+") {
      const card = e.target.closest(".card");
      if (!card) return;
      const nombre = card.querySelector("h4").innerText;
      const precioStr = card.querySelector(".card-price span").innerText;
      const precio = parseFloat(precioStr.replace("€", ""));

      itemActual = { nombre, precio };
      document.getElementById("modal-titulo").innerText = nombre;
      document.getElementById("seccion-extras").style.display =
        window.location.pathname.includes("Complementos.html")
          ? "block"
          : "none";
      document.getElementById("custom-modal").style.display = "flex";
    }
  });
}

function confirmarCompra() {
  const cantidad = parseInt(document.getElementById("input-cantidad").value);
  const extra = document.getElementById("select-extras").value;
  let pFinal = itemActual.precio;
  let nFinal = itemActual.nombre;

  if (extra && window.location.pathname.includes("Complementos.html")) {
    pFinal += 0.5;
    nFinal += ` + ${extra}`;
  }

  carrito.push({
    nombre: nFinal,
    precio: pFinal,
    cantidad: cantidad,
    total: pFinal * cantidad,
  });
  localStorage.setItem("javaFoodCart", JSON.stringify(carrito));
  actualizarVistaCarrito();
  cerrarModal();
}

function cerrarModal() {
  document.getElementById("custom-modal").style.display = "none";
}

function actualizarVistaCarrito() {
  const contenedor = document.querySelector(".cart-box");
  if (!contenedor) return;
  contenedor.querySelectorAll(".cart-item").forEach((i) => i.remove());
  let total = 0;
  carrito.forEach((item) => {
    const div = document.createElement("div");
    div.className = "cart-item";
    div.style.display = "flex";
    div.style.justifyContent = "space-between";
    div.innerHTML = `<span>${item.cantidad}x ${item.nombre}</span><span>${item.total.toFixed(2)}€</span>`;
    contenedor.insertBefore(
      div,
      contenedor.querySelector(".total-row") || null,
    );
    total += item.total;
  });
  const tDisplay = document.querySelector(".total-price");
  if (tDisplay) tDisplay.innerText = `${total.toFixed(2)}€`;
}

async function enviarPedidoAlServidor() {
  if (carrito.length === 0) return alert("Carrito vacío");

  try {
    const response = await fetch("http://localhost:8080/api/pedidos/pagar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(carrito),
    });

    if (response.ok) {
      alert("¡Pedido realizado con éxito!");
      carrito = [];
      localStorage.removeItem("javaFoodCart");
      actualizarVistaCarrito();
    } else {
      const errorText = await response.text();
      alert("Error del servidor: " + errorText);
    }
  } catch (e) {
    alert("No se pudo conectar con el servidor Java. Verifica el puerto 8080.");
  }
}

async function enviarPedidoAlServidor() {
  if (carrito.length === 0) {
    alert("El carrito está vacío");
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/api/pedidos/pagar", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(carrito),
    });

    const resultado = await response.text();

    if (response.ok && resultado.includes("OK")) {
      alert("¡Pedido guardado en la base de datos!");
      carrito = [];
      localStorage.removeItem("javaFoodCart");
      actualizarVistaCarrito();
    } else {
      console.error("Respuesta del servidor:", resultado);
      alert("Error en el servidor: " + resultado);
    }
  } catch (error) {
    console.error("Error de conexión:", error);
    alert(
      "No se pudo conectar con el servidor Java. ¿Está el backend encendido?",
    );
  }
}

//SOLUCION CONFLICTO GRUPAL:
console.log("Funcionalidad de Login");
console.log("Funcionalidad de Registro");
console.log("Funcionalidad de Login de MIGUEL ANGEL");
