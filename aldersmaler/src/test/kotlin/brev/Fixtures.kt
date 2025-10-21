package no.nav.pensjon.brev

import brev.maler.aldersovergang.createVedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.model.alder.adhoc.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAldersovergangEps60AarAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAldersovergangEps62AarAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.Ytelse
import no.nav.pensjon.brev.model.alder.aldersovergang.YtelseType
import no.nav.pensjon.brev.fixtures.alder.createAlderspensjonGjtOppryddingAutoDto
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {

    val felles = no.nav.brev.brevbaker.Fixtures.felles

    val fellesAuto = no.nav.brev.brevbaker.Fixtures.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            InfoAldersovergangEps60AarAutoDto::class -> InfoAldersovergangEps60AarAutoDto(ytelse = Ytelse.ALDER) as T
            InfoAldersovergangEps62AarAutoDto::class -> InfoAldersovergangEps62AarAutoDto(ytelse = YtelseType.ALDER) as T
            VedtakAldersovergang67AarGarantitilleggAutoDto::class -> createVedtakAldersovergang67AarGarantitilleggAutoDto() as T
            AdhocAlderspensjonGjtOppryddingAutoDto::class -> createAlderspensjonGjtOppryddingAutoDto() as T
            EmptyBrevdata::class -> EmptyBrevdata as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}
