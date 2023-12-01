fun main() {
    fun part1(input: List<String>): Int {
        var total = 0;

        input.forEach {
            val digits = it.replace(Regex("\\D"), "")
            total += (digits.first().digitToInt() * 10) + digits.last().digitToInt()
        }
        return total
    }

    fun part2(input: List<String>): Int {
        var total = 0;

        input.forEach {
            val digits = it
                // We can't simply replace string with int because that may break other number strings
                .replace("one", "one1one")
                .replace("two", "two2two")
                .replace("three", "three3three")
                .replace("four", "four4four")
                .replace("five", "five5five")
                .replace("six", "six6six")
                .replace("seven", "seven7seven")
                .replace("eight", "eight8eight")
                .replace("nine", "nine9nine")
                .replace(Regex("\\D"), "")
            val tens = digits.first().digitToInt()
            val ones = digits.last().digitToInt()
            total += ((tens * 10) + ones)
        }
        return total
    }

    val testInput = readInput("Day01_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
