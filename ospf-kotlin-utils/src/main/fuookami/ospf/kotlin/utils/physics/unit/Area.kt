package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.physics.dimension.*

data object SquaredMeter : PhysicalUnit {
    private val unit = Meter * Meter

    override val name = "square meter"
    override val symbol = "m^2"

    override val quantity = Area
    override val system by unit::system
    override val scale by unit::scale

    override fun toString(): String {
        return symbol
    }
}
