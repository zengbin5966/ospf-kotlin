package fuookami.ospf.kotlin.core.frontend.model.mechanism

import java.io.*
import java.nio.file.*
import kotlin.io.path.*
import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.error.*
import fuookami.ospf.kotlin.utils.functional.*
import fuookami.ospf.kotlin.core.frontend.variable.*
import fuookami.ospf.kotlin.core.frontend.expression.monomial.*
import fuookami.ospf.kotlin.core.frontend.expression.polynomial.*
import fuookami.ospf.kotlin.core.frontend.expression.symbol.*
import fuookami.ospf.kotlin.core.frontend.inequality.*
import fuookami.ospf.kotlin.core.frontend.model.*

sealed interface MetaModel<C : Category> : ModelInterface {
    class SubObject<C : Category>(
        val parent: MetaModel<C>,
        val category: ObjectCategory,
        val polynomial: Polynomial<C>,
        val name: String = polynomial.name
    ) {
        fun value(): Flt64 {
            return polynomial.value(parent.tokens)
        }

        fun value(results: List<Flt64>): Flt64 {
            return polynomial.value(results, parent.tokens)
        }
    }

    val name: String
    val constraints: MutableList<Inequality<C>>
    override val objectCategory: ObjectCategory
    val subObjects: MutableList<SubObject<C>>
    val tokens: TokenTable<C>

    override fun addVar(item: Item<*, *>) {
        tokens.add(item)
    }

    override fun addVars(items: Combination<*, *, *>) {
        tokens.add(items)
    }

    override fun addVars(items: CombinationView<*, *>) {
        tokens.add(items)
    }

    override fun remove(item: Item<*, *>) {
        tokens.remove(item)
    }

    fun addSymbol(symbol: Symbol<C>) {
        tokens.add(symbol)
    }

    fun addSymbols(symbols: SymbolCombination<C, *>) {
        tokens.add(symbols)
    }

    fun addSymbols(symbols: SymbolView<C>) {
        tokens.add(symbols)
    }

    @Suppress("UNCHECKED_CAST")
    override fun addConstraint(inequality: Inequality<*>, name: String?, displayName: String?) {
        inequality as Inequality<C>

        if (name != null) {
            inequality.name = name
        }
        if (displayName != null) {
            inequality.displayName = name
        }
        constraints.add(inequality)
    }

    fun registerConstraintGroup(name: String)
    fun indicesOfConstraintGroup(name: String): IntRange?

    @Suppress("UNCHECKED_CAST")
    override fun addObject(category: ObjectCategory, polynomial: Polynomial<*>, name: String?, displayName: String?) {
        polynomial as Polynomial<C>

        if (name != null) {
            polynomial.name = name
        }
        if (displayName != null) {
            polynomial.displayName = displayName
        }
        subObjects.add(SubObject(this, category, polynomial))
    }

    override fun setSolution(solution: Solution) {
        tokens.setSolution(solution)
    }

    override fun clearSolution() {
        tokens.clearSolution()
    }

    fun flush() {
        for (constraint in constraints) {
            constraint.lhs.flush()
            constraint.rhs.flush()
        }
        for (objective in subObjects) {
            objective.polynomial.flush()
        }
    }

    fun export(): Try<Error> {
        return export("$name.opm")
    }

    fun export(name: String): Try<Error> {
        return export(kotlin.io.path.Path(".").resolve(name))
    }

    fun export(path: Path): Try<Error> {
        val file = if (path.isDirectory()) {
            path.resolve("$name.opm").toFile()
        } else {
            path.toFile()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
        val writer = FileWriter(file)
        val result = when (file.extension) {
            "opm" -> {
                exportOpm(writer)
            }
            // todo: raise error with unknown format
            else -> {
                Ok(success)
            }
        }
        writer.flush()
        writer.close()
        return result
    }

    private fun exportOpm(writer: FileWriter): Try<Error> {
        writer.append("Model Name: $name\n")
        writer.append("\n")

        writer.append("Variables:\n")
        for (token in tokens.tokens.toList().sortedBy { it.solverIndex }) {
            val range = token.range
            writer.append("${token.name}, ${token.type}, ")
            if (range.empty()) {
                writer.append("empty\n")
            } else {
                writer.append("${range.lowerInterval.toLowerSign()}${range.lowerBound}, ${range.upperBound}${range.upperInterval.toUpperSign()}\n")
            }
        }
        writer.append("\n")

        writer.append("Symbols:\n")
        for (symbol in tokens.symbols.toList().sortedBy { it.name }) {
            val range = symbol.range
            writer.append("${symbol.name} = $symbol, ")
            if (range.empty()) {
                writer.append("empty")
            } else {
                writer.append("${range.lowerInterval.toLowerSign()}${range.lowerBound}, ${range.upperBound}${range.upperInterval.toUpperSign()}\n")
            }
        }
        writer.append("\n")

        writer.append("Objectives:\n")
        for (obj in subObjects) {
            writer.append("${obj.category} ${obj.name}: ${obj.polynomial} \n")
        }
        writer.append("\n")

        writer.append("Subject to:\n")
        for (constraint in constraints) {
            writer.append("${constraint.name}: $constraint\n")
        }
        writer.append("\n")

        return Ok(success)
    }
}

class LinearMetaModel(
    override var name: String = "",
    override val objectCategory: ObjectCategory = ObjectCategory.Minimum,
    manualTokenAddition: Boolean = true
) : MetaModel<Linear> {
    override val constraints: ArrayList<Inequality<Linear>> = ArrayList()
    override val subObjects: ArrayList<MetaModel.SubObject<Linear>> = ArrayList()
    override val tokens: TokenTable<Linear> = if (manualTokenAddition) {
        ManualAddTokenTable()
    } else {
        AutoAddTokenTable()
    }

    private var currentConstraintGroup: String? = null
    private var currentConstraintGroupIndexLowerBound: Int? = null
    private val constraintGroupIndexMap = HashMap<String, IntRange>()

    override fun registerConstraintGroup(name: String) {
        if (currentConstraintGroup != null) {
            assert(currentConstraintGroupIndexLowerBound != null)

            constraintGroupIndexMap[currentConstraintGroup!!] = currentConstraintGroupIndexLowerBound!! until constraints.size
        }
        currentConstraintGroup = name
        currentConstraintGroupIndexLowerBound = constraints.size
    }

    override fun indicesOfConstraintGroup(name: String): IntRange? {
        if (currentConstraintGroup != null) {
            assert(currentConstraintGroupIndexLowerBound != null)

            constraintGroupIndexMap[currentConstraintGroup!!] = currentConstraintGroupIndexLowerBound!! until constraints.size
            currentConstraintGroup = null
            currentConstraintGroupIndexLowerBound = null
        }
        return constraintGroupIndexMap[name]
    }
}
