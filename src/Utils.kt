import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Return the value of a whole number given an index to a digit in that number.inside a string
 *
 * This function will return null if the index doesn't point to a digit
 */
fun getWholeNumberAtIndexOfString(text: String, index: Int): Int? {
    if (!text[index].isDigit()) return null

    var startIndex = index
    while ((startIndex >= 0) && (text[startIndex].isDigit())) {
        startIndex--
    }
    startIndex++ // Rewind one for that last decrement

    var stopIndex = index
    while ((stopIndex < text.length) && (text[stopIndex].isDigit())) {
        stopIndex++
    }
    // No need to rewind stopIndex because it is exclusive in the range

    return text.substring(startIndex, stopIndex).toInt()
}
