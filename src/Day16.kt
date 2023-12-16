enum class CardinalDirection {
    North,
    East,
    South,
    West
}

fun main() {
    val cache = mutableListOf<Triple<Int, Int, CardinalDirection>>()

    fun travelBeam(grid: List<CharArray>, x: Int, y: Int, heading: CardinalDirection, visitedTiles: MutableList<CharArray>): List<CharArray> {
        var localX = x
        var localY = y

        if (x < 0 || x >= grid.first().size) return visitedTiles
        if (y < 0 || y >= grid.size) return visitedTiles

        // Stop cycles
        if (cache.contains(Triple(x, y, heading))) return visitedTiles

        when (heading) {
            CardinalDirection.North -> {
                while (localY >= 0 && listOf('.', '|').contains(grid.at(localX, localY))) {
                    visitedTiles.set(localX, localY, '#')
                    localY--
                }
                cache.add(Triple(x, y, heading))
                if (localY >= 0) {
                    visitedTiles.set(localX, localY, '#')
                    when (grid.at(localX, localY)) {
                        '\\' -> travelBeam(grid, localX-1, localY, CardinalDirection.West, visitedTiles)
                        '/'  -> travelBeam(grid, localX+1, localY, CardinalDirection.East, visitedTiles)
                        '-'  -> {
                            travelBeam(grid, localX-1, localY, CardinalDirection.West, visitedTiles)
                            travelBeam(grid, localX+1, localY, CardinalDirection.East, visitedTiles)
                        }
                    }
                }
            }
            CardinalDirection.South -> {
                while (localY < grid.size && listOf('.', '|').contains(grid.at(localX, localY))) {
                    visitedTiles.set(localX, localY, '#')
                    localY++
                }
                cache.add(Triple(x, y, heading))
                if (localY < grid.size) {
                    visitedTiles.set(localX, localY, '#')
                    when (grid.at(localX, localY)) {
                        '\\' -> travelBeam(grid, localX+1, localY, CardinalDirection.East, visitedTiles)
                        '/'  -> travelBeam(grid, localX-1, localY, CardinalDirection.West, visitedTiles)
                        '-'  -> {
                            travelBeam(grid, localX+1, localY, CardinalDirection.East, visitedTiles)
                            travelBeam(grid, localX-1, localY, CardinalDirection.West, visitedTiles)
                        }
                    }
                }
            }
            CardinalDirection.East -> {
                while (localX < grid.first().size && listOf('.', '-').contains(grid.at(localX, localY))) {
                    visitedTiles.set(localX, localY, '#')
                    localX++
                }
                cache.add(Triple(x, y, heading))
                if (localX < grid.first().size) {
                    visitedTiles.set(localX, localY, '#')
                    when (grid.at(localX, localY)) {
                        '\\' -> travelBeam(grid, localX, localY+1, CardinalDirection.South, visitedTiles)
                        '/'  -> travelBeam(grid, localX, localY-1, CardinalDirection.North, visitedTiles)
                        '|'  -> {
                            travelBeam(grid, localX, localY+1, CardinalDirection.South, visitedTiles)
                            travelBeam(grid, localX, localY-1, CardinalDirection.North, visitedTiles)
                        }
                    }
                }
            }
            CardinalDirection.West -> {
                while (localX > 0 && listOf('.', '-').contains(grid.at(localX, localY))) {
                    visitedTiles.set(localX, localY, '#')
                    localX--
                }
                cache.add(Triple(x, y, heading))
                if (localX >= 0) {
                    visitedTiles.set(localX, localY, '#')
                    when (grid.at(localX, localY)) {
                        '\\' -> travelBeam(grid, localX, localY-1, CardinalDirection.North, visitedTiles)
                        '/'  -> travelBeam(grid, localX, localY+1, CardinalDirection.South, visitedTiles)
                        '|'  -> {
                            travelBeam(grid, localX, localY-1, CardinalDirection.North, visitedTiles)
                            travelBeam(grid, localX, localY+1, CardinalDirection.South, visitedTiles)
                        }
                    }
                }
            }
        }

        return visitedTiles
    }

    fun part1(input: List<String>): Long {
        cache.clear()
        val visitedTiles = MutableList<CharArray>(input.asGrid().size) { CharArray(input.asGrid().first().size) }
        travelBeam(input.asGrid(), 0, 0, CardinalDirection.East, visitedTiles)
        return visitedTiles.sumOf { it.count { it == '#' } }.toLong()
    }

    fun part2(input: List<String>): Long {
        var maxLit = 0L

        IntRange(0, input.size-1).forEachIndexed { index, i ->
            cache.clear()
            val visitedTiles = MutableList<CharArray>(input.asGrid().size) { CharArray(input.asGrid().first().size) }
            travelBeam(input.asGrid(), 0, index, CardinalDirection.East, visitedTiles)
            val visited = visitedTiles.sumOf { it.count { it == '#' } }.toLong()
            if (visited > maxLit) maxLit = visited
        }
        IntRange(0, input.size-1).forEachIndexed { index, i ->
            cache.clear()
            val visitedTiles = MutableList<CharArray>(input.asGrid().size) { CharArray(input.asGrid().first().size) }
            travelBeam(input.asGrid(), input.first().length-1, index, CardinalDirection.West, visitedTiles)
            val visited = visitedTiles.sumOf { it.count { it == '#' } }.toLong()
            if (visited > maxLit) maxLit = visited
        }
        IntRange(0, input.first().length-1).forEachIndexed { index, i ->
            cache.clear()
            val visitedTiles = MutableList<CharArray>(input.asGrid().size) { CharArray(input.asGrid().first().size) }
            travelBeam(input.asGrid(), index, 0, CardinalDirection.South, visitedTiles)
            val visited = visitedTiles.sumOf { it.count { it == '#' } }.toLong()
            if (visited > maxLit) maxLit = visited
        }
        IntRange(0, input.first().length-1).forEachIndexed { index, i ->
            cache.clear()
            val visitedTiles = MutableList<CharArray>(input.asGrid().size) { CharArray(input.asGrid().first().size) }
            travelBeam(input.asGrid(), index, input.size-1, CardinalDirection.North, visitedTiles)
            val visited = visitedTiles.sumOf { it.count { it == '#' } }.toLong()
            if (visited > maxLit) maxLit = visited
        }

        return maxLit
    }

    val testData = """
.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....
    """.trimIndent()

    val actualAnswer1 = part1(testData.split("(\\r\\n|\\r|\\n)".toRegex()))
    val expectedAnswer1 = 46L

    val testInput = readInput("Day16_test")

    println("Test 1: $actualAnswer1 [$expectedAnswer1]")
    println("Part 1: ${part1(testInput)}")

    val actualAnswer2 = part2(testData.split("(\\r\\n|\\r|\\n)".toRegex()))
    val expectedAnswer2 = 51L

    println("Test 2: $actualAnswer2 [$expectedAnswer2]")
    println("Part 2: ${part2(testInput)}")
}
