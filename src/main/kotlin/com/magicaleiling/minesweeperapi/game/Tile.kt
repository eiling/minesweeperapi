package com.magicaleiling.minesweeperapi.game

class Tile(val type: Int) {
    companion object {
        const val WALL: Int = 1
        const val SAFE: Int = 2
        const val BOMB: Int = 3
    }

    val character: String = when (type) {
        WALL -> " "
        SAFE -> "0"
        BOMB -> "X"
        else -> " "
    }

    var open = type == WALL
    var bombCount = 0

    fun print(): String {
        if (open) {
            if (type == SAFE) {
                return bombCount.toString()
            }
            return character
        }
        return "O"
    }
}
