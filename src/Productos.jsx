const Producto = ({ nombre, precio, imagen, onAgregar, onEliminar }) => {
  return (
    <div className="producto-card">
      <img src={imagen} alt={nombre}/>
      <h3>{nombre}</h3>
      <p>${precio}</p>
      <button onClick={onAgregar}>Agregar al carrito</button>
      <button onClick={onEliminar} className="eliminar">Eliminar</button>
    </div>
  )
}

export default Producto

