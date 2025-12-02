package no.nav.pensjon.brev.api

import io.ktor.server.plugins.NotFoundException
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.Felles

class AlltidValgbartVedleggLibrary(vedlegg: Set<AlltidValgbartVedlegg<*>>) {
    private val vedlegg: Map<AlltidValgbartVedleggKode, AlltidValgbartVedlegg<*>> = vedlegg.associateBy { it.kode }

    fun getVedlegg(kode: AlltidValgbartVedleggKode, felles: Felles) =
        vedlegg[kode]?.asIncludeAttachment(felles) ?: throw NotFoundException("Vedlegg '$kode' doesn't exist")
}