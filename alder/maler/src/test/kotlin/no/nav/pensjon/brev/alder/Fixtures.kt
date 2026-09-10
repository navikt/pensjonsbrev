package no.nav.pensjon.brev.alder

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.maler.adhoc.createAdhocTidligereUfoereGradertAPAutoDto
import no.nav.pensjon.brev.alder.maler.afpprivat.createAvslagAfpPrivatDto
import no.nav.pensjon.brev.alder.maler.afp.createAvslagAfpGammelDto
import no.nav.pensjon.brev.alder.maler.afpprivat.createAvslagAfpPrivatAutoDto
import no.nav.pensjon.brev.alder.maler.afpprivat.createVedtakAfpPrivatEndringDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerIngenEndringDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerEtterbetalingDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto
import no.nav.pensjon.brev.alder.maler.afpprivat.createInnvilgelseAvAfpAutoDto
import no.nav.pensjon.brev.alder.maler.afpprivat.createInnvilgelseAvAfpDto
import no.nav.pensjon.brev.alder.maler.afp.createInnvilgelseAvAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.maler.afp.createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto
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
import no.nav.pensjon.brev.alder.maler.afpprivat.createMaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.alder.maler.afpprivat.createVedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.createOmregningAlderUfore2016Dto
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.createOmregningAlderUfore2016RedigerbarDto
import no.nav.pensjon.brev.alder.maler.avslag.createAvslagPaaGjenlevenderettIAlderspensjon
import no.nav.pensjon.brev.alder.maler.avslag.uttak.createUnder5AarTrygdetidAutoDto
import no.nav.pensjon.brev.alder.maler.createForespoerselOmDokumentasjonAvBotidINorgeAlderDto
import no.nav.pensjon.brev.alder.maler.endring.createEndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.alder.maler.endring.createEndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.alder.maler.endring.createOpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto
import no.nav.pensjon.brev.alder.maler.endring.createOpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.alder.maler.endring.createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto
import no.nav.pensjon.brev.alder.maler.endring.createVedtakEndringAvAlderspensjonInstitusjonsoppholdDto
import no.nav.pensjon.brev.alder.maler.endring.createVedtakEndringAvUttaksgradDto
import no.nav.pensjon.brev.alder.maler.endring.createVedtakEndringAvUttaksgradStansBrukerEllerVergeDto
import no.nav.pensjon.brev.alder.maler.endring.createVedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto
import no.nav.pensjon.brev.alder.maler.endring.createVedtakEndringVedFlyttingMellomLandDto
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
import no.nav.pensjon.brev.alder.model.aldersovergang.*
import no.nav.pensjon.brev.alder.model.avslag.*
import no.nav.pensjon.brev.alder.model.endring.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto
import no.nav.pensjon.brev.alder.model.endring.OpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.model.maler.EmptyRedigerbarBrevdata
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
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.fixtures.alder.*
import no.nav.pensjon.brev.fixtures.redigerbar.createAvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnhentingInformasjonFraBrukerDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVedtakEndringAvAlderspensjonGjenlevenderettigheterDto
import no.nav.pensjon.brev.template.BrevTemplate
import kotlin.reflect.KClass
import no.nav.pensjon.brev.alder.maler.*
import no.nav.pensjon.brev.alder.maler.adhoc.*
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.*
import no.nav.pensjon.brev.alder.maler.afp.*
import no.nav.pensjon.brev.alder.maler.afpprivat.*
import no.nav.pensjon.brev.alder.maler.aldersovergang.*
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.*
import no.nav.pensjon.brev.alder.maler.avslag.*
import no.nav.pensjon.brev.alder.maler.avslag.gradsendring.*
import no.nav.pensjon.brev.alder.maler.avslag.uttak.*
import no.nav.pensjon.brev.alder.maler.endring.*
import no.nav.pensjon.brev.alder.maler.info.BekreftelseAvUtsendtKravTilUtlandet
import no.nav.pensjon.brev.alder.maler.info.afpprivatutforetrygdbrev.*
import no.nav.pensjon.brev.alder.maler.innvilgelse.*
import no.nav.pensjon.brev.alder.maler.sivilstand.*
import no.nav.pensjon.brev.alder.maler.stans.VedtakStansAlderspensjonFlyttingMellomLand
import no.nav.pensjon.brev.aldersovergang.InfoAldersovergang67AarAuto

object Fixtures : LetterDataFactory {
    val fellesAuto = FellesFactory.fellesAuto

    @Suppress("UNCHECKED_CAST")
    override fun <T : BrevbakerBrevdata> create(templateType: KClass<out BrevTemplate<T, *>>): T =
        when (templateType) {
            AdhocAlderspensjonGjtOpprydding::class -> createAlderspensjonGjtOppryddingAutoDto() as T
            AdhocTidligereUfoereGradertAP::class -> createAdhocTidligereUfoereGradertAPAutoDto() as T
            VarselGjpForlengetArskull6270::class -> createGjenlevenderett2027Dto() as T
            VarselGjpForlengetArskull6061Utland::class -> createGjenlevenderett2027Dto() as T
            VarselGjpForlengetArskull6270Utland::class -> createGjenlevenderett2027Dto() as T
            VedtakGjpForlengetArskull6270::class -> createGjenlevenderett2027Dto() as T
            VedtakGjpForlengetArskull6061::class -> createGjenlevenderett2027Dto() as T
            VarselGjpOpphorArskull6070Utland::class -> createGjenlevenderett2027Dto() as T
            VarselGjpOpphorArskull6070::class -> createGjenlevenderett2027Dto() as T
            VedtakGjpOpphorArskull6070Utland::class -> createGjenlevenderett2027Dto() as T
            VedtakGjpForlengetArskull6061Utland::class -> createGjenlevenderett2027Dto() as T
            VedtakGjpForlengetArskull6270Utland::class -> createGjenlevenderett2027Dto() as T
            VedtakGjpOpphorArskull6070::class -> createGjenlevenderett2027Dto() as T
            VarselGjpForlengetArskull6061::class -> createGjenlevenderett2027Dto() as T
            AvslagForLiteTrygdetidAP::class -> createAvslagForLiteTrygdetidAPDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAuto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAar::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() as T
            AvslagPaaGjenlevenderettIAlderspensjon::class -> createAvslagPaaGjenlevenderettIAlderspensjon() as T
            AvslagUttakFoerNormertPensjonsalderAP2016Auto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalder2016Auto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016::class -> createAvslagUttakFoerNormertPensjonsalderAP2016Dto() as T
            AvslagGradsendringFoerNormertPensjonsalderAP2016::class -> createAvslagUttakFoerNormertPensjonsalderAP2016Dto() as T
            AvslagUttakFoerNormertPensjonsalderAuto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalderAuto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagUttakFoerNormertPensjonsalder::class -> createAvslagUttakFoerNormertPensjonsalderDto() as T
            AvslagGradsendringFoerNormertPensjonsalder::class -> createAvslagUttakFoerNormertPensjonsalderDto() as T
            AvslagUnder5AartrygdetidAuto::class -> createUnder5AarTrygdetidAutoDto() as T
            InfoFyller67AarSaerskiltSats::class -> EmptyAutobrevdata as T
            AdhocVarselTilBrukerMedForsoergingstilleggTilUtbetaling::class -> EmptyAutobrevdata as T
            AdhocSkjermingstilleggFeilMottaker::class -> EmptyAutobrevdata as T
            AdhocAlderspensjonFraFolketrygden2::class -> EmptyAutobrevdata as T
            AdhocSkjermingstilleggFeilBeroertBruker::class -> EmptyAutobrevdata as T
            AdhocAFPInformasjonOekningToleransebeloep::class -> EmptyAutobrevdata as T
            AdhocVarselTilBrukerForsoergingstilleggIkkeTilUtbetaling::class -> EmptyAutobrevdata as T
            AdhocAlderspensjonFraFolketrygden::class -> EmptyAutobrevdata as T
            AdhocGjenlevendEtter1970::class -> EmptyAutobrevdata as T
            FeilUtsendingAvGjenlevenderett::class -> EmptyAutobrevdata as T
            AdhocAlderspensjonGjtVarselBrev::class -> EmptyAutobrevdata as T
            BekreftelseAvUtsendtKravTilUtlandet::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = lagSaksbehandlervalg()) as T
            EndringAvAlderspensjonFordiDuFyller75AarAuto::class -> createEndringAvAlderspensjonFordiDuFyller75AarAutoDto() as T
            EndringAvAlderspensjonAvdodAuto::class -> createEndringAvAlderspensjonAvdodAuto() as T
            EndringAvAlderspensjonPgaGarantitillegg::class -> createEndringAvAlderspensjonGarantitilleggDto() as T
            EndringAvAlderspensjonSivilstandAuto::class -> createEndringAvAlderspensjonSivilstandAutoDto() as T
            EndringAvAlderspensjonSivilstand::class -> createEndringAvAlderspensjonSivilstandDto() as T
            EndringAvAlderspensjonSivilstandSaerskiltSats::class -> createEndringAvAlderspensjonSivilstandSaerskiltSatsDto() as T
            EndringAvUttaksgradAuto::class -> createEndringAvUttaksgradAutoDto() as T
            EndringPgaOpptjeningAuto::class -> createEndringPgaOpptjeningAutoDto() as T
            ForespoerselOmDokumentasjonAvBotidINorgeAlder::class -> createForespoerselOmDokumentasjonAvBotidINorgeAlderDto() as T
            InnhentingInformasjonFraBruker::class -> createInnhentingInformasjonFraBrukerDto() as T
            InnvilgelseAvAfpAuto::class -> createInnvilgelseAvAfpAutoDto() as T
            AvslagAfpPrivat::class -> createAvslagAfpPrivatDto() as T
            AvslagAfpPrivatAuto::class -> createAvslagAfpPrivatAutoDto() as T
            AvslagAfpGammel::class -> createAvslagAfpGammelDto() as T
            VedtakAfpPrivatEndring::class -> createVedtakAfpPrivatEndringDto() as T
            VedtakAfpEtteroppgjoerIngenEndring::class -> createVedtakAfpEtteroppgjoerIngenEndringDto() as T
            VedtakAfpEtteroppgjoerEtterbetaling::class -> createVedtakAfpEtteroppgjoerEtterbetalingDto() as T
            VedtakAfpEtteroppgjoerIngenEndringAndreAvvik::class -> createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto() as T
            InnvilgelseAvAfp::class -> createInnvilgelseAvAfpDto() as T
            InnvilgelseAvAfpOffentligSektor::class -> createInnvilgelseAvAfpOffentligSektorDto() as T
            InnvilgelseAvAlderspensjonAuto::class -> createInnvilgelseAvAlderspensjonAutoDto() as T
            InnvilgelseAvAlderspensjon::class -> createInnvilgelseAvAlderspensjonDto() as T
            InnvilgelseAvAlderspensjonTrygdeavtale::class -> createInnvilgelseAvAlderspensjonTrygdeavtaleDto() as T
            VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvar::class -> createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto() as T
            VedtakAfpEtteroppgjoerEtterbetalingAuto::class -> createVedtakAfpEtteroppgjoerEtterbetalingAutoDto() as T
            VedtakAfpEtteroppgjoerEtterbetalingEtterSvar::class -> createVedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto() as T
            VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAuto::class -> createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto() as T
            VedtakAfpEtteroppgjoerIngenEndringEtterSvar::class -> createVedtakAfpEtteroppgjoerIngenEndringEtterSvarDto() as T
            VedtakAfpEtteroppgjoerTilbakekrevingAuto::class -> createVedtakAfpEtteroppgjoerTilbakekrevingAutoDto() as T
            VarselAfpEtteroppgjoerForeloepigAuto::class -> createVarselAfpEtteroppgjoerForeloepigAutoDto() as T
            VarselAfpEtteroppgjoerForeloepig::class -> createVarselAfpEtteroppgjoerForeloepigDto() as T
            VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysninger::class -> createVedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto() as T
            VedtakAfpEtteroppgjoerIngenEndringAuto::class -> createVedtakAfpEtteroppgjoerIngenEndringAutoDto() as T
            VedtakEndringAfpOffentligSektor::class -> createVedtakEndringAfpOffentligSektorDto() as T
            InfoAldersovergangEps60AarAuto::class -> InfoAldersovergangEps60AarAutoDto(ytelse = Ytelse.ALDER) as T
            InfoAldersovergangEps62AarAuto::class -> InfoAldersovergangEps62AarAutoDto(ytelse = YtelseType.ALDER) as T
            InfoAldersovergang67AarAuto::class -> createInfoAlderspensjonOvergang67AarAutoDto() as T
            OmregningAlderUfore2016Auto::class -> createOmregningAlderUfore2016Dto() as T
            OmregningAlderUfore2016::class -> createOmregningAlderUfore2016RedigerbarDto() as T
            VedtakAldersovergang67AarGarantitilleggAuto::class -> createVedtakAldersovergang67AarGarantitilleggAutoDto() as T
            VedtakEndringAvAlderspensjonGjenlevenderettigheter::class -> createVedtakEndringAvAlderspensjonGjenlevenderettigheterDto() as T
            VedtakEndringAvAlderspensjonInstitusjonsopphold::class -> createVedtakEndringAvAlderspensjonInstitusjonsoppholdDto() as T
            VedtakEndringAFPEndretOpptjeningAuto::class -> createVedtakEndringAFPEndretOpptjeningAutoDto() as T
            VedtakEndringAvUttaksgrad::class -> createVedtakEndringAvUttaksgradDto() as T
            VedtakEndringAvUttaksgradStansInitiertAvBrukerEllerVerge::class -> createVedtakEndringAvUttaksgradStansBrukerEllerVergeDto() as T
            VedtakEndringAvUttaksgradStansIkkeInitiertAvBrukerEllerVerge::class -> createVedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto() as T
            VedtakEndringVedFlyttingMellomLand::class -> createVedtakEndringVedFlyttingMellomLandDto() as T
            VedtakEndringAvAlderspensjonFordiOpptjeningErEndret::class -> createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto() as T
            VedtakStansAlderspensjonFlyttingMellomLand::class -> createVedtakStansAlderspensjonFlyttingMellomLandDto() as T
            VedtakOmregningAFPTilEnsligPensjonistAuto::class -> createVedtakOmregningAFPTilEnsligPensjonistAutoDto() as T
            VedtakOmregningGjenlevendepensjonTilAlderspensjonAuto::class -> createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto() as T
            UforetrygdSokerAfpPrivat::class -> createUforeTrygdSokerAfpPrivatDto() as T
            AfpPrivatSokerUforeTrygd::class -> createAfpPrivatSokerUforeTrygdDto() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${templateType.qualifiedName}")
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
