import { useState } from "react";
import { useParams, useNavigate } from 'react-router-dom'
const Carrito = () => {
  const [carrito, setCarrito] = useState(JSON.parse(localStorage.getItem("carrito")) || []);
  const [metodoPago, setMetodoPago] = useState("");
  const navigate = useNavigate();

  const totalCarrito = carrito.reduce((acc, prod) => acc + prod.precio, 0);

  const handleCompra = () => {
    const metodo = prompt("Seleccioná método de pago: (tarjeta, transferencia, efectivo)");
    if (metodo) {
      setMetodoPago(metodo);
      alert(`¡Gracias por tu compra! Método elegido: ${metodo}`);
    }
  };
  const quitarDelCarrito = (index) => {
    const nuevoCarrito = [...carrito];
    nuevoCarrito.splice(index, 1);
    setCarrito(nuevoCarrito);
    localStorage.setItem("carrito", JSON.stringify(nuevoCarrito));
  };

  return (
    <>
    <div className="boton"><button onClick={() => navigate(-1)}>← Volver</button></div>
    <div className="carrito-div">
      <h2>Carrito de compras</h2>
      <p className="total-p">Total: ${totalCarrito}</p>
      <div className="carrito-grid">
        {carrito.length === 0 ? (
          <p>El carrito está vacío</p>
        ) : (
          carrito.map((producto, index) => (
            <div className="producto-card" key={index}>
              <img src={producto.imagen} alt={producto.nombre} />
              <h4>{producto.nombre}</h4>
              <p>${producto.precio}</p>
              <button onClick={() => quitarDelCarrito(index)}>Quitar</button>
            </div>
          ))
        )}
      </div>
      {carrito.length > 0 && (
        <button onClick={handleCompra} style={{ marginTop: "20px" }}>
          Finalizar compra
        </button>
      )}
      {metodoPago && <p className="metodo-pago">Método de pago elegido: $ {metodoPago} $</p>}
    </div>
    </>
  );
};

export default Carrito
