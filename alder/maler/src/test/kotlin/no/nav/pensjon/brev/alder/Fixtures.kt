package no.nav.pensjon.brev.alder

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.alder.maler.adhoc.createAdhocTidligereUfoereGradertAPAutoDto
import no.nav.pensjon.brev.alder.maler.afpprivat.createAvslagAfpPrivatDto
import no.nav.pensjon.brev.alder.maler.afp.createAvslagAfpGammelDto
import no.nav.pensjon.brev.alder.maler.afp.createAvslagAfpPrivatAutoDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpPrivatEndringDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpPrivatEndringOpptjeningAutoDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerIngenEndringDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerEtterbetalingDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto
import no.nav.pensjon.brev.alder.maler.afpprivat.createInnvilgelseAvAfpAutoDto
import no.nav.pensjon.brev.alder.maler.afpprivat.createInnvilgelseAvAfpDto
import no.nav.pensjon.brev.alder.maler.afp.createInnvilgelseAvAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerIngenEndringEtterSvarDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerTilbakekrevingAutoDto
import no.nav.pensjon.brev.alder.maler.afp.createVarselAfpEtteroppgjoerForeloepigAutoDto
import no.nav.pensjon.brev.alder.maler.afp.createVarselAfpEtteroppgjoerForeloepigDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerIngenEndringAutoDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakEndringAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createEndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createMaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.createOmregningAlderUfore2016Dto
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.createOmregningAlderUfore2016RedigerbarDto
import no.nav.pensjon.brev.alder.maler.avslag.uttak.createUnder5AarTrygdetidAutoDto
import no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev.createAfpPrivatSokerUforeTrygdDto
import no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev.createAfpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev.createUforeTrygdSokerAfpPrivatDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonAvdodAuto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonGarantitilleggDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createEndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.alder.maler.sivilstand.createMaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.maler.sivilstand.createVedtakOmregningAFPTilEnsligPensjonistAutoDto
import no.nav.pensjon.brev.alder.maler.stans.createVedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkattAFPOffentligDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createHvordanPensjonenErBeregnetAfpOffentligDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerOmBeregningenAfpDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOversiktOverPensjonenAfpDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOversiktOverPensjonenAfpPrivatDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.adhoc.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.alder.model.adhoc.AdhocTidligereUfoereGradertAPAutoDto
import no.nav.pensjon.brev.alder.model.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatDto
import no.nav.pensjon.brev.alder.model.afp.AvslagAfpGammelDto
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDto
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpDto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDto
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDto
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakEndringAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.model.aldersovergang.*
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016Dto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016RedigerbarDto
import no.nav.pensjon.brev.alder.model.avslag.*
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.UforeTrygdSokerAfpPrivatDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonAvdodAutoDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonGarantitilleggDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDto
import no.nav.pensjon.brev.alder.model.stans.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDto
import no.nav.pensjon.brev.alder.model.vedlegg.HvordanPensjonenErBeregnetAfpOffentligDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpPrivatDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.fixtures.alder.*
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
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016AutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016Dto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016Dto() as T
            AvslagUttakFoerNormertPensjonsalderAutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagUttakFoerNormertPensjonsalderDto::class -> createAvslagUttakFoerNormertPensjonsalderDto() as T
            AvslagUnder5AarTrygdetidAutoDto::class -> createUnder5AarTrygdetidAutoDto() as T
            EmptyAutobrevdata::class -> EmptyAutobrevdata as T
            EmptyRedigerbarBrevdata::class -> EmptyRedigerbarBrevdata as T
            EndringAvAlderspensjonFordiDuFyller75AarAutoDto::class -> createEndringAvAlderspensjonFordiDuFyller75AarAutoDto() as T
            EndringAvAlderspensjonAvdodAutoDto::class -> createEndringAvAlderspensjonAvdodAuto() as T
            EndringAvAlderspensjonGarantitilleggDto::class -> createEndringAvAlderspensjonGarantitilleggDto() as T
            EndringAvAlderspensjonSivilstandAutoDto::class -> createEndringAvAlderspensjonSivilstandAutoDto() as T
            EndringAvAlderspensjonSivilstandDto::class -> createEndringAvAlderspensjonSivilstandDto() as T
            EndringAvAlderspensjonSivilstandSaerskiltSatsDto::class -> createEndringAvAlderspensjonSivilstandSaerskiltSatsDto() as T
            InnvilgelseAvAfpAutoDto::class -> createInnvilgelseAvAfpAutoDto() as T
            AvslagAfpPrivatDto::class -> createAvslagAfpPrivatDto() as T
            AvslagAfpPrivatAutoDto::class -> createAvslagAfpPrivatAutoDto() as T
            AvslagAfpGammelDto::class -> createAvslagAfpGammelDto() as T
            VedtakAfpPrivatEndringOpptjeningAutoDto::class -> createVedtakAfpPrivatEndringOpptjeningAutoDto() as T
            VedtakAfpPrivatEndringDto::class -> createVedtakAfpPrivatEndringDto() as T
            VedtakAfpEtteroppgjoerIngenEndringDto::class -> createVedtakAfpEtteroppgjoerIngenEndringDto() as T
            VedtakAfpEtteroppgjoerEtterbetalingDto::class -> createVedtakAfpEtteroppgjoerEtterbetalingDto() as T
            VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto::class -> createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto() as T
            InnvilgelseAvAfpDto::class -> createInnvilgelseAvAfpDto() as T
            InnvilgelseAvAfpOffentligSektorDto::class -> createInnvilgelseAvAfpOffentligSektorDto() as T
            VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerDto::class -> createVedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerDto() as T
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
            OmregningAlderUfore2016RedigerbarDto::class -> createOmregningAlderUfore2016RedigerbarDto() as T
            VedtakAldersovergang67AarGarantitilleggAutoDto::class -> createVedtakAldersovergang67AarGarantitilleggAutoDto() as T
            VedtakEndringAFPEndretOpptjeningAutoDto::class -> createVedtakEndringAFPEndretOpptjeningAutoDto() as T
            VedtakStansAlderspensjonFlyttingMellomLandDto::class -> createVedtakStansAlderspensjonFlyttingMellomLandDto() as T
            VedtakOmregningAFPTilEnsligPensjonistAutoDto::class -> createVedtakOmregningAFPTilEnsligPensjonistAutoDto() as T
            VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto::class -> createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto() as T
            UforeTrygdSokerAfpPrivatDto::class -> createUforeTrygdSokerAfpPrivatDto() as T
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
        OpplysningerOmAvdoedBruktIBeregningDto::class -> createOpplysningerOmAvdoedBruktIBeregningDto() as T
        OrienteringOmRettigheterOgPlikterDto::class -> createOrienteringOmRettigheterOgPlikterDto() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }
}
