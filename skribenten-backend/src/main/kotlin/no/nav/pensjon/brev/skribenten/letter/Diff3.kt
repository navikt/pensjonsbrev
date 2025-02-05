package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Diff3.Coords.Companion.origin
import no.nav.pensjon.brev.skribenten.letter.EditOperation.Delete
import no.nav.pensjon.brev.skribenten.letter.EditOperation.Insert

sealed class EditOperation<T : Any> {
    data class Insert<T : Any>(val insert: T) : EditOperation<T>()
    data class Delete<T : Any>(val delete: T) : EditOperation<T>()
}

object Diff3 {

    fun <T : Any> shortestEditScript(old: Sequence<T>, new: Sequence<T>): List<EditOperation<T>> =
        shortestEditScript(Diff(old.toList(), new.toList()))

    private fun <T : Any> shortestEditScript(diff: Diff<T>): List<EditOperation<T>> {
        return if (diff.n > 0 && diff.m > 0) {
            val snake = findMiddleSnake(diff)

            if (snake.d > 1 || (snake.from != snake.to)) {
                shortestEditScript(diff.slice(from = origin, to = snake.from)) +
                        shortestEditScript(diff.slice(from = snake.to, to = diff.endCoords))
            } else if (diff.m > diff.n) {
                shortestEditScript(diff.slice(from = Coords(x = diff.n, y = diff.n), to = Coords(x = diff.n, y = diff.m)))
            } else {
                shortestEditScript(diff.slice(from = Coords(x = diff.m, y = diff.m), to = Coords(x = diff.n, y = diff.m)))
            }
        } else if (diff.n > 0) {
            diff.first.map { Delete(it) }
        } else {
            diff.second.map { Insert(it) }
        }
    }

    private fun <T : Any> findMiddleSnake(diff: Diff<T>): MiddleSnake {
        val vForward = Vectors(n = diff.n, m = diff.m, initialVector = 0)
        val vReverse = Vectors(n = diff.n, m = diff.m, initialVector = diff.m)

        for (d in 0..diff.halfway) {
            // forward search
            val forwards = searchForwards(d, vForward, vReverse, diff)
            if (forwards != null) {
                return forwards
            }

            val backwards = searchBackwards(d, vReverse, vForward, diff)
            if (backwards != null) {
                return backwards
            }
        }

        return MiddleSnake(d = diff.n + diff.m, from = origin, Coords(diff.n, diff.m))
    }

    // Searches forwards for a d-path, and optimizing for x (i.e. equal or deletions)
    private fun <T : Any> searchForwards(
        d: Int,
        vForward: Vectors,
        vReverse: Vectors,
        diff: Diff<T>,
    ): MiddleSnake? {
        for (k in -d..d step 2) {
            val path = findFurthestReachingForwardDpathInDiagonalK(d, k, vForward, diff)
            vForward[k] = path.to.x

            if (diff.deltaIsOdd && k in (diff.delta - (d - 1))..(diff.delta + (d - 1))) {
                if (path.from.y >= vReverse[k - diff.delta]) {
                    return MiddleSnake(d = 2 * d - 1, from = path.from, to = path.to)
                }
            }
        }
        return null
    }

    // Searches backwards for a d-path, and optimizing for y (i.e. equal or insertions)
    private fun <T : Any> searchBackwards(
        d: Int,
        vReverse: Vectors,
        vForward: Vectors,
        diff: Diff<T>,
    ): MiddleSnake? {
        for (k in -d..d step 2) {
            val path = findFurthestReachingReverseDpathInDiagonalK(d, k, vReverse, diff)
            vReverse[k] = path.to.y

            if (!diff.deltaIsOdd && (k + diff.delta) in -d..d) {
                if (path.to.x <= vForward[k]) {
                    // Since the path is found through a reverse search we have to flip from and to
                    return MiddleSnake(d = 2 * d, from = path.to, to = path.from)
                }
            }
        }
        return null
    }

    private fun <T : Any> findFurthestReachingForwardDpathInDiagonalK(d: Int, k: Int, vForward: Vectors, diff: Diff<T>): Path {
        val fromX = if (k == -d || k != d && vForward[k - 1] < vForward[k + 1]) {
            vForward[k + 1]
        } else {
            vForward[k - 1] + 1
        }
        val fromY = fromX - k


        var x = fromX
        var y = fromY
        while (x < diff.n && y < diff.m && diff.first[x] == diff.second[y]) {
            x++
            y++
        }

        return Path(from = Coords(x = fromX, y = fromY), to = Coords(x = x, y = y))
    }

    private fun <T : Any> findFurthestReachingReverseDpathInDiagonalK(d: Int, k: Int, vReverse: Vectors, diff: Diff<T>): Path {
        val fromY = if (k == -d || k != d && vReverse[k - 1] > vReverse[k + 1]) {
            vReverse[k + 1]
        } else {
            vReverse[k - 1] - 1
        }
        val fromX = fromY + (k + diff.delta)

        var x = fromX
        var y = fromY
        while (x > 0 && y > 0 && diff.first[x - 1] == diff.second[y - 1]) {
            x--
            y--
        }

        return Path(
            from = Coords(x = fromX, y = fromY),
            to = Coords(x = x, y = y),
        )
    }

    private data class Path(val from: Coords, val to: Coords)
    private data class Coords(val x: Int, val y: Int) {
        companion object {
            val origin = Coords(0, 0)
        }
    }
    private data class MiddleSnake(val d: Int, val from: Coords, val to: Coords)

    private class Diff<T : Any>(val first: List<T>, val second: List<T>) {
        val n get() = first.size
        val m get() = second.size
        val size = n + m
        val delta = n - m
        val deltaIsOdd = (delta % 2) != 0
        val halfway = size / 2 + (if (deltaIsOdd) 1 else 0)
        val endCoords get() = Coords(n, m)

        init {
            if (first !is RandomAccess) {
                throw RuntimeException("first er ikke RandomAccess (ArrayList)")
            }
            if (second !is RandomAccess) {
                throw RuntimeException("second er ikke RandomAccess (ArrayList)")
            }
        }

        fun slice(from: Coords, to: Coords): Diff<T> =
            Diff(first.slice(from.x..<to.x), second.slice(from.y..<to.y))

    }

    private class Vectors private constructor(private val negativeOffset: Int, private val array: IntArray) {
        constructor(n: Int, m: Int, initialVector: Int) : this(m + n, IntArray((m + n) * 2 + 1).also { it[1 + n + m] = initialVector })

        operator fun get(i: Int) = array[i + negativeOffset]
        operator fun set(i: Int, v: Int) {
            array[i + negativeOffset] = v
        }
    }
}
