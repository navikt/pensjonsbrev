package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertBrev
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

object ApiV2 {
    data class OppdaterBrevRequest(
        val saksbehandlerValg: SaksbehandlervalgMap,
        val redigertBrev: EditV2.Letter,
    )

    data class OppdaterAttesteringRequest(
        val redigertBrev: EditV2.Letter,
    )

    data class RedigertVedleggRequest(val redigertVedlegg: EditV2.Attachment)

    data class BrevResponse(
        val info: Api.BrevInfo,
        val redigertBrev: EditV2.Letter,
        val redigertBrevHash: Hash<VersjonertBrev>,
        val saksbehandlerValg: SaksbehandlervalgMap,
        val propertyUsage: Set<LetterMarkupWithDataUsage.Property>?,
        val valgteVedlegg: List<AlltidValgbartVedleggBrevkode>?,
    )
}
