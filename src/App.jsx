import Form from './form'
import './App.css'
import { useState } from 'react'

const App =()=> {
   const [cantidadCarrito, setCantidadCarrito] = useState(0)
  return (
    <>
      <center>
        <h1>Tienda online Marcketplace</h1>
        <Form setCantidadCarrito={setCantidadCarrito} />
      </center>
      
      <div className="carrito-flotante">
        <img src="https://cdn-icons-png.flaticon.com/512/9341/9341730.png" alt="Carrito" />
        <span className="contador">{cantidadCarrito}</span>
      </div>
    </>
  )
}

export default App
