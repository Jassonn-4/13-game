import axios from 'axios'
const API_BASE = import.meta.env.VITE_API_URL;

export async function handlePlay(selectedCards, playerIndex, setSelectedCards) {
    try {
      const response = await axios.post(`${API_BASE}/api/game/play`, selectedCards, {
        params: { playerIndex }
      });
  
      if (response.data === true) {
        console.log("Valid Play");
        setSelectedCards([]);
      } else {
        alert("Invalid play. Try again");
      }
    } catch (error) {
      console.error("Error playing cards:", error);
    }
  }
  
  export async function handleRestart(setIsGameOver, navigate) {
    try {
      await axios.post(`${API_BASE}/api/game/restart`);
      setIsGameOver(false);
      navigate('/');
    } catch (error) {
      console.error('Failed to restart game:', error);
    }
  }