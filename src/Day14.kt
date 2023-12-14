fun main() {
    fun weightOfColumn(col: CharArray): Long {
        val stops = mutableListOf(col.size + 1)
        col.forEachIndexed { index, c -> if (c == '#') stops.add(col.size - index) }
        var currentStopIndex = 0
        var currentPastLastStop = 0
        var currentWeight = 0L
        col.forEachIndexed { index, c ->
            if (c == 'O') {
                if (((currentStopIndex + 1) < stops.size)
                    && (stops[currentStopIndex] - currentPastLastStop) == stops[currentStopIndex+1]) {
                    currentStopIndex++
                    currentPastLastStop = 0
                }
                while (((currentStopIndex + 1) < stops.size) && ((col.size - index) <= stops[currentStopIndex+1])) {
                    currentStopIndex++
                    currentPastLastStop = 0
                }
                currentPastLastStop++
                val weight = stops[currentStopIndex] - currentPastLastStop
                currentWeight += weight
            }
        }

        return currentWeight
    }

    fun rollRocks(grid: List<CharArray>, direction: Direction): List<CharArray> {
        val rows = grid.size
        val cols = grid.first().size
        val afterRoll = Array(rows) { CharArray(cols) { '.' } }

        when (direction) {
            Direction.Top -> {
                for (j in 0 until cols) {
                    var index = 0
                    for (i in 0 until rows) {
                        if (grid[i][j] == 'O') {
                            afterRoll[index++][j] = 'O'
                        } else if (grid[i][j] == '#') {
                            afterRoll[i][j] = '#'
                            index = i + 1
                        }
                    }
                }
            }
            Direction.Left -> {
                for (i in 0 until rows) {
                    var index = 0
                    for (j in 0 until cols) {
                        if (grid[i][j] == 'O') {
                            afterRoll[i][index++] = 'O'
                        } else if (grid[i][j] == '#') {
                            afterRoll[i][j] = '#'
                            index = j + 1
                        }
                    }
                }
            }
            Direction.Bottom -> {
                for (j in 0 until cols) {
                    var index = rows - 1
                    for (i in rows - 1 downTo 0) {
                        if (grid[i][j] == 'O') {
                            afterRoll[index--][j] = 'O'
                        } else if (grid[i][j] == '#') {
                            afterRoll[i][j] = '#'
                            index = i - 1
                        }
                    }
                }
            }
            Direction.Right -> {
                for (i in 0 until rows) {
                    var index = cols - 1
                    for (j in cols - 1 downTo 0) {
                        if (grid[i][j] == 'O') {
                            afterRoll[i][index--] = 'O'
                        } else if (grid[i][j] == '#') {
                            afterRoll[i][j] = '#'
                            index = j - 1
                        }
                    }
                }
            }
        }

        return afterRoll.toList()
    }

    fun spin(input: List<CharArray>): List<CharArray> {
        var grid = input
        grid = rollRocks(grid, Direction.Top)
        grid = rollRocks(grid, Direction.Left)
        grid = rollRocks(grid, Direction.Bottom)
        grid = rollRocks(grid, Direction.Right)
        return grid
    }

    fun findWeight(input: List<CharArray>): Int {
        return input.withIndex().sumOf { (index, row) ->
            row.count { it == 'O' } * (input.size - index)
        }
    }

    fun part1(input: List<String>): Long {
        val cols = mutableListOf<CharArray>()

        IntRange(0, input.size-1).forEach {col ->
            cols.add(input.map { it[col] }.toCharArray())
        }

        return cols.sumOf { weightOfColumn(it) }.toLong()
    }

    fun part2(input: List<String>): Long {
        val cache = mutableMapOf<List<String>, Int>()
        var grid = input.map { it.toCharArray() }
        var cycle = 0
        var cycleLength = 0

        while (cycle < 1_000_000_000) {
            val key = grid.map { it.concatToString() } // CharArrays can't be tested for equality to be used as a key
            if (key in cache) {
                cycleLength = cycle - cache[key]!!
                break
            }
            cache[key] = cycle
            grid = spin(grid)
            cycle++
        }

        if (cycleLength > 0) {
            val remaining = (1_000_000_000 - cycle) % cycleLength
            IntRange(0, remaining - 1).forEach {
                grid = spin(grid)
            }
        }

        return findWeight(grid).toLong()
    }

    val testData = mutableListOf<String>(
        "O....#....",
        "O.OO#....#",
        ".....##...",
        "OO.#O....O",
        ".O.....O#.",
        "O.#..O.#.#",
        "..O..#O..O",
        ".......O..",
        "#....###..",
        "#OO..#...."
    )

    val actualAnswer1 = part1(testData)
    val expectedAnswer1 = 136L

    val testInput = readInput("Day14_test")

    println("Test 1: $actualAnswer1 [$expectedAnswer1]")
    println("Part 1: ${part1(testInput)}")

    val actualAnswer2 = part2(testData)
    val expectedAnswer2 = 64L

    println("Test 2: $actualAnswer2 [$expectedAnswer2]")
    println("Part 2: ${part2(testInput)}")
}
