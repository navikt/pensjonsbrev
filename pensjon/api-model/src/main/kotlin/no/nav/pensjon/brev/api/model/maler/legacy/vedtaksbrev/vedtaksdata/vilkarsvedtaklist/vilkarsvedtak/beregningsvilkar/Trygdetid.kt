package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.beregningsvilkar.ttutlandtrygdeavtaleliste.TTUtlandTrygdeAvtaleListe

//ttutlandtrygdeavtaleliste_safe.ttutlandtrygdeavtale_safe.fattbilateral_safe
data class Trygdetid(
    val fatteos: Int?,
    val framtidigtteos: Int?,
    val framtidigttnorsk: Int?,
    val redusertframtidigtrygdetid: Boolean?,
    val fattnorge: Int?,
    val tttellereos: Int?,
    val ttnevnereos: Int?,
    val ttnordisk: Int?,
    val tttellernordisk: Int?,
    val ttnevnernordisk: Int?,
    val faTTA10Netto: Int?,
    val ttutlandtrygdeavtaleliste: TTUtlandTrygdeAvtaleListe?
)