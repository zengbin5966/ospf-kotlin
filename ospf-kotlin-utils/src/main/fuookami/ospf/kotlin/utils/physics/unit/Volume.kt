package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.physics.dimension.*

data object CubicDecimeter : PhysicalUnit {
    private val unit = Decimeter * Decimeter * Decimeter

    override val name = "cubic decimeter"
    override val symbol = "dm^3"

    override val quantity = Volume
    override val system by unit::system
    override val scale by unit::scale

    override fun toString(): String {
        return symbol
    }
}

data object Liter : PhysicalUnit {
    private val unit = CubicDecimeter

    override val name = "liter"
    override val symbol = "l"

    override val quantity = Volume
    override val system by unit::system
    override val scale by unit::scale

    override fun toString(): String {
        return symbol
    }
}

data object CubicMeter : PhysicalUnit {
    private val unit = Meter * Meter * Meter

    override val name = "cubic meter"
    override val symbol = "m^3"

    override val quantity = Volume
    override val system by unit::system
    override val scale by unit::scale

    override fun toString(): String {
        return symbol
    }
}
