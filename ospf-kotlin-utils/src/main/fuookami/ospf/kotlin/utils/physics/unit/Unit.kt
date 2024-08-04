package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.physics.dimension.*

interface Unit {
    val name: String?
    val symbol: String?

    val system: UnitSystem
    val quantity: DerivedQuantity
    val scale: Scale

    fun to(system: UnitSystem): Unit {
        if (system == this.system) {
            return this
        } else {
            TODO("not implemented yet")
        }
    }
}

data class AnonymousUnit(
    override val system: UnitSystem,
    override val quantity: DerivedQuantity,
    override val scale: Scale,
    override val name: String?,
    override val symbol: String?
) : Unit
