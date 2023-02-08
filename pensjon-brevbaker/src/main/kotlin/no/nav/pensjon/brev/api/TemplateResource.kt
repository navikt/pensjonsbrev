package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.template.*


val prodVedtaksbrevTemplates: Set<VedtaksbrevTemplate<*>> = setOf(
    OmsorgEgenAuto,
    UngUfoerAuto,
    UfoerOmregningEnslig,
    OpptjeningVedForhoeyetHjelpesats,
    OpphoerBarnetilleggAuto,
)

val prodRedigerbareTemplates: Set<RedigerbarTemplate<*>> = setOf(
    InformasjonOmSaksbehandlingstid
)

class TemplateResource(
    vedtaksbrevTemplates: Set<VedtaksbrevTemplate<*>> = prodVedtaksbrevTemplates,
    redigerbareTemplates: Set<RedigerbarTemplate<*>> = prodRedigerbareTemplates,
) {
    private val vedtaksbrevMap: Map<Brevkode.Vedtak, VedtaksbrevTemplate<*>> =
        vedtaksbrevTemplates.associateBy { it.kode }

    private val redigerbareBrevMap: Map<Brevkode.Redigerbar, RedigerbarTemplate<*>> =
        redigerbareTemplates.associateBy { it.kode }

    fun getVedtaksbrev(): Set<Brevkode.Vedtak> =
        vedtaksbrevMap.keys

    fun getVedtaksbrev(kode: Brevkode.Vedtak): LetterTemplate<*, *>? =
        vedtaksbrevMap[kode]?.template

    fun getRedigerbareBrev(): Set<Brevkode.Redigerbar> =
        redigerbareBrevMap.keys

    fun getRedigerbartBrev(kode: Brevkode.Redigerbar): LetterTemplate<*, *>? =
        redigerbareBrevMap[kode]?.template
}