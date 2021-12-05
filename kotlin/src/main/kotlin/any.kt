import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*


// Prints the supplied string indenting every line except the first. Leading
// and trailing whitespace is removed from each line and an extra empty line
// is printed at the end.
fun String.describe() {
  lineUp("\n")
    .split("\n")
    .onFirst { println(it) }
    .onEachExcept(true) { println("\t$it") }
  println()
}

// Returns the negated result of the function f.
inline fun not(f: () -> Boolean): Boolean = f().not()

// Returns true if the collection contains a single item.
fun <T> Collection<T>.isSingleton(): Boolean = size == 1

// Returns the value wrapped in an Optional.
fun <T> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

// Returns true if the trimmed string matches 'Y' or 'YES' (case insensitive).
fun String.toBool(): Boolean = this.trim().matches(
  "([Yy])|([Yy][Ee][Ss])".toRegex()
)

// Returns the string representation of the boolean in the form 'Y' or 'N'.
fun Boolean.toYorN(lowercase: Boolean = false): String = when {
  lowercase && this -> "y"
  lowercase && !this -> "n"
  this -> "Y"
  else -> "N"
}

// Returns the string representation of the boolean in the form 'YES' or 'NO'.
fun Boolean.toYesOrNo(lowercase: Boolean = false): String = when {
  lowercase && this -> "yes"
  lowercase && !this -> "no"
  this -> "YES"
  else -> "NO"
}

// Splits a string into two on the first occurrence of the specified delimiter.
// If no match is found the pairs first will contain the whole string and the
// second will contain null.
fun String.bifurcate(delim: String): Pair<String, String?>
  = bifurcate(Regex.fromLiteral(delim))

// Splits a string into two on the first occurrence of the specified regex. If
// no match is found the pairs first will contain the whole string and the
// second will contain null.
fun String.bifurcate(regex: Regex): Pair<String, String?> {
  val p = split(regex, 2)
  return Pair(p[0], p.getOrNull(1))
}

// Returns the substring that matches the regular expression.
fun String.substring(regex: Regex): String? = substring(regex, 0)

// Returns the first substring that matches the group found by the regular
// expression or null if no match was found.
fun String.substring(regex: Regex, group: Int): String? {
  val m = regex.find(this)
  return m?.groups?.get(group)?.value
}

// Prepends a prefix string to this string but only if the prefix is not
// already present.
fun String.prefix(prefix: String): String = when {
  this.startsWith(prefix) -> this
  else -> prefix.plus(this)
}

// Appends a suffix to the string only if it does not already have it
fun String.suffix(suffix: String): String = when {
  this.endsWith(suffix) -> this
  else -> this.plus(suffix)
}

// First removes leading and trailing whitespace then replaces line breaks
// (including their leading and trailing whitespace) with the supplied
// delimiter. By default a space is used as the new separator.
fun String.lineUp(delim: String = " "): String = when {
  isBlank() -> ""
  else -> split("\n")
    .filter { it.isNotBlank() }
    .map { it.trim() }
    .joinToString(delim)
}

// Removes empty lines from a list of strings.
fun List<String>.strip(): List<String> = filterNot { it.isBlank() }

// Removes empty lines from the start and end of a list of strings.
fun List<String>.trim(): List<String>
  = dropWhile { it.isBlank() }
  .dropLastWhile { it.isBlank() }

// Returns the index of the first string to match the specified regular
// expression.
fun List<String>.indexOf(regex: Regex): Int {
  for ((i, line) in withIndex()) {
    regex.find(line) ?: continue
    return i
  }
  return -1
}

// Parses a hex string into a byte array
fun String.hexToByteArray(): ByteArray {

  // TODO: Could be done with better performance?

  length.ifOdd {
    throw Exception("Odd length string cannot be a hex string")
  }

  return ByteArray(length / 2) { i ->
    val j = i * 2

    val first = Character.digit(this[j], 16)
    if (first == -1) {
      throw Exception("Not a valid hex character '${this[j]}'")
    }

    val second = Character.digit(this[j + 1], 16)
    if (second == -1) {
      throw Exception("Not a valid hex character '${this[j + 1]}'")
    }

    ((first shl 4) + second).toByte()
  }
}

// Generates a SHA-256 hash of the string.
// TODO: What about Charset? UTF-8 is assumed at the moment.
fun String.sha256(): ByteArray = MessageDigest
  .getInstance("SHA-256")
  .digest(this.toByteArray())

// Generates a SHA-512 hash of the string.
// TODO: What about Charset? UTF-8 is assumed at the moment
fun String.sha512(): ByteArray = MessageDigest
  .getInstance("SHA-512")
  .digest(this.toByteArray())

// Generates a SHA-256 hash of a file.
// TODO: What about Charset? UTF-8 is assumed at the moment
fun Path.fileToSha256(
  buffer: ByteArray = ByteArray(1024 * 16)
): ByteArray {

  Files.newInputStream(this).use { stream ->
    val hasher = MessageDigest.getInstance("SHA-256")

    var read = stream.read(buffer)
    while (read != -1) {
      hasher.update(buffer, 0, read)
      read = stream.read(buffer)
    }

    return hasher.digest()
  }
}

// Generates a SHA-512 hash of a file.
// TODO: What about Charset? UTF-8 is assumed at the moment.
fun Path.fileToSha512(
  buffer: ByteArray = ByteArray(1024 * 16)
): ByteArray {

  Files.newInputStream(this).use { stream ->
    val hasher = MessageDigest.getInstance("SHA-512")

    var read = stream.read(buffer)
    while (read != -1) {
      hasher.update(buffer, 0, read)
      read = stream.read(buffer)
    }

    return hasher.digest()
  }
}

// Invokes the supplied function on the first item if it has one and returns
// the list unchanged.
inline fun <T> List<T>.onFirst(f: (T) -> Unit): List<T> {
  if (size > 0) f(first())
  return this
}

// Invokes the supplied function on the last item if it has one and returns
// the list unchanged.
inline fun <T> List<T>.onLast(f: (T) -> Unit): List<T> {
  if (size > 0) f(last())
  return this
}

// Invokes the supplied function on the each item except maybe the first or
// last, dependent on parameters, then returns the list unchanged.
inline fun <T> List<T>.onEachExcept(
  first: Boolean = false,
  last: Boolean = false,
  f: (T) -> Unit
): List<T> {
  val start = if (first) 1 else 0
  val end = if (last) size-1 else size

  if(start >= end) return this

  for(i in start until end) {
    f(this[i])
  }

  return this
}

// Invokes the function if this object is null.
inline fun <T> T?.ifNull(f: () -> T?): T? = this ?: f()

// Invokes the function if this object is NOT null.
inline fun <T> T?.ifNotNull(f: () -> T?): T? = when {
  this != null -> f()
  else -> null
}

// Invokes the function if this boolean is true.
inline fun Boolean.ifTrue(f: () -> Boolean): Boolean = when {
  this -> f()
  else -> this
}

// Invokes the function if this boolean is false.
inline fun Boolean.ifFalse(f: () -> Boolean): Boolean = when {
  this -> this
  else -> f()
}

// Invokes the first function if this boolean is true else invokes the second.
inline fun <T> Boolean.ifTrueElse(
  whenTrue: () -> T,
  whenFalse: () -> T
): T = when {
  this -> whenTrue()
  else -> whenFalse()
}

// Invokes the first function if this boolean is false else invokes the second.
inline fun <T> Boolean.ifFalseElse(
  whenFalse: () -> T,
  whenTrue: () -> T
): T = when {
  this -> whenTrue()
  else -> whenFalse()
}

// Invokes the function if this [Number] is negative.
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifNegative(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) < 0) f(this) else this

// Invokes the function if this [Number] is not negative.
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifNonNegative(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) >= 0) f(this) else this

// Invokes the function if this [Number] is positive and not zero.
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifPositive(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) > 0) f(this) else this

// Invokes the function if this [Number] is not positive.
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifNonPositive(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) <= 0) f(this) else this

// Invokes the function if this [Number] is exactly zero.
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifZero(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) == 0) f(this) else this

// Invokes the function if this [Number] is not zero.
@Suppress("UNCHECKED_CAST")
inline fun <N> N.ifNotZero(f: N.() -> N): N
  where N: Number,
        N: Comparable<N>
  = if(compareTo(0 as N) != 0) f(this) else this

// Invokes the function if this [Int] is odd.
@Suppress("UNCHECKED_CAST")
inline fun Int.ifOdd(f: Int.() -> Int): Int
  = if(this % 2 != 0) f(this) else this

// Invokes the function if this [Long] is odd.
@Suppress("UNCHECKED_CAST")
inline fun Long.ifOdd(f: Long.() -> Long): Long
  = if(this % 2 != 0L) f(this) else this


// Invokes the function if this [Int] is even.
@Suppress("UNCHECKED_CAST")
inline fun Int.ifEven(f: Int.() -> Int): Int
  = if(this % 2 == 0) f(this) else this

// Invokes the function if this [Long] is even.
@Suppress("UNCHECKED_CAST")
inline fun Long.ifEven(f: Long.() -> Long): Long
  = if(this % 2 == 0L) f(this) else this

//Creates a time sensitive greeting, uses UTC clock by default.
fun createGreeting(
  localTime: LocalTime = OffsetDateTime.now(ZoneOffset.UTC).toLocalTime()
): String = when {
  localTime.isAfter(LocalTime.MIDNIGHT) && localTime.isBefore(LocalTime.NOON) -> "Good morning"
  localTime.isBefore(LocalTime.of(18, 0)) -> "Good afternoon"
  else -> "Good evening"
}
