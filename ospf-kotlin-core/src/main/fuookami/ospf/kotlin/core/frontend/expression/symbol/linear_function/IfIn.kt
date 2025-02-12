package fuookami.ospf.kotlin.core.frontend.expression.symbol.linear_function

import org.apache.logging.log4j.kotlin.*
import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.math.symbol.*
import fuookami.ospf.kotlin.utils.math.value_range.*
import fuookami.ospf.kotlin.utils.functional.*
import fuookami.ospf.kotlin.core.frontend.variable.*
import fuookami.ospf.kotlin.core.frontend.expression.polynomial.*
import fuookami.ospf.kotlin.core.frontend.expression.symbol.*
import fuookami.ospf.kotlin.core.frontend.inequality.*
import fuookami.ospf.kotlin.core.frontend.model.mechanism.*

class IfInFunction(
    private val x: AbstractLinearPolynomial<*>,
    lowerBound: AbstractLinearPolynomial<*>,
    upperBound: AbstractLinearPolynomial<*>,
    override var name: String,
    override var displayName: String? = null
) : LinearLogicFunctionSymbol {
    private val logger = logger()

    private val lb = lowerBound
    private val ub = upperBound

    private val inequalities: List<LinearInequality>

    init {
        val lowerBoundInequality = (x geq lb).normalize()
        lowerBoundInequality.name = "${name}_lb"
        val upperBoundInequality = (x leq ub).normalize()
        upperBoundInequality.name = "${name}_ub"
        inequalities = listOf(lowerBoundInequality, upperBoundInequality)
    }

    private val y: BinVar by lazy {
        BinVar("${name}_y")
    }

    private val polyY: AbstractLinearPolynomial<*> by lazy {
        val polyY = LinearPolynomial(y)
        polyY.range.set(possibleRange)
        polyY
    }

    override val discrete = true

    override val range get() = polyY.range
    override val lowerBound get() = polyY.lowerBound
    override val upperBound get() = polyY.upperBound

    override val category = Linear

    override val dependencies: Set<IntermediateSymbol>
        get() {
            val dependencies = HashSet<IntermediateSymbol>()
            dependencies.addAll(x.dependencies)
            for (inequality in inequalities) {
                dependencies.addAll(inequality.lhs.dependencies)
                dependencies.addAll(inequality.rhs.dependencies)
            }
            return dependencies
        }
    override val cells get() = polyY.cells
    override val cached get() = polyY.cached

    private val possibleRange: ValueRange<Flt64>
        get() {
            // todo: impl by Inequality.judge()
            return ValueRange(Flt64.zero, Flt64.one).value!!
        }

    override fun flush(force: Boolean) {
        for (inequality in inequalities) {
            inequality.flush(force)
        }
        polyY.flush(force)
        polyY.range.set(possibleRange)
    }

    override fun prepare(tokenTable: AbstractTokenTable) {
        x.cells
        for (inequality in inequalities) {
            inequality.lhs.cells
            inequality.rhs.cells
        }

        if (tokenTable.cachedSolution && tokenTable.cached(this) == false) {
            val bins = inequalities.map { it.isTrue(tokenTable) }
            if (bins.all { it != null }) {
                val bin = bins.all { it == true }
                val yValue = if (bin) {
                    Flt64.one
                } else {
                    Flt64.zero
                }

                logger.trace { "Setting IfInFunction ${name}.y initial solution: $bin" }
                tokenTable.find(y)?.let { token ->
                    token._result = yValue
                }

                tokenTable.cache(this, null, yValue)
            }
        }
    }

    override fun register(tokenTable: AbstractMutableTokenTable): Try {
        when (val result = tokenTable.add(y)) {
            is Ok -> {}

            is Failed -> {
                return Failed(result.error)
            }
        }

        return ok
    }

    override fun register(model: AbstractLinearMechanismModel): Try {
        for (inequality in inequalities) {
            when (val result = inequality.register(name, y, model)) {
                is Ok -> {}

                is Failed -> {
                    return Failed(result.error)
                }
            }
        }

        return ok
    }

    override fun toString(): String {
        return displayName ?: name
    }

    override fun toRawString(unfold: Boolean): String {
        return "if_in(${x.toRawString(unfold)}, [${lb.toRawString(unfold)}, ${ub.toRawString(unfold)}])"
    }

    override fun evaluate(tokenList: AbstractTokenList, zeroIfNone: Boolean): Flt64? {
        val xValue = x.evaluate(tokenList, zeroIfNone) ?: return null
        val lbValue = lb.evaluate(tokenList, zeroIfNone) ?: return null
        val ubValue = ub.evaluate(tokenList, zeroIfNone) ?: return null
        return if (lbValue leq xValue && xValue leq ubValue) {
            Flt64.one
        } else {
            Flt64.zero
        }
    }

    override fun evaluate(results: List<Flt64>, tokenList: AbstractTokenList, zeroIfNone: Boolean): Flt64? {
        val xValue = x.evaluate(results, tokenList, zeroIfNone) ?: return null
        val lbValue = lb.evaluate(results, tokenList, zeroIfNone) ?: return null
        val ubValue = ub.evaluate(results, tokenList, zeroIfNone) ?: return null
        return if (lbValue leq xValue && xValue leq ubValue) {
            Flt64.one
        } else {
            Flt64.zero
        }
    }

    override fun calculateValue(tokenTable: AbstractTokenTable, zeroIfNone: Boolean): Flt64? {
        val xValue = x.evaluate(tokenTable, zeroIfNone) ?: return null
        val lbValue = lb.evaluate(tokenTable, zeroIfNone) ?: return null
        val ubValue = ub.evaluate(tokenTable, zeroIfNone) ?: return null
        return if (lbValue leq xValue && xValue leq ubValue) {
            Flt64.one
        } else {
            Flt64.zero
        }
    }

    override fun calculateValue(results: List<Flt64>, tokenTable: AbstractTokenTable, zeroIfNone: Boolean): Flt64? {
        val xValue = x.evaluate(results, tokenTable, zeroIfNone) ?: return null
        val lbValue = lb.evaluate(results, tokenTable, zeroIfNone) ?: return null
        val ubValue = ub.evaluate(results, tokenTable, zeroIfNone) ?: return null
        return if (lbValue leq xValue && xValue leq ubValue) {
            Flt64.one
        } else {
            Flt64.zero
        }
    }
}
