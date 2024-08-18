package fuookami.ospf.kotlin.framework.solver

import kotlinx.coroutines.*
import org.apache.logging.log4j.kotlin.*
import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.error.*
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

    companion object {
        @JvmName("constructBySolvers")
        operator fun invoke(
            solvers: Iterable<QuadraticSolver>,
            mode: ParallelCombinatorialMode = ParallelCombinatorialMode.Best
        ): ParallelCombinatorialQuadraticSolver {
            return ParallelCombinatorialQuadraticSolver(solvers.map { lazy { it } }, mode)
        }

        @JvmName("constructBySolverExtractors")
        operator fun invoke(
            solvers: Iterable<() -> QuadraticSolver>,
            mode: ParallelCombinatorialMode = ParallelCombinatorialMode.Best
        ): ParallelCombinatorialQuadraticSolver {
            return ParallelCombinatorialQuadraticSolver(solvers.map { lazy { it() } }, mode)
        }
    }

    override val name by lazy { "ParallelCombinatorial(${solvers.joinToString(",") { it.value.name }})" }

    override suspend fun invoke(model: QuadraticTetradModelView): Ret<SolverOutput> {
        return when (mode) {
            ParallelCombinatorialMode.First -> {
                var result: SolverOutput? = null
                val lock = Any()
                try {
                    coroutineScope {
                        val promises = solvers.map {
                            launch(Dispatchers.Default) {
                                when (val ret = it.value.invoke(model)) {
                                    is Ok -> {
                                        logger.info { "Solver ${it.value.name} found a solution." }
                                        synchronized(lock) {
                                            result = ret.value
                                            cancel()
                                        }
                                    }

                                    is Failed -> {
                                        logger.warn { "Solver ${it.value.name} failed with error ${ret.error.code}: ${ret.error.message}" }
                                    }
                                }
                            }
                        }
                        promises.forEach { it.join() }
                        if (result != null) {
                            Ok(result!!)
                        } else {
                            Failed(ErrorCode.SolverNotFound, "No solver valid.")
                        }
                    }
                } catch (e: Exception) {
                    if (result != null) {
                        Ok(result!!)
                    } else {
                        Failed(ErrorCode.OREngineSolvingException)
                    }
                }
            }

            ParallelCombinatorialMode.Best -> {
                coroutineScope {
                    val promises = solvers.map {
                        async(Dispatchers.Default) {
                            val result = it.value.invoke(model)
                            when (result) {
                                is Ok -> {
                                    logger.info { "Solver ${it.value.name} found a solution." }
                                }

                                is Failed -> {
                                    logger.warn { "Solver ${it.value.name} failed with error ${result.error.code}: ${result.error.message}" }
                                }
                            }
                            result
                        }
                    }
                    val results = promises.map { it.await() }
                    val successResults = results.mapNotNull {
                        when (it) {
                            is Ok -> {
                                it.value
                            }

                            is Failed -> {
                                null
                            }
                        }
                    }
                    if (successResults.isNotEmpty()) {
                        val bestResult = when (model.objective.category) {
                            ObjectCategory.Minimum -> {
                                successResults.minBy { it.obj }
                            }

                            ObjectCategory.Maximum -> {
                                successResults.maxBy { it.obj }
                            }
                        }
                        Ok(bestResult)
                    } else {
                        Failed(ErrorCode.SolverNotFound, "No solver valid.")
                    }
                }
            }
        }
    }

    override suspend fun invoke(model: QuadraticTetradModelView, solutionAmount: UInt64): Ret<Pair<SolverOutput, List<Solution>>> {
        return when (mode) {
            ParallelCombinatorialMode.First -> {
                var result: Pair<SolverOutput, List<Solution>>? = null
                val lock = Any()
                try {
                    coroutineScope {
                        val promises = solvers.map {
                            launch(Dispatchers.Default) {
                                when (val ret = it.value.invoke(model, solutionAmount)) {
                                    is Ok -> {
                                        logger.info { "Solver ${it.value.name} found a solution." }
                                        synchronized(lock) {
                                            result = ret.value
                                            cancel()
                                        }
                                    }

                                    is Failed -> {
                                        logger.warn { "Solver ${it.value.name} failed with error ${ret.error.code}: ${ret.error.message}" }
                                    }
                                }
                            }
                        }
                        promises.forEach { it.join() }
                        if (result != null) {
                            Ok(result!!)
                        } else {
                            Failed(ErrorCode.SolverNotFound, "No solver valid.")
                        }
                    }
                } catch (e: Exception) {
                    if (result != null) {
                        Ok(result!!)
                    } else {
                        Failed(ErrorCode.OREngineSolvingException)
                    }
                }
            }

            ParallelCombinatorialMode.Best -> {
                coroutineScope {
                    val promises = solvers.map {
                        async(Dispatchers.Default) {
                            val result = it.value.invoke(model, solutionAmount)
                            when (result) {
                                is Ok -> {
                                    logger.info { "Solver ${it.value.name} found a solution." }
                                }

                                is Failed -> {
                                    logger.warn { "Solver ${it.value.name} failed with error ${result.error.code}: ${result.error.message}" }
                                }
                            }
                            result
                        }
                    }
                    val results = promises.map { it.await() }
                    val successResults = results.mapNotNull {
                        when (it) {
                            is Ok -> {
                                it.value
                            }

                            is Failed -> {
                                null
                            }
                        }
                    }
                    if (successResults.isNotEmpty()) {
                        val bestResult = when (model.objective.category) {
                            ObjectCategory.Minimum -> {
                                successResults.minBy { it.first.obj }
                            }

                            ObjectCategory.Maximum -> {
                                successResults.maxBy { it.first.obj }
                            }
                        }
                        Ok(bestResult)
                    } else {
                        Failed(ErrorCode.SolverNotFound, "No solver valid.")
                    }
                }
            }
        }
    }
}
