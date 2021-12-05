import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

// Represents a store for key value pairs.
interface KeyValueMedia {

  // Reads a key value store into a map.
  fun read(): AnyResult<Map<String, String>>

  // Writes a map to a key value store file.
  fun write(map: Map<String, String>): BinaryResult
}

// Implementation of [KeyValueMedia] that uses files as the media.
class KeyValueFile(val path: Path): KeyValueMedia {

  // Matches true if:
  // - There are NO carriage returns '\r' or newlines '\n'
  // - There are NO equals symbols '='
  // - There is at least one non-whitespace character
  val KEY_REGEX = "^[^\\S\\r\\n]*([^=\\r\\n\\s][^=\\r\\n]*?)[^\\S\\r\\n]*\$".toRegex()

  // Matches true if:
  // - There are NO carriage returns '\r' or newlines '\n'.
  val VALUE_REGEX = "^[^\\r\\n]*\$".toRegex()

  // Matches true if:
  // - There are NO carriage returns '\r' or newlines '\n'
  // - There is at least one equals symbol '='
  // - There is at least one non-whitespace character appearing before the
  //   equals symbol
  val KEY_VALUE_REGEX = "^[^\\S\\r\\n]*([^=\\r\\n\\s][^=\\r\\n]*?)[^\\S\\r\\n]*=([^\\r\\n]*)\$".toRegex()

  // Returns true if the key value file exists and thus can be read.
  fun doesFileExist() = Files.exists(path)

  // {@inheritDoc}
  override fun write(map: Map<String, String>): BinaryResult {
    val joiner = StringJoiner("")

    for((k, v) in map) {
      when {
        not { KEY_REGEX.matches(k) } -> return BinaryResult.bad(
          "The key '$k' does not match the required format '$KEY_REGEX'"
        )
        not { VALUE_REGEX.matches(v) } -> return BinaryResult.bad(
          "The value '$v' does not match the required format '$VALUE_REGEX'"
        )
      }

      joiner.add("$k=$v\n")
    }

    try {
      Files.write(
        path,
        joiner.toString().toByteArray(Charsets.UTF_8)
      )
    } catch (ex: Exception) {
      return BinaryResult.bad("Failed to write data to file", ex)
    }

    return BinaryResult.good()
  }

  // {@inheritDoc}
  override fun read(): AnyResult<Map<String, String>> {

    val lines = try {
      Files.readAllLines(path, Charsets.UTF_8)
    } catch (ex: Exception) {
      return BinaryResult.bad("Failed to read data from file", ex)
    }

    val data = mutableMapOf<String, String>()

    for((i, line) in lines.withIndex()) {
      if(line.isBlank()) continue

      if( not { KEY_VALUE_REGEX.matches(line) } ) return AnyResult.bad(
        "Key value pair at line ${i + 1} does not match" +
          " the required format '$KEY_VALUE_REGEX'"
      )

      val keyValue = line.bifurcate("=")
      data[keyValue.first] = keyValue.second!!
    }

    return AnyResult.good(data.toMap())
  }
}
