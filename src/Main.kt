package minesweeper

import java.util.*

const val PROMPT = "Set/delete mines marks (x and y coordinates): "

fun main() {
    val scanner = Scanner(System.`in`)
    print("How many mines do you want on the field? ")
    val numOfMines = scanner.nextInt()
    val fieldSize = 9

    val minefield = Minefield(fieldSize)
    minefield.placeMines(numOfMines)
    minefield.printPlayerField()

    while (!Minefield.gameComplete) {
        print(PROMPT)
        val x = scanner.nextInt()
        val y = scanner.nextInt()
        val action = scanner.next()
        minefield.openCell(x, y, action)
    }
}