package no.nav.pensjon.brevbaker.api.model

interface AlltidValgbartVedleggKode {
    val kode: String
    val visningstekst: String
    val spraak: Set<LanguageCode>
}
