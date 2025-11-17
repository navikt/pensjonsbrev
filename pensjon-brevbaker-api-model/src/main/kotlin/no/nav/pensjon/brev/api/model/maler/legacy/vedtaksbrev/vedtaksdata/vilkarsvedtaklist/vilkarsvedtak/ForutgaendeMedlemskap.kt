package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak

data class ForutgaendeMedlemskap(
    val unntakfraforutgaendemedlemskap: Boolean?,
    val inngangunntak: String?,
    val minst20arbotid: Boolean?,
    val minsttrearsfmnorge: Boolean?
)
