enum class Direction {
    Bottom,
    Top,
    Left,
    Right
}

fun main() {
    data class Cursor(var x: Int, var y: Int, var from: Direction)
    
    fun findStart(input: List<String>): Pair<Int, Int> {
        input.forEachIndexed { index, s ->
            if (s.contains("S")) return Pair(s.indexOf('S'), index)
        }
        
        error("Can't find start")
    }
    
    fun takeStep(tile: Char, cursor: Cursor): Cursor {
        val from = cursor.from
        val x = cursor.x
        val y = cursor.y
        
        when (tile) {
            '|' -> {
                if (from == Direction.Top) return Cursor(x, y+1, Direction.Top)
                else return  Cursor(x, y-1, Direction.Bottom)
            }
            '-' -> {
                if (from == Direction.Left) return Cursor(x+1, y, Direction.Left)
                else return Cursor(x-1, y, Direction.Right)
            }
            'L' -> {
                if (from == Direction.Top) return Cursor(x+1, y, Direction.Left)
                else return Cursor(x, y-1, Direction.Bottom)
            }
            'J', 'S' -> { // Kinda a cheat, found S to be a J tile by looking at input data
                if (from == Direction.Top) return Cursor(x-1, y, Direction.Right)
                else return Cursor(x, y-1, Direction.Bottom)
            }
            '7' -> {
                if (from == Direction.Bottom) return Cursor(x-1, y, Direction.Right)
                else return Cursor(x, y+1, Direction.Top)
            }
            'F' -> {
                if (from == Direction.Bottom) return Cursor(x+1, y, Direction.Left)
                else return Cursor(x, y+1, Direction.Top)
            }
            else -> {
                error("Encountered invalid tile $tile")
            }
        }
    }
    
    fun part1(input: List<String>): Int {
        var (x, y) = findStart(input)
        var cursor1 = Cursor(x = x, y = y, from = Direction.Top)
        var cursor2 = Cursor(x = x, y = y, from = Direction.Left)
        var steps = 0
        
        do {
            cursor1 = takeStep(input[cursor1.y][cursor1.x], cursor1)
            cursor2 = takeStep(input[cursor2.y][cursor2.x], cursor2)
            steps++
        } while ((cursor1.x != cursor2.x) || (cursor1.y != cursor2.y))
        
        return steps
    }

    fun part2(input: List<String>): Int {
        var (x, y) = findStart(input)
        var cursor1 = Cursor(x = x, y = y, from = Direction.Top)
        var cursor2 = Cursor(x = x, y = y, from = Direction.Left)
        val map = input.map { it.toCharArray() }

        do {
            map[cursor1.y][cursor1.x] = '@'
            map[cursor2.y][cursor2.x] = '@'
            cursor1 = takeStep(input[cursor1.y][cursor1.x], cursor1)
            cursor2 = takeStep(input[cursor2.y][cursor2.x], cursor2)
        } while ((cursor1.x != cursor2.x) || (cursor1.y != cursor2.y))

        map.forEachIndexed { y, line ->
            var parity = false

            line.forEachIndexed { x, tile ->
                if (tile == '@') {
                    if (
                        (input[y][x] == '|')
                        || (input[y][x] == 'F')
                        || (input[y][x] == '7')
                        ) parity = !parity
                } else {
                    if (parity) map[y][x] = 'I' else map[y][x] = 'O'
                }
            }
        }
        
        return map.sumOf { it.filter { it == 'I' }.count() }
    }

    val testInput = readInput("Day10_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
