package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.physics.dimension.*

data object CubicMeterPerSecond : PhysicalUnit() {
    private val unit = CubicMeter / Second

    override val name: String = "cubic meter per second"
    override val symbol: String = "m3ps"

    override val quantity = FlowVelocity
    override val system by unit::system
    override val scale by unit::scale
}

data object LiterPerSecond : PhysicalUnit() {
    private val unit = Liter / Second

    override val name: String = "liter per second"
    override val symbol: String = "lps"

    override val quantity = FlowVelocity
    override val system by unit::system
    override val scale by unit::scale
}
