package fuookami.ospf.kotlin.core.backend.plugins.scip

import jscip.*
import fuookami.ospf.kotlin.utils.functional.*
import fuookami.ospf.kotlin.core.backend.solver.output.*

abstract class ScipSolver {
    companion object {
        init {
            System.loadLibrary("jscip")
        }
    }

    lateinit var scip: Scip
    lateinit var status: SolvingStatus

    protected open fun finalize() {
        scip.free()
    }

    protected suspend fun init(name: String): Try {
        scip = Scip()
        scip.create(name)
        return ok
    }

    protected suspend fun analyzeStatus(): Try {
        val solution = scip.bestSol
        status = when (scip.status) {
            SCIP_Status.SCIP_STATUS_OPTIMAL -> {
                SolvingStatus.Optimal
            }

            SCIP_Status.SCIP_STATUS_INFEASIBLE -> {
                SolvingStatus.NoSolution
            }

            SCIP_Status.SCIP_STATUS_UNBOUNDED -> {
                SolvingStatus.Unbounded
            }

            SCIP_Status.SCIP_STATUS_INFORUNBD -> {
                SolvingStatus.SolvingException
            }

            else -> {
                if (solution != null) {
                    SolvingStatus.Feasible
                } else {
                    SolvingStatus.SolvingException
                }
            }
        }
        return ok
    }
}
