
/******************************************************************************
 * Thrown when an assertion fails
 *
 * @param[errorMessage] Message to initialise any error messages with
 * @param[cause] Root cause of the failed assertion if appropriate
 *****************************************************************************/
class FailedVerification(
  errorMessage: String,
  cause: Throwable? = null
): Exception(errorMessage, cause)

/******************************************************************************
 * Asserts the value is not null throwing an exception if it is. Always returns
 * the non-nullable version of the type
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <T> T?.verifyNotNull(errorMessage: String): T = when {
  this == null -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the value is null throwing an exception if it isn't
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <T> T?.verifyNull(errorMessage: String) = when {
  this != null -> throw FailedVerification(errorMessage)
  else -> Unit
}

/******************************************************************************
 * Asserts the string is not empty throwing an exception if it is. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun String.verifyNotEmpty(errorMessage: String): String = when {
  isEmpty() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the string is not blank throwing an exception if it is. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun String.verifyNotBlank(errorMessage: String): String = when {
  isBlank() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the string is of a specified length throwing an exception if it is
 * not. Always returns the value unmodified
 *
 * @param[length] Length to assert
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun String.verifyLength(length: Int, errorMessage: String): String = when {
  this.length != length -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the string matches the regular expression throwing an exception if
 * not. Always returns the value unmodified
 *
 * @param[regex] Regex to match
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun String.verifyMatches(regex: Regex, errorMessage: String): String = when {
  matches(regex) -> this
  else -> throw FailedVerification(errorMessage)
}

/******************************************************************************
 * Asserts the collection is not empty throwing an exception if it is. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <C, V> C.verifyNotEmpty(errorMessage: String): C
  where C: Collection<V> = when {
  this.isEmpty() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the collection is empty throwing an exception if it is. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <C, V> C.verifyEmpty(errorMessage: String): C
  where C: Collection<V> = when {
  this.isNotEmpty() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the collection is of a specified size throwing an exception if it is
 * not. Always returns the value unmodified
 *
 * @param[length] Length to assert
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <C, V> C.verifyLength(length: Int, errorMessage: String): C
  where C: Collection<V> = when {
  this.size != length -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the collection does NOT contain any duplicate entries throwing an
 * exception if it is. Always returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <C, V> C.verifyNoDuplicates(errorMessage: String): C
  where C: Collection<V> {
  for ((outerIndex, outer) in this.withIndex()) {
    this.forEachIndexed { innerIndex, inner ->
      when {
        innerIndex == outerIndex -> return@forEachIndexed
        outer == null && inner == null -> throw FailedVerification(errorMessage)
        outer != null && outer == inner -> throw FailedVerification(errorMessage)
      }
    }
  }
  return this
}

/******************************************************************************
 * Asserts the map is not empty throwing an exception if it is. Always returns
 * the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <K,V> Map<K,V>.verifyNotEmpty(errorMessage: String): Map<K,V> = when {
  this.isEmpty() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the map is empty throwing an exception if it is. Always returns the
 * value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <K,V> Map<K,V>.verifyEmpty(errorMessage: String): Map<K,V> = when {
  this.isNotEmpty() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the map is of a specified size throwing an exception if it is not.
 * Always returns the value unmodified
 *
 * @param[length] Length to assert
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <K,V> Map<K,V>.verifyLength(
  length: Int,
  errorMessage: String
): Map<K,V> = when {
  this.size != length -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is not zero throwing an exception if not. Always returns
 * the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.verifyZero(errorMessage: String): Number = when {
  this.toDouble() != 0.0 -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is not negative throwing an exception if not. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.verifyNotNegative(errorMessage: String): Number = when {
  this.toDouble() < 0 -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is not positive throwing an exception if not. Always
 * returns the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.verifyNotPositive(errorMessage: String): Number = when {
  this.toDouble() > 0 -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is negative throwing an exception if not. Always returns
 * the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.verifyNegative(errorMessage: String): Number = when {
  this.toDouble() >= 0  -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is positive throwing an exception if not. Always returns
 * the value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.verifyPositive(errorMessage: String): Number = when {
  this.toDouble() <= 0 -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is less than the supplied throwing an exception if not.
 * Always returns the value unmodified
 *
 * @param[other] other number to compare to
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.verifyLessThan(other: Number, errorMessage: String): Number = when {
  this.toDouble() >= other.toDouble() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is less than or equal to the supplied throwing an
 * exception if not. Always returns the value unmodified
 *
 * @param[other] other number to compare to
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.verifyLessThanOrEqual(
  other: Number,
  errorMessage: String
): Number = when {
  this.toDouble() > other.toDouble() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is greater than the supplied throwing an exception if not.
 * Always returns the value unmodified
 *
 * @param[other] other number to compare to
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.verifyGreaterThan(
  other: Number,
  errorMessage: String
): Number = when {
  this.toDouble() <= other.toDouble() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the number is greater than or equal to the supplied throwing an
 * exception if not. Always returns the value unmodified
 *
 * @param[other] other number to compare to
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Number.verifyGreaterThanOrEqual(
  other: Number,
  errorMessage: String
): Number = when {
  this.toDouble() < other.toDouble() -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the integer is even throwing an exception if not. Always returns the
 * value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Int.verifyEven(errorMessage: String): Int = when {
  this % 2 == 0 -> this
  else -> throw FailedVerification(errorMessage)
}

/******************************************************************************
 * Asserts the integer is odd throwing an exception if not. Always returns the
 * value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Int.verifyOdd(errorMessage: String): Int = when {
  this % 2 == 0 -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the int is divisible by the value provided throwing an exception if
 * not. Always returns the value unmodified
 *
 * @param[divisor] Value to be divisible by
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Int.verifyDivisibleBy(divisor: Int, errorMessage: String): Int = when {
  this % divisor != 0 -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the long is even throwing an exception if not. Always returns the
 * value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Long.verifyEven(errorMessage: String): Long = when {
  this % 2 == 0L -> this
  else -> throw FailedVerification(errorMessage)
}

/******************************************************************************
 * Asserts the long is odd throwing an exception if not. Always returns the
 * value unmodified
 *
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Long.verifyOdd(errorMessage: String): Long = when {
  this % 2 == 0L -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts the long is divisible by the value provided throwing an exception if
 * not. Always returns the value unmodified
 *
 * @param[divisor] Value to be divisible by
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun Long.verifyDivisibleBy(divisor: Long, errorMessage: String): Long = when {
  this % divisor != 0L -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts this object is equal to the other throwing an exception if not.
 * Always returns this value
 *
 * @param[other] Other instance
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <T> T.verifyEquals(other: T, errorMessage: String): T = when {
  this != other -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts this object is not equal to the other throwing an exception if not.
 * Always returns this value
 *
 * @param[other] Other instance
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <T> T.verifyNotEquals(other: T, errorMessage: String): T = when {
  this == other -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts this object is the same as the other (reference check) throwing an
 * exception if not. Always returns this value
 *
 * @param[other] Other instance
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <T> T.verifySame(other: T, errorMessage: String): T = when {
  this !== other -> throw FailedVerification(errorMessage)
  else -> this
}

/******************************************************************************
 * Asserts this object is not the same as the other (reference check) throwing
 * an exception if not. Always returns this value
 *
 * @param[other] Other instance
 * @param[errorMessage] Message to initialise any error messages with
 *****************************************************************************/
fun <T> T.verifyNotSame(other: T, errorMessage: String): T = when {
  this === other -> throw FailedVerification(errorMessage)
  else -> this
}
