package fuookami.ospf.kotlin.utils.math

import java.util.*
import kotlin.reflect.full.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import fuookami.ospf.kotlin.utils.concept.*
import fuookami.ospf.kotlin.utils.math.ordinary.*
import fuookami.ospf.kotlin.utils.operator.*
import fuookami.ospf.kotlin.utils.math.value_range.*

open class ValueRangeSerializer<T>(
    private val valueSerializer: ValueWrapperSerializer<T>
) : KSerializer<ValueRange<T>> where T : RealNumber<T>, T : NumberField<T> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ValueRange<T>") {
        element<JsonElement>("lowerBound")
        element<JsonElement>("upperBound")
        element<String>("lowerInterval")
        element<String>("upperInterval")
    }

    override fun serialize(encoder: Encoder, value: ValueRange<T>) {
        require(encoder is JsonEncoder)
        encoder.encodeJsonElement(
            buildJsonObject {
                put("lowerBound", encoder.json.encodeToJsonElement(valueSerializer, value.lowerBound))
                put("upperBound", encoder.json.encodeToJsonElement(valueSerializer, value.upperBound))
                put("lowerInterval", value.lowerInterval.toString().lowercase(Locale.getDefault()))
                put("upperInterval", value.upperInterval.toString().lowercase(Locale.getDefault()))
            }
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): ValueRange<T> {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        require(element is JsonObject)
        require(descriptor.elementNames.all { it in element })
        return ValueRange(
            decoder.json.decodeFromJsonElement(valueSerializer, element["lowerBound"]!!),
            decoder.json.decodeFromJsonElement(valueSerializer, element["upperBound"]!!),
            Interval.valueOf(element["lowerInterval"]!!.jsonPrimitive.content.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }),
            Interval.valueOf(element["upperInterval"]!!.jsonPrimitive.content.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }),
            valueSerializer.constants
        )
    }
}

data object ValueRangeInt64Serializer : ValueRangeSerializer<Int64>(ValueWrapperSerializer<Int64>())
data object ValueRangeUInt64Serializer : ValueRangeSerializer<UInt64>(ValueWrapperSerializer<UInt64>())
data object ValueRangeFlt64Serializer : ValueRangeSerializer<Flt64>(ValueWrapperSerializer<Flt64>())

// todo: Bound<T>

data class ValueRange<T>(
    private val _lowerBound: ValueWrapper<T>,
    private val _upperBound: ValueWrapper<T>,
    val lowerInterval: Interval,
    val upperInterval: Interval,
    private val constants: RealNumberConstants<T>
) : Cloneable, Copyable<ValueRange<T>>,
    Plus<ValueRange<T>, ValueRange<T>>, Minus<ValueRange<T>, ValueRange<T>>,
    Times<ValueRange<T>, ValueRange<T>>, Div<T, ValueRange<T>>, Eq<ValueRange<T>>
        where T : RealNumber<T>, T : NumberField<T> {

    companion object {
        @Suppress("UNCHECKED_CAST")
        inline operator fun <reified T> invoke(
            lowerBound: T,
            upperBound: T,
            lowerInterval: Interval = Interval.Closed,
            upperInterval: Interval = Interval.Closed
        ): ValueRange<T> where T : RealNumber<T>, T : NumberField<T> {
            val constants = (T::class.companionObjectInstance!! as RealNumberConstants<T>)
            return ValueRange(
                ValueWrapper.Value(lowerBound, constants),
                ValueWrapper.Value(upperBound, constants),
                lowerInterval,
                upperInterval,
                constants
            )
        }

        operator fun <T> invoke(
            lowerBound: T,
            upperBound: T,
            lowerInterval: Interval,
            upperInterval: Interval,
            constants: RealNumberConstants<T>
        ): ValueRange<T> where T : RealNumber<T>, T : NumberField<T> {
            return ValueRange(
                ValueWrapper.Value(lowerBound, constants),
                ValueWrapper.Value(upperBound, constants),
                lowerInterval,
                upperInterval,
                constants
            )
        }

        @Suppress("UNCHECKED_CAST")
        inline operator fun <reified T> invoke(
            _inf: GlobalNegativeInfinity,
            upperBound: T,
            upperInterval: Interval = Interval.Closed
        ): ValueRange<T> where T : RealNumber<T>, T : NumberField<T> {
            val constants = (T::class.companionObjectInstance!! as RealNumberConstants<T>)
            return ValueRange(
                ValueWrapper.NegativeInfinity(constants),
                ValueWrapper.Value(upperBound, constants),
                Interval.Open,
                upperInterval,
                constants
            )
        }

        operator fun <T> invoke(
            _inf: GlobalNegativeInfinity,
            upperBound: T,
            upperInterval: Interval,
            constants: RealNumberConstants<T>
        ): ValueRange<T> where T : RealNumber<T>, T : NumberField<T> {
            return ValueRange(
                ValueWrapper.NegativeInfinity(constants),
                ValueWrapper.Value(upperBound, constants),
                Interval.Open,
                upperInterval,
                constants
            )
        }

        @Suppress("UNCHECKED_CAST")
        inline operator fun <reified T> invoke(
            lowerBound: T,
            _inf: GlobalInfinity,
            lowerInterval: Interval = Interval.Closed
        ): ValueRange<T> where T : RealNumber<T>, T : NumberField<T> {
            val constants = (T::class.companionObjectInstance!! as RealNumberConstants<T>)
            return ValueRange(
                ValueWrapper.Value(lowerBound, constants),
                ValueWrapper.Infinity(constants),
                lowerInterval,
                Interval.Open,
                constants
            )
        }

        operator fun <T> invoke(
            lowerBound: T,
            _inf: GlobalInfinity,
            lowerInterval: Interval,
            constants: RealNumberConstants<T>
        ): ValueRange<T> where T : RealNumber<T>, T : NumberField<T> {
            return ValueRange(
                ValueWrapper.Value(lowerBound, constants),
                ValueWrapper.Infinity(constants),
                lowerInterval,
                Interval.Open,
                constants
            )
        }

        @Suppress("UNCHECKED_CAST")
        inline operator fun <reified T> invoke(): ValueRange<T> where T : RealNumber<T>, T : NumberField<T> {
            val constants = (T::class.companionObjectInstance!! as RealNumberConstants<T>)
            return ValueRange(
                ValueWrapper.NegativeInfinity(constants),
                ValueWrapper.Infinity(constants),
                Interval.Open,
                Interval.Open,
                constants
            )
        }

        operator fun <T> invoke(
            constants: RealNumberConstants<T>
        ): ValueRange<T> where T : RealNumber<T>, T : NumberField<T> {
            return ValueRange(
                ValueWrapper.NegativeInfinity(constants),
                ValueWrapper.Infinity(constants),
                Interval.Open,
                Interval.Open,
                constants
            )
        }
    }

    override fun copy() = ValueRange(
        _lowerBound,
        _upperBound,
        lowerInterval,
        upperInterval,
        constants
    )

    public override fun clone() = copy()

    val lowerBound: ValueWrapper<T>
        get() {
            @Throws(IllegalArgumentException::class)
            if (empty) {
                throw IllegalArgumentException("Illegal argument of value range: ${lowerInterval.lowerSign}${_lowerBound}, ${_upperBound}${upperInterval.upperSign}!!!")
            }
            return _lowerBound
        }
    val upperBound: ValueWrapper<T>
        get() {
            @Throws(IllegalArgumentException::class)
            if (empty) {
                throw IllegalArgumentException("Illegal argument of value range: ${lowerInterval.lowerSign}${_lowerBound}, ${_upperBound}${upperInterval.upperSign}!!!")
            }
            return _upperBound
        }
    val mean: ValueWrapper<T>
        get() {
            @Throws(IllegalArgumentException::class)
            if (empty) {
                throw IllegalArgumentException("Illegal argument of value range: ${lowerInterval.lowerSign}${_lowerBound}, ${_upperBound}${upperInterval.upperSign}!!!")
            }
            return (_lowerBound + _upperBound) / constants.two
        }

    val fixed by lazy {
        lowerInterval == Interval.Closed
                && upperInterval == Interval.Closed
                && !lowerBound.isInfinityOrNegativeInfinity
                && !upperBound.isInfinityOrNegativeInfinity
                && lowerBound eq upperBound
    }

    val empty by lazy {
        if (lowerInterval == Interval.Closed && upperInterval == Interval.Closed) {
            _lowerBound gr _upperBound
        } else {
            _lowerBound geq _upperBound
        }
    }

    val fixedValue: T? by lazy {
        if (fixed) {
            (lowerBound as ValueWrapper.Value).value
        } else {
            null
        }
    }

    operator fun contains(value: T): Boolean {
        val wrapper = ValueWrapper.Value(value, constants)
        return when (lowerInterval) {
            Interval.Open -> lowerBound ls wrapper
            Interval.Closed -> lowerBound leq wrapper
        } && when (upperInterval) {
            Interval.Open -> upperBound gr wrapper
            Interval.Closed -> upperBound geq wrapper
        }
    }

    operator fun contains(value: ValueRange<T>): Boolean {
        return when (lowerInterval) {
            Interval.Open -> lowerBound ls value.lowerBound
            Interval.Closed -> lowerBound leq value.lowerBound
        } && when (upperInterval) {
            Interval.Open -> upperBound gr value.upperBound
            Interval.Closed -> upperBound geq value.upperBound
        }
    }

    override fun plus(rhs: ValueRange<T>) = ValueRange(
        lowerBound + rhs.lowerBound,
        upperBound + rhs.upperBound,
        lowerInterval intersect rhs.lowerInterval,
        upperInterval intersect rhs.upperInterval,
        constants
    )

    operator fun plus(rhs: T) = ValueRange(
        lowerBound + rhs,
        upperBound + rhs,
        lowerInterval,
        upperInterval,
        constants
    )

    override fun minus(rhs: ValueRange<T>) = ValueRange(
        lowerBound - rhs.upperBound,
        upperBound - rhs.lowerBound,
        lowerInterval intersect rhs.lowerInterval,
        upperInterval intersect rhs.upperInterval,
        constants
    )

    operator fun minus(rhs: T) = ValueRange(
        lowerBound - rhs,
        upperBound - rhs,
        lowerInterval,
        upperInterval,
        constants
    )

    override fun times(rhs: ValueRange<T>): ValueRange<T> {
        val bounds = listOf(
            Pair(lowerBound * rhs.lowerBound, lowerInterval intersect rhs.lowerInterval),
            Pair(lowerBound * rhs.upperBound, lowerInterval intersect rhs.upperInterval),
            Pair(upperBound * rhs.lowerBound, upperInterval intersect rhs.lowerInterval),
            Pair(upperBound * rhs.upperBound, upperInterval intersect rhs.upperInterval)
        )
        val newLowerBound = bounds.minBy { it.first }
        val newUpperBound = bounds.maxBy { it.first }
        return ValueRange(
            newLowerBound.first,
            newUpperBound.first,
            newLowerBound.second,
            newUpperBound.second,
            constants
        )
    }

    operator fun times(rhs: T) = when {
        rhs < constants.zero -> ValueRange(
            upperBound * rhs,
            lowerBound * rhs,
            lowerInterval,
            upperInterval,
            constants
        )

        else -> ValueRange(
            lowerBound * rhs,
            upperBound * rhs,
            lowerInterval,
            upperInterval,
            constants
        )
    }

    override fun div(rhs: T) = when {
        rhs < constants.zero -> ValueRange(
            upperBound / rhs,
            lowerBound / rhs,
            lowerInterval,
            upperInterval,
            constants
        )

        else -> ValueRange(
            lowerBound / rhs,
            upperBound / rhs,
            lowerInterval,
            upperInterval,
            constants
        )
    }

    override fun partialEq(rhs: ValueRange<T>): Boolean {
        return lowerBound eq rhs.lowerBound
                && upperBound eq rhs.upperBound
                && lowerInterval == rhs.lowerInterval
                && upperInterval == rhs.upperInterval
    }

    infix fun intersect(rhs: ValueRange<T>) = ValueRange(
        max(lowerBound, rhs.lowerBound),
        min(upperBound, rhs.upperBound),
        lowerInterval intersect rhs.lowerInterval,
        upperInterval intersect rhs.upperInterval,
        constants
    )

    fun toFlt64() = ValueRange(
        lowerBound.toFlt64(),
        upperBound.toFlt64(),
        lowerInterval,
        upperInterval
    )

    override fun toString() = "${lowerInterval.lowerSign}$lowerBound, $upperBound${upperInterval.upperSign}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ValueRange<*>

        if (_lowerBound.toFlt64() neq other._lowerBound.toFlt64()) return false
        if (_upperBound.toFlt64() neq other._upperBound.toFlt64()) return false
        if (lowerInterval != other.lowerInterval) return false
        if (upperInterval != other.upperInterval) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _lowerBound.hashCode()
        result = 31 * result + _upperBound.hashCode()
        result = 31 * result + lowerInterval.hashCode()
        result = 31 * result + upperInterval.hashCode()
        return result
    }
}

operator fun <T> T.times(rhs: ValueRange<T>) where T : RealNumber<T>, T : NumberField<T> = rhs * this
