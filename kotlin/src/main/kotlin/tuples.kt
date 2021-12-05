
import kotlin.random.Random

// Swaps the first and second values around.
fun <A, B> Pair<A, B>.swap(): Pair<B, A> = second to first

// Randomly shuffles the members.
fun <T> Pair<T, T>.shuffle(): Pair<T, T>
  = if(Random.nextBoolean()) this else swap()

// Returns true if the supplied value is contained within the pair.
operator fun <T> Pair<T, T>.contains(value: T): Boolean
  = (value == first) or (value == second)

// Maps a pair into another pair.
inline fun <A, B, A2, B2> Pair<A, B>.map(
  f: (Pair<A, B>) -> Pair<A2, B2>
): Pair<A2, B2> = f(this)

// Appends another value to the pair to form a [Triple].
inline infix operator fun <A, B, C> Pair<A, B>.plus(
  f: Pair<A, B>.() -> C
): Triple<A, B, C> = Triple(first, second, f(this))

// Appends another value to the pair to form a [Triple].
infix operator fun <A, B, C> Pair<A, B>.plus(
  item: C
): Triple<A, B, C> = Triple(first, second, item)

// Transforms a pair into a [Triple] by inserting a value into the center.
inline fun <A, B, C> Pair<A, C>.midsert(
  f: Pair<A, C>.() -> B
): Triple<A, B, C> = Triple(first, f(this), second)

// Transforms a pair into a [Triple] by inserting a value into the center.
fun <A, B, C> Pair<A, C>.midsert(
  item: B
): Triple<A, B, C> = Triple(first, item, second)

// Transforms a pair into a [Triple] by prepending a value.
fun <A, B, C> Pair<B, C>.insert(
  item: A
): Triple<A, B, C> = Triple(item, first, second)

// Transforms a pair into a [Triple] by prepending a value.
inline fun <A, B, C> Pair<B, C>.insert(
  f: Pair<B, C>.() -> A
): Triple<A, B, C> = Triple(f(), first, second)

// Returns a value from the pair by index.
infix operator fun <T> Pair<T, T>.get(
  index: Int
): T = when (index) {
  0 -> first
  1 -> second
  else -> throw IndexOutOfBoundsException(
    "Pairs have a size of two, only 0 or 1 may be used as indexes"
  )
}

// Joins the pair into a string with a user supplied delimiter, an empty string
// is used by default.
fun <A, B> Pair<A, B>.join(delimiter: String = ""): String
  = "$first$delimiter$second"

// Reverse the order of the members.
fun <A, B, C> Triple<A, B, C>.reverse(): Triple<C, B, A>
  = Triple(third, second, first)

// Shifts all values to the left with the left most item becoming the last item.
fun <A, B, C> Triple<A, B, C>.shiftLeft(): Triple<B, C, A>
  = Triple(second, third, first)

// Shifts all values to the right with the right most item becoming the first
// item.
fun <A, B, C> Triple<A, B, C>.shiftRight(): Triple<C, A, B>
  = Triple(third, first, second)

// Randomly shuffles the members.
fun <T> Triple<T, T, T>.shuffle(): Triple<T, T, T>
  = toList().shuffled().let {
  Triple(it[0], it[1], it[2])
}

// Returns true if `value in triple`.
operator fun <T> Triple<T, T, T>.contains(value: T): Boolean
  = (value == first) or (value == second) or (value == third)

// Maps the triple into another triple.
inline fun <A, B, C, A2, B2, C2> Triple<A, B, C>.map(
  f: Triple<A, B, C>.() -> Triple<A2, B2, C2>
): Triple<A2, B2, C2> = f(this)

// Shrinks the triple to a [Pair] by dropping the first member.
fun <A, B, C> Triple<A, B, C>.dropFirst(): Pair<B, C> = second to third

// Shrinks the triple to a [Pair] by dropping the middle member.
fun <A, B, C> Triple<A, B, C>.dropSecond(): Pair<A, C> = first to third

// Shrinks the triple to a [Pair] by dropping the last member.
fun <A, B, C> Triple<A, B, C>.dropThird(): Pair<A, B> = first to second

// Shrinks the triple into a [Pair].
inline fun <A, B, C, X, Y> Triple<A, B, C>.shrink(
  f: Triple<A, B, C>.() -> Pair<X, Y>
): Pair<X, Y> = f(this)

// Returns a value from the triple by index.
infix operator fun <T> Triple<T, T, T>.get(
  index: Int
): T = when (index) {
  0 -> first
  1 -> second
  2 -> third
  else -> throw IndexOutOfBoundsException(
    "Triples have a size of two, only 0, 1 or 2 may be used as indexes"
  )
}

// Converts this triple into a set.
fun <T> Triple<T, T, T>.toSet(): Set<T>
  = setOf(first, second, third)

// Joins the triple into a string with a user supplied delimiter between them.
fun <A, B, C> Triple<A, B, C>.join(delimiter: String = ""): String
  = "$first$delimiter$second$delimiter$third"
