package no.nav.pensjon.brev.template.render

import com.natpryce.hamkrest.*

/**
 * An isA matcher with overrides for actual and expected type names.
 * Useful when type names are long, makes for a more readable test failure text.
 */
inline fun <R : Any, reified T : R> isA(
    crossinline actualType: R.() -> Any,
    expectedType: String,
    downcastMatcher: Matcher<T> = anything,
) =
    object : Matcher<R> {
        override fun invoke(actual: R) =
            if (actual is T) {
                downcastMatcher(actual)
            } else {
                MatchResult.Mismatch("was a ${actual.actualType()}")
            }

        override val description: String
            get() = "is a $expectedType that (${downcastMatcher.description})"
    }

/**
 * A matcher that applies the provided matcher on the specified index of a list.
 *
 */
class ListIndexMatcher<T>(private val index: Int, private val match: Matcher<T>) : Matcher.Primitive<List<T>>() {
    override val description: String
        get() = "at [$index] ${describe(match)}"

    override fun invoke(actual: List<T>): MatchResult =
        if (index in actual.indices) {
            val result = match(actual[index])
            if (result is MatchResult.Mismatch) {
                MatchResult.Mismatch("at [$index] " + result.description)
            } else {
                result
            }
        } else {
            MatchResult.Mismatch("was missing at [$index]")
        }

    companion object {
        /**
         * Convert a list of matchers to a Matcher for a list.
         * The resulting Matcher<List<T>> will apply each matcher to their corresponding index.
         */
        fun <T> forList(matchers: List<Matcher<T>>): Matcher<List<T>> =
            allOf(matchers.mapIndexed { index, matcher -> ListIndexMatcher(index, matcher) })
    }
}
