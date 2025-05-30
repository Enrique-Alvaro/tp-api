import { useState } from 'react'
import Producto from '../components/Producto'
import { productosMock } from '../utils/productosMock'
import { Link } from 'react-router-dom'

const Home = () => {
  const [productos, setProductos] = useState(productosMock)
  const [carrito, setCarrito] = useState([])
  const [nuevoProducto, setNuevoProducto] = useState({ nombre: '', precio: '', imagen: '' })

  const handleChange = (e) => {
    setNuevoProducto({ ...nuevoProducto, [e.target.name]: e.target.value })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    if (!nuevoProducto.nombre || !nuevoProducto.precio || !nuevoProducto.imagen) return alert('Completa todos los campos')

    const nuevo = { ...nuevoProducto, precio: Number(nuevoProducto.precio) }
    setProductos([...productos, nuevo])
    setNuevoProducto({ nombre: '', precio: '', imagen: '' })
  }

  const agregarAlCarrito = (producto) => {
  const nuevoCarrito = [...carrito, producto];
  setCarrito(nuevoCarrito);

    localStorage.setItem("carrito", JSON.stringify(nuevoCarrito));
    
    setCantidadCarrito(nuevoCarrito.length);
  }


  const eliminarProducto = (index) => {
    const nuevos = [...productos]
    nuevos.splice(index, 1)
    setProductos(nuevos)
  }

  return (
    <>
      <center>
        <h1>Tienda online Marketplace</h1>
      </center>

      <form className="formulario" onSubmit={handleSubmit}>
        <input name="nombre" value={nuevoProducto.nombre} onChange={handleChange} placeholder="Nombre" />
        <input name="precio" value={nuevoProducto.precio} onChange={handleChange} placeholder="Precio" />
        <input name="imagen" value={nuevoProducto.imagen} onChange={handleChange} placeholder="Imagen URL" />
        <button type="submit">Agregar producto</button>
      </form>

      <h2>Productos disponibles</h2>
      <div className="productos-grid">
        {productos.map((p, i) => (
          <Producto
            key={i}
            index={i}
            nombre={p.nombre}
            precio={p.precio}
            imagen={p.imagen}
            onAgregar={() => agregarAlCarrito(p)}
            onEliminar={() => eliminarProducto(i)}
          />
        ))}
      </div>

      <div className="carrito-flotante">
            <Link to="/carrito">
                <img src="https://cdn-icons-png.flaticon.com/512/9341/9341730.png" alt="Carrito" />
                <span className="contador">{carrito.length}</span>
            </Link>
       </div>
    </>
  )
}

export default Home