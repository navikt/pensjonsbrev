package no.nav.pensjon.brev.alder

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.alder.maler.aldersovergang.createEndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.maler.avslag.uttak.createAvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.alder.maler.avslag.uttak.createUnder5AarTrygdetidAutoDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonGarantitilleggDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createVedtakOmregningAFPTilEnsligPensjonistAutoDto
import no.nav.pensjon.brev.alder.maler.stans.createVedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.alder.model.adhoc.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.*
import no.nav.pensjon.brev.alder.model.avslag.*
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonGarantitilleggDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDto
import no.nav.pensjon.brev.alder.model.stans.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.fixtures.alder.*
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {
    val fellesAuto = FellesFactory.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            AdhocAlderspensjonGjtOppryddingAutoDto::class -> createAlderspensjonGjtOppryddingAutoDto() as T
            AvslagForLiteTrygdetidAPDto::class -> createAvslagForLiteTrygdetidAPDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016AutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016Dto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016Dto() as T
            AvslagUttakFoerNormertPensjonsalderAutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagUttakFoerNormertPensjonsalderDto::class -> createAvslagUttakFoerNormertPensjonsalderDto() as T
            AvslagUnder5AarTrygdetidAutoDto::class -> createUnder5AarTrygdetidAutoDto() as T
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
            VedtakOmregningAFPTilEnsligPensjonistAutoDto::class -> createVedtakOmregningAFPTilEnsligPensjonistAutoDto() as T
            VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto::class -> createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}
