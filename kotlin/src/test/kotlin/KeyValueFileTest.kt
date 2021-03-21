import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.OS
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val resources = Paths.get("src/test/resources").toAbsolutePath()
private val file_1 = resources.resolve("store_1.kvs")

class KeyValueFileTest {

  companion object {

    @BeforeAll
    @JvmStatic
    fun beforeAllTests() {
      Files.createDirectories(resources)
    }
  }

  @AfterEach
  fun afterEachTest() {
    Files.deleteIfExists(file_1)
  }

  private val abc = "abc" to "Rincewind"
  private val efg = "efg" to "Weatherwax"
  private val hij = "   hij   " to "Mort"
  private val klm = "klm" to ""

  private val valid_data = mapOf(
    abc,
    efg,
    hij,
    klm
  )

  fun osDeadPath() = when {
    OS.WINDOWS.isCurrentOs -> Paths.get("I:\\asdasdasda\\")
    OS.LINUX.isCurrentOs -> Paths.get("$resources/asdasdasda/\\0")
    else -> throw Exception("Operating system unknown to test case")
  }

  @Test
  fun write___1() {
    val data = mapOf("abcd" to "Ogg")
    val result = KeyValueFile(osDeadPath()).write(data)
    assertTrue(result.isBad)
  }

  @Test
  fun write___2() {
    val data = mapOf("ab=cd" to "Ogg")
    val result = KeyValueFile(file_1).write(data)
    assertTrue(result.isBad)
  }

  @Test
  fun write___3() {
    val data = mapOf("ab\ncd" to "Ogg")
    val result = KeyValueFile(file_1).write(data)
    assertTrue(result.isBad)
  }

  @Test
  fun write___4() {
    val data = mapOf("abcd" to "Ogg")
    val result = KeyValueFile(file_1).write(data)
    assertTrue(result.isGood)
  }

  @Test
  fun write___5() {
    val data = mapOf("abcd" to "  Og\ng  ")
    val result = KeyValueFile(file_1).write(data)
    assertTrue(result.isBad)
  }

  @Test
  fun write___6() {
    val data = mapOf("abcd" to "Ogg")
    val result = KeyValueFile(file_1).write(data)
    assertTrue(result.isGood)
  }

  @Test
  fun write___7() {
    val result = KeyValueFile(file_1).write(valid_data)
    assertTrue(result.isGood)
  }

  @Test
  fun write___8() {
    val kvFile = KeyValueFile(file_1)
    kvFile.write(valid_data)
    assertTrue(Files.exists(file_1))
  }

  @Test
  fun read___1() {
    val kvFile = KeyValueFile(osDeadPath())
    val result = kvFile.read()
    assertTrue(result.isBad)
  }

  @Test
  fun read___2() {
    Files.write(
      file_1,
      "abc=Susan\n".toByteArray(Charsets.UTF_8)
    )

    val result = KeyValueFile(file_1).read()
    assertTrue(result.isGood)
  }

  @Test
  fun read___3() {
    Files.write(
      file_1,
      "ab\nc=ogg".toByteArray(Charsets.UTF_8)
    )

    val result = KeyValueFile(file_1).read()
    assertTrue(result.isBad)
  }

  @Test
  fun writeThenRead___4() {
    KeyValueFile(file_1).write(valid_data)
    val result = KeyValueFile(file_1).read()

    assertTrue(result.isGood)
    assertTrue(Files.exists(file_1))
    assertEquals(valid_data, result.unwrap())
  }
}