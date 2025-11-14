package no.nav.pensjon.brev

import brev.maler.aldersovergang.createEndringAvAlderspensjonFordiDuFyller75AarAutoDto
import brev.maler.aldersovergang.createVedtakAldersovergang67AarGarantitilleggAutoDto
import brev.maler.aldersovergang.createVedtakEndringAFPEndretOpptjeningAutoDto
import brev.maler.sivilstand.createEndringAvAlderspensjonGarantitilleggDto
import brev.maler.sivilstand.createEndringAvAlderspensjonSivilstandAutoDto
import brev.maler.sivilstand.createEndringAvAlderspensjonSivilstandDto
import brev.maler.sivilstand.createEndringAvAlderspensjonSivilstandSaerskiltSatsDto
import brev.maler.stans.createVedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.fixtures.alder.createAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAP2016Dto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.model.alder.adhoc.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAldersovergangEps60AarAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAldersovergangEps62AarAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.Ytelse
import no.nav.pensjon.brev.model.alder.aldersovergang.YtelseType
import no.nav.pensjon.brev.model.alder.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
import no.nav.pensjon.brev.model.alder.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAP2016Dto
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.model.alder.sivilstand.EndringAvAlderspensjonGarantitilleggDto
import no.nav.pensjon.brev.model.alder.sivilstand.EndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.model.alder.sivilstand.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.model.alder.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.model.alder.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.model.alder.stans.VedtakStansAlderspensjonFlyttingMellomLandDto
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {
    val felles = no.nav.brev.brevbaker.FellesFactory.felles

    val fellesAuto = no.nav.brev.brevbaker.FellesFactory.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            AdhocAlderspensjonGjtOppryddingAutoDto::class -> createAlderspensjonGjtOppryddingAutoDto() as T
            EndringAvAlderspensjonGarantitilleggDto::class -> createEndringAvAlderspensjonGarantitilleggDto() as T
            EndringAvAlderspensjonSivilstandAutoDto::class -> createEndringAvAlderspensjonSivilstandAutoDto() as T
            EndringAvAlderspensjonSivilstandDto::class -> createEndringAvAlderspensjonSivilstandDto() as T
            EndringAvAlderspensjonSivilstandSaerskiltSatsDto::class -> createEndringAvAlderspensjonSivilstandSaerskiltSatsDto() as T
            InfoAldersovergangEps60AarAutoDto::class -> InfoAldersovergangEps60AarAutoDto(ytelse = Ytelse.ALDER) as T
            InfoAldersovergangEps62AarAutoDto::class -> InfoAldersovergangEps62AarAutoDto(ytelse = YtelseType.ALDER) as T
            VedtakAldersovergang67AarGarantitilleggAutoDto::class -> createVedtakAldersovergang67AarGarantitilleggAutoDto() as T
            VedtakEndringAFPEndretOpptjeningAutoDto::class -> createVedtakEndringAFPEndretOpptjeningAutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagUttakFoerNormertPensjonsalderDto::class -> createAvslagUttakFoerNormertPensjonsalderDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016AutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016Dto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016Dto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() as T
            EmptyAutobrevdata::class -> EmptyAutobrevdata as T
            EndringAvAlderspensjonFordiDuFyller75AarAutoDto::class -> createEndringAvAlderspensjonFordiDuFyller75AarAutoDto() as T
            VedtakStansAlderspensjonFlyttingMellomLandDto::class -> createVedtakStansAlderspensjonFlyttingMellomLandDto() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}
