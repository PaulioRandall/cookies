import org.junit.jupiter.api.Test
import java.lang.IndexOutOfBoundsException
import kotlin.test.*

class tuplesTest {

  @Test
  fun pair_swap___1() {
    val before = 1 to 2
    val after = before.swap()
    assertEquals(1, after.second)
    assertEquals(2, after.first)
  }

  @Test
  fun pair_shuffle___1() {
    val before = 1 to 2
    for (i in 0..100) {
      val after = before.shuffle()
      if(after.first == 2) return
    }
    fail("Streak of 100 non-swaps is so tiny the function must be bugged")
  }

  @Test
  fun pair_contains___1() {
    val actual = (1 to 2).contains(1)
    assertTrue(actual)
  }

  @Test
  fun pair_contains___2() {
    val actual = (1 to 2).contains(2)
    assertTrue(actual)
  }

  @Test
  fun pair_contains___3() {
    val actual = (1 to 2).contains(3)
    assertFalse(actual)
  }

  @Test
  fun pair_map___1() {
    val expected = 3.0 to 4.0
    val actual = (1 to 2).map { it.first + 2.0 to it.second + 2.0 }
    assertEquals(expected, actual)
  }

  @Test
  fun pair_plus___1() {
    val actual = (1 to 2) + 3
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }

  @Test
  fun pair_plus___2() {
    val actual = (1 to 2) + { 3 }
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }

  @Test
  fun pair_midsert___1() {
    val actual = (1 to 3).midsert(2)
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }

  @Test
  fun pair_midsert___2() {
    val actual = (1 to 3).midsert { first + 1 }
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }

  @Test
  fun pair_insert___1() {
    val actual = (2 to 3).insert(1)
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }

  @Test
  fun pair_insert___2() {
    val actual = (2 to 3).insert { first - 1 }
    assertEquals(1, actual.first)
    assertEquals(2, actual.second)
    assertEquals(3, actual.third)
  }

  @Test
  fun pair_get___1() {
    val actual = (1 to 2).get(0)
    assertEquals(1, actual)
  }

  @Test
  fun pair_get___2() {
    val actual = (1 to 2)[1]
    assertEquals(2, actual)
  }

  @Test
  fun pair_get___3() {
    assertFailsWith<IndexOutOfBoundsException> {
      (1 to 2)[2]
    }
  }

  @Test
  fun pair_join___1() {
    val actual = (1 to 2).join(":")
    assertEquals("1:2", actual)
  }

  @Test
  fun triple_reverse___1() {
    val actual = Triple(1, 2,3).reverse()
    assertEquals(3, actual.first)
    assertEquals(2, actual.second)
    assertEquals(1, actual.third)
  }

  @Test
  fun triple_shiftLeft___1() {
    val actual = Triple(1, 2,3).shiftLeft()
    assertEquals(2, actual.first)
    assertEquals(3, actual.second)
    assertEquals(1, actual.third)
  }

  @Test
  fun triple_shiftRight___1() {
    val actual = Triple(1, 2,3).shiftRight()
    assertEquals(3, actual.first)
    assertEquals(1, actual.second)
    assertEquals(2, actual.third)
  }

  @Test
  fun triple_shuffle___1() {
    val before = Triple(1, 2, 3)
    for (i in 0..100) {
      val after = before.shuffle()
      if(after.first != 1) return
    }
    fail(
      "Streak of 100 shuffles that result in no actual change is so tiny" +
        " the function must be bugged"
    )
  }

  @Test
  fun triple_contains___1() {
    val actual = Triple(1,2,3).contains(1)
    assertTrue(actual)
  }

  @Test
  fun triple_contains___2() {
    val actual = Triple(1,2,3).contains(3)
    assertTrue(actual)
  }

  @Test
  fun triple_contains___3() {
    val actual = Triple(1,2,3).contains(4)
    assertFalse(actual)
  }

  @Test
  fun triple_map___1() {
    val actual = Triple(1,2,3).map {
      Triple(first+3, second+3, third+3)
    }
    val expected = Triple(4,5,6)
    assertEquals(expected, actual)
  }

  @Test
  fun triple_dropFirst___1() {
    val actual = Triple(1,2,3).dropFirst()
    assertEquals(2 to 3, actual)
  }

  @Test
  fun triple_dropSecond___1() {
    val actual = Triple(1,2,3).dropSecond()
    assertEquals(1 to 3, actual)
  }

  @Test
  fun triple_dropThird___1() {
    val actual = Triple(1,2,3).dropThird()
    assertEquals(1 to 2, actual)
  }

  @Test
  fun triple_shrink___1() {
    val actual = Triple(1,2,3).shrink {
      first to second
    }
    assertEquals(1 to 2, actual)
  }

  @Test
  fun triple_get___1() {
    val actual = Triple(1, 2, 3).get(0)
    assertEquals(1, actual)
  }

  @Test
  fun triple_get___2() {
    val actual = Triple(1, 2, 3)[2]
    assertEquals(3, actual)
  }

  @Test
  fun triple_get___3() {
    assertFailsWith<IndexOutOfBoundsException> {
      Triple(1,2,3)[3]
    }
  }

  @Test
  fun triple_toSet___1() {
    val actual = Triple(1, 2, 3).toSet()
    assertEquals(setOf(1,2,3), actual)
  }

  @Test
  fun triple_join___1() {
    val actual = Triple(1, 2, 3).join(":")
    assertEquals("1:2:3", actual)
  }
}