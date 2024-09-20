package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.legacy.EndretUfoeretrygdPGAInntektLegacy
import no.nav.pensjon.brev.maler.legacy.EtteroppgjoerEtterbetalingAutoLegacy
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAuto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object ProductionTemplates {
    val autobrev: Set<AutobrevTemplate<BrevbakerBrevdata>> = setOf(
        AdhocAlderspensjonFraFolketrygden,
        AdhocGjenlevendEtter1970,
        AdhocUfoeretrygdEtterbetalingDagpenger,
        AdhocUfoeretrygdKombiDagpenger,
        AdhocUfoeretrygdKombiDagpengerInntektsavkorting,
        AdhocUfoeretrygdVarselOpphoerEktefelletillegg,
        AdhocVarselOpphoerMedHvilendeRett,
        EndretUfoeretrygdPGAInntektLegacy,
        EtteroppgjoerEtterbetalingAutoLegacy,
        ForhaandsvarselEtteroppgjoerUfoeretrygdAuto,
        OmsorgEgenAuto,
        OpphoerBarnetilleggAuto,
        OpptjeningVedForhoeyetHjelpesats,
        UfoerOmregningEnslig,
        UngUfoerAuto,
        VarselSaksbehandlingstidAuto,
    )
    val redigerbare: Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(
        InformasjonOmSaksbehandlingstid
    )
}