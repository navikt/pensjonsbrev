package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.api.model.ISakstype

interface AlltidValgbartVedleggKode {
    val kode: String
    val visningstekst: String
    val spraak: Set<LanguageCode>
    val stoettedeSakstyper: Set<ISakstype>
}