package fuookami.ospf.kotlin.framework.solver

import org.apache.logging.log4j.kotlin.*
import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.functional.*
import fuookami.ospf.kotlin.core.frontend.model.*
import fuookami.ospf.kotlin.core.frontend.model.mechanism.*
import fuookami.ospf.kotlin.core.backend.intermediate_model.*
import fuookami.ospf.kotlin.core.backend.solver.*
import fuookami.ospf.kotlin.core.backend.solver.output.*

class ParallelCombinatorialLinearSolver(
    private val solvers: List<Lazy<LinearSolver>>,
    private val mode: ParallelCombinatorialMode = ParallelCombinatorialMode.Best
): LinearSolver {
    private val logger = logger()

    constructor(solvers: Iterable<LinearSolver>): this(solvers.map { lazy { it } })
    constructor(solvers: Iterable<() -> LinearSolver>): this(solvers.map { lazy { it() } })

    override val name by lazy { "ParallelCombinatorial(${solvers.joinToString(",") { it.value.name }})" }

    override suspend fun invoke(model: LinearTriadModelView): Ret<SolverOutput> {
        TODO("Not yet implemented")
    }

    override suspend fun invoke(model: LinearTriadModelView, solutionAmount: UInt64): Ret<Pair<SolverOutput, List<Solution>>> {
        TODO("Not yet implemented")
    }
}
