import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun <T : Comparable<T>> top(n: Int, collection: Iterable<T>): List<T> {
    return collection.fold(ArrayList()) { topList, candidate ->
        if (topList.size < n || candidate > topList.last()) {
            // ideally insert at the right place
            topList.add(candidate)
            topList.sortDescending()
            // trim to size
            if (topList.size > n)
                topList.removeAt(n)
        }
        topList
    }
}
