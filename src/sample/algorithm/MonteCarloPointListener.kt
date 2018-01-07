package sample.algorithm

import sample.model.MonteCarloPoint

interface MonteCarloPointListener {

    fun onPointCreated(point: MonteCarloPoint)
}