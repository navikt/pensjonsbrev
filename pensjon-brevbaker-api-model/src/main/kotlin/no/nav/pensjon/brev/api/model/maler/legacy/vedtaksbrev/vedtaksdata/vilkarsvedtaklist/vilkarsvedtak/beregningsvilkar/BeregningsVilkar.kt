package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar

import no.nav.pensjon.brevbaker.api.model.Kroner


data class BeregningsVilkar(
    val IEUBegrunnelse: String?,
    val IFUBegrunnelse: String?,
    val IFUInntekt: Kroner?,
    val Trygdetid: Trygdetid?,
    val Uforegrad: Int?
)