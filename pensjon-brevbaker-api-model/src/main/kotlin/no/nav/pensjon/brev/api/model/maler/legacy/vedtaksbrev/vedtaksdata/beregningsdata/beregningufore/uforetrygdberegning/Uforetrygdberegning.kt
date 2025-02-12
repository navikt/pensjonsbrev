package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.uforetrygdberegning

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class Uforetrygdberegning(
    val anvendttrygdetid: Int?,
    val mottarminsteytelse: Boolean?,
    val uforegrad: Int?,
    val yrkesskadegrad: Int?,
    val grunnbelop: Kroner?,
    val instoppholdtype: String?,
    val beregningsmetode: String?,
    val uforetidspunkt: LocalDate?,
    val proratabrokteller: Int?,
    val proratabroknevner: Int?,
    val instopphanvendt: Boolean?,
)
