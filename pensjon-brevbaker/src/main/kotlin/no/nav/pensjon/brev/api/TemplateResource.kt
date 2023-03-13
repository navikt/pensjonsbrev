package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.maler.adhoc.GjenlevendeInfoEtter1970
import no.nav.pensjon.brev.maler.adhoc.GjenlevendeInfoFoer1971
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.template.*


val prodAutobrevTemplates: Set<AutobrevTemplate<*>> = setOf(
    GjenlevendeInfoEtter1970,
    GjenlevendeInfoFoer1971,
    OmsorgEgenAuto,
    OpphoerBarnetilleggAuto,
    OpptjeningVedForhoeyetHjelpesats,
    UfoerOmregningEnslig,
    UngUfoerAuto,
    UngUfoerAutoTmp, // TODO slett etter overgangsfase i forbindelse med endring i brevkoden til ung ufør auto
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

    fun getAutoBrev(kode: Brevkode.AutoBrev): LetterTemplate<*, *>? =
        autoBrevMap[kode]?.template

    fun getRedigerbareBrev(): Set<Brevkode.Redigerbar> =
        redigerbareBrevMap.keys

    fun getRedigerbartBrev(kode: Brevkode.Redigerbar): LetterTemplate<*, *>? =
        redigerbareBrevMap[kode]?.template
}