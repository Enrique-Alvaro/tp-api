import { useParams, useNavigate } from 'react-router-dom'
import { productosMock } from '../utils/productosMock'

const DetalleProducto = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const producto = productosMock[parseInt(id)]

  if (!producto) return <p>Producto no encontrado</p>

  return (
    <div className="producto-detalle">
      <div className="boton"><button onClick={() => navigate(-1)}>← Volver</button></div>
      <img src={producto.imagen} alt={producto.nombre} />
      <h2>{producto.nombre}</h2>
      <p>Precio: ${producto.precio}</p>
      <p>Este producto esta piola... ¡Compralo!</p>
    </div>
  )
}

export default DetalleProducto