package no.nav.pensjon.brev.api

import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedlegg
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode

class AlltidValgbartVedleggLibrary<out T: AlltidValgbartVedlegg>(vedlegg: Set<T>) {
    private val vedlegg: Map<String, T> = vedlegg.associateBy { it.kode.kode() }

    fun getVedlegg(kode: AlltidValgbartVedleggKode) = vedlegg[kode.kode()]
}