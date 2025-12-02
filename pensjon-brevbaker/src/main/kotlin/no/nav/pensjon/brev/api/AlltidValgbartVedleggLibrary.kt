package no.nav.pensjon.brev.api

import io.ktor.server.plugins.NotFoundException
import no.nav.pensjon.brev.template.ValgbareVedlegg
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode

class AlltidValgbartVedleggLibrary(vedlegg: Set<ValgbareVedlegg<*>>) {
    private val vedlegg: Map<AlltidValgbartVedleggKode, ValgbareVedlegg<*>> = vedlegg.associateBy { it.kode }

    fun getVedlegg(kode: AlltidValgbartVedleggKode): ValgbareVedlegg<*> = vedlegg[kode] ?: throw NotFoundException("Vedlegg '$kode' doesn't exist")
}