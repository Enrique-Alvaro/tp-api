import Producto from "./Productos"
import { useState } from "react"

const Form = ({setCantidadCarrito}) => {
  const [productos, setProductos] = useState([
    {
      nombre: 'Remera estampada',
      precio: 4999,
      imagen: 'https://www.aceroymagia.com/Images/articulo/camiseta-loki-logo-badge/01-Camiseta-Loki-Logo-Badge.jpg'
    },
    {
      nombre: 'Zapatillas deportivas',
      precio: 14999,
      imagen: 'https://static.nike.com/a/images/w_1280,q_auto,f_auto/ennqnnblj5irlj6oakba/air-jordan-1-origin-story-release-date.jpg'
    },
    {
      nombre: 'Campera impermeable',
      precio: 18999,
      imagen: 'https://http2.mlstatic.com/D_911271-MLA82072181925_012025-O.jpg'
    }
  ])

  const [carrito, setCarrito] = useState([])

  const [nuevoProducto, setNuevoProducto] = useState({
    nombre: '',
    precio: '',
    imagen: ''
  })

  const handleChange = (e) => {
    setNuevoProducto({
      ...nuevoProducto,
      [e.target.name]: e.target.value
    })
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    if (
      nuevoProducto.nombre.trim() === '' ||
      nuevoProducto.precio.trim() === '' ||
      nuevoProducto.imagen.trim() === ''
    ) {
      alert('Por favor completa todos los campos')
      return
    }

    setProductos([
      ...productos,
      { ...nuevoProducto, precio: Number(nuevoProducto.precio) }
    ])
    setNuevoProducto({ nombre: '', precio: '', imagen: '' })
  }

  const agregarAlCarrito = (producto) => {
    const nuevoCarrito = [...carrito, producto]
    setCarrito(nuevoCarrito)
    setCantidadCarrito(nuevoCarrito.length)
  }

  const eliminarProducto = (index) => {
    const nuevosProductos = [...productos]
    nuevosProductos.splice(index, 1)
    setProductos(nuevosProductos)
  }

  const eliminarDelCarrito = (index) => {
    const nuevoCarrito = [...carrito]
    nuevoCarrito.splice(index, 1)
    setCarrito(nuevoCarrito)
    setCantidadCarrito(nuevoCarrito.length)
  }

  const totalCarrito = carrito.reduce((acc, prod) => acc + prod.precio, 0)
  
  return (
    <>
      <form className="formulario" onSubmit={handleSubmit}>
        <input
          type="text"
          name="nombre"
          placeholder="Nombre del producto"
          value={nuevoProducto.nombre}
          onChange={handleChange}
        />
        <input
          type="text"
          name="precio"
          placeholder="Precio"
          value={nuevoProducto.precio}
          onChange={handleChange}
        />
        <input
          type="text"
          name="imagen"
          placeholder="URL de la imagen"
          value={nuevoProducto.imagen}
          onChange={handleChange}
        />
        <button type="submit">Agregar producto</button>
      </form>

      <h2>Productos disponibles</h2>
      <div className="productos-grid">
        {productos.map((producto, index) => (
          <Producto
            key={index}
            nombre={producto.nombre}
            precio={producto.precio}
            imagen={producto.imagen}
            onAgregar={() => agregarAlCarrito(producto)}
            onEliminar={() => eliminarProducto(index)}
          />
        ))}
      </div>
      <center>
      <section className="carrito">
        <div className="title-carrito">
            <h2>Carrito de compras</h2>
            <p className="total-p">Total: ${totalCarrito}</p>
        </div>
        <div className="carrito-grid">
            {carrito.length === 0 ? (
            <p className="total-p">El carrito está vacío</p>
            ) : (
            carrito.map((producto, index) => (
                <div className="producto-card" key={index}>
                <img src={producto.imagen} alt={producto.nombre} />
                <h4>{producto.nombre}</h4>
                <p>${producto.precio}</p>
                <button onClick={() => eliminarDelCarrito(index)} className="eliminar">
                    Quitar del carrito
                </button>
                </div>
            ))
            )}
        </div>
        </section>
        </center>
    </>
  )
}

export default Form
