package minesweeper

import java.lang.IndexOutOfBoundsException

fun main() {
    val rows: Int = 9
    val places: Int = 9
    var mines: Int = 0

    do {
        println("How many mines do you want on the field?")
        mines = readln().toInt()
    }
    while (mines > rows * places)

    val mineSweeper = Minesweeper(rows, places, mines)

    mineSweeper.GenerateMap()

    mineSweeper.showMap()
//    mineSweeper.showMapWithHints()

    mineSweeper.setMarker()
}

class Minesweeper (private val rows: Int,
                   private val places: Int,
                   private val mines: Int) {

    val listOfMines = mutableListOf<String>()
    val listOfMarkers = mutableListOf<String>()
    val exploredCels = mutableListOf<String>()
    val minesMap: MutableList<MutableList<String>> = mutableListOf()

    fun GenerateMap() {

        for (row in 0 until this.rows) {
            val rowList: MutableList<String> = mutableListOf()
            repeat(this.places) {
                rowList.add(".")
            }

            this.minesMap.add(rowList)
        }
        generateMines(this.minesMap)
    }

    fun showMines() {
        for (mine in listOfMines) {
            val x = mine.split("+")[1].toInt()
            val y= mine.split("+")[0].toInt()
            this.minesMap[y][x] = "X"
        }
    }

    fun setMarker () {
        if (this.listOfMarkers == this.listOfMines) {
            println("\nCongratulations! You found all the mines!")
            System.exit(0)
        }

        println("\nSet/unset mines marks or claim a cell as free:")
        val (x, y, t) = readln().split(" ")

        if(t == "free") {
            when {
                this.listOfMines.count { it == "${y.toInt()-1}+${x.toInt()-1}" } == 1 -> {
                    println("\nYou stepped on a mine and failed!")
                    this.showMines()
                    this.showMap()
                    System.exit(0)
                }
                this.listOfMines.count { it == "${y.toInt()-1}+${x.toInt()-1}" } == 0 -> {
                    val minesAround = this.checkMinesAround(y.toInt()-1, x.toInt()-1)

                    this.showMap()
                    this.setMarker()
                }
            }
        }
        else {
            if (this.minesMap[y.toInt() - 1][x.toInt() - 1] != "." && this.minesMap[y.toInt() - 1][x.toInt() - 1] != "*") {
                println("There is a number here!")
                this.setMarker()
            } else if (this.minesMap[y.toInt() - 1][x.toInt() - 1] == "*") {
                this.minesMap[y.toInt() - 1][x.toInt() - 1] = "."
                this.listOfMarkers.remove("${y.toInt() - 1}+${x.toInt() - 1}")
                this.listOfMarkers.sort()
                this.showMap()
                this.setMarker()
            } else {
                this.minesMap[y.toInt() - 1][x.toInt() - 1] = "*"
                this.listOfMarkers.add("${y.toInt() - 1}+${x.toInt() - 1}")
                this.listOfMarkers.sort()
                this.showMap()
                this.setMarker()
            }
        }
    }

    fun generateMines (minesMap: MutableList<MutableList<String>>) {
        var rowPosition: Int = 0
        var placePosition: Int = 0

        repeat(mines) {
            do {
                rowPosition = (0 until this.rows ).random()
                placePosition = (0 until this.places ).random()
            }
            while(this.listOfMines.count { it == "$rowPosition+$placePosition" } == 1)

            this.listOfMines.add("$rowPosition+$placePosition")

//            this.minesMap[rowPosition][placePosition] = "X"
        }

        this.listOfMines.sort()

        this.minesMap
    }

    fun showMap(hints: Boolean = false) {

        println()
        print(" |")
        for (n in 1..this.places) {
            print(n)
        }
        print("|")

        println()
        print("-|")
        for (n in 1..this.places) {
            print("-")
        }
        print("|")
        println()

        for(rowIndex in 0 until this.minesMap.size){

            print("${rowIndex + 1}|")
            for (placeIndex in 0 until this.minesMap[rowIndex].size) {
                if (this.listOfMines.count { it == "$rowIndex+${placeIndex}" } == 0) {
                    if(hints) {
                        val minesAround = this.checkMinesAround(rowIndex, placeIndex)
                    }
                }
                print(this.minesMap[rowIndex][placeIndex])
            }
            print("|")

            println()
        }

        print("-|")
        for (n in 1..this.places) {
            print("-")
        }
        print("|")
    }

    private fun checkMinesAround (rowIndex: Int, placeIndex: Int): Int {
        var sum: Int = 0

        val rowBefore = if (rowIndex > 0) rowIndex - 1 else 0
        val rowAfter = if (rowIndex < this.rows - 1) rowIndex + 1 else this.rows - 1

        if (rowBefore != rowIndex) {
            if (this.listOfMines.count { it == "$rowBefore+${placeIndex - 1}" } == 1) sum++
            if (this.listOfMines.count { it == "$rowBefore+$placeIndex" } == 1) sum++
            if (this.listOfMines.count { it == "$rowBefore+${placeIndex + 1}" } == 1) sum++
        }

        if (this.listOfMines.count { it == "$rowIndex+${placeIndex - 1}" } == 1) sum++
        if (this.listOfMines.count { it == "$rowIndex+${placeIndex + 1}" } == 1) sum++

        if (rowAfter != rowIndex) {
            if (this.listOfMines.count { it == "$rowAfter+${placeIndex - 1}" } == 1) sum++
            if (this.listOfMines.count { it == "$rowAfter+$placeIndex" } == 1) sum++
            if (this.listOfMines.count { it == "$rowAfter+${placeIndex + 1}" } == 1) sum++
        }

        if (rowIndex in 0 until this.rows && placeIndex in 0 until this.places && this.exploredCels.count { it == "$rowIndex+$placeIndex" } == 0) {
            if (sum != 0) {
                this.minesMap[rowIndex][placeIndex] = sum.toString()
                this.exploredCels.add("$rowIndex+$placeIndex")
            } else {
                this.minesMap[rowIndex][placeIndex] = "/"
                this.exploredCels.add("$rowIndex+$placeIndex")
                this.exploreAround(rowIndex, placeIndex)
            }
        }

        return sum
    }

    private fun exploreAround (rowIndex: Int, placeIndex: Int) {

        try {
            this.checkMinesAround(rowIndex - 1,placeIndex - 1)
        } catch (_: IndexOutOfBoundsException) {
        }

        try {
            this.checkMinesAround(rowIndex - 1, placeIndex)
        } catch (_: IndexOutOfBoundsException) {
        }

        try {
            this.checkMinesAround(rowIndex - 1, placeIndex + 1)
        } catch (_: IndexOutOfBoundsException) {
        }

        try {
            this.checkMinesAround(rowIndex, placeIndex - 1)
        } catch (_: IndexOutOfBoundsException) {
        }

        try {
            this.checkMinesAround(rowIndex, placeIndex + 1)
        } catch (_: IndexOutOfBoundsException) {
        }

        try {
            this.checkMinesAround(rowIndex + 1, placeIndex - 1)
        } catch (_: IndexOutOfBoundsException) {
        }

        try {
            this.checkMinesAround(rowIndex + 1, placeIndex)
        } catch (_: IndexOutOfBoundsException) {
        }

        try {
            this.checkMinesAround(rowIndex + 1,placeIndex + 1)
        } catch (_: IndexOutOfBoundsException) {
        }
    }
}
