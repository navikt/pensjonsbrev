package no.nav.pensjon.brev

import no.nav.brev.brevbaker.LetterDataFactory
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {

    val felles = no.nav.brev.brevbaker.Fixtures.felles

    val fellesAuto = no.nav.brev.brevbaker.Fixtures.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}