import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        var winningTotal = 0
        
        input.forEach { line ->
            val winningNumbers = line.split(':')[1].split('|')[0].split(' ').filter { it.isNotEmpty() }.map {
                it.trim().toInt()
            }
            val selectedNumbers = line.split(':')[1].split('|')[1].split(' ').filter { it.isNotEmpty() }.map {
                it.trim().toInt()
            }
            val cardTotal = winningNumbers.intersect(selectedNumbers).size
            if (cardTotal > 0) winningTotal += 2.toDouble().pow(cardTotal.toDouble() - 1.0).toInt()
        }
        
        return winningTotal
    }

    fun part2(input: List<String>): Int {
        data class CardWinData(var points: Int, var numTimesWon: Int)
        val lineWins = mutableListOf<CardWinData>()

        input.forEach { line ->
            val winningNumbers = line.split(':')[1].split('|')[0].split(' ').filter { it.isNotEmpty() }.map {
                it.trim().toInt()
            }
            val selectedNumbers = line.split(':')[1].split('|')[1].split(' ').filter { it.isNotEmpty() }.map {
                it.trim().toInt()
            }
            val cardTotal = winningNumbers.intersect(selectedNumbers).size
            lineWins.add(CardWinData(cardTotal, 1))
        }
        
        fun winMoreCards(cards: List<CardWinData>, index: Int) {
            if (cards[index].points > 0) {
                IntRange(1, cards[index].points).forEach {
                    cards[index+it].numTimesWon++
                    winMoreCards(cards, index + it)
                }
            }
        }

        lineWins.forEachIndexed { index, winData ->
            winMoreCards(lineWins, index)
        }

        return lineWins.sumOf { it.numTimesWon }
    }

    val testInput = readInput("Day04_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
