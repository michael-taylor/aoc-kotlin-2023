import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun expandGalaxy(input: List<String>): Array<CharArray> {
        val expandedGalaxy = mutableListOf<String>()

        input.forEach {
            if (it.contains('#'))
                expandedGalaxy.add(it)
            else {
                expandedGalaxy.add(it)
                expandedGalaxy.add(it)
            }
        }

        var numEmptyCols = 0
        IntRange(0, input.first().length-1).forEachIndexed { colIndex, col ->
            var isEmpty = true

            IntRange(0, input.size-1).forEachIndexed { rowIndex, row ->
                if (input[rowIndex][colIndex] == '#') isEmpty = false
            }

            if (isEmpty) {
                IntRange(0, expandedGalaxy.size-1).forEachIndexed { rowIndex, row ->
                    expandedGalaxy[rowIndex] = expandedGalaxy[rowIndex].substring(0, colIndex + numEmptyCols) + '.' + expandedGalaxy[rowIndex].substring(colIndex + numEmptyCols)
                }
                numEmptyCols++
            }
        }

        return expandedGalaxy.map { it.toCharArray() }.toTypedArray()
    }

    fun getEmptyRows(input: List<String>): List<Int> {
        val emptyRows = mutableListOf<Int>()
        input.forEachIndexed { index, row ->
            if (!row.contains('#')) emptyRows.add(index)
        }
        return emptyRows
    }

    fun getEmptyCols(input: List<String>): List<Int> {
        val emptyCols = mutableListOf<Int>()
        IntRange(0, input.first().length-1).forEachIndexed { colIndex, col ->
            var isEmpty = true

            IntRange(0, input.size-1).forEachIndexed { rowIndex, row ->
                if (input[rowIndex][colIndex] == '#') isEmpty = false
            }

            if (isEmpty) {
                emptyCols.add(colIndex)
            }
        }
        return emptyCols
    }

    fun getColExpansion(colIndexes: List<Int>, x1: Int, x2: Int): Int {
        val lower = min(x1, x2)
        val upper = max(x1, x2)
        return colIndexes.count { it in (lower + 1)..<upper }
    }

    fun getRowExpansion(rowIndexes: List<Int>, y1: Int, y2: Int): Int {
        val lower = min(y1, y2)
        val upper = max(y1, y2)
        return rowIndexes.count { it in (lower + 1)..<upper }
    }

    data class Galaxy(val x: Int, val y: Int)

    fun part1(input: List<String>): Int {
        val expanded = expandGalaxy(input)
        val galaxies = mutableListOf<Galaxy>()

        expanded.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, item ->
                if (item == '#') galaxies.add(Galaxy(x = colIndex, y = rowIndex))
            }
        }

        val links = mutableListOf<Pair<Galaxy, Galaxy>>()
        galaxies.forEach { galaxy1 ->
            galaxies.forEach { galaxy2 ->
                if (galaxy1 != galaxy2) // && !links.contains(Pair(galaxy1, galaxy2)) && !links.contains(Pair(galaxy2, galaxy1)))
                    links.add(Pair(galaxy1, galaxy2))
            }
        }

        return links.sumOf {
            abs(it.second.x - it.first.x) + abs(it.second.y - it.first.y)
        } / 2
    }

    fun part2(input: List<String>, expansionFactor: Int = 1000000): Long {
        val emptyCols = getEmptyCols(input)
        val emptyRows = getEmptyRows(input)
        val galaxies = mutableListOf<Galaxy>()

        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, item ->
                if (item == '#') galaxies.add(Galaxy(x = colIndex, y = rowIndex))
            }
        }

        val links = mutableListOf<Pair<Galaxy, Galaxy>>()
        galaxies.forEach { galaxy1 ->
            galaxies.forEach { galaxy2 ->
                if (galaxy1 != galaxy2)
                    links.add(Pair(galaxy1, galaxy2))
            }
        }

        var totalDistance = 0L
        links.forEach {
            totalDistance += (abs(it.second.x - it.first.x) + abs(it.second.y - it.first.y)
                    - getColExpansion(emptyCols, it.first.x, it.second.x)
                    - getRowExpansion(emptyRows, it.first.y, it.second.y)
                    + (expansionFactor * getColExpansion(emptyCols, it.first.x, it.second.x))
                    + (expansionFactor * getRowExpansion(emptyRows, it.first.y, it.second.y)))
        }
        return totalDistance / 2L
    }

    // TESTS
    val testGalaxy = listOf(
        "...#......",
        ".......#..",
        "#.........",
        "..........",
        "......#...",
        ".#........",
        ".........#",
        "..........",
        ".......#..", // 1, 6
        "#...#....."  // 5, 11
    )

    val testInput = readInput("Day11_test")
    println("Test 1: ${part1(testGalaxy)} [374]")
    println("Part 1: ${part1(testInput)} [9608724]")
    println("Test 2: ${part2(testGalaxy, 10)} [1030]")
    println("Test 3: ${part2(testGalaxy, 100)} [8410]")
    println("Part 2: ${part2(testInput)}")
}
