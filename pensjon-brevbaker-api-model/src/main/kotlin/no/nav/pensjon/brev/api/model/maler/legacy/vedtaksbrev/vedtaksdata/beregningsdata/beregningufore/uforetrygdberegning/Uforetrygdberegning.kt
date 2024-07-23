package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.uforetrygdberegning

import no.nav.pensjon.brevbaker.api.model.Kroner


data class Uforetrygdberegning(
    val AnvendtTrygdetid: Int?,
    val Mottarminsteytelse: Boolean?,
    val Uforegrad: Int?,
    val Yrkesskadegrad: Int?,
    val Grunnbelop: Kroner?,
    val InstOppholdType: String?,
)