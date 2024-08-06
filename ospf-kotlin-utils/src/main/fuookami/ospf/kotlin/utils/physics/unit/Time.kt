package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.physics.dimension.*

data object Millisecond : PhysicalUnit {
    override val name: String = "millisecond"
    override val symbol: String = "ms"

    override val quantity = Time
    override val system = SI
    override val scale: Scale = Scale(Flt64.ten, -Flt64.three)

    override fun toString(): String {
        return symbol
    }
}

data object Second : PhysicalUnit {
    override val name: String = "second"
    override val symbol: String = "s"

    override val quantity = Time
    override val system = SI
    override val scale: Scale = Scale()

    override fun toString(): String {
        return symbol
    }
}

data object Minute : PhysicalUnit {
    override val name: String = "minute"
    override val symbol: String = "min"

    override val quantity = Time
    override val system = SI
    override val scale: Scale = Scale(Flt64(60.0), Flt64.one)

    override fun toString(): String {
        return symbol
    }
}

data object Hour : PhysicalUnit {
    override val name: String = "hour"
    override val symbol: String = "h"

    override val quantity = Time
    override val system = SI
    override val scale: Scale = Scale(Flt64(60.0), Flt64.two)

    override fun toString(): String {
        return symbol
    }
}

data object Day : PhysicalUnit {
    override val name: String = "day"
    override val symbol: String = "d"

    override val quantity = Time
    override val system = SI
    override val scale: Scale = Scale(
        listOf(
            Flt64(60.0) to Flt64.two,
            Flt64(24.0) to Flt64.one
        )
    )

    override fun toString(): String {
        return symbol
    }
}
