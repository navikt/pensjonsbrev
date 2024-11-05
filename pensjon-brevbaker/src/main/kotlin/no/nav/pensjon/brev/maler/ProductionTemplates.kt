package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.legacy.EndretBarnetilleggUfoerertrygd
import no.nav.pensjon.brev.maler.legacy.EndretUfoeretrygdPGAInntektLegacy
import no.nav.pensjon.brev.maler.legacy.EndretUforetrygdPGAOpptjeningLegacy
import no.nav.pensjon.brev.maler.legacy.EtteroppgjoerEtterbetalingAutoLegacy
import no.nav.pensjon.brev.maler.legacy.redigerbar.AvslagUfoeretrygd
import no.nav.pensjon.brev.maler.redigerbar.BekreftelsePaaFlyktningstatus
import no.nav.pensjon.brev.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeAlder
import no.nav.pensjon.brev.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte
import no.nav.pensjon.brev.maler.redigerbar.VarselOmMuligAvslag
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.maler.redigerbar.OrienteringOmSaksbehandlingstid
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAuto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object ProductionTemplates {
    val autobrev: Set<AutobrevTemplate<BrevbakerBrevdata>> = setOf(
        AdhocAlderspensjonFraFolketrygden,
        AdhocAlderspensjonFraFolketrygden2,
        AdhocGjenlevendEtter1970,
        AdhocFeilEtteroppgjoer2023,
        AdhocUfoeretrygdEtterbetalingDagpenger,
        AdhocUfoeretrygdKombiDagpenger,
        AdhocInformasjonHvilendeRett4Aar,
        AdhocMidlertidigOpphoerHvilenderett10Aar,
        AdhocUfoeretrygdKombiDagpengerInntektsavkorting,
        AdhocUfoeretrygdVarselOpphoerEktefelletillegg,
        AdhocVarselOpphoerMedHvilendeRett,
        EndretBarnetilleggUfoerertrygd,
        EndretUfoeretrygdPGAInntektLegacy,
        EtteroppgjoerEtterbetalingAutoLegacy,
        FeilUtsendingAvGjenlevenderett,
        ForhaandsvarselEtteroppgjoerUfoeretrygdAuto,
        OmsorgEgenAuto,
        OpphoerBarnetilleggAuto,
        OpptjeningVedForhoeyetHjelpesats,
        UfoerOmregningEnslig,
        UngUfoerAuto,
        VarselSaksbehandlingstidAuto,
        EndretUforetrygdPGAOpptjeningLegacy,
    )
    val redigerbare: Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(
        AvslagUfoeretrygd,
        BekreftelsePaaFlyktningstatus,
        ForespoerselOmDokumentasjonAvBotidINorgeAlder,
        ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte,
        InformasjonOmSaksbehandlingstid,
        OrienteringOmSaksbehandlingstid,
        VarselOmMuligAvslag,
    )
}