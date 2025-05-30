import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import Carrito from './pages/Carrito'
import DetalleProducto from './pages/DetalleProducto'
import './App.css'
const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/carrito" element={<Carrito />} />
        <Route path="/producto/:id" element={<DetalleProducto />} />
      </Routes>
   </BrowserRouter>

  )
}

export default App
