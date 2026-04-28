package no.nav.pensjon.brev.alder.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocAFPInformasjonOekningToleransebeloep
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocAlderspensjonFraFolketrygden
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocAlderspensjonFraFolketrygden2
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocAlderspensjonGjtOpprydding
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocAlderspensjonGjtVarselBrev
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocGjenlevendEtter1970
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocSkjermingstilleggFeilBeroertBruker
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocSkjermingstilleggFeilMottaker
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocTidligereUfoereGradertAP
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocVarselTilBrukerForsoergingstilleggIkkeTilUtbetaling
import no.nav.pensjon.brev.alder.maler.adhoc.AdhocVarselTilBrukerMedForsoergingstilleggTilUtbetaling
import no.nav.pensjon.brev.alder.maler.adhoc.FeilUtsendingAvGjenlevenderett
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VarselGjpForlengetArskull6061
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VarselGjpForlengetArskull6061Utland
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VarselGjpForlengetArskull6270
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VarselGjpForlengetArskull6270Utland
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VarselGjpOpphorArskull6070
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VarselGjpOpphorArskull6070Utland
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VedtakGjpForlengetArskull6061
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VedtakGjpForlengetArskull6061Utland
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VedtakGjpForlengetArskull6270
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VedtakGjpForlengetArskull6270Utland
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VedtakGjpOpphorArskull6070
import no.nav.pensjon.brev.alder.maler.adhoc.gjenlevenderett2027.VedtakGjpOpphorArskull6070Utland
import no.nav.pensjon.brev.alder.maler.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAuto
import no.nav.pensjon.brev.alder.maler.aldersovergang.InfoAldersovergangEps60AarAuto
import no.nav.pensjon.brev.alder.maler.aldersovergang.InfoAldersovergangEps62AarAuto
import no.nav.pensjon.brev.alder.maler.aldersovergang.InfoFyller67AarSaerskiltSats
import no.nav.pensjon.brev.alder.maler.aldersovergang.VedtakAldersovergang67AarGarantitilleggAuto
import no.nav.pensjon.brev.alder.maler.aldersovergang.VedtakEndringAFPEndretOpptjeningAuto
import no.nav.pensjon.brev.alder.maler.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAuto
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.OmregningAlderUfore2016
import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.OmregningAlderUfore2016Auto
import no.nav.pensjon.brev.alder.maler.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalder
import no.nav.pensjon.brev.alder.maler.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalder2016Auto
import no.nav.pensjon.brev.alder.maler.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderAP2016
import no.nav.pensjon.brev.alder.maler.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderAuto
import no.nav.pensjon.brev.alder.maler.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderFoerEttAar
import no.nav.pensjon.brev.alder.maler.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAuto
import no.nav.pensjon.brev.alder.maler.avslag.uttak.AvslagUnder5AartrygdetidAuto
import no.nav.pensjon.brev.alder.maler.avslag.uttak.AvslagUttakFoerNormertPensjonsalderAP2016
import no.nav.pensjon.brev.alder.maler.avslag.uttak.AvslagUttakFoerNormertPensjonsalderAP2016Auto
import no.nav.pensjon.brev.alder.maler.avslag.uttak.AvslagUttakFoerNormertPensjonsalderAuto
import no.nav.pensjon.brev.alder.maler.sivilstand.EndringAvAlderspensjonAvdodAuto
import no.nav.pensjon.brev.alder.maler.info.BekreftelseAvUtsendtKravTilUtlandet
import no.nav.pensjon.brev.alder.maler.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygd
import no.nav.pensjon.brev.alder.maler.info.afpprivatutforetrygdbrev.UforetrygdSokerAfpPrivat
import no.nav.pensjon.brev.alder.maler.sivilstand.EndringAvAlderspensjonPgaGarantitillegg
import no.nav.pensjon.brev.alder.maler.sivilstand.EndringAvAlderspensjonSivilstand
import no.nav.pensjon.brev.alder.maler.sivilstand.EndringAvAlderspensjonSivilstandAuto
import no.nav.pensjon.brev.alder.maler.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSats
import no.nav.pensjon.brev.alder.maler.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAuto
import no.nav.pensjon.brev.alder.maler.stans.VedtakStansAlderspensjonFlyttingMellomLand
import no.nav.pensjon.brev.alder.maler.vedlegg.alltidValgbare.skjemaForBankopplysninger
import no.nav.pensjon.brev.alder.maler.vedlegg.alltidValgbare.uttaksskjema
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.aldersovergang.InfoAldersovergang67AarAuto
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object AlderTemplates : AllTemplates {
    override fun hentAutobrevmaler(): Set<AutobrevTemplate<AutobrevData>> =
        setOf(
            AdhocAFPInformasjonOekningToleransebeloep,
            AdhocAlderspensjonFraFolketrygden,
            AdhocAlderspensjonFraFolketrygden2,
            AdhocAlderspensjonGjtOpprydding,
            AdhocAlderspensjonGjtVarselBrev,
            AdhocGjenlevendEtter1970,
            AdhocSkjermingstilleggFeilBeroertBruker,
            AdhocSkjermingstilleggFeilMottaker,
            AdhocTidligereUfoereGradertAP,
            AdhocVarselTilBrukerForsoergingstilleggIkkeTilUtbetaling,
            AdhocVarselTilBrukerMedForsoergingstilleggTilUtbetaling,
            AvslagGradsendringFoerNormertPensjonsalder2016Auto,
            AvslagGradsendringFoerNormertPensjonsalderAuto,
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAuto,
            AvslagUttakFoerNormertPensjonsalderAP2016Auto,
            AvslagUttakFoerNormertPensjonsalderAuto,
            AvslagUnder5AartrygdetidAuto,
            EndringAvAlderspensjonAvdodAuto,
            EndringAvAlderspensjonSivilstandAuto,
            EndringAvAlderspensjonFordiDuFyller75AarAuto,
            OmregningAlderUfore2016Auto,
            FeilUtsendingAvGjenlevenderett,
            VarselGjpForlengetArskull6061,
            VarselGjpForlengetArskull6061Utland,
            VarselGjpForlengetArskull6270,
            VarselGjpForlengetArskull6270Utland,
            VarselGjpOpphorArskull6070,
            VarselGjpOpphorArskull6070Utland,
            VedtakGjpForlengetArskull6061,
            VedtakGjpForlengetArskull6061Utland,
            VedtakGjpForlengetArskull6270,
            VedtakGjpForlengetArskull6270Utland,
            VedtakGjpOpphorArskull6070,
            VedtakGjpOpphorArskull6070Utland,
            InfoAldersovergangEps60AarAuto,
            InfoAldersovergangEps62AarAuto,
            InfoAldersovergang67AarAuto,
            InfoFyller67AarSaerskiltSats,
            VedtakAldersovergang67AarGarantitilleggAuto,
            VedtakEndringAFPEndretOpptjeningAuto,
            VedtakOmregningAFPTilEnsligPensjonistAuto,
            VedtakOmregningGjenlevendepensjonTilAlderspensjonAuto,
        )

    override fun hentRedigerbareMaler(): Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> =
        setOf(
            AvslagGradsendringFoerNormertPensjonsalder,
            AvslagGradsendringFoerNormertPensjonsalderAP2016,
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAar,
            AvslagUttakFoerNormertPensjonsalder,
            AvslagUttakFoerNormertPensjonsalderAP2016,
            BekreftelseAvUtsendtKravTilUtlandet,
            EndringAvAlderspensjonPgaGarantitillegg,
            EndringAvAlderspensjonSivilstand,
            EndringAvAlderspensjonSivilstandSaerskiltSats,
            OmregningAlderUfore2016,
            VedtakStansAlderspensjonFlyttingMellomLand,
            AfpPrivatSokerUforeTrygd,
            UforetrygdSokerAfpPrivat,
        )

    override fun hentAlltidValgbareVedlegg(): Set<AlltidValgbartVedlegg<*>> = setOf(
        AlltidValgbartVedlegg(
            skjemaForBankopplysninger,
            Aldersbrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
        ),
        AlltidValgbartVedlegg(
            uttaksskjema,
            Aldersbrevkoder.AlltidValgbareVedlegg.UTTAKSSKJEMA
        )
    )
}
