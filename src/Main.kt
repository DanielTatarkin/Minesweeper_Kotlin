package minesweeper

import java.util.*

fun main() {
    print("How many mines do you want on the field? ")
    val numOfMines = Scanner(System.`in`).nextInt()
    val fieldSize = 9

    val minefield = Minefield(fieldSize)
    minefield.placeMines(numOfMines)
    minefield.printField()
}