package no.nav.pensjon.brev.alder

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.alder.maler.aldersovergang.createEndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonGarantitilleggDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.alder.maler.stans.createVedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.alder.model.adhoc.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.InfoAldersovergangEps60AarAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.InfoAldersovergangEps62AarAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.Ytelse
import no.nav.pensjon.brev.alder.model.aldersovergang.YtelseType
import no.nav.pensjon.brev.alder.model.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAP2016Dto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonGarantitilleggDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.alder.model.stans.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.fixtures.alder.createAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAP2016Dto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.fixtures.alder.createInfoAlderspensjonOvergang67AarAutoDto
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {
    val fellesAuto = FellesFactory.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            AdhocAlderspensjonGjtOppryddingAutoDto::class -> createAlderspensjonGjtOppryddingAutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016AutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016Dto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016Dto() as T
            AvslagUttakFoerNormertPensjonsalderAutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagUttakFoerNormertPensjonsalderDto::class -> createAvslagUttakFoerNormertPensjonsalderDto() as T
            EmptyAutobrevdata::class -> EmptyAutobrevdata as T
            EndringAvAlderspensjonFordiDuFyller75AarAutoDto::class -> createEndringAvAlderspensjonFordiDuFyller75AarAutoDto() as T
            EndringAvAlderspensjonGarantitilleggDto::class -> createEndringAvAlderspensjonGarantitilleggDto() as T
            EndringAvAlderspensjonSivilstandAutoDto::class -> createEndringAvAlderspensjonSivilstandAutoDto() as T
            EndringAvAlderspensjonSivilstandDto::class -> createEndringAvAlderspensjonSivilstandDto() as T
            EndringAvAlderspensjonSivilstandSaerskiltSatsDto::class -> createEndringAvAlderspensjonSivilstandSaerskiltSatsDto() as T
            InfoAldersovergangEps60AarAutoDto::class -> InfoAldersovergangEps60AarAutoDto(ytelse = Ytelse.ALDER) as T
            InfoAldersovergangEps62AarAutoDto::class -> InfoAldersovergangEps62AarAutoDto(ytelse = YtelseType.ALDER) as T
            InfoAlderspensjonOvergang67AarAutoDto::class -> createInfoAlderspensjonOvergang67AarAutoDto() as T
            VedtakAldersovergang67AarGarantitilleggAutoDto::class -> createVedtakAldersovergang67AarGarantitilleggAutoDto() as T
            VedtakEndringAFPEndretOpptjeningAutoDto::class -> createVedtakEndringAFPEndretOpptjeningAutoDto() as T
            VedtakStansAlderspensjonFlyttingMellomLandDto::class -> createVedtakStansAlderspensjonFlyttingMellomLandDto() as T
            VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto::class -> createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}
