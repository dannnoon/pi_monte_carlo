package sample.algorithm

import javafx.geometry.Point2D

data class MonteCarloPoint(
        val point2D: Point2D,
        var isInside: Boolean = false
)