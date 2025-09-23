package no.nav.pensjon.brev

import brev.maler.auto.createVedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.auto.*
import kotlin.reflect.KClass

object Fixtures {

    val felles = no.nav.brev.brevbaker.Fixtures.felles

    val fellesAuto = no.nav.brev.brevbaker.Fixtures.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            InfoAldersovergangEps60AarAutoDto::class -> InfoAldersovergangEps60AarAutoDto(ytelse = Ytelse.ALDER) as T
            InfoAldersovergangEps62AarAutoDto::class -> InfoAldersovergangEps62AarAutoDto(ytelse = YtelseType.ALDER) as T
            VedtakAldersovergang67AarGarantitilleggAutoDto::class -> createVedtakAldersovergang67AarGarantitilleggAutoDto() as T
            EmptyBrevdata::class -> EmptyBrevdata as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}
