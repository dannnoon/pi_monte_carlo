package sample.model

data class PiResult(
        val value: Double,
        private val samples: Int
) {
    override fun toString(): String {
        return "PI value: $value, samples: $samples"
    }
}