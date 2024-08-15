package no.nav.pensjon.brev.skribenten

import org.assertj.core.api.AbstractAssert
import java.util.function.Consumer

inline fun <reified T> AbstractAssert<*, *>.isInstanceOfSatisfying(block: Consumer<T>) =
    isInstanceOfSatisfying(T::class.java, block)!!

inline fun <reified T> AbstractAssert<*, *>.isInstanceOf() =
    isInstanceOf(T::class.java)!!