import './App.css'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import StartPage from "./views/StartPage"
import GameBoard from "./views/GameBoard"

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<StartPage />} />
          <Route path="/GameBoard" element={<GameBoard />} />
        </Routes>
      </Router>
    </>
  )
}

export default App
