import React from 'react'
import '../styles/GameBoard.css'
import { useLocation } from "react-router-dom"
import { useState } from 'react'
import { useCurrentPlay, 
  usePlayerHand, 
  useTurn, 
  useGameOver,
  useOtherPlayers,
  useHandleLeave,
  useGameEndEvent,
  useIsGameStarted
} from "../useEffects/myapi"
import { renderCards,
  toggleCardSelection,
  getCardImagePath
} from '../useEffects/handlerfuncs.jsx'
import { handlePlay,
  handleRestart
 } from '../useEffects/onclicks'


export default function GameTable() {
  const location = useLocation();
  const { playerIndex, playerName } = location.state;

  const [currentPlay, setCurrentPlay] = useState([]);
  const [hand, setHand] = useState([]);
  const [selectedCards, setSelectedCards] = useState([]);
  const [turn, setTurn] = useState(null);
  const [isGameOver, setIsGameOver] = useState(true);
  const [isGameStarted, setIsGameStarted] = useState(false);
  const [leftHandSize, setLeftHandSize] = useState(0);
  const [rightHandsize, setRightHandSize] = useState(0);

useIsGameStarted(setIsGameStarted)
useHandleLeave(playerIndex, isGameOver, isGameStarted);
useOtherPlayers(playerIndex, setLeftHandSize, setRightHandSize, isGameStarted);

useGameEndEvent();
useCurrentPlay(setCurrentPlay);
usePlayerHand(playerIndex, setHand);
useTurn(setTurn);
useGameOver(setIsGameOver);

    return (
      <div className="table">
        <div className="player-info">
        <h2>Player: {playerName}</h2>
        <h3>Index: {playerIndex}</h3>
        {turn === playerIndex
        ? <p style={{ color: "blue" }}>Your turn</p>
        : <p style={{ color: "red" }}>Waiting for other players...</p>
        }
        </div>
        <div className="player left">
          <div className="scroll-vertical">
            {renderCards(leftHandSize)}
          </div>
        </div>
  
        <div className="player right">
          <div className="scroll-vertical">
            {renderCards(rightHandsize)}
          </div>
        </div>
  
        <div className="community-cards">
          <div className="scroll-horizontal">
            {currentPlay.length > 0 ? (
              currentPlay.map((card, index) => (
                <img
                  key={index}
                  src={getCardImagePath(card)}
                  alt={`${card.rank} of ${card.suit}`}
                  className="card-back"
                />
              ))
            ) : (
              <p style={{ color: "white" }}>No cards played yet</p>
            )}
          </div>
        </div>
  
        <div className="player bottom">
          <div className="scroll-horizontal">
            {hand.length > 0 ? (
              hand.map((card, index) => (
                <img
                  key={index}
                  src={getCardImagePath(card)}
                  alt={`${card.rank} of ${card.suit}`}
                  className={`card-back ${selectedCards.some(
                    (c) => c.rank === card.rank && c.suit === card.suit
                  ) ? "selected" : ""}`}
                  onClick={() => toggleCardSelection(card, selectedCards, setSelectedCards)}
                />
              ))
            ) : (
              <p style={{ color: "white" }}>No cards given yet</p>
            )}
          </div>
        </div>

        <button
          className="play-button"
          onClick={() => handlePlay(selectedCards, playerIndex, setSelectedCards)}
          disabled={turn !== playerIndex}
        >
          Play Selected Cards
        </button>
      </div>
    );
  };