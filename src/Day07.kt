enum class HandType {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAkind,
}

val cardRanks = mapOf(
    '2' to 2,
    '3' to 3,
    '4' to 4,
    '5' to 5,
    '6' to 6,
    '7' to 7,
    '8' to 8,
    '9' to 9,
    'T' to 10,
    'J' to 11,
    'Q' to 12,
    'K' to 13,
    'A' to 14,
    )

fun getType(hand: Hand): HandType {
    hand.cards.forEach { card ->
        if (hand.cards.count { it == card } == 5) return HandType.FiveOfAkind
        if (hand.cards.count { it == card } == 4) return HandType.FourOfAKind
        if ((hand.cards.count { it == card } == 3) && (hand.cards.toSet().size != 2)) return HandType.ThreeOfAKind
    }

    if (hand.cards.toSet().size == 2) return HandType.FullHouse
    else if (hand.cards.toSet().size == 3) return HandType.TwoPair
    else if (hand.cards.toSet().size == 4) return HandType.OnePair
    else return HandType.HighCard
}

fun getTypeWithWild(hand: Hand): HandType {
    if (hand.cards.filter { it != 'J' }.size == 0) return HandType.FiveOfAkind
    hand.cards.forEach { card ->
        if (card != 'J') {
            if (hand.cards.count { (it == card) || (it == 'J') } == 5) return HandType.FiveOfAkind
            if (hand.cards.count { (it == card) || (it == 'J') } == 4) return HandType.FourOfAKind
            if ((hand.cards.count { (it == card) || (it == 'J') } == 3) &&
                ((hand.cards.contains('J') && hand.cards.toSet().size != 3)
                 || (!hand.cards.contains('J') && hand.cards.toSet().size !=2))) return HandType.ThreeOfAKind
        //if ((hand.cards.count { (it == card) || (it == 'J') } == 3) && ((hand.cards + 'J').toSet().size != 3)) return HandType.ThreeOfAKind
        }
    }
    
    val wildHand = hand.cards + 'J'

    if (wildHand.toSet().size == 3) return HandType.FullHouse
    else if (wildHand.toSet().size == 4) return HandType.TwoPair
    else if (wildHand.toSet().size == 5) return HandType.OnePair
    else return HandType.HighCard
}

fun handCompare(a: Hand, b: Hand): Int {
    val aType = getType(a)
    val bType = getType(b)

    if (aType > bType) return 1
    else if (bType > aType) return -1
    else if (bType == aType) {
        IntRange(0, 4).forEach {
            if (cardRanks[a.cards[it]]!! > cardRanks[b.cards[it]]!!) return 1
            else if (cardRanks[b.cards[it]]!! > cardRanks[a.cards[it]]!!) return -1
        }
    }

    return 0
}

fun handCompareWithWild(a: Hand, b: Hand): Int {
    val aType = getTypeWithWild(a)
    val bType = getTypeWithWild(b)

    if (aType > bType) return 1
    else if (bType > aType) return -1
    else if (bType == aType) {
        IntRange(0, 4).forEach {
            val aRank = if (a.cards[it] == 'J') 1 else cardRanks[a.cards[it]]!!
            var bRank = if (b.cards[it] == 'J') 1 else cardRanks[b.cards[it]]!!
            if (aRank > bRank) return 1
            else if (bRank > aRank) return -1
        }
    }

    return 0
}

data class Hand(val cards: List<Char>, val bid: Int): Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        return handCompare(this, other)
    }
}

fun main() {
    fun parseHand(line: String): Hand {
        val bid = line.split(" ")[1].toInt()
        val cards = line.split(" ")[0].toCharArray().toList()
        return Hand(cards, bid)
    }
    
    fun part1(input: List<String>): Int {
        val hands = input.map {
            parseHand(it)
        }.sorted()
        
        var total = 0
        hands.forEachIndexed { index, hand ->
            total += hand.bid * (index + 1)
        }
        
        return total
    }

    fun part2(input: List<String>): Int {
        val hands = input.map {
            parseHand(it)
        }.sortedWith(::handCompareWithWild)
        
        hands.filter { it.cards.count { it == 'J' } > 1 }.forEach{ println("${it.cards}: ${getTypeWithWild(it)}") }

        var total = 0
        hands.forEachIndexed { index, hand ->
            total += hand.bid * (index + 1)
        }

        return total
    }
    
    //println(getType(Hand(listOf('2', 'T', 'T', 'T', 'T'), 1)))
    //println(getTypeWithWild(Hand(listOf('2', '2', '4', '4', '2'), 1)))
    println(getTypeWithWild(Hand(listOf('J', 'A', 'T', 'T', 'A'), 1)))
    println(getTypeWithWild(Hand(listOf('4', 'J', 'K', 'K', 'J'), 1)))
    println(getTypeWithWild(Hand(listOf('J', 'J', '6', 'J', '6'), 1)))
    println(getTypeWithWild(Hand(listOf('J', '6', '6', 'J', '6'), 1)))

    val testInput = readInput("Day07_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
