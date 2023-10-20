package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.template.*


val prodAutobrevTemplates: Set<AutobrevTemplate<*>> = setOf(
    FeilUtsendingAvGjenlevenderett,
    ForhaandsvarselEtteroppgjoerUfoeretrygdAuto,
    GjenlevendeInfoEtter1970,
    GjenlevendeInfoFoer1971,
    AdhocRegelendretGjenlevendetilleggKonvertert,
    AdhocRegelendretGjenlevendetillegg,
    OmsorgEgenAuto,
    OpphoerBarnetilleggAuto,
    OpptjeningVedForhoeyetHjelpesats,
    UfoerOmregningEnslig,
    UngUfoerAuto,
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

    fun getRedigerbartBrev(kode: Brevkode.Redigerbar): LetterTemplate<*, out BrevbakerBrevdata>? =
        redigerbareBrevMap[kode]?.template
}