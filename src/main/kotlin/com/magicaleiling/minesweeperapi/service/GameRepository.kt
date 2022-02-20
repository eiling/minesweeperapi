package com.magicaleiling.minesweeperapi.service

import com.magicaleiling.minesweeperapi.game.Minesweeper
import org.springframework.stereotype.Component
import java.util.*

@Component
class GameRepository {
    // TODO limit amount of games
    // TODO expire old games so it doesn't run out of memory

    val map: MutableMap<String, Minesweeper> = HashMap()

    fun addGame(game: Minesweeper): String {
        val id = UUID.randomUUID().toString()
        map[id] = game
        return id
    }

    fun getGame(id: String): Minesweeper? {
        return map[id]
    }

    fun removeGame(id: String): Minesweeper? {
        return map.remove(id)
    }
}
