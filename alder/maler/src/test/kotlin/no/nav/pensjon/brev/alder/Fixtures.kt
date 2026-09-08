package no.nav.pensjon.brev.alder

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.alder.maler.adhoc.createAdhocTidligereUfoereGradertAPAutoDto
import no.nav.pensjon.brev.alder.maler.afp.*
import no.nav.pensjon.brev.alder.maler.afpprivat.*
import no.nav.pensjon.brev.alder.maler.aldersovergang.createEndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.createOmregningAlderUfore2016Dto
import no.nav.pensjon.brev.alder.maler.avslag.createAvslagPaaGjenlevenderettIAlderspensjon
import no.nav.pensjon.brev.alder.maler.avslag.uttak.createUnder5AarTrygdetidAutoDto
import no.nav.pensjon.brev.alder.maler.endring.*
import no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev.createAfpPrivatSokerUforeTrygdDto
import no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev.createAfpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.maler.sivilstand.*
import no.nav.pensjon.brev.alder.maler.stans.createVedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.alder.maler.vedlegg.*
import no.nav.pensjon.brev.alder.model.adhoc.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.alder.model.adhoc.AdhocTidligereUfoereGradertAPAutoDto
import no.nav.pensjon.brev.alder.model.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.alder.model.afp.*
import no.nav.pensjon.brev.alder.model.afpprivat.*
import no.nav.pensjon.brev.alder.model.aldersovergang.*
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016Dto
import no.nav.pensjon.brev.alder.model.avslag.*
import no.nav.pensjon.brev.alder.model.endring.*
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDto
import no.nav.pensjon.brev.alder.model.innvilgelse.InnvilgelseAvAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.model.innvilgelse.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.alder.model.innvilgelse.InnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.alder.model.sivilstand.*
import no.nav.pensjon.brev.alder.model.stans.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.alder.model.vedlegg.*
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.fixtures.alder.*
import no.nav.pensjon.brev.fixtures.redigerbar.createAvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVedtakEndringAvAlderspensjonGjenlevenderettigheterDto
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {
    val fellesAuto = FellesFactory.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            AdhocAlderspensjonGjtOppryddingAutoDto::class -> createAlderspensjonGjtOppryddingAutoDto() as T
            AdhocTidligereUfoereGradertAPAutoDto::class -> createAdhocTidligereUfoereGradertAPAutoDto() as T
            Gjenlevenderett2027Dto::class -> createGjenlevenderett2027Dto() as T
            AvslagForLiteTrygdetidAPDto::class -> createAvslagForLiteTrygdetidAPDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() as T
            AvslagPaaGjenlevenderettIAlderspensjonDto::class -> createAvslagPaaGjenlevenderettIAlderspensjon() as T
            AvslagUttakFoerNormertPensjonsalderAP2016AutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagUnder5AarTrygdetidAutoDto::class -> createUnder5AarTrygdetidAutoDto() as T
            EmptyAutobrevdata::class -> EmptyAutobrevdata as T
            EndringAvAlderspensjonFordiDuFyller75AarAutoDto::class -> createEndringAvAlderspensjonFordiDuFyller75AarAutoDto() as T
            EndringAvAlderspensjonAvdodAutoDto::class -> createEndringAvAlderspensjonAvdodAuto() as T
            EndringAvAlderspensjonGarantitilleggDto::class -> createEndringAvAlderspensjonGarantitilleggDto() as T
            EndringAvAlderspensjonSivilstandAutoDto::class -> createEndringAvAlderspensjonSivilstandAutoDto() as T
            EndringAvAlderspensjonSivilstandDto::class -> createEndringAvAlderspensjonSivilstandDto() as T
            EndringAvAlderspensjonSivilstandSaerskiltSatsDto::class -> createEndringAvAlderspensjonSivilstandSaerskiltSatsDto() as T
            EndringAvUttaksgradAutoDto::class -> createEndringAvUttaksgradAutoDto() as T
            EmptyFagsystemdata::class -> EmptyFagsystemdata as T
            EndringPgaOpptjeningAutoDto::class -> createEndringPgaOpptjeningAutoDto() as T
            InnvilgelseAvAfpAutoDto::class -> createInnvilgelseAvAfpAutoDto() as T
            AvslagAfpPrivatDto::class -> createAvslagAfpPrivatDto() as T
            AvslagAfpPrivatAutoDto::class -> createAvslagAfpPrivatAutoDto() as T
            AvslagAfpGammelDto::class -> createAvslagAfpGammelDto() as T
            VedtakAfpPrivatEndringDto::class -> createVedtakAfpPrivatEndringDto() as T
            VedtakAfpEtteroppgjoerIngenEndringDto::class -> createVedtakAfpEtteroppgjoerIngenEndringDto() as T
            VedtakAfpEtteroppgjoerEtterbetalingDto::class -> createVedtakAfpEtteroppgjoerEtterbetalingDto() as T
            VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto::class -> createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto() as T
            InnvilgelseAvAfpDto::class -> createInnvilgelseAvAfpDto() as T
            InnvilgelseAvAfpOffentligSektorDto::class -> createInnvilgelseAvAfpOffentligSektorDto() as T
            InnvilgelseAvAlderspensjonAutoDto::class -> createInnvilgelseAvAlderspensjonAutoDto() as T
            InnvilgelseAvAlderspensjonDto::class -> createInnvilgelseAvAlderspensjonDto() as T
            InnvilgelseAvAlderspensjonTrygdeavtaleDto::class -> createInnvilgelseAvAlderspensjonTrygdeavtaleDto() as T
            VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto::class -> createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto() as T
            VedtakAfpEtteroppgjoerEtterbetalingAutoDto::class -> createVedtakAfpEtteroppgjoerEtterbetalingAutoDto() as T
            VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto::class -> createVedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto() as T
            VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto::class -> createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto() as T
            VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto::class -> createVedtakAfpEtteroppgjoerIngenEndringEtterSvarDto() as T
            VedtakAfpEtteroppgjoerTilbakekrevingAutoDto::class -> createVedtakAfpEtteroppgjoerTilbakekrevingAutoDto() as T
            VarselAfpEtteroppgjoerForeloepigAutoDto::class -> createVarselAfpEtteroppgjoerForeloepigAutoDto() as T
            VarselAfpEtteroppgjoerForeloepigDto::class -> createVarselAfpEtteroppgjoerForeloepigDto() as T
            VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto::class -> createVedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto() as T
            VedtakAfpEtteroppgjoerIngenEndringAutoDto::class -> createVedtakAfpEtteroppgjoerIngenEndringAutoDto() as T
            VedtakEndringAfpOffentligSektorDto::class -> createVedtakEndringAfpOffentligSektorDto() as T
            InfoAldersovergangEps60AarAutoDto::class -> InfoAldersovergangEps60AarAutoDto(ytelse = Ytelse.ALDER) as T
            InfoAldersovergangEps62AarAutoDto::class -> InfoAldersovergangEps62AarAutoDto(ytelse = YtelseType.ALDER) as T
            InfoAlderspensjonOvergang67AarAutoDto::class -> createInfoAlderspensjonOvergang67AarAutoDto() as T
            OmregningAlderUfore2016Dto::class -> createOmregningAlderUfore2016Dto() as T
            VedtakAldersovergang67AarGarantitilleggAutoDto::class -> createVedtakAldersovergang67AarGarantitilleggAutoDto() as T
            VedtakEndringAvAlderspensjonGjenlevenderettigheterDto::class -> createVedtakEndringAvAlderspensjonGjenlevenderettigheterDto() as T
            VedtakEndringAvAlderspensjonInstitusjonsoppholdDto::class -> createVedtakEndringAvAlderspensjonInstitusjonsoppholdDto() as T
            VedtakEndringAFPEndretOpptjeningAutoDto::class -> createVedtakEndringAFPEndretOpptjeningAutoDto() as T
            VedtakEndringAvUttaksgradDto::class -> createVedtakEndringAvUttaksgradDto() as T
            VedtakEndringAvUttaksgradStansBrukerEllerVergeDto::class -> createVedtakEndringAvUttaksgradStansBrukerEllerVergeDto() as T
            VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto::class -> createVedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto() as T
            VedtakEndringVedFlyttingMellomLandDto::class -> createVedtakEndringVedFlyttingMellomLandDto() as T
            VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto::class -> createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto() as T
            VedtakStansAlderspensjonFlyttingMellomLandDto::class -> createVedtakStansAlderspensjonFlyttingMellomLandDto() as T
            VedtakOmregningAFPTilEnsligPensjonistAutoDto::class -> createVedtakOmregningAFPTilEnsligPensjonistAutoDto() as T
            VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto::class -> createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto() as T
            AfpPrivatSokerUforeTrygdDto::class -> createAfpPrivatSokerUforeTrygdDto() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when(letterDataType) {
        DineRettigheterOgMulighetTilAaKlageDto::class -> createDineRettigheterOgMulighetTilAaKlageDto() as T
        AFpPrivatSokerUforeTrygdVedleggDto::class -> createAfpPrivatSokerUforeTrygdVedleggDto() as T
        MaanedligPensjonFoerSkattDto::class -> createMaanedligPensjonFoerSkatt() as T
        MaanedligPensjonFoerSkattAFPDto::class -> createMaanedligPensjonFoerSkattAFPDto() as T
        MaanedligPensjonFoerSkattAFPOffentligDto::class -> createMaanedligPensjonFoerSkattAFPOffentligDto() as T
        HvordanPensjonenErBeregnetAfpOffentligDto::class -> createHvordanPensjonenErBeregnetAfpOffentligDto() as T
        OpplysningerOmBeregningenAfpDto::class -> createOpplysningerOmBeregningenAfpDto() as T
        OversiktOverPensjonenAfpDto::class -> createOversiktOverPensjonenAfpDto() as T
        OversiktOverPensjonenAfpPrivatDto::class -> createOversiktOverPensjonenAfpPrivatDto() as T
        MaanedligPensjonFoerSkattAP2025Dto::class -> createMaanedligPensjonFoerSkattAP2025Dto() as T
        MaanedligPensjonFoerSkattAlderspensjonDto::class -> createMaanedligPensjonFoerSkattAlderspensjonDto() as T
        OpplysningerBruktIBeregningen::class -> createOpplysningerBruktIBeregningen() as T
        OpplysningerBruktIBeregningenAlderDto::class -> createOpplysningerBruktIBeregningAlderDto() as T
        OpplysningerBruktIBeregningenAlderAP2025Dto::class -> createOpplysningerBruktIBeregningAlderAP2025Dto() as T
        OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto::class -> createOpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto() as T
        OpplysningerBruktIBeregningenEndretUttaksgradDto::class -> createOpplysningerBruktIBeregningenEndretUttaksgradDto() as T
        OpplysningerOmAvdoedBruktIBeregningDto::class -> createOpplysningerOmAvdoedBruktIBeregningDto() as T
        OrienteringOmRettigheterOgPlikterDto::class -> createOrienteringOmRettigheterOgPlikterDto() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }
}
