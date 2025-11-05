# 13 Game – 3 Player (Single Room)

A browser-based implementation of the card game **13** (also known as Tiến Lên) for **three players** in a single online room.  
This project allows three people to connect, play in real-time, and compete until one player runs out of cards.

---

##  Game Overview

- **Mode:** Player vs Player vs Player (3 players)
- **Room Type:** Single shared room (all players automatically join the same room)
- **Goal:** Be the first to play all your cards
- **Rules:** Standard "13" rules with turn-based play

---

##  Features

- Multi-room lobby system with access control for up to three players per room
- Real-time multiplayer gameplay via WebSocket
- Automatic turn order handling for 3 players
- Card validation (only legal plays are allowed)
- Dynamic game state updates for all players
- Game starts when **all 3 players have joined**
- Instant win detection: all players are directed back to startpage after a win
- Instant restart: if a player leaves or disconnects all players are directed to start page to start again

---

## Tech Stack

- **Frontend:** React (JavaScript), CSS
- **Backend:** Spring Boot (Java) with WebSocket support
