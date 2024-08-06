package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.physics.dimension.*

interface PhysicalUnit {
    val name: String?
    val symbol: String?

    val system: UnitSystem
    val quantity: DerivedQuantity
    val scale: Scale

    fun to(system: UnitSystem): PhysicalUnit {
        if (system == this.system) {
            return this
        } else {
            TODO("not implemented yet")
        }
    }
}

data class AnonymousPhysicalUnit(
    override val system: UnitSystem,
    override val quantity: DerivedQuantity,
    override val scale: Scale,
    override val name: String? = null,
    override val symbol: String? = null
) : PhysicalUnit {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnonymousPhysicalUnit

        if (system != other.system) return false
        if (quantity != other.quantity) return false
        if (scale != other.scale) return false

        return true
    }

    override fun hashCode(): Int {
        var result = system.hashCode()
        result = 31 * result + quantity.hashCode()
        result = 31 * result + scale.hashCode()
        return result
    }

    override fun toString(): String {
        return symbol ?: TODO("not implemented yet")
    }
}

operator fun PhysicalUnit.times(other: PhysicalUnit): PhysicalUnit {
    return if (this.system != other.system) {
        TODO("not implemented yet")
    } else if (this.quantity != other.quantity) {
        AnonymousPhysicalUnit(
            system = this.system,
            quantity = this.quantity * other.quantity,
            scale = this.scale * other.scale
        )
    } else {
        AnonymousPhysicalUnit(
            system = this.system,
            quantity = this.quantity,
            scale = this.scale * other.scale
        )
    }
}

operator fun PhysicalUnit.div(other: PhysicalUnit): PhysicalUnit {
    return if (this.system != other.system) {
        TODO("not implemented yet")
    } else if (this.quantity != other.quantity) {
        AnonymousPhysicalUnit(
            system = this.system,
            quantity = this.quantity / other.quantity,
            scale = this.scale / other.scale
        )
    } else {
        AnonymousPhysicalUnit(
            system = this.system,
            quantity = this.quantity,
            scale = this.scale / other.scale
        )
    }
}
