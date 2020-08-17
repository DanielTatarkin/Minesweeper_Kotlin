package minesweeper

import java.util.*

const val mineChar = 'X'
const val safeChar = '.'

fun main() {
    val scanner = Scanner(System.`in`)
    print("How many mines do you want on the field? ")
    val numOfMines = scanner.nextInt()
    val fieldSize = 9
    val mineField = createField(fieldSize)
    val newField = placeMines(mineField, numOfMines)
    println(newField)
}

/**
 * Creates an empty field
 *
 * @param fieldSize - [Int] Size of the field (eg 9 then 9x9)
 */
fun createField(fieldSize: Int): String {
    val emptyField = CharArray(fieldSize) { safeChar }.joinToString("", postfix = "\n")
    var mineField = ""
    repeat(fieldSize) { mineField += emptyField }
    return mineField
}

fun placeMines(mineField: String, numOfMines: Int): String {
    val newFieldArray = mineField.toCharArray()

    repeat(numOfMines) {
        var success = false
        while (!success) {
            val placement = mineField.indices.random()
            newFieldArray[placement] = if (newFieldArray[placement] != '\n' && newFieldArray[placement] != 'X') mineChar else continue
            success = true
        }
    }

    return newFieldArray.joinToString("")
}