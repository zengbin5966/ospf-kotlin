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

    @Test
    fun testIntersection() {
        val range = ValueRange(Flt64.one, Flt64.three).value!!
        val leftHalfRange1 = ValueRange(Flt64.zero, Flt64.two).value!! intersect range
        assert(leftHalfRange1 != null && leftHalfRange1.lowerBound.value.unwrap() eq Flt64.one)
        assert(leftHalfRange1 != null && leftHalfRange1.upperBound.value.unwrap() eq Flt64.two)
        val rightHalfRange1 = range intersect ValueRange(Flt64.zero, Flt64.two).value!!
        assert(rightHalfRange1 != null && rightHalfRange1.lowerBound.value.unwrap() eq Flt64.one)
        assert(rightHalfRange1 != null && rightHalfRange1.upperBound.value.unwrap() eq Flt64.two)
        val leftHalfRange2 = ValueRange(Flt64.two, Flt64.ten).value!! intersect range
        assert(leftHalfRange2 != null && leftHalfRange2.lowerBound.value.unwrap() eq Flt64.two)
        assert(leftHalfRange2 != null && leftHalfRange2.upperBound.value.unwrap() eq Flt64.three)
        val rightHalfRange2 = range intersect ValueRange(Flt64.two, Flt64.ten).value!!
        assert(rightHalfRange2 != null && rightHalfRange2.lowerBound.value.unwrap() eq Flt64.two)
        assert(rightHalfRange2 != null && rightHalfRange2.upperBound.value.unwrap() eq Flt64.three)
        val noneRange = range intersect ValueRange(Flt64(4.0), Flt64.ten).value!!
        assert(noneRange == null)
    }

    @Test
    fun testUnion() {
        val range = ValueRange(Flt64.one, Flt64.three).value!!
        val unionRange1 = range union ValueRange(Flt64.zero, Flt64.two).value!!
        assert(unionRange1 != null && unionRange1.lowerBound.value.unwrap() eq Flt64.zero)
        assert(unionRange1 != null && unionRange1.upperBound.value.unwrap() eq Flt64.three)
        val unionRange2 = range union ValueRange(Flt64.two, Flt64.ten).value!!
        assert(unionRange2 != null && unionRange2.lowerBound.value.unwrap() eq Flt64.one)
        assert(unionRange2 != null && unionRange2.upperBound.value.unwrap() eq Flt64.ten)
        val unionRange3 = range union ValueRange(Flt64.zero, Flt64.ten).value!!
        assert(unionRange3 != null && unionRange3.lowerBound.value.unwrap() eq Flt64.zero)
        assert(unionRange3 != null && unionRange3.upperBound.value.unwrap() eq Flt64.ten)
        val noneRange = range union ValueRange(Flt64(4.0), Flt64.ten).value!!
        assert(noneRange == null)
    }
}
