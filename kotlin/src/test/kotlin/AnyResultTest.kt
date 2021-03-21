
import org.junit.jupiter.api.Test
import java.lang.Exception
import java.lang.NullPointerException
import kotlin.test.*

class AnyResultTest {

  private val pass = 9
  private val fail = -1
  private val passAlt = ":)"

  private val errMsg = "ERROR"
  private val exMsg = "EXCEPTION"
  private val errMsgAlt = ":("
  private val errEx = Exception(exMsg)

  private val goodPass = GoodResult(pass)
  private val goodFail = GoodResult(fail)
  private val bad = BadResult<Int>(errMsg, errEx)

  private val realGoodPass = GoodResult<Int?>(pass)
  private val realGoodFail = GoodResult<Int?>(fail)
  private val nullGood = GoodResult<Int?>(null)

  private fun <R> newGood(good: GoodResult<R>) = AnyResult(good, null)

  private fun <R> newBad(bad: BadResult<R>) = AnyResult(null, bad)

  @Test
  fun constructor___1() {
    assertFailsWith(Exception::class) {
      AnyResult<Boolean>(null, null)
    }
  }

  @Test
  fun constructor___2() {
    assertFailsWith(Exception::class) {
      AnyResult(goodPass, bad)
    }
  }

  @Test
  fun constructor___3() {
    AnyResult(goodPass, null)
  }

  @Test
  fun constructor___4() {
    AnyResult(null, bad)
  }

  @Test
  fun good_toGood___1() {
    newGood(goodPass).toGood()
  }

  @Test
  fun good_toBad___1() {
    assertFailsWith(Exception::class) {
      newGood(goodPass).toBad()
    }
  }

  @Test
  fun bad_toBad___1() {
    newBad(bad).toBad()
  }

  @Test
  fun bad_toGood___1() {
    assertFailsWith(Exception::class) {
      newBad(bad).toGood()
    }
  }

  @Test
  fun good_ifGood___1() {
    var actual = fail
    newGood(goodPass).ifGood { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_ifBad___1() {
    var actual = fail
    newBad(bad).ifBad { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_ifBad___1() {
    var actual = pass
    newGood(goodPass).ifBad { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_ifBad___2() {
    var actual = pass
    newBad(bad).ifGood { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun good_whenGood___1() {
    val actual = newGood(goodFail).whenGood(pass) { fail }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_whenBad___1() {
    val actual = newBad(bad).whenBad(pass) { fail }
    assertEquals(pass, actual)
  }

  @Test
  fun good_whenBad___1() {
    val actual = newGood(goodFail).whenBad(fail) { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_whenGood___1() {
    val actual = newBad(bad).whenGood(fail) { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_map___1() {
    var actual = fail
    newGood(goodPass).map { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_map___2() {
    val actual = newGood(goodPass).map { passAlt }
    assertTrue(actual.isGood)
    actual.toGood()
    assertEquals(passAlt, actual.toGood().result)

  }

  @Test
  fun bad_map___1() {
    val actual = newBad(bad).map { passAlt }
    assertTrue(actual.isBad)
    actual.toBad()
    assertEquals(errMsg, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }


  @Test
  fun good_map___String___1() {
    var actual = fail
    newGood(goodPass).map(errMsgAlt) { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_map___String___2() {
    val actual = newGood(goodPass).map(errMsgAlt) { passAlt }
    assertTrue(actual.isGood)
    actual.toGood()
    assertEquals(passAlt, actual.toGood().result)
  }

  @Test
  fun bad_map___String___1() {
    val actual = newBad(bad).map(errMsgAlt) { passAlt }
    assertTrue(actual.isBad)
    actual.toBad()
    assertEquals(errMsgAlt, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }

  @Test
  fun good_mapIfBad___1() {
    val expected = newGood(goodPass)
    val actual = expected.mapIfBad { expected }
    assertEquals(expected, actual)
  }

  @Test
  fun bad_mapIfBad___1() {
    val expected = newBad(bad)
    val actual = newBad(bad).mapIfBad { expected }
    assertEquals(expected, actual)
  }

  @Test
  fun good_throwIfBad___1() {
    newGood(goodPass).throwIfBad()
  }

  @Test
  fun bad_throwIfBad___1() {
    assertFailsWith(Exception::class) {
      newBad(bad).throwIfBad()
    }
  }

  @Test
  fun good_throwIfBad___function___1() {
    newGood(goodPass).throwIfBad { Exception(errMsgAlt) }
  }

  @Test
  fun bad_throwIfBad___function___1() {
    assertFailsWith(Exception::class) {
      newBad(bad).throwIfBad { Exception(errMsgAlt) }
    }
  }

  @Test
  fun good_or___1() {
    val actual = newGood(goodPass).or { fail }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_or___1() {
    val actual = newBad(bad).or { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun good_unwrap___1() {
    val actual = newGood(goodPass).unwrap()
    assertEquals(pass, actual)
  }

  @Test
  fun bad_unwrap___1() {
    assertFailsWith(Exception::class) {
      newBad(bad).unwrap()
    }
  }

  @Test
  fun realGood_ifReal___1() {
    var actual = fail
    realGoodPass.ifReal { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun realGood_ifNull___1() {
    var actual = pass
    realGoodPass.ifNull { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun nullGood_ifNull___1() {
    var actual = fail
    nullGood.ifNull { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun nullGood_ifReal___1() {
    var actual = pass
    nullGood.ifReal { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun realGood_mapIfNull___1() {
    val actual = realGoodPass.mapIfNull{ realGoodFail }
    assertEquals(realGoodPass, actual)
  }

  @Test
  fun nullGood_mapIfNull___1() {
    val actual = nullGood.mapIfNull{ realGoodPass }
    assertEquals(realGoodPass, actual)
  }

  @Test
  fun bad_escalate___1() {
    val actual = bad.escalate(errMsgAlt)
    assertTrue(actual.isBad)
    assertNotEquals(bad, actual.toBad())
    assertEquals(errMsgAlt, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }

  @Test
  fun bad_mapEscalate___1() {
    val actual = bad.mapEscalate<String>()
    assertTrue(actual.isBad)
    assertEquals(errMsg, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }

  @Test
  fun bad_escalate___String__1() {
    val actual = bad.mapEscalate<String>(errMsgAlt)
    assertTrue(actual.isBad)
    assertEquals(errMsgAlt, actual.toBad().message)
    assertEquals(errEx, actual.toBad().exception)
  }

  @Test
  fun bad_throwEx___1() {
    assertFailsWith(Exception::class) {
      bad.throwEx()
    }
  }

  @Test
  fun bad_throwWrappedEx___1() {
    assertFailsWith(Exception::class) {
      bad.throwWrappedEx()
    }
  }

  @Test
  fun bad_throwEx_withCustomEx___1() {
    assertFailsWith(NullPointerException::class) {
      bad.throwEx { NullPointerException() }
    }
  }

  @Test
  fun real_optResult___1() {
    val r = pass.optResult(errMsgAlt)
    assertTrue(r.isGood)
    r.toGood()
    assertEquals(pass, r.toGood().result)
  }

  @Test
  fun null_optResult___1() {
    val v: Int? = null
    val r = v.optResult(errMsgAlt)
    assertTrue(r.isBad)
    r.toBad()
    assertEquals(errMsgAlt, r.toBad().message)
  }

  @Test
  fun real_ifReal___1() {
    var actual: Int? = fail
    newGood(realGoodPass).ifReal { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun real_ifNull___1() {
    var actual: Int? = pass
    newGood(realGoodPass).ifNull { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun null_ifNull___1() {
    var actual = fail
    newGood(nullGood).ifNull { actual = pass }
    assertEquals(pass, actual)
  }

  @Test
  fun null_ifReal___1() {
    var actual = pass
    newGood(nullGood).ifReal { actual = fail }
    assertEquals(pass, actual)
  }

  @Test
  fun real_mapIfNull___1() {
    val expected = newGood(realGoodPass)
    val actual = expected.mapIfNull{ newGood(realGoodFail) }
    assertEquals(expected, actual)
  }

  @Test
  fun null_mapIfNull___1() {
    val expected = newGood(realGoodPass)
    val actual = newGood(nullGood).mapIfNull{ expected }
    assertEquals(expected, actual)
  }

  @Test
  fun real_mapIfBadOrNull___1() {
    val expected = newGood(realGoodPass)
    val actual = expected.mapIfBadOrNull { newGood(realGoodFail) }
    assertEquals(expected, actual)
  }

  @Test
  fun null_mapIfBadOrNull___1() {
    val expected = newGood(realGoodPass)
    val actual = newGood(nullGood).mapIfBadOrNull { expected }
    assertEquals(expected, actual)
  }

  @Test
  fun bad_mapIfBadOrNull___1() {
    val expected = newGood(realGoodPass)
    val actual = AnyResult.bad<Int?>(errMsg).mapIfBadOrNull { expected }
    assertEquals(expected, actual)
  }

  @Test
  fun real_orReal___1() {
    val actual = newGood(realGoodPass).orReal { fail }
    assertEquals(pass, actual)
  }

  @Test
  fun null_orReal___1() {
    val actual = newGood(nullGood).orReal { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun bad_orReal___1() {
    val actual = AnyResult.bad<Int?>(errMsg).orReal { pass }
    assertEquals(pass, actual)
  }

  @Test
  fun real_toReal___1() {
    val actual = newGood(realGoodPass).toReal(errMsgAlt)
    assertTrue(actual.isGood)
  }

  @Test
  fun null_toReal___1() {
    val actual = newGood(nullGood).toReal(errMsgAlt)
    assertTrue(actual.isBad)
    actual.toBad()
    assertEquals(errMsgAlt, actual.toBad().message)
  }

  @Test
  fun bad_toReal___1() {
    val actual = AnyResult.bad<Int?>(errMsg).toReal(errMsgAlt)
    assertTrue(actual.isBad)
    actual.toBad()
    assertEquals(errMsg, actual.toBad().message)
    assertNull(actual.toBad().exception)
  }

  @Test
  fun good_of___1() {
    val expected = pass
    val actual = AnyResult.of { pass }
    assertTrue(actual.isGood)
    assertEquals(expected, actual.unwrap())
  }

  @Test
  fun bad_of___1() {
    val expectedMessage = errMsg
    val actual = AnyResult.of { throw Exception(errMsg) }
    assertTrue(actual.isBad)
    assertEquals(expectedMessage, actual.toBad().message)
    assertNotNull(actual.toBad().exception)
  }

  @Test
  fun bad_of___2() {
    val actual = AnyResult.of { throw Exception() }
    assertTrue(actual.isBad)
    assertNotNull(actual.toBad().message)
    assertNotNull(actual.toBad().exception)
  }

  @Test
  fun bad_of___String___1() {
    val expectedMessage = errMsgAlt
    val actual = AnyResult.of(errMsgAlt) { throw Exception(errMsg) }
    assertTrue(actual.isBad)
    assertEquals(expectedMessage, actual.toBad().message)
    assertNotNull(actual.toBad().exception)
  }
}
