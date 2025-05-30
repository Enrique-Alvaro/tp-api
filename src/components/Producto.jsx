import { Link } from 'react-router-dom'

const Producto = ({ nombre, precio, imagen, onAgregar, onEliminar, index }) => (
  <div className="producto-card">
    <Link to={`/producto/${index}`}>
      <img src={imagen} alt={nombre} />
      <h3>{nombre}</h3>
    </Link>
    <p>${precio}</p>
    <button onClick={onAgregar}>Agregar al carrito</button>
    <button onClick={onEliminar} className="eliminar">Eliminar</button>
  </div>
)

export default Producto