package fuookami.ospf.kotlin.core.frontend.expression.symbol.quadratic_function

import org.apache.logging.log4j.kotlin.*
import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.math.ordinary.*
import fuookami.ospf.kotlin.utils.functional.*
import fuookami.ospf.kotlin.core.frontend.variable.*
import fuookami.ospf.kotlin.core.frontend.expression.monomial.*
import fuookami.ospf.kotlin.core.frontend.expression.polynomial.*
import fuookami.ospf.kotlin.core.frontend.expression.symbol.*
import fuookami.ospf.kotlin.core.frontend.inequality.*
import fuookami.ospf.kotlin.core.frontend.model.mechanism.*

class IntDivFunction(
    private val x: AbstractQuadraticPolynomial<*>,
    private val d: AbstractQuadraticPolynomial<*>,
    override var name: String = "${x}_intDiv_${d}",
    override var displayName: String? = "$x intDiv $d"
) : QuadraticFunctionSymbol {
    private val logger = logger()

    private val dLinear: LinearFunction by lazy {
        LinearFunction(d, "${name}_d")
    }

    private val q: IntVar by lazy {
        val q = IntVar("${name}_q")
        q.range.set(
            ValueRange(
                possibleRange.lowerBound.unwrap().toInt64(),
                possibleRange.upperBound.unwrap().toInt64()
            )
        )
        q
    }

    private val r: URealVar by lazy {
        val r = URealVar("${name}_r")
        r.range.leq(possibleModUpperBound)
        r
    }

    private val y: AbstractQuadraticPolynomial<*> by lazy {
        val y = QuadraticPolynomial(r, "${name}_y")
        y.range.set(possibleRange)
        y
    }

    private val possibleRange: ValueRange<Flt64>
        get() {
            return if (d.range.range.contains(Flt64.zero)) {
                ValueRange(
                    (x.upperBound / d.lowerBound).floor(),
                    Flt64.maximum
                )
            } else {
                val q1 = (x.upperBound / d.upperBound).floor()
                val q2 = (x.upperBound / d.lowerBound).floor()
                val q3 = (x.lowerBound / d.upperBound).floor()
                val q4 = (x.lowerBound / d.lowerBound).floor()
                ValueRange(min(q1, q2, q3, q4), max(q1, q2, q3, q4))
            }
        }

    private val possibleModUpperBound
        get() = max(
            if (d.upperBound geq Flt64.zero) {
                d.upperBound.floor()
            } else {
                d.upperBound.ceil().abs()
            },
            if (d.lowerBound geq Flt64.zero) {
                d.upperBound.floor()
            } else {
                d.lowerBound.ceil().abs()
            }
        )

    override val discrete by lazy {
        x.discrete && d.discrete
    }

    override val range get() = y.range
    override val lowerBound get() = y.lowerBound
    override val upperBound get() = y.upperBound

    override val category: Category = Linear

    override val dependencies get() = x.dependencies + d.dependencies
    override val cells get() = y.cells
    override val cached get() = y.cached

    override fun flush(force: Boolean) {
        x.flush(force)
        d.flush(force)
        dLinear.flush(force)
        y.flush(force)
        q.range.set(ValueRange(possibleRange.lowerBound.unwrap().toInt64(), possibleRange.upperBound.unwrap().toInt64()))
        r.range.set(ValueRange(Flt64.zero, possibleModUpperBound))
        y.range.set(possibleRange)
    }

    override suspend fun prepare(tokenTable: AbstractTokenTable) {
        x.cells
        d.cells

        if (tokenTable.cachedSolution && tokenTable.cached(this) == false) {
            x.value(tokenTable)?.let { xValue ->
                d.value(tokenTable)?.let { dValue ->
                    val qValue = (xValue / dValue).let {
                        if (it geq Flt64.zero) {
                            it.floor()
                        } else {
                            it.ceil()
                        }
                    }
                    logger.trace { "Setting ModFunction ${name}.q initial solution: $qValue" }
                    tokenTable.find(q)?.let { token ->
                        token._result = qValue
                    }
                    val rValue = xValue - dValue * qValue
                    logger.trace { "Setting ModFunction ${name}.r initial solution: $rValue" }
                    tokenTable.find(r)?.let { token ->
                        token._result = rValue
                    }

                    tokenTable.cache(this, null, qValue)
                }
            }
        }
    }

    override fun register(tokenTable: MutableTokenTable): Try {
        when (val result = tokenTable.add(dLinear)) {
            is Ok -> {}

            is Failed -> {
                return Failed(result.error)
            }
        }

        when (val result = tokenTable.add(q)) {
            is Ok -> {}

            is Failed -> {
                return Failed(result.error)
            }
        }

        when (val result = tokenTable.add(r)) {
            is Ok -> {}

            is Failed -> {
                return Failed(result.error)
            }
        }

        return ok
    }

    override fun register(model: AbstractQuadraticMechanismModel): Try {
        when (val result = dLinear.register(model)) {
            is Ok -> {}

            is Failed -> {
                return Failed(result.error)
            }
        }

        when (val result = model.addConstraint(
            x eq (dLinear * q + r),
            name = name
        )) {
            is Ok -> {}

            is Failed -> {
                return Failed(result.error)
            }
        }

        return ok
    }

    override fun toString(): String {
        return displayName ?: name
    }

    override fun toRawString(unfold: Boolean): String {
        return "${x.toRawString(unfold)} mod $d"
    }

    override fun value(tokenList: AbstractTokenList, zeroIfNone: Boolean): Flt64? {
        return x.value(tokenList, zeroIfNone)?.let { xValue ->
            d.value(tokenList, zeroIfNone)?.let { dValue ->
                xValue % dValue
            }
        }
    }

    override fun value(results: List<Flt64>, tokenList: AbstractTokenList, zeroIfNone: Boolean): Flt64? {
        return x.value(results, tokenList, zeroIfNone)?.let { xValue ->
            d.value(results, tokenList, zeroIfNone)?.let { dValue ->
                xValue % dValue
            }
        }
    }

    override fun calculateValue(tokenTable: AbstractTokenTable, zeroIfNone: Boolean): Flt64? {
        return x.value(tokenTable, zeroIfNone)?.let { xValue ->
            d.value(tokenTable, zeroIfNone)?.let { dValue ->
                xValue % dValue
            }
        }
    }

    override fun calculateValue(results: List<Flt64>, tokenTable: AbstractTokenTable, zeroIfNone: Boolean): Flt64? {
        return x.value(results, tokenTable, zeroIfNone)?.let { xValue ->
            d.value(results, tokenTable, zeroIfNone)?.let { dValue ->
                xValue % dValue
            }
        }
    }
}
