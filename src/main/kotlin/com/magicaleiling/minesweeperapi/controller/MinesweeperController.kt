package com.magicaleiling.minesweeperapi.controller

import com.magicaleiling.minesweeperapi.service.MinesweeperService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MinesweeperController @Autowired constructor(val minesweeperService: MinesweeperService) {
    // TODO define json object for the response

    @GetMapping("/new/{difficulty}/{move}")
    fun startGameAndMove(@PathVariable difficulty: String, @PathVariable move: String): String {
        val m = move.split(":")
        val id = minesweeperService.newGame(difficulty, m[0].toInt(), m[1].toInt())
        return id + "\n" + minesweeperService.prettyPrint(id)
    }

    @GetMapping("/{id}")
    fun getGame(@PathVariable id: String): String {
        return minesweeperService.prettyPrint(id) + "\n" + minesweeperService.gameState(id)
    }

    @GetMapping("/{id}/{move}")
    fun move(@PathVariable id: String, @PathVariable move: String): String {
        val m = move.split(":")
        minesweeperService.move(id, m[0].toInt(), m[1].toInt())
        return minesweeperService.prettyPrint(id) + "\n" + minesweeperService.gameState(id)
    }
}
