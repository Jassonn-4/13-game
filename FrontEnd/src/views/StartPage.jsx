import { useNavigate } from "react-router-dom"
import axios from 'axios'
import { useState } from 'react'
import '../styles/StartPage.css'

export default function StartPage() {
    const [playerName, setPlayerName] = useState('');
    const [message, setMessage] = useState('');

    const navigate = useNavigate();
    const handleJoin = async () => {
        try {
            const response = await axios.post(`/api/game/join?playerName=${encodeURIComponent(playerName)}`);
            setMessage(response.data);
            navigate("./GameBoard", {
                state: {
                    playerName,
                    playerIndex: response.data
                }
            });
        } catch (error) {
            console.error(error);
            setMessage(error.response?.data || 'an error ocurred');
        }
    };

    return (
        <>
        <div className="front-page">
            <div className="title">
                <p>13-Game</p>
            </div>
                <input
                    type="text"
                    value={playerName}
                    onChange={(e) => setPlayerName(e.target.value)}
                    placeholder="Enter your name"
                    required
                />
            <button className="enter-game" onClick={handleJoin}>
                Join Game
            </button>
            {message && <p>{message}</p>}
        </div>
        </>
    );
}