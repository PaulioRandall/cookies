
import java.util.*

/******************************************************************************
 * Represents a result that has no value, either a pass or fail
 *****************************************************************************/
typealias BinaryResult = AnyResult<Boolean>

/******************************************************************************
 * Represents a result from a function. The `Any` prefix is
 * used to avoid conflict with Kotlin's builtin `Result` type
 * which is not allowed to be used as a function return type.
 * [Stackoverflow](https://stackoverflow.com/questions/52631827/why-cant-kotlin-result-be-used-as-a-return-type)
 *
 * @property[good] Good result, mutually exclusive with the `bad` parameter
 * @property[good] Bad result, mutually exclusive with the `good` parameter
 * @param[R] Type of the result
 *****************************************************************************/
class AnyResult<R> internal constructor(
  val good: GoodResult<R>? = null,
  val bad: BadResult<R>? = null
) {

  init {
    when {
      good == null && bad == null -> throw Exception(
        "(BUG) Only one parameter must be null, either Good or Bad"
      )
      good != null && bad != null -> throw Exception(
        "(BUG) Only one parameter must be non-null, either Good or Bad"
      )
    }
  }

  /****************************************************************************
   * Returns true if it is a good result
   ***************************************************************************/
  val isGood: Boolean = good != null

  /****************************************************************************
   * Returns true if it is a bad result
   ***************************************************************************/
  val isBad: Boolean = bad != null

  /****************************************************************************
   * Returns true if the result is bad or null
   ***************************************************************************/
  val isNull: Boolean = good?.result == null

  /****************************************************************************
   * Returns the result cast to a good result while throwing an exception if it
   * is a bad result
   ***************************************************************************/
  fun toGood(): GoodResult<R>
    = good ?: throw Exception("Can't return a bad result as if it were a good one")

  /****************************************************************************
   * Returns the result cast to a bad result while throwing an exception if it
   * is a good result
   ***************************************************************************/
  fun toBad(): BadResult<R>
    = bad ?: throw Exception("Can't return a good result as if it were a bad one")

  /****************************************************************************
   * Invokes the result function if its a good result
   *
   * @param[f] Function to invoke
   ***************************************************************************/
  fun ifGood(f: GoodResult<R>.() -> Unit): AnyResult<R> = when {
    isGood -> apply { f(good!!) }
    else -> this
  }

  /****************************************************************************
   * Invokes the result function if its a bad result
   *
   * @param[f] Function to invoke
   ***************************************************************************/
  fun ifBad(f: BadResult<R>.() -> Unit): AnyResult<R> = when {
    isBad -> apply { f(bad!!) }
    else -> this
  }

  /****************************************************************************
   * Returns the input result if its a good result else returns the result of
   * the function
   *
   * @param[ifGood] Value to return
   * @param[f] Function to invoke
   ***************************************************************************/
  inline fun <R2> whenGood(ifGood: R2, f: () -> R2): R2
    = good?.let { ifGood } ?: f()

  /****************************************************************************
   * Returns the input result if its a bad result else returns the result of
   * the function
   *
   * @param[ifBad] Value to return
   * @param[f] Function to invoke
   ***************************************************************************/
  inline fun <R2> whenBad(ifBad: R2, f: () -> R2): R2
    = bad?.let { ifBad } ?: f()

  /****************************************************************************
   * Invokes the function and returns the result if its a good result else
   * returns a new bad result
   *
   * @param[f] Function to invoke
   ***************************************************************************/
  inline fun <R2> map(f: (R) -> R2): AnyResult<R2> = when {
    isGood -> good(f(good!!.result))
    else -> bad(bad!!.message, bad.exception)
  }

  /****************************************************************************
   * Invokes the function and returns the result if its a good result else
   * returns a new bad result
   *
   * @param[errorMessage] New error message to initialise the exception with
   * @param[f] Function to invoke
   ***************************************************************************/
  inline fun <R2> map(errorMessage: String, f: (R) -> R2): AnyResult<R2> = when {
    isGood -> good(f(good!!.result))
    else -> bad(errorMessage, bad!!.exception)
  }

  /****************************************************************************
   * Invokes the function and returns the result if its a bad result else
   * return this result
   *
   * @param[f] Function to invoke
   ***************************************************************************/
  inline fun mapIfBad(f: BadResult<R>.() -> AnyResult<R>): AnyResult<R> = when {
    isBad -> f(bad!!)
    else -> this
  }

  /****************************************************************************
   * Throws the exception if the result is bad creating an exception if one is
   * not present
   ***************************************************************************/
  fun throwIfBad(): AnyResult<R> = ifBad {
    exception ?: throw Exception(message)
    throw exception
  }

  /****************************************************************************
   * Throws the exception if the result is bad creating an exception if one is
   * not present
   *
   * @param[f] Function used to supply the exception
   ***************************************************************************/
  fun throwIfBad(f: BadResult<R>.() -> Exception): AnyResult<R> = ifBad {
    throw f()
  }

  /****************************************************************************
   * Returns the result if its a good result else invokes the function
   *
   * @param[f] Function to invoke
   ***************************************************************************/
  inline fun or(f: BadResult<R>.() -> R): R = when {
    isGood -> good!!.result
    else -> f(bad!!)
  }

  /****************************************************************************
   * Unwraps the value throwing an exception if the result is bad
   ***************************************************************************/
  fun unwrap(): R = throwIfBad().let { toGood().result }

  companion object {

    /**************************************************************************
     * Creates a new good result without a value
     *************************************************************************/
    fun good(): AnyResult<Boolean>
      = AnyResult(good = GoodResult(true))

    /**************************************************************************
     * Creates a new good result
     *
     * @param[result] Result value
     * @param[R] Type of the result
     *************************************************************************/
    fun <R> good(result: R): AnyResult<R>
      = AnyResult(good = GoodResult(result))

    /**************************************************************************
     * Creates a new bad result
     *
     * @param[message] Error message
     * @param[R] Type of the result
     *************************************************************************/
    fun <R> bad(message: String): AnyResult<R>
      = AnyResult(bad = BadResult(message))

    /**************************************************************************
     * Creates a new bad result
     *
     * @param[message] Error message
     * @param[exception] Exception thrown by the operation
     * @param[R] Type of the result
     *************************************************************************/
    fun <R> bad(message: String, exception: Exception?): AnyResult<R>
      = AnyResult(bad = BadResult(message, exception))

    /**************************************************************************
     * Returns the result of a function. If the function throws an exception it
     * is caught and wrapped in a [BadResult]
     *
     * @param[errorMessage] Error message to initialise bad results with
     * @param[f] Function to invoke
     *************************************************************************/
    fun <R> of(errorMessage: String? = null, f: () -> R): AnyResult<R> = try {
      AnyResult.good(f())
    } catch (ex: Exception) {
      when {
        errorMessage != null -> AnyResult.bad(errorMessage, ex)
        ex.message != null -> AnyResult.bad(ex.message!!, ex)
        else -> AnyResult.bad("Exception while attempting to compute result", ex)
      }
    }
  }
}

/******************************************************************************
 * Represents a good [AnyResult]
 *
 * @property[result] Result value
 * @param[R] Type of the result
 *****************************************************************************/
class GoodResult<R> (
  val result: R
) {

  /****************************************************************************
   * Converts the good result to an Optional
   ***************************************************************************/
  fun toOptional(): Optional<R> = Optional.ofNullable(result)

  /****************************************************************************
   * Invokes the function only if it's a good result and the value is present
   *
   * @param[f] Function to invoke
   ***************************************************************************/
  inline fun ifReal(f: (R) -> Unit): GoodResult<R> = when {
    result == null -> this
    else -> apply { f(result!!) }
  }

  /****************************************************************************
   * Invokes the function only if it's a good result and the value is empty
   *
   * @param[f] Function to invoke
   ***************************************************************************/
  inline fun ifNull(f: () -> Unit): GoodResult<R> = when {
    result != null -> this
    else -> apply { f() }
  }

  /****************************************************************************
   * Invokes the function and returns the result if its a good result but empty
   * else returns this result
   *
   * @param[f] Function to invoke
   ***************************************************************************/
  inline fun mapIfNull(f: () -> GoodResult<R>): GoodResult<R> = when {
    result != null -> this
    else -> f()
  }
}

/******************************************************************************
 * Represents a bad [AnyResult]
 *
 * @property[message] Error message if an error
 * @property[exception] Exception if an error
 * @param[R] Type of the result
 *****************************************************************************/
class BadResult<R>(
  val message: String,
  val exception: Exception? = null
) {

  /****************************************************************************
   * Returns a new bad result with a new type and error message
   *
   * @param[newMessage] New message to replace the current message
   ***************************************************************************/
  fun escalate(newMessage: String): AnyResult<R>
    = AnyResult(bad = BadResult(newMessage, exception))

  /****************************************************************************
   * Maps the bad result to a new type
   *
   * @param[R2] Type of the result
   ***************************************************************************/
  fun <R2> mapEscalate(): AnyResult<R2>
    = AnyResult(bad = BadResult(message, exception))

  /****************************************************************************
   * Maps the bad result to a new type and error message
   *
   * @param[newMessage] New message to replace the current message
   * @param[R2] Type of the result
   ***************************************************************************/
  fun <R2> mapEscalate(newMessage: String): AnyResult<R2>
    = AnyResult(bad = BadResult(newMessage, exception))

  /****************************************************************************
   * Throws the bad result as an exception creating one if it's null
   ***************************************************************************/
  fun throwEx() {
    exception ?: throw Exception(message)
    throw exception
  }

  /****************************************************************************
   * Throws the bad result as an exception but always wraps the exception first
   * so the message is always included
   ***************************************************************************/
  fun throwWrappedEx(): Unit = throw Exception(message, exception)

  /****************************************************************************
   * Throws the bad result with a custom exception
   *
   * @param[f] Function to generate the exception to throw
   ***************************************************************************/
  fun throwEx(f: BadResult<R>.() -> Exception): Unit = throw f()
}

/******************************************************************************
 * Wraps the value in an [GoodResult]
 *****************************************************************************/
fun <T> T.realResult(): AnyResult<T> = AnyResult.good(this)

/******************************************************************************
 * Wraps the value in an [GoodResult] if the result is not null else a
 * [BadResult]
 *
 * @param[errorMessage] Error message to initialise a bad result with
 *****************************************************************************/
fun <T> T?.optResult(errorMessage: String): AnyResult<T> = when {
  this != null -> AnyResult.good(this)
  else -> AnyResult.bad(errorMessage)
}

/******************************************************************************
 * Invokes the function only if it's a good result and the value is present
 *
 * @param[f] Function to invoke
 * @param[R] Type of the result
 *****************************************************************************/
inline fun <R> AnyResult<R?>.ifReal(f: (R) -> Unit): AnyResult<R?> = when {
  good == null -> this
  good.result == null -> this
  else -> apply { f(good!!.result!!) }
}

/******************************************************************************
 * Invokes the function only if it's a good result and the value is empty
 *
 * @param[f] Function to invoke
 * @param[R] Type of the result
 *****************************************************************************/
inline fun <R> AnyResult<R?>.ifNull(f: () -> Unit): AnyResult<R?> = when {
  good == null -> this
  good.result != null -> this
  else -> apply { f() }
}

/******************************************************************************
 * Invokes the function and returns the result if its a good result but empty
 * else returns this result
 *
 * @param[f] Function to invoke
 * @param[R] Type of the result
 *****************************************************************************/
inline fun <R> AnyResult<R?>.mapIfNull(
  f: () -> AnyResult<R?>
): AnyResult<R?> = when {
  good == null -> this
  good.result != null -> this
  else -> f()
}

/******************************************************************************
 * Invokes the function and returns the result if its bad or null result else
 * returns this result
 *
 * @param[f] Function to invoke
 * @param[R] Type of the result
 *****************************************************************************/
inline fun <R> AnyResult<R?>.mapIfBadOrNull(
  f: () -> AnyResult<R?>
): AnyResult<R?> = when {
  good?.result != null -> this
  else -> f()
}

/******************************************************************************
 * Returns the result if its good and value present else invokes the function
 *
 * @param[f] Function to invoke
 * @param[R] Type of the result
 *****************************************************************************/
inline fun <R> AnyResult<R?>.orReal(f: (BadResult<R?>?) -> R): R = when {
  good?.result != null -> good.result
  else -> f(bad)
}

/******************************************************************************
 * Converts the result to a good [AnyResult] with a [BadResult] being produced
 * if the result value is null
 *
 * @param[errorMessage] Error message if the result value is empty
 * @param[R] Type of the result
 *****************************************************************************/
fun <R> AnyResult<R?>.toReal(errorMessage: String): AnyResult<R> = when {
  isBad -> AnyResult.bad(bad!!.message, bad.exception)
  good?.result == null -> AnyResult.bad(errorMessage)
  else -> AnyResult.good(good.result)
}
