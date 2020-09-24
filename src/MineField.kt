package minesweeper

import java.lang.Integer.parseInt

const val MINE_CHAR = 'X'
const val SAFE_CHAR = '.'
const val FLAG_CHAR = '*'

/**
 * This object initializes an empty minefield
 *
 * @param fieldSize: Int - Size of the field (eg 9 then 9x9)
 */
class Minefield(val fieldSize: Int) {
    val currFieldArray: Array<CharArray> = Array(fieldSize) { CharArray(fieldSize) { SAFE_CHAR } }
    var numOfMines: Int = 0

    companion object MinefieldState {
        var gameComplete = false
        var fieldArray: Array<CharArray>? = null
        var minesFound = 0
        var flagsSet = 0
    }

    /**
     * Function for adding mines onto the field
     *
     * @param numOfMines: Int - number of mines to add to the field
     */
    fun placeMines(numOfMines: Int) {
        this.numOfMines += numOfMines

        repeat(numOfMines) {
            var success = false
            while (!success) {
                val row = this.currFieldArray.indices.random()
                val column = this.currFieldArray.indices.random()
                val fieldCell = currFieldArray[row][column]
                if (fieldCell != MINE_CHAR) {
                    currFieldArray[row][column] = MINE_CHAR
                    addHint(row, column)
                } else continue
                success = true
            }
        }

        fieldArray = currFieldArray.copy()

        currFieldArray.forEachIndexed { rowNum,row -> row.forEachIndexed { cellNum,cell -> if (cell == MINE_CHAR) currFieldArray[rowNum][cellNum] = SAFE_CHAR }}
    }

    // Array deep copy util function
    fun Array<CharArray>.copy() = Array(size) { get(it).clone() }

    fun addHint(mineRow: Int, mineColumn: Int) {
        for (row in -1..1) {
            for (column in -1..1) {
                if (mineColumn + column in 0..8 && mineRow + row in 0..8)
                    increaseCellHint(mineRow + row, mineColumn + column)
            }
        }
    }

    fun increaseCellHint(row: Int, column: Int) {
        val selectedCell = this.currFieldArray[row][column]
        when (selectedCell) {
            MINE_CHAR -> return
            SAFE_CHAR -> {
                this.currFieldArray[row][column] = '1'
            }
            else -> {
                this.currFieldArray[row][column] = (selectedCell.toInt() + 1).toChar()
            }
        }
    }

    fun checkWin() {
        if (minesFound == numOfMines && flagsSet == numOfMines) {
//            printField()
            println("Congratulations! You found all the mines!")
            gameComplete = true
        }
    }

    fun placeFlag(x_cord: Int, y_cord: Int) {
        val selectedCell = this.currFieldArray[y_cord-1][x_cord-1]

        when {
            selectedCell == FLAG_CHAR -> {
                this.currFieldArray[y_cord-1][x_cord-1] = SAFE_CHAR
                flagsSet--
                if (fieldArray!![y_cord-1][x_cord-1] == MINE_CHAR) minesFound--
            }
            selectedCell.isDigit() -> {
                println("There is a number here!")
                return
            }
            else -> {
                this.currFieldArray[y_cord-1][x_cord-1] = FLAG_CHAR
                flagsSet++
                if (fieldArray!![y_cord-1][x_cord-1] == MINE_CHAR) minesFound++
            }
        }
        printFieldState()
        checkWin()
    }

    fun printFieldState() {
        print(" |")
        for (x in 1..this.fieldSize) {
            print(x)
        }
        println("|")
        print("—|")
        for (x in 1..this.fieldSize) {
            print("—")
        }
        println("|")
        var rowNum = 1
        for (row in this.currFieldArray) {
            print("${rowNum++}|")
            for (cell in row) {
                print(cell)
            }
            println("|")
        }
        print("—|")
        for (x in 1..this.fieldSize) {
            print("—")
        }
        println("|")
    }

    fun printField() {
        print("\n |")
        for (x in 1..this.fieldSize) {
            print(x)
        }
        println("|")
        print("—|")
        for (x in 1..this.fieldSize) {
            print("—")
        }
        println("|")
        var rowNum = 1
        for (row in fieldArray!!) {
            print("${rowNum++}|")
            for (cell in row) {
                print(cell)
            }
            println("|")
        }
        print("—|")
        for (x in 1..this.fieldSize) {
            print("—")
        }
        println("|")
    }
}