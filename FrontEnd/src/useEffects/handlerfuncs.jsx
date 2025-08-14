import cardBack from '../assets/Cards/card_back.png'
import React from "react"

export function renderCards(count) {
  return Array(count)
    .fill(0)
    .map((_, index) => (
      <img
        key={index}
        src={cardBack}
        alt="Card Back"
        className="card-back"
      />
    ));
}

export function toggleCardSelection(card, selectedCards, setSelectedCards) {
    const index = selectedCards.findIndex(
      (c) => c.rank === card.rank && c.suit === card.suit
    );
  
    if (index > -1) {
      setSelectedCards((prev) => 
        prev.filter((_, i) => i !== index)
      );
    } else {
      setSelectedCards((prev) => [...prev, card]);
    }
  }

export function getCardImagePath(card) {
    const suit = card.suit.toLowerCase();
    const rankMap = {
      11: 'J',
      12: 'Q',
      13: 'K',
      14: 'A',
      15: '2'
    };
    let rank = rankMap[card.rank] || card.rank.toString();
    if (!isNaN(rank) && rank.length === 1) {
      rank = '0' + rank;
    }

    const url = new URL(`../assets/Cards/card_${suit}_${rank}.png`, import.meta.url).href;
    console.log("Card image:", url);
    return url;
  }