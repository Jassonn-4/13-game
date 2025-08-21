import { useEffect, useState } from "react"
import { useNavigate } from 'react-router-dom'
import axios from "axios"
const API_BASE = import.meta.env.VITE_API_URL;

// gets play on the table
export function useCurrentPlay(setCurrentPlay) {
useEffect(() => {
    const interval = setInterval(() => {
    axios.get(`${API_BASE}/api/game/currentPlay`)
    .then((response) => {
      setCurrentPlay(response.data); // Array of Card Objects
    })
    .catch((error) => {
      console.error('Failed to fetch current play:', error);
    });
  }, 2000);
  return () => clearInterval(interval);
  }, [setCurrentPlay]);
}

// gets players hand
export function usePlayerHand(playerIndex, setHand) {
  useEffect(() => {
    const interval = setInterval(() => {
    axios.get(`${API_BASE}/api/game/hand/${playerIndex}`)
    .then((response) => {
      setHand(response.data); // Array of current hand
    })
    .catch((error) => {
      console.error('Failed to fetch player hand:', error);
    });
  }, 2000);
  return () => clearInterval(interval);
  }, [playerIndex, setHand]);
}

// gets turn index
export function useTurn(setTurn) {
  useEffect(() => {
    const interval = setInterval(() => {
      axios.get(`${API_BASE}/api/game/turn`)
      .then((response) => {
        setTurn(response.data); // index of current turn
      })
      .catch((error) => {
        console.error('Failed to get the current turn:', error);
      });
    }, 2000);
    return () => clearInterval(interval);
  }, [setTurn]);
}

// checks if the game is over
export function useGameOver(setIsGameOver) {
  useEffect(() => {
    const interval = setInterval(() => {
      axios.get(`${API_BASE}/api/game/isOver`)
      .then((response) => {
        setIsGameOver(response.data);
      })
      .catch((error) => {
        console.error('Failed to check if game is over:', error);
      });
    }, 2000);
    return () => clearInterval(interval);
  }, []);
}

// gets other players handsizes
export function useOtherPlayers(playerIndex, setLeftHandSize, setRightHandSize, isGameStarted) {
  useEffect(() => {
    if (!isGameStarted) return;

    const leftIndex = (playerIndex + 1) % 3;
    const rightIndex = (playerIndex + 2) % 3;
  
    const interval = setInterval(() => {
      axios.get(`${API_BASE}/api/game/handSize/${leftIndex}`)
       .then(res => setLeftHandSize(res.data))
       .catch(err => console.error('Failed to get index:', err));
  
       axios.get(`${API_BASE}/api/game/handSize/${rightIndex}`)
        .then(res => setRightHandSize(res.data))
        .catch(err => console.error('Failed to get index:', err));
    }, 2000);
    return () => clearInterval(interval);
  }, [playerIndex, setLeftHandSize, setRightHandSize, isGameStarted]);
}

// gets if game is started
export function useIsGameStarted(setIsGameStarted) {
  useEffect(() => {
    const interval = setInterval(() => {
      axios.get(`${API_BASE}/api/game/isStarted`)
      .then((response) => {
        setIsGameStarted(response.data);
      })
      .catch((error) => {
        console.error('Failed to check if the game has started:', error);
      });
    }, 2000);
    return () => clearInterval(interval);
  }, [setIsGameStarted]);
}

// used to check if a player leaves the game and ends it for everyone
export function useHandleLeave(playerIndex, isGameOver, isGameStarted) {
  useEffect(() => {
    const handleLeave = async () => {
      try {
        await axios.post(`${API_BASE}/api/game/leave?playerIndex=${playerIndex}`);
      } catch (error) {
        console.error('Failed to notify backend of leaving:', error);
      }
    };

    const beforeUnload = (e) => {
      if (isGameStarted && !isGameOver) handleLeave();
      e.preventDefault();
      e.returnValue = '';
    };

    window.addEventListener('beforeunload', beforeUnload);

    return () => {
      window.removeEventListener('beforeunload', beforeUnload);
      if (isGameStarted && !isGameOver) handleLeave();
    };
  }, [playerIndex, isGameOver, isGameStarted]);
}

export function useGameEndEvent() {
  const navigate = useNavigate();

  useEffect(() => {
    const interval = setInterval(async () => {
      try {
        const response = await axios.get(`${API_BASE}/api/game/endEvent`);
        if (response.data === true) {
          await axios.post(`${API_BASE}/api/game/restart`);
          navigate('/');
        }
      } catch (error) {
        console.error('Failed to check gameEvent:', error);
      }
    }, 2000);
    return () => clearInterval(interval);
  }, [navigate]);
}
