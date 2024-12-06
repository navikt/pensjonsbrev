package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.trygdetidavdod

data class TrygdetidAvdod(
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
