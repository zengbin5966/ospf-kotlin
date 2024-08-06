package fuookami.ospf.kotlin.utils.physics.dimension

// L^2
val Area = AnonymousDerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 2)
    ), "area"
)

// L^3 t^-1
val FlowVelocity = AnonymousDerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 3),
        FundamentalQuantity(FundamentalQuantityDimension.Time, -1)
    ), "flow velocity"
)

// L
val Length = AnonymousDerivedQuantity(FundamentalQuantityDimension.Length, "length")

// m
val Mass = AnonymousDerivedQuantity(FundamentalQuantityDimension.Mass, "mass")

// L^-3 m
val MassDensity = AnonymousDerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, -3),
        FundamentalQuantity(FundamentalQuantityDimension.Mass, 1)
    ), "mass density"
)

// L^-2 m
val SurfaceDensity = AnonymousDerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, -2),
        FundamentalQuantity(FundamentalQuantityDimension.Mass, 1)
    ), "surface density"
)

// t
val Time = AnonymousDerivedQuantity(FundamentalQuantityDimension.Time, "time")

// L^1 t^-1
val Velocity = AnonymousDerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 1),
        FundamentalQuantity(FundamentalQuantityDimension.Time, -1)
    ), "velocity"
)

// L^3
val Volume = AnonymousDerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 3)
    ), "volume"
)
