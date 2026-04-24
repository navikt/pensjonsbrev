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

fun <T : Any> shortestEditScript(old: List<T>, new: List<T>): List<EditOperation<T>> =
    MyersDiff(old, new).shortestEditScript()

sealed class EditOperation<T : Any> {
    abstract val value: T
    abstract val position: Int

    data class Insert<T : Any>(val insert: T, override val position: Int) : EditOperation<T>() {
        override val value get() = insert
    }
    data class Delete<T : Any>(val delete: T, override val position: Int) : EditOperation<T>() {
        override val value get() = delete
    }
}

private class Vectors private constructor(private val negativeOffset: Int, private val array: IntArray) {
    constructor(n: Int, m: Int, initialVector: Int) : this(
        negativeOffset = calculateOffset(n, m),
        array = IntArray(calculateSize(n, m)).also { it[1 + n + m] = initialVector }
    )

    operator fun get(i: Int) = array[i + negativeOffset]
    operator fun set(i: Int, v: Int) {
        array[i + negativeOffset] = v
    }

    companion object {
        // Vectors need space for diagonals from -(n+m) to +(n+m)
        private fun calculateSize(n: Int, m: Int) = (m + n) * 2 + 1
        private fun calculateOffset(n: Int, m: Int) = n + m
    }
}
private data class Path(val from: Coords, val to: Coords)
private data class Coords(val x: Int, val y: Int) {
    companion object {
        val origin = Coords(0, 0)
    }
}
private data class MiddleSnake(val d: Int, val from: Coords, val to: Coords)

private class MyersDiff<T : Any>(val old: List<T>, val new: List<T>, val oldOffset: Int = 0, val newOffset: Int = 0) {
    private val n get() = old.size
    private val m get() = new.size
    private val size = n + m
    private val delta = n - m
    private val deltaIsOdd = (delta % 2) != 0
    private val halfway = size / 2 + (if (deltaIsOdd) 1 else 0)
    private val endCoords get() = Coords(n, m)

    init {
        require(old is RandomAccess) { "first er ikke RandomAccess (ArrayList)" }
        require(new is RandomAccess) { "second er ikke RandomAccess (ArrayList)" }
    }

    fun slice(from: Coords, to: Coords): MyersDiff<T> =
        MyersDiff(old.slice(from.x..<to.x), new.slice(from.y..<to.y), oldOffset + from.x, newOffset + from.y)

    fun shortestEditScript(): List<EditOperation<T>> {
        return if (n > 0 && m > 0) {
            val snake = findMiddleSnake()

            if (snake.d > 1 || snake.from != snake.to) {
                slice(from = origin, to = snake.from).shortestEditScript() +
                        slice(from = snake.to, to = endCoords).shortestEditScript()
            } else if (m > n) {
                slice(from = Coords(x = n, y = n), to = Coords(x = n, y = m)).shortestEditScript()
            } else {
                slice(from = Coords(x = m, y = m), to = Coords(x = n, y = m)).shortestEditScript()
            }
        } else if (n > 0) {
            old.mapIndexed { i, it -> Delete(it, oldOffset + i) }
        } else {
            new.mapIndexed { i, it -> Insert(it, newOffset + i) }
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

        return Path(from = Coords(fromX, fromY), to = extendSnake(Coords(fromX, fromY)))
    }

    private fun extendSnake(start: Coords): Coords {
        var x = start.x
        var y = start.y
        while (x < n && y < m && old[x] == new[y]) {
            x++
            y++
        }
        return Coords(x = x, y = y)
    }

    // Searches backwards for a d-path, and optimizing for y (i.e. equal or insertions)
    private fun searchBackwards(d: Int, vReverse: Vectors, vForward: Vectors): MiddleSnake? {
        for (k in -d..d step 2) {
            val path = findFurthestReachingReverseDpathInDiagonalK(d, k, vReverse)
            vReverse[k] = path.to.y

            if (!deltaIsOdd && (k + delta) in -d..d) {
                if (path.to.x <= vForward[k + delta]) {
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
        while (x > 0 && y > 0 && old[x - 1] == new[y - 1]) {
            x--
            y--
        }

        return Path(
            from = Coords(x = fromX, y = fromY),
            to = Coords(x = x, y = y),
        )
    }
}

