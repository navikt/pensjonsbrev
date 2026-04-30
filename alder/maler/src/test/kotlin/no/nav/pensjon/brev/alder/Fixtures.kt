package no.nav.pensjon.brev.alder

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.alder.maler.adhoc.createAdhocTidligereUfoereGradertAPAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createEndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createMaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.createOmregningAlderUfore2016Dto
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.createOmregningAlderUfore2016RedigerbarDto
import no.nav.pensjon.brev.alder.maler.avslag.uttak.createUnder5AarTrygdetidAutoDto
import no.nav.pensjon.brev.alder.maler.etteroppgjoer.afp.createAfpEtteroppgjoerVedtakIngenEndringAutoDto
import no.nav.pensjon.brev.alder.maler.etteroppgjoer.afp.createAfpEtteroppgjoerVedtakIngenEndringDto
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
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.adhoc.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.alder.model.adhoc.AdhocTidligereUfoereGradertAPAutoDto
import no.nav.pensjon.brev.alder.model.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.alder.model.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.InfoAldersovergangEps60AarAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.InfoAldersovergangEps62AarAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.Ytelse
import no.nav.pensjon.brev.alder.model.aldersovergang.YtelseType
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016Dto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016RedigerbarDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUnder5AarTrygdetidAutoDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAP2016Dto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.alder.model.avslag.OpplysningerBruktIBeregningen
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakIngenEndringAutoDto
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakIngenEndringDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDto
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
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.fixtures.alder.createAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAP2016Dto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.fixtures.alder.createGjenlevenderett2027Dto
import no.nav.pensjon.brev.fixtures.alder.createInfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.fixtures.alder.createOpplysningerBruktIBeregningen
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {
    val fellesAuto = FellesFactory.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            AdhocAlderspensjonGjtOppryddingAutoDto::class -> createAlderspensjonGjtOppryddingAutoDto() as T
            AdhocTidligereUfoereGradertAPAutoDto::class -> createAdhocTidligereUfoereGradertAPAutoDto() as T
            AfpPrivatSokerUforeTrygdDto::class -> createAfpPrivatSokerUforeTrygdDto() as T
            AfpEtteroppgjoerVedtakIngenEndringAutoDto::class -> createAfpEtteroppgjoerVedtakIngenEndringAutoDto() as T
            AfpEtteroppgjoerVedtakIngenEndringDto::class -> createAfpEtteroppgjoerVedtakIngenEndringDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() as T
            AvslagUnder5AarTrygdetidAutoDto::class -> createUnder5AarTrygdetidAutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016AutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016Dto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016Dto() as T
            AvslagUttakFoerNormertPensjonsalderAutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagUttakFoerNormertPensjonsalderDto::class -> createAvslagUttakFoerNormertPensjonsalderDto() as T
            EmptyAutobrevdata::class -> EmptyAutobrevdata as T
            EmptyRedigerbarBrevdata::class -> EmptyRedigerbarBrevdata as T
            EndringAvAlderspensjonAvdodAutoDto::class -> createEndringAvAlderspensjonAvdodAuto() as T
            EndringAvAlderspensjonFordiDuFyller75AarAutoDto::class -> createEndringAvAlderspensjonFordiDuFyller75AarAutoDto() as T
            EndringAvAlderspensjonGarantitilleggDto::class -> createEndringAvAlderspensjonGarantitilleggDto() as T
            EndringAvAlderspensjonSivilstandAutoDto::class -> createEndringAvAlderspensjonSivilstandAutoDto() as T
            EndringAvAlderspensjonSivilstandDto::class -> createEndringAvAlderspensjonSivilstandDto() as T
            EndringAvAlderspensjonSivilstandSaerskiltSatsDto::class -> createEndringAvAlderspensjonSivilstandSaerskiltSatsDto() as T
            Gjenlevenderett2027Dto::class -> createGjenlevenderett2027Dto() as T
            InfoAldersovergangEps60AarAutoDto::class -> InfoAldersovergangEps60AarAutoDto(ytelse = Ytelse.ALDER) as T
            InfoAldersovergangEps62AarAutoDto::class -> InfoAldersovergangEps62AarAutoDto(ytelse = YtelseType.ALDER) as T
            InfoAlderspensjonOvergang67AarAutoDto::class -> createInfoAlderspensjonOvergang67AarAutoDto() as T
            OmregningAlderUfore2016Dto::class -> createOmregningAlderUfore2016Dto() as T
            OmregningAlderUfore2016RedigerbarDto::class -> createOmregningAlderUfore2016RedigerbarDto() as T
            UforeTrygdSokerAfpPrivatDto::class -> createUforeTrygdSokerAfpPrivatDto() as T
            VedtakAldersovergang67AarGarantitilleggAutoDto::class -> createVedtakAldersovergang67AarGarantitilleggAutoDto() as T
            VedtakEndringAFPEndretOpptjeningAutoDto::class -> createVedtakEndringAFPEndretOpptjeningAutoDto() as T
            VedtakOmregningAFPTilEnsligPensjonistAutoDto::class -> createVedtakOmregningAFPTilEnsligPensjonistAutoDto() as T
            VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto::class -> createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto() as T
            VedtakStansAlderspensjonFlyttingMellomLandDto::class -> createVedtakStansAlderspensjonFlyttingMellomLandDto() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when(letterDataType) {
        DineRettigheterOgMulighetTilAaKlageDto::class -> createDineRettigheterOgMulighetTilAaKlageDto() as T
        AFpPrivatSokerUforeTrygdVedleggDto::class -> createAfpPrivatSokerUforeTrygdVedleggDto() as T
        MaanedligPensjonFoerSkattDto::class -> createMaanedligPensjonFoerSkatt() as T
        MaanedligPensjonFoerSkattAFPDto::class -> createMaanedligPensjonFoerSkattAFPDto() as T
        MaanedligPensjonFoerSkattAFPOffentligDto::class -> createMaanedligPensjonFoerSkattAFPOffentligDto() as T
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
