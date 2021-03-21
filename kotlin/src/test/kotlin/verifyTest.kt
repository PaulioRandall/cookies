import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class VerifyTest {

  @Test
  fun T_verifyNotNull___1() {
    "".verifyNotNull("Should not throw")
  }

  @Test
  fun T_verifyNotNull___2() {
    assertFailsWith<FailedVerification> {
      null.verifyNotNull("Should throw")
    }
  }

  @Test
  fun T_verifyNull___1() {
    null.verifyNull("Should not throw")
  }

  @Test
  fun T_verifyNull___2() {
    assertFailsWith<FailedVerification> {
      "".verifyNull("Should throw")
    }
  }

  @Test
  fun String_verifyNotEmpty___1() {
    "not empty".verifyNotEmpty("Should not throw")
  }

  @Test
  fun String_verifyNotEmpty___2() {
    assertFailsWith<FailedVerification> {
      "".verifyNotEmpty("Should throw")
    }
  }

  @Test
  fun String_verifyNotBlank___1() {
    "not blank".verifyNotBlank("Should not throw")
  }

  @Test
  fun String_verifyNotBlank___2() {
    assertFailsWith<FailedVerification> {
      "  \n  \r  \t  ".verifyNotBlank("Should throw")
    }
  }

  @Test
  fun String_verifyLength___1() {
    "1234".verifyLength(4, "Should not throw")
  }

  @Test
  fun String_verifyLength___2() {
    assertFailsWith<FailedVerification> {
      "1234".verifyLength(5, "Should throw")
    }
  }

  @Test
  fun String_verifyMatches___1() {
    "abc".verifyMatches("[a-c]{3}".toRegex(), "Should not throw")
  }

  @Test
  fun String_verifyMatches___2() {
    assertFailsWith<FailedVerification> {
      "abc".verifyMatches("[x-z]{3}".toRegex(), "Should throw")
    }
  }

  @Test
  fun Collection_verifyNotEmpty___1() {
    listOf(1,2,3).verifyNotEmpty("Should not throw")
  }

  @Test
  fun Collection_verifyNotEmpty___2() {
    assertFailsWith<FailedVerification> {
      listOf<Any>().verifyNotEmpty("Should throw")
    }
  }

  @Test
  fun Collection_verifyEmpty___1() {
    listOf<Any>().verifyEmpty("Should not throw")
  }

  @Test
  fun Collection_verifyEmpty___2() {
    assertFailsWith<FailedVerification> {
      listOf(1,2,3).verifyEmpty("Should throw")
    }
  }

  @Test
  fun Collection_verifyLength___1() {
    listOf(1,2,3).verifyLength(3, "Should not throw")
  }

  @Test
  fun Collection_verifyLength___2() {
    assertFailsWith<FailedVerification> {
      listOf(1,2,3).verifyLength(4, "Should throw")
    }
  }

  @Test
  fun Collection_verifyNoDuplicates__noDuplicates__notThrowException() {
    listOf(1, 2, 3, 4).verifyNoDuplicates("Test failed!")
  }

  @Test
  fun Collection_verifyNoDuplicates__withDuplicates__throwsException() {
    assertFailsWith<FailedVerification> {
      listOf(1, 2, 3, 1).verifyNoDuplicates("Test passed!")
    }
  }

  @Test
  fun Collection_verifyNoDuplicates__withNullButNoDuplicates__notThrowException() {
    listOf(1, null, 3, 4).verifyNoDuplicates("Test failed!")
  }

  @Test
  fun Collection_verifyNoDuplicates__withNullDuplicates__throwsException() {
    assertFailsWith<FailedVerification> {
      listOf(null, 2, 3, null).verifyNoDuplicates("Test passed!")
    }
  }

  @Test
  fun Map_verifyNotEmpty___1() {
    mapOf(1 to "1", 2 to "2").verifyNotEmpty("Should not throw")
  }

  @Test
  fun Map_verifyNotEmpty___2() {
    assertFailsWith<FailedVerification> {
      mapOf<Any, Any>().verifyNotEmpty("Should throw")
    }
  }

  @Test
  fun Map_verifyEmpty___1() {
    mapOf<Any, Any>().verifyEmpty("Should not throw")
  }

  @Test
  fun Map_verifyEmpty___2() {
    assertFailsWith<FailedVerification> {
      mapOf(1 to "1", 2 to "2").verifyEmpty("Should throw")
    }
  }

  @Test
  fun Map_verifyLength___1() {
    mapOf(1 to "1", 2 to "2").verifyLength(2, "Should not throw")
  }

  @Test
  fun Map_verifyLength___2() {
    assertFailsWith<FailedVerification> {
      mapOf(1 to "1", 2 to "2").verifyLength(3, "Should throw")
    }
  }

  @Test
  fun Number_verifyZero___1() {
    0.0.verifyZero("Should not throw")
  }

  @Test
  fun Number_verifyZero___2() {
    assertFailsWith<FailedVerification> {
      1.1.verifyZero("Should throw")
    }
  }

  @Test
  fun Number_verifyNotNegative___1() {
    1.verifyNotNegative("Should not throw")
  }

  @Test
  fun Number_verifyNotNegative___2() {
    assertFailsWith<FailedVerification> {
      (-1).verifyNotNegative("Should throw")
    }
  }

  @Test
  fun Number_verifyNotNegative___3() {
    0.verifyNotNegative("Should not throw")
  }

  @Test
  fun Number_verifyNotPositive___1() {
    (-1).verifyNotPositive("Should not throw")
  }

  @Test
  fun Number_verifyNotPositive___2() {
    assertFailsWith<FailedVerification> {
      1.verifyNotPositive("Should throw")
    }
  }

  @Test
  fun Number_verifyNotPositive___3() {
    0.verifyNotPositive("Should not throw")
  }

  @Test
  fun Number_verifyNegative___1() {
    (-1).verifyNegative("Should not throw")
  }

  @Test
  fun Number_verifyNegative___2() {
    assertFailsWith<FailedVerification> {
      1.verifyNegative("Should throw")
    }
  }

  @Test
  fun Number_verifyNegative___3() {
    assertFailsWith<FailedVerification> {
      0.verifyNegative("Should throw")
    }
  }

  @Test
  fun Number_verifyPositive___1() {
    1.verifyPositive("Should not throw")
  }

  @Test
  fun Number_verifyPositive___2() {
    assertFailsWith<FailedVerification> {
      (-1).verifyPositive("Should throw")
    }
  }

  @Test
  fun Number_verifyPositive___3() {
    assertFailsWith<FailedVerification> {
      0.verifyPositive("Should throw")
    }
  }

  @Test
  fun Number_verifyLessThan___1() {
    1.verifyLessThan(2, "Should not throw")
  }

  @Test
  fun Number_verifyLessThan___2() {
    assertFailsWith<FailedVerification> {
      2.verifyLessThan(2, "Should throw")
    }
  }

  @Test
  fun Number_verifyLessThan___3() {
    assertFailsWith<FailedVerification> {
      3.verifyLessThan(2, "Should throw")
    }
  }

  @Test
  fun Number_verifyLessThanOrEqual___1() {
    1.verifyLessThanOrEqual(2, "Should not throw")
  }

  @Test
  fun Number_verifyLessThanOrEqual___2() {
    2.verifyLessThanOrEqual(2, "Should not throw")
  }

  @Test
  fun Number_verifyLessThanOrEqual___3() {
    assertFailsWith<FailedVerification> {
      3.verifyLessThanOrEqual(2, "Should throw")
    }
  }

  @Test
  fun Number_verifyGreaterThan___1() {
    3.verifyGreaterThan(2, "Should not throw")
  }

  @Test
  fun Number_verifyGreaterThan___2() {
    assertFailsWith<FailedVerification> {
      2.verifyGreaterThan(2, "Should throw")
    }
  }

  @Test
  fun Number_verifyGreaterThan___3() {
    assertFailsWith<FailedVerification> {
      1.verifyGreaterThan(2, "Should throw")
    }
  }

  @Test
  fun Number_verifyGreaterThanOrEqual___1() {
    3.verifyGreaterThanOrEqual(2, "Should not throw")
  }

  @Test
  fun Number_verifyGreaterThanOrEqual___2() {
    2.verifyGreaterThanOrEqual(2, "Should not throw")
  }

  @Test
  fun Number_verifyGreaterThanOrEqual___3() {
    assertFailsWith<FailedVerification> {
      1.verifyGreaterThanOrEqual(2, "Should throw")
    }
  }

  @Test
  fun Int_verifyEven___1() {
    0.verifyEven("Should not throw")
  }

  @Test
  fun Int_verifyEven___2() {
    assertFailsWith<FailedVerification> {
      1.verifyEven("Should throw")
    }
  }

  @Test
  fun Int_verifyEven___3() {
    2.verifyEven("Should not throw")
  }

  @Test
  fun Int_verifyOdd___1() {
    assertFailsWith<FailedVerification> {
      0.verifyOdd("Should throw")
    }
  }

  @Test
  fun Int_verifyOdd___2() {
    1.verifyOdd("Should not throw")
  }

  @Test
  fun Int_verifyOdd___3() {
    assertFailsWith<FailedVerification> {
      2.verifyOdd("Should throw")
    }
  }

  @Test
  fun Int_verifyDivisibleBy___1() {
    12.verifyDivisibleBy(4, "Should not throw")
  }

  @Test
  fun Int_verifyDivisibleBy___2() {
    0.verifyDivisibleBy(20, "Should not throw")
  }

  @Test
  fun Int_verifyDivisibleBy___3() {
    assertFailsWith<FailedVerification> {
      12.verifyDivisibleBy(7, "Should throw")
    }
  }

  @Test
  fun Long_verifyEven___1() {
    0L.verifyEven("Should not throw")
  }

  @Test
  fun Long_verifyEven___2() {
    assertFailsWith<FailedVerification> {
      1L.verifyEven("Should throw")
    }
  }

  @Test
  fun Long_verifyEven___3() {
    2L.verifyEven("Should not throw")
  }

  @Test
  fun Long_verifyOdd___1() {
    assertFailsWith<FailedVerification> {
      0L.verifyOdd("Should throw")
    }
  }

  @Test
  fun Long_verifyOdd___2() {
    1L.verifyOdd("Should not throw")
  }

  @Test
  fun Long_verifyOdd___3() {
    assertFailsWith<FailedVerification> {
      2L.verifyOdd("Should throw")
    }
  }

  @Test
  fun Long_verifyDivisibleBy___1() {
    12L.verifyDivisibleBy(4L, "Should not throw")
  }

  @Test
  fun Long_verifyDivisibleBy___2() {
    0L.verifyDivisibleBy(20L, "Should not throw")
  }

  @Test
  fun Long_verifyDivisibleBy___3() {
    assertFailsWith<FailedVerification> {
      12L.verifyDivisibleBy(7L, "Should throw")
    }
  }

  @Test
  fun T_verifyEquals___1() {
    val input = "abc"
    input.verifyEquals(input, "Should not throw")
  }

  @Test
  fun T_verifyEquals___2() {
    val thisInput = String("abc".toCharArray())
    val otherInput = String("abc".toCharArray())
    thisInput.verifyEquals(otherInput, "Should not throw")
  }

  @Test
  fun T_verifyEquals___3() {
    val thisInput = "abc"
    val otherInput = "efg"
    assertFailsWith<FailedVerification> {
      thisInput.verifyEquals(otherInput, "Should throw")
    }
  }

  @Test
  fun T_verifyNotEquals___1() {
    val thisInput = "abc"
    val otherInput = "efg"
    thisInput.verifyNotEquals(otherInput, "Should not throw")
  }

  @Test
  fun T_verifyNotEquals___2() {
    val thisInput = String("abc".toCharArray())
    val otherInput = String("abc".toCharArray())
    assertFailsWith<FailedVerification> {
      thisInput.verifyNotEquals(otherInput, "Should throw")
    }
  }

  @Test
  fun T_verifyNotEquals___3() {
    val input = "abc"
    assertFailsWith<FailedVerification> {
      input.verifyNotEquals(input, "Should throw")
    }
  }

  @Test
  fun T_verifySame___1() {
    val input = "abc"
    input.verifySame(input, "Should not throw")
  }

  @Test
  fun T_verifySame___2() {
    val thisInput = String("abc".toCharArray())
    val otherInput = String("abc".toCharArray())
    assertFailsWith<FailedVerification> {
      thisInput.verifySame(otherInput, "Should throw")
    }
  }

  @Test
  fun T_verifySame___3() {
    val thisInput = "abc"
    val otherInput = "efg"
    assertFailsWith<FailedVerification> {
      thisInput.verifySame(otherInput, "Should throw")
    }
  }

  @Test
  fun T_verifyNotSame___1() {
    val thisInput = String("abc".toCharArray())
    val otherInput = String("abc".toCharArray())
    thisInput.verifyNotSame(otherInput, "Should not throw")
  }

  @Test
  fun T_verifyNotSame___2() {
    val input = "abc"
    assertFailsWith<FailedVerification> {
      input.verifyNotSame(input, "Should throw")
    }
  }
}
