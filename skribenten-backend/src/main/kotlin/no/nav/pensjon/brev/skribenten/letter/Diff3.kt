package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Coords.Companion.origin
import no.nav.pensjon.brev.skribenten.letter.EditOperation.Delete
import no.nav.pensjon.brev.skribenten.letter.EditOperation.Insert

/**
 * Find the shortest edit script of insert and delete operations to transform
 * old into new.
 *
 * Uses an implementation of Eugene W. Myers Difference Algorithm, specifically
 * the linear space refinement from section 4b.
 *
 * @param T must implement an equals-method
 */
fun <T : Any> shortestEditScript(old: Sequence<T>, new: Sequence<T>): List<EditOperation<T>> =
    MyersDiff(old.toList(), new.toList()).shortestEditScript()

sealed class EditOperation<T : Any> {
    data class Insert<T : Any>(val insert: T) : EditOperation<T>()
    data class Delete<T : Any>(val delete: T) : EditOperation<T>()
}

private class Vectors private constructor(private val negativeOffset: Int, private val array: IntArray) {
    constructor(n: Int, m: Int, initialVector: Int) : this(m + n, IntArray((m + n) * 2 + 1).also { it[1 + n + m] = initialVector })

    operator fun get(i: Int) = array[i + negativeOffset]
    operator fun set(i: Int, v: Int) {
        array[i + negativeOffset] = v
    }
}
private data class Path(val from: Coords, val to: Coords)
private data class Coords(val x: Int, val y: Int) {
    companion object {
        val origin = Coords(0, 0)
    }
}
private data class MiddleSnake(val d: Int, val from: Coords, val to: Coords)

private class MyersDiff<T : Any>(val first: List<T>, val second: List<T>) {
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

    fun slice(from: Coords, to: Coords): MyersDiff<T> =
        MyersDiff(first.slice(from.x..<to.x), second.slice(from.y..<to.y))

    fun shortestEditScript(): List<EditOperation<T>> {
        return if (n > 0 && m > 0) {
            val snake = findMiddleSnake()

            if (snake.d > 1 || (snake.from != snake.to)) {
                slice(from = origin, to = snake.from).shortestEditScript() +
                        slice(from = snake.to, to = endCoords).shortestEditScript()
            } else if (m > n) {
                slice(from = Coords(x = n, y = n), to = Coords(x = n, y = m)).shortestEditScript()
            } else {
                slice(from = Coords(x = m, y = m), to = Coords(x = n, y = m)).shortestEditScript()
            }
        } else if (n > 0) {
            first.map { Delete(it) }
        } else {
            second.map { Insert(it) }
        }
    }

    private fun findMiddleSnake(): MiddleSnake {
        val vForward = Vectors(n = n, m = m, initialVector = 0)
        val vReverse = Vectors(n = n, m = m, initialVector = m)

        for (d in 0..halfway) {
            val forwards = searchForwards(d, vForward, vReverse)
            if (forwards != null) {
                return forwards
            }

            val backwards = searchBackwards(d, vReverse, vForward)
            if (backwards != null) {
                return backwards
            }
        }

        return MiddleSnake(d = n + m, from = origin, Coords(n, m))
    }

    // Searches forwards for a d-path, and optimizing for x (i.e. equal or deletions)
    private fun searchForwards(d: Int, vForward: Vectors, vReverse: Vectors): MiddleSnake? {
        for (k in -d..d step 2) {
            val path = findFurthestReachingForwardDpathInDiagonalK(d, k, vForward)
            vForward[k] = path.to.x

            if (deltaIsOdd && k in (delta - (d - 1))..(delta + (d - 1))) {
                if (path.from.y >= vReverse[k - delta]) {
                    return MiddleSnake(d = 2 * d - 1, from = path.from, to = path.to)
                }
            }
        }
        return null
    }

    private fun findFurthestReachingForwardDpathInDiagonalK(d: Int, k: Int, vForward: Vectors): Path {
        val fromX = if (k == -d || k != d && vForward[k - 1] < vForward[k + 1]) {
            vForward[k + 1]
        } else {
            vForward[k - 1] + 1
        }
        val fromY = fromX - k


        var x = fromX
        var y = fromY
        while (x < n && y < m && first[x] == second[y]) {
            x++
            y++
        }

        return Path(from = Coords(x = fromX, y = fromY), to = Coords(x = x, y = y))
    }

    // Searches backwards for a d-path, and optimizing for y (i.e. equal or insertions)
    private fun searchBackwards(d: Int, vReverse: Vectors, vForward: Vectors): MiddleSnake? {
        for (k in -d..d step 2) {
            val path = findFurthestReachingReverseDpathInDiagonalK(d, k, vReverse)
            vReverse[k] = path.to.y

            if (!deltaIsOdd && (k + delta) in -d..d) {
                if (path.to.x <= vForward[k]) {
                    // Since the path is found through a reverse search we have to flip from and to
                    return MiddleSnake(d = 2 * d, from = path.to, to = path.from)
                }
            }
        }
        return null
    }

    private fun findFurthestReachingReverseDpathInDiagonalK(d: Int, k: Int, vReverse: Vectors): Path {
        val fromY = if (k == -d || k != d && vReverse[k - 1] > vReverse[k + 1]) {
            vReverse[k + 1]
        } else {
            vReverse[k - 1] - 1
        }
        val fromX = fromY + (k + delta)

        var x = fromX
        var y = fromY
        while (x > 0 && y > 0 && first[x - 1] == second[y - 1]) {
            x--
            y--
        }

        return Path(
            from = Coords(x = fromX, y = fromY),
            to = Coords(x = x, y = y),
        )
    }
}

