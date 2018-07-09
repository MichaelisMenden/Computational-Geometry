/*
 * CapitalCity: data class representing a city
 */

data class CapitalCity(val name: String, public val centerX: String, public val centerY: String) {
    public val center: Pair<Float, Float>
        get() = Pair(centerX.toFloat(), centerY.toFloat())
}