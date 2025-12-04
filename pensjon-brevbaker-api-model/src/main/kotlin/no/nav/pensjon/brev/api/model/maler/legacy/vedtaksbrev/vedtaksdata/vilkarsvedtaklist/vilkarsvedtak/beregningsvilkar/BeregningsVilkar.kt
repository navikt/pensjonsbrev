package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Month

data class BeregningsVilkar(
    val ieubegrunnelse: String?,
    val ieuinntekt: Kroner?,
    val ifubegrunnelse: String?,
    val ifuinntekt: Kroner?,
    val skadetidspunkt: LocalDate?,
    val trygdetid: Trygdetid?,
    val uforegrad: Int?,
    val uforetidspunkt: LocalDate?,
    val virkningstidpunkt: LocalDate?,
    val yrkesskadegrad: Int?,
    val virkningbegrunnelse: String? = null,
    val uforetidspunktbegrunnelse: String? = null
) {
    val virkningstidspunktmaned: Month? = virkningstidpunkt?.month
}