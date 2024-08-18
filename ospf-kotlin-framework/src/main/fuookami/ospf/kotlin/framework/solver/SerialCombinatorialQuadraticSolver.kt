package fuookami.ospf.kotlin.framework.solver

import org.apache.logging.log4j.kotlin.*
import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.error.*
import fuookami.ospf.kotlin.utils.functional.*
import fuookami.ospf.kotlin.core.frontend.model.*
import fuookami.ospf.kotlin.core.backend.intermediate_model.*
import fuookami.ospf.kotlin.core.backend.solver.*
import fuookami.ospf.kotlin.core.backend.solver.output.*

class SerialCombinatorialQuadraticSolver(
    private val solvers: List<Lazy<QuadraticSolver>>,
    private val stopErrorCode: Set<ErrorCode> = setOf(ErrorCode.ORModelNoSolution, ErrorCode.ORModelUnbounded)
): QuadraticSolver {
    private val logger = logger()

    constructor(solvers: Iterable<QuadraticSolver>): this(solvers.map { lazy { it } })
    constructor(solvers: Iterable<() -> QuadraticSolver>): this(solvers.map { lazy { it() } })

    override val name: String by lazy { "SerialCombinatorial(${solvers.joinToString(",") { it.value.name }})" }

    override suspend operator fun invoke(model: QuadraticTetradModelView): Ret<SolverOutput> {
        for (solver in solvers) {
            when (val result = solver.value.invoke(model)) {
                is Ok -> {
                    return Ok(result.value)
                }

                is Failed -> {
                    if (stopErrorCode.contains(result.error.code)) {
                        return Failed(result.error.code, result.error.message)
                    } else {
                        logger.warn { "Solver ${solver.value.name} failed with error ${result.error.code}: ${result.error.message}" }
                    }
                }
            }
        }
        return Failed(ErrorCode.SolverNotFound, "No solver valid.")
    }

    override suspend operator fun invoke(model: QuadraticTetradModelView, solutionAmount: UInt64): Ret<Pair<SolverOutput, List<Solution>>> {
        for (solver in solvers) {
            when (val result = solver.value.invoke(model, solutionAmount)) {
                is Ok -> {
                    return Ok(result.value)
                }

                is Failed -> {
                    if (stopErrorCode.contains(result.error.code)) {
                        return Failed(result.error.code, result.error.message)
                    } else {
                        logger.warn { "Solver ${solver.value.name} failed with error ${result.error.code}: ${result.error.message}" }
                    }
                }
            }
        }
        return Failed(ErrorCode.SolverNotFound, "No solver valid.")
    }
}
