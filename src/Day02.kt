fun main() {
    // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    fun part1(input: List<String>): Int {
        fun getCount(value: String): Int {
            return value.replace(Regex("\\D"), "").toInt()
        }
        
        var gameNumTotal = 0
        input.forEach {
            val gameNum = it.split(" ")[1].replace(":", "").toInt()
            val games = it.split(":")[1].split(";")
            var green = 0
            var red = 0;
            var blue = 0
            games.forEach {
                val colors = it.split(",")
                colors.forEach {
                    if (it.endsWith("blue") && getCount(it) > blue) blue = getCount(it)
                    if (it.endsWith("green") && getCount(it) > green) green = getCount(it)
                    if (it.endsWith("red") && getCount(it) > red) red = getCount(it)
                }
            }
            
            if ((red <= 12) && (green <= 13) && (blue <= 14)) gameNumTotal += gameNum
        }
        
        return gameNumTotal
    }

    fun part2(input: List<String>): Int {
        fun getCount(value: String): Int {
            return value.replace(Regex("\\D"), "").toInt()
        }

        var gamePowerTotal = 0
        input.forEach {
            val gameNum = it.split(" ")[1].replace(":", "").toInt()
            val games = it.split(":")[1].split(";")
            var green = 0
            var red = 0;
            var blue = 0
            games.forEach {
                val colors = it.split(",")
                colors.forEach {
                    if (it.endsWith("blue") && getCount(it) > blue) blue = getCount(it)
                    if (it.endsWith("green") && getCount(it) > green) green = getCount(it)
                    if (it.endsWith("red") && getCount(it) > red) red = getCount(it)
                }
            }
            
            val gamePower = green * red * blue
            gamePowerTotal += gamePower
        }

        return gamePowerTotal
    }

    val testInput = readInput("Day02_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
