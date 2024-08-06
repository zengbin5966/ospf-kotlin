package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.physics.dimension.*

data object MeterPerSecond : PhysicalUnit {
    private val unit = Meter / Second

    override val name: String = "meter per second"
    override val symbol: String = "m/s"

    override val quantity = Velocity
    override val system by unit::system
    override val scale by unit::scale

    override fun toString(): String {
        return symbol
    }
}

data object KilometerPerHour : PhysicalUnit {
    private val Unit = Kilometer / Hour

    override val name: String = "kilometer per hour"
    override val symbol: String = "km/h"

    override val quantity = Velocity
    override val system by Unit::system
    override val scale by Unit::scale

    override fun toString(): String {
        return symbol
    }
}
