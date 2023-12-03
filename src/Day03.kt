import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        var total = 0
        val lineLength = input.first().length
        val numberRegex = Regex("[0-9]+")
        
        input.forEachIndexed { index, line ->
            numberRegex.findAll(line).forEach { match ->
                val safeStart = max(0, match.range.first - 1)
                val safeStop = min(lineLength, match.range.last + 2)
                if ((match.range.first - 1) > 0 && line[match.range.first-1] != '.') total += match.value.toInt()
                else if ((match.range.last + 1) < lineLength && line[match.range.last+1] != '.') total += match.value.toInt()
                else if ((index > 0) && (input[index-1].substring(safeStart, safeStop).replace(".", "").isNotEmpty())) total += match.value.toInt()
                else if ((index < input.size - 1) && (input[index+1].substring(safeStart, safeStop).replace(".", "").isNotEmpty())) total += match.value.toInt()
            }
        }
        
        return total
    }

    fun part2(input: List<String>): Int {
        fun getWholeNumber(text: String, index: Int): Int {
            if (!text[index].isDigit()) return 0
            var numStr = text[index].toString()
            var offset = 1
            while ((index - offset >= 0) && text[index - offset].isDigit()) {
                numStr = text[index - offset].toString() + numStr
                offset++
            }
            offset = 1
            while ((index + offset < text.length) && text[index + offset].isDigit()) {
                numStr = numStr + text[index + offset].toString()
                offset++
            }
            
            return numStr.toInt()
        }

        val lineLength = input.first().length
        val gearRegex = Regex("\\*")
        val numberRegex = Regex("[0-9]+")
        var total = 0

        input.forEachIndexed { index, line ->
            val gearRegex = gearRegex
                .findAll(line).forEach { match ->
                    val numList = mutableListOf<Int>()
                    val safeStart = max(0, match.range.first - 1)
                    val safeStop = min(lineLength, match.range.last + 2)
                    if ((match.range.first - 1) > 0 && line[match.range.first-1].isDigit()) numList.add(getWholeNumber(line, match.range.first-1))
                    if ((match.range.last + 1) < lineLength && line[match.range.last+1].isDigit()) numList.add(getWholeNumber(line, match.range.last+1))
                    if ((index > 0) && (input[index-1].substring(safeStart, safeStop).contains(numberRegex))) {
                        var i = 0
                        val subNumList = mutableListOf<Int>()
                        while (safeStart + i < safeStop) {
                            subNumList.add(getWholeNumber(input[index-1], safeStart + i))
                            i++
                        }
                        
                        numList.addAll(subNumList.filterNot { it == 0 }.toSet())
                    }
                    if ((index < input.size - 1) && (input[index+1].substring(safeStart, safeStop).contains(numberRegex))) {
                        var i = 0
                        val subNumList = mutableListOf<Int>()
                        while (safeStart + i < safeStop) {
                            subNumList.add(getWholeNumber(input[index+1], safeStart + i))
                            i++
                        }

//                        println("")
                        numList.addAll(subNumList.filterNot { it == 0 }.toSet())
                    }
                    
                    if (numList.size == 2) {
//                        println("${numList[0]} x ${numList[1]}")
                        total += (numList[0] * numList[1])
                    }
                }
            }
        
        return total
    }

    val testInput = readInput("Day03_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
