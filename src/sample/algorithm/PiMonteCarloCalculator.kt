package sample.algorithm

import javafx.geometry.Point2D
import sample.model.MonteCarloPoint

class PiMonteCarloCalculator(private var samples: Int) : PiCalculator {

    companion object {

        private const val RANGE = 1.0
    }

    var pointListener: MonteCarloPointListener? = null

    override fun calculate(): Double {
        var insidePoints = 0

        (0 until samples).forEach {
            val point = createRandomPoint()

            if (isInsideOfCircle(point.point2D)) {
                insidePoints++
                point.isInside = true
            }

            pointListener?.onPointCreated(point)
        }

        return countWithMonteCarloMethod(insidePoints)
    }

    private fun createRandomPoint() = MonteCarloPoint(Point2D(Math.random(), Math.random()))

    private fun isInsideOfCircle(it: Point2D) =
            Math.pow(it.x - RANGE / 2, 2.0) + Math.pow(it.y - RANGE / 2, 2.0) <= Math.pow(RANGE / 2, 2.0)

    private fun countWithMonteCarloMethod(insidePoints: Int) = 4.0 * insidePoints / samples
}

