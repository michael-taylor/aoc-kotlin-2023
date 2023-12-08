fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input.first().toCharArray()
        var network = mutableMapOf<String, Pair<String, String>>()
        input.drop(2).forEach {
            val node = it.split("=").first().trim()
            val left = it.substring(7, 10)
            val right = it.substring(12, 15)
            network[node] = Pair(left, right)
        }
        
        var count = 0
        var current = "AAA"
        do {
            instructions.forEach {
                if (it == 'L') current = network[current]!!.first
                else current = network[current]!!.second
                count++
                if (current == "ZZZ") return count
            }
        } while (true)
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first().toCharArray()
        var network = mutableMapOf<String, Pair<String, String>>()
        input.drop(2).forEach {
            val node = it.split("=").first().trim()
            val left = it.substring(7, 10)
            val right = it.substring(12, 15)
            network[node] = Pair(left, right)
        }

        var counts = mutableListOf<Long>()
        var currentNodes = network.keys.filter { it.endsWith("A") }

        currentNodes.forEach { node ->
            var current = node
            var count = 0L
            run breaking@ {
                do {
                    instructions.forEach {
                        if (it == 'L') current = network[current]!!.first
                        else current = network[current]!!.second
                        count++
                        if (current.endsWith("Z")) return@breaking
                    }
                } while (true)
            }
            counts.add(count)
        }
        
        return lcm(counts)
    }
    
    /*
    fun part2(input: List<String>): Int {
        val instructions = input.first().toCharArray()
        var network = mutableMapOf<String, Pair<String, String>>()
        input.drop(2).forEach {
            val node = it.split("=").first().trim()
            val left = it.substring(7, 10)
            val right = it.substring(12, 15)
            network[node] = Pair(left, right)
        }

        var count = 0
        var currentNodes = network.keys.filter { it.endsWith("A") }
        do {
            instructions.forEach {
                if (it == 'L') currentNodes = currentNodes.map { network[it]!!.first }
                else currentNodes = currentNodes.map { network[it]!!.second }
                count++
                if (currentNodes.all { it.endsWith("Z") }) return count
            }
        } while (true)
    }
*/
    val testInput = readInput("Day08_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
