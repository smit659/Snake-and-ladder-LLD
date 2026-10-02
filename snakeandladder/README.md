# Snake and Ladder — Low-Level Design (LLD)

A modular, extensible, and clean Low-Level Design for the classic **Snake and Ladder** game implemented in Java.

---

## 🏗 Architecture & Design Patterns

### 1. Design Patterns Used
- **Strategy Pattern (`DiceStrategy`)**: Encapsulates dice rolling logic. Easily swap between `FairDiceStrategy` (random gameplay) and `ManualDiceStrategy` (deterministic script for unit testing/replays).
- **Polymorphic Hierarchy (`Jump` $\to$ `Snake`, `Ladder`)**: Unified abstraction for all board jumping entities. Enforces domain invariants (`head > tail` for Snakes, `start < end` for Ladders).
- **State Pattern / Game Lifecycle (`GameState`)**: Controls game stages (`NOT_STARTED`, `IN_PROGRESS`, `GAME_OVER`).
- **Round-Robin Queue (`Deque<Player>`)**: Provides $O(1)$ turn rotations, immediate re-queueing for bonus rolls (e.g. rolling 6), and multi-winner rank tracking.

---

## 📊 Class Diagram

```mermaid
classDiagram
    class Player {
        -String id
        -String name
        -int position
        +moveTo(int position)
    }

    class Jump {
        <<abstract>>
        #int start
        #int end
        +getEntityName()* String
        +isSnake()* boolean
    }
    class Snake {
        +getHead() int
        +getTail() int
    }
    class Ladder {
        +getBottom() int
        +getTop() int
    }
    Jump <|-- Snake
    Jump <|-- Ladder

    class Board {
        -int totalCells
        -Map~Integer, Jump~ jumps
        +addSnake(int head, int tail)
        +addLadder(int start, int end)
        +hasJump(int pos) boolean
        +getJumpAt(int pos) Jump
    }
    Board o-- Jump

    class DiceStrategy {
        <<interface>>
        +roll(diceCount, sides) int
    }
    class FairDiceStrategy {
        +roll(diceCount, sides) int
    }
    class ManualDiceStrategy {
        +roll(diceCount, sides) int
    }
    DiceStrategy <|.. FairDiceStrategy
    DiceStrategy <|.. ManualDiceStrategy

    class Dice {
        -int count
        -int sides
        -DiceStrategy strategy
        +roll() int
    }
    Dice o-- DiceStrategy

    class SnakeAndLadderEngine {
        -Board board
        -Dice dice
        -Deque~Player~ playerQueue
        -List~Player~ winners
        -GameState state
        +playNextTurn() TurnResult
        +playAll() List~Player~
    }
    SnakeAndLadderEngine o-- Board
    SnakeAndLadderEngine o-- Dice
    SnakeAndLadderEngine o-- Player
```

---

## 📁 Package Structure

```
snakeandladder/
├── model/
│   ├── GameState.java           # Game lifecycle enum
│   ├── Player.java              # Player entity tracking id, name, position
│   ├── Jump.java                # Abstract jumper
│   ├── Snake.java               # Snake (head > tail)
│   ├── Ladder.java              # Ladder (start < end)
│   └── Board.java               # Board grid, jump registry, cycle prevention
├── strategy/
│   ├── DiceStrategy.java        # Strategy interface for dice rolls
│   ├── FairDiceStrategy.java    # Random fair dice rolling
│   └── ManualDiceStrategy.java  # Scripted rolls for testing/replays
├── engine/
│   ├── Dice.java                # Dice model configuring count & sides
│   ├── TurnResult.java          # Encapsulates turn outcome & audit log
│   └── SnakeAndLadderEngine.java# Game orchestrator, overshoot rules, bonus rolls
└── SnakeAndLadderDemo.java      # End-to-end demo runner
```

---

## 🚀 How to Run

```bash
# Compile
javac -d out snakeandladder/model/*.java snakeandladder/strategy/*.java snakeandladder/engine/*.java snakeandladder/*.java

# Run Demo
java -cp out snakeandladder.SnakeAndLadderDemo
```
