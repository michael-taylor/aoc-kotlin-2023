fun main() {
    data class RaceData(val time: Int, val recordDistance: Int)
    
    fun getRaceData(input: List<String>): List<RaceData> {
        val timeTokens = input[0].split(" ").drop(1).filter { it.isNotBlank() }
        val distanceTokens = input[1].split(" ").drop(1).filter { it.isNotBlank() }
        val raceData = mutableListOf<RaceData>()
        IntRange(0, timeTokens.size - 1).forEach {
            raceData.add(RaceData(timeTokens[it].trim().toInt(), distanceTokens[it].trim().toInt()))
        }
        return raceData
    }
    
    data class RaceDataL(val time: Long, val recordDistance: Long)
    
    fun getPart2RaceData(input: List<String>): List<RaceDataL> {
        val timeTokens = input[0].replace(" ", "").split(":").drop(1)
        val distanceTokens = input[1].replace(" ", "").split(":").drop(1).filter { it.isNotBlank() }
        val raceData = mutableListOf<RaceDataL>()
        IntRange(0, timeTokens.size - 1).forEach {
            raceData.add(RaceDataL(timeTokens[it].trim().toLong(), distanceTokens[it].trim().toLong()))
        }
        return raceData
    }
    
    fun part1(input: List<String>): Int {
        val raceData = getRaceData(input)
        val waysToWin = mutableListOf<Int>()
        
        raceData.forEach { rd ->
            var wins = 0
            IntRange(0, rd.time).forEach {
                if (it * (rd.time - it) > rd.recordDistance) wins++ 
            }
            waysToWin.add(wins)
        }
        
        return waysToWin.reduce { S, T ->
            S * T
        }
    }

    fun part2(input: List<String>): Long {
        val raceData = getPart2RaceData(input)
        val waysToWin = mutableListOf<Long>()

        raceData.forEach { rd ->
            var wins = 0L
            LongRange(0, rd.time).forEach {
                if (it * (rd.time - it) > rd.recordDistance) wins++ 
            }
            waysToWin.add(wins)
        }

        return waysToWin.reduce { S, T ->
            S * T
        }
    }

    val testInput = readInput("Day06_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
