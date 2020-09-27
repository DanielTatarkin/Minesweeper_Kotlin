package minesweeper

import kotlin.system.exitProcess


const val MINE_CHAR = 'X'
const val EMPTY_CHAR = '.'
const val FLAG_CHAR = '*'
const val FREE_CHAR = '/'

/**
 * This object initializes an empty minefield
 *
 * @param fieldSize: Int - Size of the field (eg 9 then 9x9)
 */
class Minefield(val fieldSize: Int) {
    val currFieldArray: Array<CharArray> = Array(fieldSize) { CharArray(fieldSize) { EMPTY_CHAR } }
    var numOfMines: Int = 0

    companion object MinefieldState {
        var gameComplete = false
        private lateinit var fieldArray: Array<CharArray>
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
            var successfulPlacement = false
            while (!successfulPlacement) {
                val row = this.currFieldArray.indices.random()
                val column = this.currFieldArray.indices.random()
                val fieldCell = currFieldArray[row][column]
                if (fieldCell != MINE_CHAR) {
                    currFieldArray[row][column] = MINE_CHAR
                    addHint(row, column)
                } else continue
                successfulPlacement = true
            }
        }

        fieldArray = currFieldArray.copy()

        fieldArray.forEachIndexed { rowNum, row ->
            row.forEachIndexed { cellNum, cell ->
                if (cell == EMPTY_CHAR) fieldArray[rowNum][cellNum] = FREE_CHAR
            }
        }

        currFieldArray.forEachIndexed { rowNum, row ->
            row.forEachIndexed { cellNum, cell ->
                currFieldArray[rowNum][cellNum] = EMPTY_CHAR
            }
        }
    }

    // Array deep copy util function
    fun Array<CharArray>.copy() = Array(size) { get(it).clone() }

    fun addHint(mineRow: Int, mineColumn: Int) {
        for (row in -1..1) {
            for (column in -1..1) {
                if (mineColumn + column in 0 until fieldSize && mineRow + row in 0 until fieldSize)
                    increaseCellHint(mineRow + row, mineColumn + column)
            }
        }
    }

    fun increaseCellHint(row: Int, column: Int) {
        val selectedCell = this.currFieldArray[row][column]
        when (selectedCell) {
            MINE_CHAR -> return
            EMPTY_CHAR -> {
                this.currFieldArray[row][column] = '1'
            }
            else -> {
                this.currFieldArray[row][column] = (selectedCell.toInt() + 1).toChar()
            }
        }
    }

    fun checkWin() {
        var count = 0
        currFieldArray.forEachIndexed { rowNum, row ->
            row.forEachIndexed { cellNum, cell ->
                if (cell == EMPTY_CHAR) count++
            }
        }
        if (count == numOfMines) {
            println("Congratulations! You found all the mines!")
            gameComplete = true
            return
        }
        if (minesFound == numOfMines && flagsSet == numOfMines) {
            printTrueField()
            println("Congratulations! You found all the mines!")
            gameComplete = true
        }
    }

    fun gameLost() {
        fieldArray.forEachIndexed { rowNum, row ->
            row.forEachIndexed { cellNum, cell ->
                if (cell == MINE_CHAR) currFieldArray[rowNum][cellNum] = MINE_CHAR
            }
        }
        printPlayerField()
        println("You stepped on a mine and failed!")
        gameComplete = true
    }

    fun openFreeCell(x: Int, y: Int) {
        for (row in -1..1) {
            for (column in -1..1) {
                if (x + column in 0 until fieldSize && y + row in 0 until fieldSize) {
                    val selectedTrueCell = fieldArray[y + row][x + column]
                    val selectedCell = currFieldArray[y + row][x + column]
                    when {
                        selectedTrueCell == FREE_CHAR && selectedCell != FREE_CHAR -> {
                            currFieldArray[y + row][x + column] = FREE_CHAR
                            openFreeCell(x + column, y + row)
                        }
                        selectedTrueCell.isDigit() -> {
                            currFieldArray[y + row][x + column] = selectedTrueCell
                        }
                    }
                }
            }
        }
    }

    fun openCell(x: Int, y: Int, action: String) {
        val selectedTrueCell = fieldArray[y - 1][x - 1]
        val selectedCell = this.currFieldArray[y - 1][x - 1]

        when (action) {
            "free" -> when {
                selectedTrueCell == FLAG_CHAR -> {
                    this.currFieldArray[y - 1][x - 1] = EMPTY_CHAR
                    flagsSet--
                    if (fieldArray[y - 1][x - 1] == MINE_CHAR) minesFound--
                }
                selectedTrueCell.isDigit() -> {
                    if (selectedCell == EMPTY_CHAR) {
                        this.currFieldArray[y - 1][x - 1] = selectedTrueCell
                    } else {
                        println("There is a number here!")
                        return
                    }
                }
                selectedTrueCell == FREE_CHAR -> {
                    this.currFieldArray[y - 1][x - 1] = FREE_CHAR
                    openFreeCell(x - 1, y - 1)
                }
                selectedTrueCell == MINE_CHAR -> {
                    gameLost()
                    exitProcess(0)
                }
            }
            "mine" -> {
                when {
                    selectedCell == FLAG_CHAR -> {
                        this.currFieldArray[y - 1][x - 1] = EMPTY_CHAR
                        flagsSet--
                        if (fieldArray[y - 1][x - 1] == MINE_CHAR) minesFound--
                    }
                    selectedTrueCell.isDigit() -> {
                        if (selectedCell == EMPTY_CHAR) {
                            this.currFieldArray[y - 1][x - 1] = FLAG_CHAR
                            flagsSet++
                        } else {
                            this.currFieldArray[y - 1][x - 1] = EMPTY_CHAR
                            flagsSet--
                            return
                        }
                    }
                    selectedTrueCell == FREE_CHAR -> {
                        if (selectedCell == EMPTY_CHAR) {
                            this.currFieldArray[y - 1][x - 1] = FLAG_CHAR
                            flagsSet++
                            if (fieldArray[y - 1][x - 1] == MINE_CHAR) minesFound++
                        } else {
                            this.currFieldArray[y - 1][x - 1] = EMPTY_CHAR
                            flagsSet--
                            if (fieldArray[y - 1][x - 1] == MINE_CHAR) minesFound--
                        }
                    }
                    else -> {
                        this.currFieldArray[y - 1][x - 1] = FLAG_CHAR
                        flagsSet++
                        if (fieldArray[y - 1][x - 1] == MINE_CHAR) minesFound++
                    }
                }
            }
            else -> {
                println("Improper action specification, type \'free\' or \'mine\' after coordinates.")
                return
            }
        }


        printPlayerField()
        checkWin()
    }

    fun printPlayerField() {
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

    fun printTrueField() {
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
        for (row in fieldArray) {
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