package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.physics.dimension.*

data object GramPerSquareMeter : PhysicalUnit {
    private val unit = Gram / SquaredMeter

    override val name = "gram per square meter"
    override val symbol = "g/m^2"

    override val quantity = SurfaceDensity
    override val system by unit::system
    override val scale by unit::scale

    override fun toString(): String {
        return symbol
    }
}

data object KilogramPerSquareMeter : PhysicalUnit {
    private val unit = Kilogram / SquaredMeter

    override val name = "kilogram per square meter"
    override val symbol = "kg/m^2"

    override val quantity = SurfaceDensity
    override val system by unit::system
    override val scale by unit::scale

    override fun toString(): String {
        return symbol
    }
}
