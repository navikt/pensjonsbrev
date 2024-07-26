package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar

import no.nav.pensjon.brevbaker.api.model.Kroner


data class BeregningsVilkar(
    val ieubegrunnelse: String?,
    val ifubegrunnelse: String?,
    val ifuinntekt: Kroner?,
    val trygdetid: Trygdetid?,
    val uforegrad: Int?
)