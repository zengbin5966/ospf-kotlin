package fuookami.ospf.kotlin.utils.physics.dimension

// L^2
val Area = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 2)
    ), "area"
)

// L t^-2
val Acceleration = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 1),
        FundamentalQuantity(FundamentalQuantityDimension.Time, -2)
    ), "acceleration"
)

// L^2 m t^-2
val Energy = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 1),
        FundamentalQuantity(FundamentalQuantityDimension.Mass, 2),
        FundamentalQuantity(FundamentalQuantityDimension.Time, -2)
    ), "energy"
)

// L m t^-2
val Force = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 1),
        FundamentalQuantity(FundamentalQuantityDimension.Mass, 1),
        FundamentalQuantity(FundamentalQuantityDimension.Time, -2)
    ), "force"
)

// L^3 t^-1
val FlowVelocity = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 3),
        FundamentalQuantity(FundamentalQuantityDimension.Time, -1)
    ), "flow velocity"
)

// L
val Length = DerivedQuantity(FundamentalQuantityDimension.Length, "length")

// m
val Mass = DerivedQuantity(FundamentalQuantityDimension.Mass, "mass")

// L^-3 m
val MassDensity = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, -3),
        FundamentalQuantity(FundamentalQuantityDimension.Mass, 1)
    ), "mass density"
)

// L^2 m t^-3
val Power = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 2),
        FundamentalQuantity(FundamentalQuantityDimension.Mass, 1),
        FundamentalQuantity(FundamentalQuantityDimension.Time, -3)
    ), "power"
)

// L^-2 m
val SurfaceDensity = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, -2),
        FundamentalQuantity(FundamentalQuantityDimension.Mass, 1)
    ), "surface density"
)

// t
val Time = DerivedQuantity(FundamentalQuantityDimension.Time, "time")

// L^1 t^-1
val Velocity = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 1),
        FundamentalQuantity(FundamentalQuantityDimension.Time, -1)
    ), "velocity"
)

// L^3
val Volume = DerivedQuantity(
    listOf(
        FundamentalQuantity(FundamentalQuantityDimension.Length, 3)
    ), "volume"
)
