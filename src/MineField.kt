package minesweeper

const val MINE_CHAR = 'X'
const val SAFE_CHAR = '.'

/**
 * This object initializes an empty minefield
 *
 * @param fieldSize: Int - Size of the field (eg 9 then 9x9)
 */
class Minefield(val fieldSize: Int) {
    val fieldArray: Array<CharArray> = Array(fieldSize) { CharArray(fieldSize) { SAFE_CHAR } }
    var numOfMines: Int = 0

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
                val row = this.fieldArray.indices.random()
                val column = this.fieldArray.indices.random()
                val fieldCell = fieldArray[row][column]
                if (fieldCell != MINE_CHAR) {
                    fieldArray[row][column] = MINE_CHAR
                    addHint(row, column)
                } else continue
                success = true
            }
        }
    }

    fun addHint(mineRow: Int, mineColumn: Int) {
        for (row in -1..1) {
            for (column in -1..1) {
                if (mineColumn + column in 0..8 && mineRow + row in 0..8)
                    increaseCellHint(mineRow + row, mineColumn + column)
            }
        }
    }

    fun increaseCellHint(row: Int, column: Int) {
        val selectedCell = this.fieldArray[row][column]
        when (selectedCell) {
            MINE_CHAR -> return
            SAFE_CHAR -> {
                this.fieldArray[row][column] = '1'
            }
            else -> {
                this.fieldArray[row][column] = (selectedCell.toInt() + 1).toChar()
            }
        }
    }

    fun printField() {
        for (row in this.fieldArray) {
            for (cell in row) {
                print(cell)
            }
            println()
        }
    }
}