package com.magicaleiling.minesweeperapi.game

import java.lang.RuntimeException
import java.util.concurrent.ThreadLocalRandom

class Minesweeper(val width: Int, val height: Int, bombs: Int, initialMoveLine: Int, initialMoveColumn: Int) {
    companion object {
        const val WALL: Int = -1
        const val UNKNOWN: Int = -2
        const val BOMB: Int = -3
    }

    val board: Array<Tile> = Array((width + 2) * (height + 2), init = { Tile(Tile.SAFE) })
    var tileCount: Int = width * height - bombs
    var state: String = "ongoing"  // ongoing/won/lost

    val adjacentSquares: IntArray =
        intArrayOf(-width - 3, -width - 2, -width - 1, -1, +1, width + 1, width + 2, width + 3)

    init {
        for (i in 0 until (width + 2) * (height + 2)) {
            if (i < width + 2
                || i % (width + 2) == 0
                || i >= (width + 2) * (height + 1)
                || i % (width + 2) == width + 1
            ) {
                board[i] = Tile(Tile.WALL)
            }
        }

        if (initialMoveLine < 0 || initialMoveLine >= height || initialMoveColumn < 0 || initialMoveColumn >= width) {
            throw RuntimeException("Invalid move")
        }

        board[initialMoveColumn + 1 + (initialMoveLine + 1) * (width + 2)].open = true
        tileCount -= 1

        val indices = IntArray(width * height, init = { i -> i })
        indices[initialMoveColumn + initialMoveLine * width] = indices[0]
        for (i in 1 until bombs + 1) {
            val r: Int = ThreadLocalRandom.current().nextInt(i, width * height)
            val bomb: Int = indices[r]
            indices[r] = indices[i]
            val l: Int = bomb / width
            val c: Int = bomb % width
            board[c + 1 + (l + 1) * (width + 2)] = Tile(Tile.BOMB)
        }

        for (i in 0 until height) {
            for (j in 0 until width) {
                val index = j + 1 + (i + 1) * (width + 2)
                board[index].bombCount = getBombCount(index)
            }
        }

        openSafeAround(initialMoveColumn + 1 + (initialMoveLine + 1) * (width + 2))
    }

    fun move(line: Int, column: Int) {
        if (line < 0 || line >= height || column < 0 || column >= width) {
            return
        }
        val tile = board[column + 1 + (line + 1) * (width + 2)]
        tile.open = true
        tileCount -= 1
        if (tile.type == Tile.BOMB) {
            state = "lost"
            return
        }
        if (tileCount == 0) {
            state = "won"
        }

        openSafeAround(column + 1 + (line + 1) * (width + 2))
    }

    fun openSafeAround(index: Int) {
        if (board[index].bombCount == 0) {
            for (i in adjacentSquares) {
                if (!board[index + i].open && board[index + i].type == Tile.SAFE) {
                    board[index + i].open = true
                    tileCount -= 1
                    openSafeAround(index + i)
                }
            }
        }
    }

    fun getBombCount(index: Int): Int {
        var count = 0
        for (i in adjacentSquares) {
            if (board[index + i].type == Tile.BOMB) {
                count += 1
            }
        }
        return count
    }

    fun prettyPrint(): String {
        val sb = StringBuilder()
        sb.append(" ".repeat(3))
        for (j in 1 until width) {
            sb.append(j % 10).append(" ")
        }
        sb.append(width % 10).append("\n")
        for (j in 0 until width + 1) {
            sb.append(board[j].print()).append(" ")
        }
        sb.append(board[width + 1].print()).append("\n")
        for (i in 1 until height + 1) {
            sb.append((i) % 10)
            for (j in 0 until width + 1) {
                sb.append(board[j + i * (width + 2)].print()).append(" ")
            }
            sb.append(board[width + 1 + i * (width + 2)].print()).append("\n")
        }
        for (j in 0 until width + 1) {
            sb.append(board[j + (height + 1) * (width + 2)].print()).append(" ")
        }
        sb.append(board[width + 1 + (height + 1) * (width + 2)].print()).append("\n")
        return sb.toString()
    }
}
