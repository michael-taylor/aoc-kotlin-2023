fun main() {
    data class History(val values: List<Long>, var nextValue: Long)
    
    fun predictNext(numbers: List<Long>): Long {
        val diffs = mutableListOf<Long>()
        
        IntRange(1, numbers.size - 1).forEach {
            diffs.add(numbers[it] - numbers[it-1])
        }
        
        if (diffs.all { it == 0L }) return 0L
        else {
            val nextDiff = predictNext(diffs)
            val nextVal = diffs.last() + nextDiff
            return nextVal
        }
    }
    
    fun predictPrev(numbers: List<Long>): Long {
        val diffs = mutableListOf<Long>()

        IntRange(1, numbers.size - 1).forEach {
            diffs.add(numbers[it] - numbers[it-1])
        }

        if (diffs.all { it == 0L }) return 0L
        else {
            val prevDiff = predictPrev(diffs)
            val prevVal = diffs.first() - prevDiff
            return prevVal
        }
    }
    
    fun part1(input: List<String>): Long {
        var histories = input.map {
            History(values = 
                it.split(" ").map {
                    it.trim().toLong()
                },
                nextValue = 0
            )
        }
        
        histories.forEach {
            it.nextValue = it.values.last() + predictNext(it.values)
        }
        
        return histories.sumOf { it.nextValue }
    }

    fun part2(input: List<String>): Long {
        var histories = input.map {
            History(values = 
                it.split(" ").map {
                    it.trim().toLong()
                                  },
                    nextValue = 0
            )
        }

        histories.forEach {
            it.nextValue = it.values.first() - predictPrev(it.values)
        }

        return histories.sumOf { it.nextValue }
    }

    val testInput = readInput("Day09_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
