package fuookami.ospf.kotlin.framework.solver

import org.apache.logging.log4j.kotlin.*
import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.functional.*
import fuookami.ospf.kotlin.core.frontend.model.*
import fuookami.ospf.kotlin.core.frontend.model.mechanism.*
import fuookami.ospf.kotlin.core.backend.intermediate_model.*
import fuookami.ospf.kotlin.core.backend.solver.*
import fuookami.ospf.kotlin.core.backend.solver.output.*

class ParallelCombinatorialQuadraticSolver(
    private val solvers: List<Lazy<QuadraticSolver>>,
    private val mode: ParallelCombinatorialMode = ParallelCombinatorialMode.Best
): QuadraticSolver {
    private val logger = logger()

    constructor(solvers: Iterable<QuadraticSolver>): this(solvers.map { lazy { it } })
    constructor(solvers: Iterable<() -> QuadraticSolver>): this(solvers.map { lazy { it() } })

    override val name by lazy { "ParallelCombinatorial(${solvers.joinToString(",") { it.value.name }})" }

    override suspend fun invoke(model: QuadraticTetradModelView): Ret<SolverOutput> {
        TODO("Not yet implemented")
    }

    override suspend fun invoke(model: QuadraticTetradModelView, solutionAmount: UInt64): Ret<Pair<SolverOutput, List<Solution>>> {
        TODO("Not yet implemented")
    }
}
