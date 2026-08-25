package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.api.model.maler.Brevkode

interface AlltidValgbartVedleggKode {
    val kode: String
    val visningstekst: String
    val spraak: Set<LanguageCode>
    val stoettedeBrevmaler: Collection<Brevkode.Redigerbart>
    fun stoetterBrevkode(brevkode: Brevkode.Redigerbart?): Boolean = brevkode != null && stoettedeBrevmaler.map { it.kode() }.contains(brevkode.kode())
}