import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class SubstitutorTest {

  private val stampVars: (String) -> String? = {
    when (it) {
      "Weather" -> "wax"
      "Rince" -> "wind"
      else -> null
    }
  }

  @Test
  fun stamp___1() {
    """
      ...stamp___1()
      When given a stencil only containing a single variable
      Returns that value
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val actual = sub.stamp("\${Weather}")
    assertEquals("wax", actual)
  }

  @Test
  fun stamp___2() {
    """
      ...stamp___2()
      When given a stencil containing two variables
      Returns a string containing mapped values for both variables
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val actual = sub.stamp("\${Weather}\${Rince}")
    assertEquals("waxwind", actual)
  }

  @Test
  fun stamp___3() {
    """
      ...stamp___3()
      When given a stencil containing two variables & other text
      Returns the exact expected string
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val actual = sub.stamp("Weather\${Weather} & Rince\${Rince}")
    assertEquals("Weatherwax & Rincewind", actual)
  }

  @Test
  fun stamp___4() {
    """
      ...stamp___4()
      When given an empty stencil
      Returns an empty string
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val actual = sub.stamp("")
    assertEquals("", actual)
  }

  @Test
  fun stamp___5() {
    """
      ...stamp___5()
      When given a stencil not containing any variables
      Returns the input
    """.describe()

    val expected = "\n luggage \n"
    val sub = Substitutor.bashStyle(stampVars)
    val actual = sub.stamp(expected)
    assertEquals(expected, actual)
  }

  @Test
  fun stamp___6() {
    """
      ...stamp___6()
      When given a stencil containing a variable
      But does not have a mapped value
      Throws an exception
    """.describe()

    assertFails {
      val sub = Substitutor.bashStyle { null }
      sub.stamp("\${abc}")
    }
  }

  @Test
  fun stamp___7() {
    """
      ...stamp___7()
      When given a stencil containing multiple occurrences of the same variable
      Returns the input with all occurrences replaced with the mapped value
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val actual = sub.stamp("\${Rince}_\${Rince}_\${Rince}")
    assertEquals("wind_wind_wind", actual)
  }

  @Test
  fun stamp___8() {
    """
      ...stamp___8()
      When given a regex that doesn't contain the specified replacement group
      Throws an exception
    """.describe()

    assertFails {
      val sub = Substitutor("abc".toRegex(), 1, 0) { "" }
      sub.stamp("abc")
    }
  }

  @Test
  fun stamp___9() {
    """
      ...stamp___9()
      When given a regex that doesn't contain the specified key group
      Throws an exception
    """.describe()

    assertFails {
      val sub = Substitutor("abc".toRegex(), 0, 1) { "" }
      sub.stamp("abc")
    }
  }

  @Test
  fun safeStamp___1() {
    """
      ...safeStamp___1()
      When given a stencil only containing a single variable
      Returns a good result that contains the expected value
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val result = sub.safeStamp("\${Weather}")
    assertTrue(result.isGood)
    assertEquals("wax", result.unwrap())
  }

  @Test
  fun safeStamp___2() {
    """
      ...safeStamp___2()
      When given a stencil containing two variables
      Returns a good result that contains mapped values for both variables
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val result = sub.safeStamp("\${Weather}\${Rince}")
    assertTrue(result.isGood)
    assertEquals("waxwind", result.unwrap())
  }

  @Test
  fun safeStamp___3() {
    """
      ...safeStamp___3()
      When given a stencil containing two variables & other text
      Returns a good result that contains the expected string
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val result = sub.safeStamp("Weather\${Weather} & Rince\${Rince}")
    assertTrue(result.isGood)
    assertEquals("Weatherwax & Rincewind", result.unwrap())
  }

  @Test
  fun safeStamp___4() {
    """
      ...safeStamp___4()
      When given an empty stencil
      Returns a good result containing an empty string
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val result = sub.safeStamp("")
    assertTrue(result.isGood)
    assertEquals("", result.unwrap())
  }

  @Test
  fun safeStamp___5() {
    """
      ...safeStamp___5()
      When given a stencil not containing any variables
      Returns a good result containing the input
    """.describe()

    val expected = "\n luggage \n"
    val sub = Substitutor.bashStyle(stampVars)
    val result = sub.safeStamp(expected)
    assertTrue(result.isGood)
    assertEquals(expected, result.unwrap())
  }

  @Test
  fun safeStamp___6() {
    """
      ...safeStamp___6()
      When given a stencil containing a variable
      But does not have a mapped value
      Returns a bad result
    """.describe()

    val sub = Substitutor.bashStyle { null }
    val result = sub.safeStamp("\${abc}")
    assertTrue(result.isBad)
  }

  @Test
  fun safeStamp___7() {
    """
      ...safeStamp___7()
      When given a stencil containing multiple occurrences of the same variable
      Returns a good result that contains the input with all occurrences replaced with the mapped value
    """.describe()

    val sub = Substitutor.bashStyle(stampVars)
    val result = sub.safeStamp("\${Rince}_\${Rince}_\${Rince}")
    assertTrue(result.isGood)
    assertEquals("wind_wind_wind", result.unwrap())
  }

  @Test
  fun safeStamp___8() {
    """
      ...safeStamp___8()
      When given a regex that doesn't contain the specified replacement group
      Returns a bad result
    """.describe()

    val sub = Substitutor("abc".toRegex(), 1, 0) { "" }
    val result = sub.safeStamp("abc")
    assertTrue(result.isBad)
  }

  @Test
  fun safeStamp___9() {
    """
      ...safeStamp___9()
      When given a regex that doesn't contain the specified key group
      Returns a bad result
    """.describe()

    val sub = Substitutor("abc".toRegex(), 0, 1) { "" }
    val result = sub.safeStamp("abc")
    assertTrue(result.isBad)
  }
}