import kotlin.math.abs

const val DEBUG = false

typealias Coords = Pair<Float, Float>

data class FederalState(val name: String) {
    // data class Region(val startPoint: Coords, val edges: List<Coords>, val vertices: List<Coords>) {
    class Region() {
        var startPoint = Coords(0.0f, 0.0f)
        var edges: List<Coords> = listOf()
        var vertices: MutableList<Coords> = mutableListOf()

        constructor(rawCoordinates: List<String>): this() {
            val mRawCoordinates: MutableList<String> = rawCoordinates.toMutableList()

            /* start point */
            if (DEBUG) { println(" #" + mRawCoordinates.count()) }
            this.startPoint = if (mRawCoordinates.first().first() == 'M') {
                Region.coordsFromRaw(mRawCoordinates.removeAt(0))
            } else {
                throw IllegalArgumentException("Invalid starting point value")
            }
            if (DEBUG) { println(" #" + mRawCoordinates.count())}

            /* edges */
            this.edges = mRawCoordinates.mapIndexedNotNull { index, s ->
                // get absolute coordinates of predecessor, if index == 0 use startPoint
                val predecessor = if (index == 0) startPoint else vertices.elementAt(index -1)

                when (s.first()) {
                // "Line To with relative coordinates"
                    'l' -> {
                        val relCoords = Region.coordsFromRaw(s)
                        val absCoords = Coords(predecessor.first + relCoords.first, predecessor.second + relCoords.second)
                        this.vertices.add(absCoords)

                        relCoords
                    }
                // "Line To with absolute coordinates"
                    'L' -> {
                        val absCoords = Region.coordsFromRaw(s)
                        val relCoords = Coords(absCoords.first - predecessor.first, absCoords.second - predecessor.second)
                        this.vertices.add(absCoords)

                        relCoords

                    }
                // "Horizontal Line To with only single absolute coordinate"
                    'H' -> {
                        val horCoord = s.drop(1).toFloat()
                        val absCoords = Coords(horCoord, predecessor.second)
                        val relCoords = Coords(horCoord - predecessor.first, 0.0f)
                        this.vertices.add(absCoords)

                        relCoords
                    }
                // "Close Path"
                    'z' -> {
                        if (DEBUG) { println("Arrived at end point") }
                        null
                    }

                    else -> {
                        if (DEBUG) { print("This should never happen!") }
                        null
                    }
                }
            }

            // short check
            if (vertices.count() == edges.count()) {
                if (DEBUG) { println("vertices == edges: #${vertices.count()}") }
            } else {
                throw ArithmeticException("vertices != edges")
            }

        }

        fun volume(): Float {
            // Gauss's area formula, A = Summe[(yi + yiplus1)/2 * (xi - xiplus1)] for i=1....n
            var volume = 0.0f
            var predecessor = startPoint
            for ((index, vertex) in vertices.withIndex()) {
                volume += (predecessor.second + vertex.second)/2 * (-edges[index].first)
                predecessor = vertex
            }

            return volume
        }

        companion object {
            /* extract coords from string, regardless they are relative or absolute */
            fun coordsFromRaw(string: String): Coords {
                val split = string.drop(1).split(",")
                return if (split.count() == 2) {
                    Coords(split[0].toFloat(), split[1].toFloat())
                } else {
                    throw IllegalArgumentException("Invalid raw coordinate value, not 2 dimensional")
                }
            }
        }
    }

    var regions: ArrayList<Region> = ArrayList()

    constructor(name: String, concatenatedPath: String) : this(name) {
        if (DEBUG) { println("- $name") }
        val pathList = concatenatedPath.split(" ").filter { it != "" }
        val regionBounds: List<Pair<Int, Int>> = pathList.mapIndexedNotNull { index, s ->
            if (s.first() == 'M') {
                val tailIndex = pathList.subList(index, pathList.lastIndex + 1).indexOfFirst {
                    it.first() == 'z' }
                Pair(index, index + tailIndex + 1)
            } else {

                null
            }
        }

        for (bounds in regionBounds) {
            if (DEBUG) { println(" first: ${bounds.first}, tail: ${bounds.second}") }
            this.regions.add(Region(pathList.subList(bounds.first, bounds.second)))
        }
    }

    fun volume(): Float {
        var vol = 0.0f
        for (region in regions) {

            val regVol = region.volume()
            vol += abs(regVol)
            if (regVol < 0.0f) {
                //print("negative region \n")
            }
        }

        return vol * 1.1f
    }
//
//    constructor(name: String, concatenatedPath: String) : this(name) {
//        val pathList = concatenatedPath.split(" ").filter { it != "" }
//
//        // Find start point
//        val sPoint = if (pathList.first().first() == 'M') {
//            val coords = pathList.first().drop(1).split(",")
//            Pair(coords[0].toFloat(), coords[1].toFloat())
//        } else {
//            null
//        }
//
//        if (sPoint != null) {
//            startPoint = sPoint
//        }
//
//        /* Just for testing see few lines below
//
//        val a: Float = 312.023f
//        val t: Float = "-1.666".toFloat()
//        System.out.println(a-t)
//
//        var test: Float = startPoint!!.first
//
//        */
//
//        // Find edges
//        edges = pathList.mapNotNull { it ->
//            when (it.drop(1)) {
//            // "Line To with relative coordinates"
//                "l" -> {
//                    val coords = it.split(",")
//                    Pair(coords[0].toFloat(), coords[1].toFloat())
//                }
//            // "Line To with absolute coordinates"
//                "L" -> {
//
//                }
//
//            // "Close Path"
//                "z" -> {
//
//                }
//
//                else -> {
//                    print("This should never happen!")
//                    null
//                }
//            }
//            if (it.first() == 'l') {
//                val coords = it.drop(1).split(",")
//                /* MANUAL TESTING: Just for testing but Float test after adding a negative value, test has more than 3 decimals???
//
//                val t: Float = coords[0].toFloat()
//                System.out.println(t)
//                test = test + t
//
//                */
//
//                Pair(coords[0].toFloat(), coords[1].toFloat())
//
//            } else {
//                null
//            }
//        }
//
//        edges.let {
//            it.fold(startPoint, { last, next ->
//                val point = Pair(last.first + next.first, last.second + next.second)
//                this.vertices.add(point)
//                point
//            })
//        }
//
//        // letzter Punkt/letzte Edge fehlt noch da L ignoriert wird momentan
//        print(1)
//    }

//    fun volume(): Float {
//        // Gauss's area formula, A = Summe[(yi + yiplus1)/2 * (xi - xiplus1)] for i=1....n
//        var volume = 0.0f
//        var predecessor = startPoint
//        for ((index, vertice) in vertices.withIndex()) {
//            volume += (predecessor.second + vertice.second)/2 * edges[index].first
//            predecessor = vertice
//        }
//
//        return volume
//    }
}