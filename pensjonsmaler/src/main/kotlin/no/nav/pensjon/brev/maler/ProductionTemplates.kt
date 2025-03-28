package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.alder.AvslagUttakFoerNormertPensjonsalderAuto
import no.nav.pensjon.brev.maler.alder.AvslagUttakFoerNormertPensjonsalder
import no.nav.pensjon.brev.maler.alder.InfoAldersovergang67AarAuto
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalder
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderAuto
import no.nav.pensjon.brev.maler.legacy.EndretBarnetilleggUfoerertrygd
import no.nav.pensjon.brev.maler.legacy.EndretUfoeretrygdPGAInntektLegacy
import no.nav.pensjon.brev.maler.legacy.EndretUforetrygdPGAOpptjeningLegacy
import no.nav.pensjon.brev.maler.legacy.EtteroppgjoerEtterbetalingAutoLegacy
import no.nav.pensjon.brev.maler.legacy.redigerbar.AvslagUfoeretrygd
import no.nav.pensjon.brev.maler.redigerbar.*
import no.nav.pensjon.brev.maler.ufoereBrev.EndretUfoeretrygdPGAInntektV2
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAuto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object ProductionTemplates : AllTemplates {
    private val autobrev: Set<AutobrevTemplate<BrevbakerBrevdata>> = setOf(
        AdhocAFPInformasjonOekningToleransebeloep,
        AdhocAlderspensjonFraFolketrygden,
        AdhocAlderspensjonFraFolketrygden2,
        AdhocAlderspensjonGjtVarselBrev,
        AdhocFeilEtteroppgjoer2023,
        AdhocGjenlevendEtter1970,
        AdhocInformasjonHvilendeRett4Aar,
        AdhocMidlertidigOpphoerHvilenderett10Aar,
        AdhocUfoeretrygdEtterbetalingDagpenger,
        AdhocUfoeretrygdKombiDagpenger,
        AdhocUfoeretrygdKombiDagpengerInntektsavkorting,
        AdhocUfoeretrygdVarselOpphoerEktefelletillegg,
        AdhocVarselOpphoerMedHvilendeRett,
        AdhocVarselTilBrukerForsoergingstilleggIkkeTilUtbetaling,
        AdhocVarselTilBrukerMedForsoergingstilleggTilUtbetaling,
        EndretBarnetilleggUfoerertrygd,
        EndretUfoeretrygdPGAInntektLegacy,
        EndretUfoeretrygdPGAInntektV2,
        EtteroppgjoerEtterbetalingAutoLegacy,
        FeilUtsendingAvGjenlevenderett,
        ForhaandsvarselEtteroppgjoerUfoeretrygdAuto,
        InfoAldersovergang67AarAuto,
        OmsorgEgenAuto,
        OpphoerBarnetilleggAuto,
        OpptjeningVedForhoeyetHjelpesats,
        UfoerOmregningEnslig,
        UngUfoerAuto,
        VarselSaksbehandlingstidAuto,
        EndretUforetrygdPGAOpptjeningLegacy,
        AvslagUttakFoerNormertPensjonsalderAuto,
        AvslagGradsendringFoerNormertPensjonsalderAuto,
    )

    private val redigerbare: Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(
        AvslagUfoeretrygd,
        AvslagGradsendringFoerNormertPensjonsalder,
        AvslagUttakFoerNormertPensjonsalder,
        BekreftelsePaaFlyktningstatus,
        ForespoerselOmDokumentasjonAvBotidINorgeAlder,
        ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte,
        ForhaandsvarselVedTilbakekreving,
        InformasjonOmSaksbehandlingstid,
        InformasjonOmSaksbehandlingstidUT,
        InnhentingDokumentasjonFraBruker,
        InnhentingInformasjonFraBruker,
        InnhentingOpplysningerFraBruker,
        OmsorgEgenManuell,
        OrienteringOmSaksbehandlingstid,
        VarselOmMuligAvslag,
        VarselRevurderingAvPensjon,
        VarselTilbakekrevingAvFeilutbetaltBeloep,
    )

    override fun hentAutobrevmaler() = autobrev

    override fun hentRedigerbareMaler() = redigerbare
}