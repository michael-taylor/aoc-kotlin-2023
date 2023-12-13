import kotlin.math.min

enum class ReflectionType {
    Vertical,
    Horizontal
}

fun main() {
    data class Pattern(val map: Array<CharArray>, var reflectionLine: Int, var reflectionType: ReflectionType)

    fun getPatterns(input: List<String>): List<Pattern> {
        val allMaps = mutableListOf<Pattern>()
        val currentMap = mutableListOf<CharArray>()

        input.forEach {line ->
            if (line.isBlank()) {
                allMaps.add(Pattern(currentMap.toTypedArray(), 0, ReflectionType.Horizontal))
                currentMap.clear()
            } else {
                currentMap.add(line.toCharArray())
            }
        }

        // In case no blank line at end of file
        if (currentMap.isNotEmpty()) allMaps.add(Pattern(currentMap.toTypedArray(), 0, ReflectionType.Horizontal))

        return allMaps
    }

    fun getRow(pattern: Pattern, index: Int): CharArray {
        return pattern.map[index]
    }

    fun getCol(pattern: Pattern, index: Int): CharArray {
        return pattern.map.map {
            it[index]
        }.toCharArray()
    }

    fun areListsEqual(a: List<CharArray>, b: List<CharArray>): Boolean {
        if (a.size != b.size) return false
        IntRange(0, a.size - 1).forEach {
            if (a[it].size != b[it].size) return false
            if (!a[it].contentEquals(b[it])) return false
        }
        return true
    }

    fun areListsEqualWithSmudge(a: List<CharArray>, b: List<CharArray>): Boolean {
        if (a.size != b.size) return false
        var unequal = 0
        IntRange(0, a.size - 1).forEach {
            if (a[it].size != b[it].size) return false
            a[it].forEachIndexed { index, c ->
                if (c != b[it][index]) unequal++
                if (unequal > 1) return false
            }
        }
        return (unequal == 1)
    }

    fun findReflection(lines: List<CharArray>, smudged: Boolean = false): Int? {
        val range = IntRange(1, lines.size - 1)

        range.forEach {
            val partition = it
            val matchLines = min(it, lines.size - it)
            val start = if (it > (lines.size / 2)) it - matchLines else 0
            val end = if (it <= (lines.size / 2)) it + matchLines else lines.size
            val firstHalf = lines.subList(start, partition)
            val lastHalf = lines.subList(partition, end).reversed()
            if (smudged && areListsEqualWithSmudge(firstHalf, lastHalf)) return partition
            else if (!smudged && areListsEqual(firstHalf, lastHalf)) return partition
        }

        return null
    }

    fun part1(input: List<String>): Long {
        val maps = getPatterns(input)

        maps.forEach { map ->
            val rows = IntRange(0, map.map.size - 1).map { i -> getRow(map, i) }
            val cols = IntRange(0, map.map.first().size - 1).map { i -> getCol(map, i) }

            var line = findReflection(rows)
            if (line != null) {
                map.reflectionLine = line
                map.reflectionType = ReflectionType.Horizontal
            } else {
                line = findReflection(cols)
                if (line != null) {
                    map.reflectionLine = line
                    map.reflectionType = ReflectionType.Vertical
                }
            }
        }

        return maps.sumOf { if (it.reflectionType == ReflectionType.Vertical) it.reflectionLine else (100 * it.reflectionLine) }.toLong()
    }

    fun part2(input: List<String>): Long {
        val maps = getPatterns(input)

        maps.forEach { map ->
            val rows = IntRange(0, map.map.size - 1).map { i -> getRow(map, i) }
            val cols = IntRange(0, map.map.first().size - 1).map { i -> getCol(map, i) }

            var line = findReflection(rows, true)
            if (line != null) {
                map.reflectionLine = line as Int
                map.reflectionType = ReflectionType.Horizontal
            } else {
                line = findReflection(cols, true)
                if (line != null) {
                    map.reflectionLine = line as Int
                    map.reflectionType = ReflectionType.Vertical
                }
            }
        }

        return maps.sumOf { if (it.reflectionType == ReflectionType.Vertical) it.reflectionLine else (100 * it.reflectionLine) }.toLong()
    }

    val testData = mutableListOf<String>(
        "#.##..##.",
        "..#.##.#.",
        "##......#",
        "##......#",
        "..#.##.#.",
        "..##..##.",
        "#.#.##.#.",
        "",
        "#...##..#",
        "#....#..#",
        "..##..###",
        "#####.##.",
        "#####.##.",
        "..##..###",
        "#....#..#"
    )

    val actualAnswer1 = part1(testData)
//    val expectedAnswer1 = 405L
//    assert(actualAnswer1 == expectedAnswer1) {
//        println("Part 1 test: expected $expectedAnswer1 but got $actualAnswer1")
//    }

    val testInput = readInput("Day13_test")

    println("Test 1: $actualAnswer1")
    println("Part 1: ${part1(testInput)}")

    val actualAnswer2 = part2(testData)
//    val expectedAnswer2 = 0L
//    assert(actualAnswer2 == expectedAnswer2) {
//        println("Part 2 test: expected $expectedAnswer2 but got $actualAnswer2")
//    }
//
    println("Test 2: $actualAnswer2")
    println("Part 2: ${part2(testInput)}")
}
