package no.nav.pensjon.brev.api

import io.ktor.server.plugins.NotFoundException
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode

class AlltidValgbartVedleggLibrary(vedlegg: Set<AlltidValgbartVedlegg<*>>) {
    private val vedlegg: Map<String, AlltidValgbartVedlegg<*>> = vedlegg.associateBy { it.kode.kode }

    fun getVedlegg(koder: List<AlltidValgbartVedleggKode>) = koder.map { vedlegg[it.kode] ?: throw NotFoundException("Vedlegg '$it' doesn't exist") }
}