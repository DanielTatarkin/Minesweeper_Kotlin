package minesweeper

import java.util.*

const val PROMPT = "Set/delete mines marks (x and y coordinates): > "

fun main() {
    val scanner = Scanner(System.`in`)
    print("How many mines do you want on the field? ")
    val numOfMines = scanner.nextInt()
    val fieldSize = 9

    val minefield = Minefield(fieldSize)
    minefield.placeMines(numOfMines)
    minefield.printFieldState()
    while (!Minefield.gameComplete) {
        print(PROMPT)
        var x_cord = scanner.nextInt()
        var y_cord = scanner.nextInt()
        minefield.placeFlag(x_cord, y_cord)
    }
}