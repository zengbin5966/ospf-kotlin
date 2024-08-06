package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.physics.dimension.*

data object Millimeter : PhysicalUnit {
    override val name = "millimeter"
    override val symbol = "mm"

    override val quantity = Length
    override val system = SI
    override val scale = Scale(Flt64.ten, -Flt64.three)

    override fun toString(): String {
        return symbol
    }
}

data object Centimeter : PhysicalUnit {
    override val name = "centimeter"
    override val symbol = "cm"

    override val quantity = Length
    override val system = SI
    override val scale = Scale(Flt64.ten, -Flt64.two)

    override fun toString(): String {
        return symbol
    }
}

data object Decimeter : PhysicalUnit {
    override val name = "decimeter"
    override val symbol = "dm"

    override val quantity = Length
    override val system = SI
    override val scale = Scale(Flt64.ten, -Flt64.one)

    override fun toString(): String {
        return symbol
    }
}

data object Meter : PhysicalUnit {
    override val name = "meter"
    override val symbol = "m"

    override val quantity = Length
    override val system = SI
    override val scale = Scale()

    override fun toString(): String {
        return symbol
    }
}

data object Kilometer : PhysicalUnit {
    override val name = "kilometer"
    override val symbol = "km"

    override val quantity = Length
    override val system = SI
    override val scale = Scale(Flt64.ten, Flt64.three)

    override fun toString(): String {
        return symbol
    }
}
