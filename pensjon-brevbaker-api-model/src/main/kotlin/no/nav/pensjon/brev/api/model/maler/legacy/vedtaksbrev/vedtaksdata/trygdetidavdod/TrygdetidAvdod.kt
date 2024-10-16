package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.trygdetidavdod

import no.nav.pensjon.brevbaker.api.model.Kroner

data class TrygdetidAvdod(
    val fatt: Kroner?,
    val fatteos: Int?,
    val fattnorge: Int?,
    val framtidigtteos: Int?,
    val framtidigttnorsk: Int?,
    val ttnevnereos: Int?,
    val ttnevnernordisk: Int?,
    val ttnordisk: Int?,
    val tttellereos: Int?,
    val tttellernordisk: Int?,
    val ttutlandtrygdeavtale: TTutlandTrygdeavtale?,
    val fatta10netto: Int?,
)
