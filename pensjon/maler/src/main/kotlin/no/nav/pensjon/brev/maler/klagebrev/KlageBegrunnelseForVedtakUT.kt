package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.RedigerbarTemplate

object KlageBegrunnelseForVedtakUT : RedigerbarTemplate<EmptyRedigerbarBrevdata

override val featureToggle = no.nav.pensjon.brev.maler.FeatureToggles.brevmalKlageBegrunnelseForVedtakUT.toggle

override val kode = "UT_KLAGE_BEGRUNNELSE_FOR_VEDTAK"
    override val kategori = no.nav.pensjon.brev.model.Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = setOf(no.nav.pensjon.brev.api.model.Sakstype.UFOREP)> {
}