package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.physics.dimension.*

data object Meter: Unit {
    override val name = "meter"
    override val symbol = "m"

    override val quantity = DerivedQuantity(FundamentalQuantityDimension.Length)
    override val system = SI
    override val scale = Scale()

    override fun toString(): String {
        return symbol
    }
}

data object Centimeter: Unit {
    override val name = "centimeter"
    override val symbol = "cm"

    override val quantity = DerivedQuantity(FundamentalQuantityDimension.Length)
    override val system = SI
    override val scale = Scale(Flt64(10.0), Flt64(-3.0))

    override fun toString(): String {
        return symbol
    }
}

data object Kilometer: Unit {
    override val name = "kilometer"
    override val symbol = "km"

    override val quantity = DerivedQuantity(FundamentalQuantityDimension.Length)
    override val system = SI
    override val scale = Scale(Flt64(10.0), Flt64(3.0))

    override fun toString(): String {
        return symbol
    }
}
