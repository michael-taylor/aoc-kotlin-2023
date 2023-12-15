fun main() {
    fun hash(text: String): Int {
        var currentVal = 0
        text.forEach { c ->
            val asciiCode = c.code
            currentVal += asciiCode
            currentVal *= 17
            currentVal %= 256
        }
        return currentVal
    }

    fun part1(input: List<String>): Long {
        return input.first().split(",").map { hash(it) }.sum().toLong()
    }

    data class Lens(val label: String, var focalLength: Int)

    fun part2(input: List<String>): Long {
        val boxes = List<MutableList<Lens>>(size = 256, init = { mutableListOf<Lens>() })

        input.first().split(",").forEach {
            val label = it.split("=", "-").first()
            val boxIndex = hash(label)

            if (it.contains("=")) {
                val focalLength = it.split("=", "-").last().toInt()
                val prevLens = boxes[boxIndex].find { it.label == label }
                if (prevLens != null) prevLens.focalLength = focalLength
                else boxes[boxIndex].add(Lens(label, focalLength))
            } else {
                boxes[boxIndex].removeIf { it.label == label }
            }
        }

        return boxes.mapIndexed { boxIndex, box -> box.mapIndexed { lensIndex, lens -> (boxIndex + 1) * (lensIndex + 1) * lens.focalLength  }.sum() }.sum().toLong()
    }

    val testData = mutableListOf<String>(
        "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
    )

    val actualAnswer1 = part1(testData)
    val expectedAnswer1 = 1320L

    val testInput = readInput("Day15_test")

    println("Test 1: $actualAnswer1 [$expectedAnswer1]")
    println("Part 1: ${part1(testInput)}")

    val actualAnswer2 = part2(testData)
    val expectedAnswer2 = 145L

    println("Test 2: $actualAnswer2 [$expectedAnswer2]")
    println("Part 2: ${part2(testInput)}")
}
