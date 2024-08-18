package fuookami.ospf.kotlin.framework.solver

import org.apache.logging.log4j.kotlin.*
import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.error.*
import fuookami.ospf.kotlin.utils.functional.*
import fuookami.ospf.kotlin.core.frontend.model.*
import fuookami.ospf.kotlin.core.backend.intermediate_model.*
import fuookami.ospf.kotlin.core.backend.solver.*
import fuookami.ospf.kotlin.core.backend.solver.output.*
import fuookami.ospf.kotlin.core.frontend.model.mechanism.LinearMetaModel

class SerialCombinatorialColumnGenerationSolver(
    private val solvers: List<Lazy<ColumnGenerationSolver>>,
    private val stopErrorCode: Set<ErrorCode> = setOf(ErrorCode.ORModelNoSolution, ErrorCode.ORModelUnbounded)
): ColumnGenerationSolver {
    private val logger = logger()

    constructor(solvers: Iterable<ColumnGenerationSolver>): this(solvers.map { lazy { it } })
    constructor(solvers: Iterable<() -> ColumnGenerationSolver>): this(solvers.map { lazy { it() } })

    override val name: String by lazy { "SerialCombinatorial(${solvers.joinToString(",") { it.value.name }})" }

    override suspend fun solveMILP(
        name: String,
        metaModel: LinearMetaModel,
        toLogModel: Boolean
    ): Ret<SolverOutput> {
        TODO("Not yet implemented")
    }

    override suspend fun solveLP(
        name: String,
        metaModel: LinearMetaModel,
        toLogModel: Boolean
    ): Ret<ColumnGenerationSolver.LPResult> {
        TODO("Not yet implemented")
    }
}
