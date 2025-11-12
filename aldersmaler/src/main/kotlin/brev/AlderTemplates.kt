package no.nav.pensjon.brev

import brev.adhoc.*
import brev.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAuto
import brev.aldersovergang.InfoAldersovergangEps60AarAuto
import brev.aldersovergang.InfoAldersovergangEps62AarAuto
import brev.aldersovergang.InfoFyller67AarSaerskiltSats
import brev.aldersovergang.VedtakAldersovergang67AarGarantitilleggAuto
import brev.aldersovergang.VedtakEndringAFPEndretOpptjeningAuto
import brev.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalder
import brev.sivilstand.EndringAvAlderspensjonPgaGarantitillegg
import brev.sivilstand.EndringAvAlderspensjonSivilstand
import brev.sivilstand.EndringAvAlderspensjonSivilstandAuto
import brev.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSats
import brev.stans.VedtakStansAlderspensjonFlyttingMellomLand
import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.alder.AvslagUttakFoerNormertPensjonsalder
import no.nav.pensjon.brev.maler.alder.AvslagUttakFoerNormertPensjonsalderAP2016
import no.nav.pensjon.brev.maler.alder.AvslagUttakFoerNormertPensjonsalderAP2016Auto
import no.nav.pensjon.brev.maler.alder.AvslagUttakFoerNormertPensjonsalderAuto
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalder2016Auto
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderAP2016
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderAuto
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderFoerEttAar
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAuto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object AlderTemplates : AllTemplates {
    override fun hentAutobrevmaler(): Set<AutobrevTemplate<BrevbakerBrevdata>> =
        setOf(
            AdhocAFPInformasjonOekningToleransebeloep,
            AdhocAlderspensjonFraFolketrygden,
            AdhocAlderspensjonFraFolketrygden2,
            AdhocAlderspensjonGjtOpprydding,
            AdhocAlderspensjonGjtVarselBrev,
            AdhocSkjermingstilleggFeilBeroertBruker,
            AdhocSkjermingstilleggFeilMottaker,
            AdhocVarselTilBrukerForsoergingstilleggIkkeTilUtbetaling,
            AdhocVarselTilBrukerMedForsoergingstilleggTilUtbetaling,
            AvslagGradsendringFoerNormertPensjonsalder2016Auto,
            AvslagGradsendringFoerNormertPensjonsalderAuto,
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAuto,
            AvslagUttakFoerNormertPensjonsalderAP2016Auto,
            AvslagUttakFoerNormertPensjonsalderAuto,
            EndringAvAlderspensjonSivilstandAuto,
            EndringAvAlderspensjonFordiDuFyller75AarAuto,
            FeilUtsendingAvGjenlevenderett,
            InfoAldersovergangEps60AarAuto,
            InfoAldersovergangEps62AarAuto,
            InfoFyller67AarSaerskiltSats,
            VedtakAldersovergang67AarGarantitilleggAuto,
            VedtakEndringAFPEndretOpptjeningAuto,
        )

    override fun hentRedigerbareMaler(): Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> =
        setOf(
            AvslagGradsendringFoerNormertPensjonsalder,
            AvslagGradsendringFoerNormertPensjonsalderAP2016,
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAar,
            AvslagUttakFoerNormertPensjonsalder,
            AvslagUttakFoerNormertPensjonsalderAP2016,
            EndringAvAlderspensjonPgaGarantitillegg,
            EndringAvAlderspensjonSivilstand,
            EndringAvAlderspensjonSivilstandSaerskiltSats,
            VedtakStansAlderspensjonFlyttingMellomLand,
        )
}
