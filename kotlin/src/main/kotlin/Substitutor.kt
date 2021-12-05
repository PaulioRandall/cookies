import AnyResult.Companion.bad
import java.lang.Exception
import kotlin.math.max

/* Substitutes variable declarations within template strings (stencils)
 *
 * @property[regex] Regular expression used to find variable declarations
 * within strings
 * @property[replaceGroup] Group to replace when performing variable
 * substitutions; 0 by default (whole match)
 * @property[keyGroup] Group within the regex that contains the key to
 * variables; 0 by default (whole match)
 * @property[variables] Function that accepts variable names and returns the
 * associated value
 */
data class Substitutor(
  val regex: Regex,
  val replaceGroup: Int = 0,
  val keyGroup: Int = 0,
  val variables: (String) -> String?
) {

  // Populates the variables within the supplied stencil string throwing an
  // exception if any problems occur.
  fun stamp(stencil: String): String {

    val minSize = max(replaceGroup + 1, keyGroup + 1)
    var result = stencil

    while(true) {
      val match = regex.find(result)
      match ?: break

      if (match.groups.size < minSize) throw Exception(
        "Regex match '$regex' does not contain enough groups, expected a minimum of $minSize"
      )

      val range = match.groups[replaceGroup]!!.range
      val key = match.groups[keyGroup]!!.value

      val value = variables(key) ?: throw Exception(
        "Could not find value for variable named '${match.value}'"
      )

      result = result.replaceRange(range, value)
    }

    return result
  }

  // Populates the variables within the supplied stencil string returning an
  // error result instead of throwing an exception.
  fun safeStamp(stencil: String): AnyResult<String> {

    val minSize = max(replaceGroup + 1, keyGroup + 1)
    var result = stencil

    while(true) {
      val match = regex.find(result)
      match ?: break

      if (match.groups.size < minSize) return bad(
        "Regex match '$regex' does not contain enough groups, expected a minimum of $minSize"
      )

      val range = match.groups[replaceGroup]!!.range
      val key = match.groups[keyGroup]!!.value

      val value = variables(key) ?: return bad(
        "Could not find value for variable named '${match.value}'"
      )

      result = result.replaceRange(range, value)
    }

    return result.realResult()
  }

  companion object {

    // Creates a new [Substitutor] using a bash style '${...}' regular
    // expression to identify variables.
    fun bashStyle(variables: (String) -> String?) = Substitutor(
      "\\\$\\{([a-zA-Z0-9_]+)}".toRegex(),
      0,
      1,
      variables
    )

  }
}
