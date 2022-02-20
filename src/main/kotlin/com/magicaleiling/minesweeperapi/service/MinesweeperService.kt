package com.magicaleiling.minesweeperapi.service

import com.magicaleiling.minesweeperapi.game.Minesweeper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MinesweeperService @Autowired constructor(val gameRepository: GameRepository) {
    fun newGame(difficulty: String, line: Int, column: Int): String {
        val game = when (difficulty) {
            "easy" -> Minesweeper(10, 7, 10, line - 1, column - 1)
            "medium" -> Minesweeper(22, 12, 40, line - 1, column - 1)
            "hard" -> Minesweeper(32, 18, 100, line - 1, column - 1)
            else -> throw RuntimeException("Invalid difficulty")
        }
        return gameRepository.addGame(game)
    }

    fun move(id: String, line: Int, column: Int) {
        val game = gameRepository.getGame(id) ?: throw RuntimeException("Game not found")
        if (game.state != "ongoing") {
            throw java.lang.RuntimeException("Game is over")
        }
        game.move(line - 1, column - 1)
    }

    fun gameState(id: String): String {
        val game = gameRepository.getGame(id) ?: throw RuntimeException("Game not found")
        return game.state
    }

    fun prettyPrint(id: String): String {
        val game = gameRepository.getGame(id) ?: throw RuntimeException("Game not found")
        return game.prettyPrint()
    }
}
