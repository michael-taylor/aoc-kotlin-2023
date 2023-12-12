fun main() {
    data class SpringConditions(val damageMap: String, val damageList: List<Int>)

    fun parseCondition(line: String): SpringConditions {
        return SpringConditions(
            damageMap = line.split(' ').first(),
            damageList = line.split(' ').last().split(',').filter { it.isNotBlank() }.map { it.toInt() }
        )
    }

    fun parseFoldedCondition(line: String): SpringConditions {
        val dl = line.split(' ').last().split(',').filter { it.isNotBlank() }.map { it.toInt() }.toMutableList()
        val dl5 = mutableListOf<Int>()
        dl5.addAll(dl)
        dl5.addAll(dl)
        dl5.addAll(dl)
        dl5.addAll(dl)
        dl5.addAll(dl)
        val dm = line.split(' ').first()
        val dm5 = mutableListOf<String>()
        dm5.add(dm)
        dm5.add(dm)
        dm5.add(dm)
        dm5.add(dm)
        dm5.add(dm)
        return SpringConditions(
            damageMap = dm5.joinToString("?"),
            damageList = dl5
        )
    }

    val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

    fun testSprings(map: String, damageList: List<Int>): Long {
        if (map.isEmpty()) {
            if (damageList.isNotEmpty()) {
                return 0L
            } else {
                return 1L
            }
        }

        if (map.startsWith(".")) return testSprings(map.substring(1), damageList)
        else if (map.startsWith("?")) {
            return (testSprings(map.substring(1), damageList)
                    + testSprings("#${map.substring(1)}", damageList).also { cache[map to damageList] = it })
        } else {
            // Check cache and shortcut
            cache[map to damageList]?.let { return it }

            var sum: Long = 0L

            if (damageList.isEmpty()) sum = 0L
            else if (map.length < damageList.first()) sum = 0L
            else if (map.substring(0, damageList.first()).contains('.')) sum = 0L
            else if (map.length == damageList.first()) {
                if (damageList.size == 1) {
                    sum = 1L
                } else {
                    sum = 0L
                }
            }
            else if (map[damageList.first()] == '#') sum = 0L
            else {
                sum = testSprings(map.substring(damageList.first()+1), damageList.drop(1))
            }

            cache[map to damageList] = sum
            return sum
        }
    }

    fun part1(input: List<String>): Long {
        val conditions = input.map { parseCondition(it) }
        val possibleArrangements = mutableListOf<Long>()
        conditions.forEach {
            val successes = testSprings(it.damageMap, it.damageList) // testAllCombinations(it.damageMap, it.damageList)
            if (successes == 0L) error("ERROR")
            possibleArrangements.add(successes)
        }
        return possibleArrangements.sum()
    }

    fun part2(input: List<String>): Long {
        val conditions = input.map { parseFoldedCondition(it) }
        val possibleArrangements = mutableListOf<Long>()
        conditions.forEach {
            val successes = testSprings(it.damageMap, it.damageList)
            if (successes == 0L) error("ERROR")
            possibleArrangements.add(successes)
        }
        return possibleArrangements.sum()
    }

    val testData = mutableListOf(
        "???.### 1,1,3",
        ".??..??...?##. 1,1,3",
        "?#?#?#?#?#?#?#? 1,3,1,6",
        "????.#...#... 4,1,1",
        "????.######..#####. 1,6,5",
        "?###???????? 3,2,1"
    )

    val testData2 = mutableListOf(
        "?#.??????#??#?#?#?#? 1,1,15", // 1
        ".?????????? 2,2"              // 21?
    )

    val testInput = readInput("Day12_test")
    println("Test 1: ${part1(testData)} [21]")
    println("Test 2: ${part1(testData2)} [22]")
    println("Part 1: ${part1(testInput)} [8193]")
    println("Test 2: ${part2(testData)} [525152]")
    println("Part 2: ${part2(testInput)}")
}
