import org.w3c.dom.Node

data class FederalState(val name: String) {
    var startPoint: Pair<Float, Float>? = null
    var edges: List<Pair<Float, Float>>? = null

    constructor(name: String, concatenatedPath: String) : this(name) {
        val pathList = concatenatedPath.split(" ").filter { it != "" }

        // Find start point
        startPoint = if (pathList.first().first() == 'M') {
            val coords = pathList.first().drop(1).split(",")
            Pair(coords[0].toFloat(), coords[1].toFloat())
        } else {
            null
        }

        /* Just for testing see few lines below

        val a: Float = 312.023f
        val t: Float = "-1.666".toFloat()
        System.out.println(a-t)

        var test: Float = startPoint!!.first

        */

        // Find edges
        edges = pathList.mapNotNull { it ->
            if (it.first() == 'l') {
                val coords = it.drop(1).split(",")

                /* MANUAL TESTING: Just for testing but Float test after adding a negative value, test has more than 3 decimals???

                val t: Float = coords[0].toFloat()
                System.out.println(t)
                test = test + t

                */

                Pair(coords[0].toFloat(), coords[1].toFloat())

            } else {
                null
            }
        }
    }

    fun volume() {

    }
}