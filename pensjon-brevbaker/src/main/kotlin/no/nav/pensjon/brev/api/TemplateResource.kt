package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAuto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


val prodAutobrevTemplates: Set<AutobrevTemplate<*>> = setOf(
    AdhocAlderspensjonFraFolketrygden,
    AdhocGjenlevendEtter1970,
    AdhocUfoeretrygdEtterbetalingDagpenger,
    AdhocUfoeretrygdKombiDagpenger,
    AdhocUfoeretrygdKombiDagpengerInntektsavkorting,
    AdhocVarselOpphoerMedHvilendeRett,
    ForhaandsvarselEtteroppgjoerUfoeretrygdAuto,
    OmsorgEgenAuto,
    OpphoerBarnetilleggAuto,
    OpptjeningVedForhoeyetHjelpesats,
    UfoerOmregningEnslig,
    UngUfoerAuto,
    VarselSaksbehandlingstidAuto,
)

val prodRedigerbareTemplates: Set<RedigerbarTemplate<*>> = setOf(
    InformasjonOmSaksbehandlingstid
)

class TemplateResource(
    autobrevTemplates: Set<AutobrevTemplate<*>> = prodAutobrevTemplates,
    redigerbareTemplates: Set<RedigerbarTemplate<*>> = prodRedigerbareTemplates,
) {
    private val autoBrevMap: Map<Brevkode.AutoBrev, AutobrevTemplate<*>> =
        autobrevTemplates.associateBy { it.kode }

    private val redigerbareBrevMap: Map<Brevkode.Redigerbar, RedigerbarTemplate<*>> =
        redigerbareTemplates.associateBy { it.kode }

    fun getAutoBrev(): Set<Brevkode.AutoBrev> =
        autoBrevMap.keys

    fun getAutoBrev(kode: Brevkode.AutoBrev): LetterTemplate<*, out BrevbakerBrevdata>? =
        autoBrevMap[kode]?.template

    fun getRedigerbareBrev(): Set<Brevkode.Redigerbar> =
        redigerbareBrevMap.keys

    fun getRedigerbareBrevMedMetadata(): Map<Brevkode.Redigerbar, LetterMetadata> =
        redigerbareBrevMap.mapValues { (_, v) -> v.template.letterMetadata }

    fun getRedigerbartBrev(kode: Brevkode.Redigerbar): LetterTemplate<*, out BrevbakerBrevdata>? =
        redigerbareBrevMap[kode]?.template
}