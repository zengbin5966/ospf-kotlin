package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.physics.dimension.*

data object KilogramPerLiter : PhysicalUnit {
    private val unit = Kilogram / Liter

    override val name = "kilogram per liter"
    override val symbol = "kg/L"

    override val quantity = MassDensity
    override val system by unit::system
    override val scale by unit::scale

    override fun toString(): String {
        return symbol
    }
}

data object KilogramPerCubicMeter : PhysicalUnit {
    private val unit = Kilogram / CubicMeter

    override val name = "kilogram per cubic meter"
    override val symbol = "kg/m^3"

    override val quantity = MassDensity
    override val system by unit::system
    override val scale by unit::scale

    override fun toString(): String {
        return symbol
    }
}
