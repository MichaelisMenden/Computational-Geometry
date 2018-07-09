import java.awt.geom.Point2D
import kotlin.math.abs
import kotlin.math.absoluteValue

/*
 * @Author: Michael Wimmer, Patrick Burger
 *
 * Federal State data class & CapitalCity data class
 */

const val DEBUG = false

// typealias for better handling coordinate pairs
public typealias Coords = Pair<Float, Float>

/*
 * Federal State data class representing a state
 */
data class FederalState(val name: String) {

    /*
     * Internal Region class representing separated areas of a state
     */
    class Region() {
        var startPoint = Coords(0.0f, 0.0f)
        var edges: List<Coords> = listOf()
        var vertices: MutableList<Coords> = mutableListOf()

        // convert vertices to List<Strecken>
        val strecken: List<Strecke>
            get() = listOf(mutableListOf<Coords>(startPoint), vertices).flatten().mapIndexedNotNull{ id, coords ->
                if (id <= vertices.lastIndex) {
                    val next = vertices.elementAt(id)
                    Strecke(coords.first.toDouble(), coords.second.toDouble(), next.first.toDouble(), next.second.toDouble())
                } else {
                    null
                }
            }

        // get maximum x-coordinate in region
        val maxX: Float
            get() = vertices.map { it.first }.max()!!

        // get maximum y-coordinate in region
        val maxY: Float
            get() = vertices.map { it.second }.max()!!

        constructor(rawCoordinates: List<String>) : this() {
            val mRawCoordinates: MutableList<String> = rawCoordinates.toMutableList()

            /* start point */
            if (DEBUG) {
                println(" #" + mRawCoordinates.count())
            }
            this.startPoint = if (mRawCoordinates.first().first() == 'M') {
                Region.coordsFromRaw(mRawCoordinates.removeAt(0))
            } else {
                throw IllegalArgumentException("Invalid starting point value")
            }
            if (DEBUG) {
                println(" #" + mRawCoordinates.count())
            }

            /* edges */
            this.edges = mRawCoordinates.mapIndexedNotNull { index, s ->
                // get absolute coordinates of predecessor, if index == 0 use startPoint
                val predecessor = if (index == 0) startPoint else vertices.elementAt(index - 1)

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
                        if (DEBUG) {
                            println("Arrived at end point")
                        }
                        null
                    }

                    else -> {
                        if (DEBUG) {
                            print("This should never happen!")
                        }
                        null
                    }
                }
            }

            // short check
            if (vertices.count() == edges.count()) {
                if (DEBUG) {
                    println("vertices == edges: #${vertices.count()}")
                }
            } else {
                throw ArithmeticException("vertices != edges")
            }



        }

        // get volume of region
        fun volume(): Float {
            // Gauss's area formula, A = Summe[(yi + yiplus1)/2 * (xi - xiplus1)] for i=1....n
            var volume = 0.0f
            var predecessor = startPoint
            for ((index, vertex) in vertices.withIndex()) {
                volume += (predecessor.second + vertex.second) / 2 * (-edges[index].first)
                predecessor = vertex
            }

            return volume
        }

        // check if point is in region
        fun pointInRegion(point: Coords): Boolean {
            val outOfRegion = Coords(maxX+1, maxY+1)
            val schnittStrecke = Strecke(point.first.toDouble(), point.second.toDouble(), outOfRegion.first.toDouble(), outOfRegion.second.toDouble())
            var s = 0
            val iSEC = IntersectingEdgeChecker()

            for (strecke in strecken) {
                val intersect = iSEC.doIntersect(strecke, schnittStrecke)
                when (intersect) {
                    1 -> s++
                }
            }

            val isInRegion = s % 2 != 0

            return isInRegion
        }

        fun isCCW(): Boolean {
            val iSEC = IntersectingEdgeChecker()
            val p1 = Point2D.Double(startPoint.first.toDouble(), startPoint.second.toDouble())
            val p2 = Point2D.Double(vertices.get(0).first.toDouble(), vertices.get(0).second.toDouble())
            val p3 = Point2D.Double(vertices.get(1).first.toDouble(), vertices.get(1).second.toDouble())

            return iSEC.ccw(p1, p2, p3) < 0
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
        if (DEBUG) {
            println("- $name")
        }
        val pathList = concatenatedPath.split(" ").filter { it != "" }
        val regionBounds: List<Pair<Int, Int>> = pathList.mapIndexedNotNull { index, s ->
            if (s.first() == 'M') {
                val tailIndex = pathList.subList(index, pathList.lastIndex + 1).indexOfFirst {
                    it.first() == 'z'
                }
                Pair(index, index + tailIndex + 1)
            } else {

                null
            }
        }

        for (bounds in regionBounds) {
            if (DEBUG) {
                println(" first: ${bounds.first}, tail: ${bounds.second}")
            }
            this.regions.add(Region(pathList.subList(bounds.first, bounds.second)))
        }
    }

    // get volume of state
    fun volume(): Float {
        var vol = 0.0f
        for (region in regions) {

            val regVol = region.volume()
            //vol += if (region.isCCW()) regVol else -regVol
            vol += abs(regVol)

            if (regVol < 0.0f) {
                //println("negative region in " + this.name + " with volume " + regVol.toString() + "\n")
            } else {
                //println("positive region " + regVol.toString() + "\n")
            }
        }

        return abs(vol) * 1.16f
    }

    // check if point is in one of the state's regions
    fun pointInRegion(point: Coords): Boolean {
        var pIR = false
        for (region in regions) {
            if (region.pointInRegion(point)) {
                pIR = if(pIR == true) false else true
            }
        }
        return pIR
    }
}

