package fuookami.ospf.kotlin.utils.math.value_range

import org.junit.jupiter.api.*
import fuookami.ospf.kotlin.utils.math.*

class ValueRangeTest {
    @Test
    fun testConstructor() {
        val range = ValueRange(Flt64.one, Flt64.two)
        assert(range.ok)
        assert(range.value!!.lowerBound.value.unwrap() eq Flt64.one)
        assert(range.value!!.upperBound.value.unwrap() eq Flt64.two)
        val invalidRange = ValueRange(Flt64.two, Flt64.one)
        assert(!invalidRange.ok)
    }

    @Test
    fun testPlus() {
        val range = ValueRange(Flt64.one, Flt64.two).value!!
        val addedRange = range + Flt64.one
        assert(addedRange.lowerBound.value.unwrap() eq Flt64.two)
        assert(addedRange.upperBound.value.unwrap() eq Flt64.three)
        val twiceRange = range + range
        assert(twiceRange.lowerBound.value.unwrap() eq Flt64.two)
        assert(twiceRange.upperBound.value.unwrap() eq Flt64(4.0))
    }

    @Test
    fun testSubtract() {
        val range = ValueRange(Flt64.one, Flt64.two).value!!
        val subtractedRange = range - Flt64.one
        assert(subtractedRange.lowerBound.value.unwrap() eq Flt64.zero)
        assert(subtractedRange.upperBound.value.unwrap() eq Flt64.one)
        val noneRange = range - range
        assert(noneRange.lowerBound.value.unwrap() eq -Flt64.one)
        assert(noneRange.upperBound.value.unwrap() eq Flt64.one)
    }

    @Test
    fun testMultiply() {
        val range = ValueRange(Flt64.one, Flt64.two).value!!
        val twiceRange = range * Flt64.two
        assert(twiceRange!!.lowerBound.value.unwrap() eq Flt64.two)
        assert(twiceRange.upperBound.value.unwrap() eq Flt64(4.0))
        val negTwinRange = range * -Flt64.two
        assert(negTwinRange!!.lowerBound.value.unwrap() eq Flt64(-4.0))
        assert(negTwinRange.upperBound.value.unwrap() eq -Flt64.two)
        val squareRange = range * range
        assert(squareRange!!.lowerBound.value.unwrap() eq Flt64.one)
        assert(squareRange.upperBound.value.unwrap() eq Flt64(4.0))
    }

    @Test
    fun testDivide() {
        val range = ValueRange(Flt64.one, Flt64.two).value!!
        val halfRange = range / Flt64.two
        assert(halfRange!!.lowerBound.value.unwrap() eq Flt64(0.5))
        assert(halfRange.upperBound.value.unwrap() eq Flt64.one)
        val negHalfRange = range / -Flt64.two
        assert(negHalfRange!!.lowerBound.value.unwrap() eq -Flt64.one)
        assert(negHalfRange.upperBound.value.unwrap() eq -Flt64(0.5))
    }
}
