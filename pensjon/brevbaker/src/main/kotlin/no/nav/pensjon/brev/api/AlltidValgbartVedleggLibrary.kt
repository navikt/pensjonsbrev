package no.nav.pensjon.brev.api

import io.ktor.server.plugins.NotFoundException
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles

class AlltidValgbartVedleggLibrary(vedlegg: Set<AlltidValgbartVedlegg<*>>) {
    private val vedlegg: Map<String, AlltidValgbartVedlegg<*>> = vedlegg.associateBy { it.kode.kode }

    fun getVedlegg(koder: List<AlltidValgbartVedleggKode>, felles: BrevbakerFelles) = koder.map { getVedlegg(it, felles) }

    private fun getVedlegg(kode: AlltidValgbartVedleggKode, felles: BrevbakerFelles) =
        vedlegg[kode.kode]?.asIncludeAttachment() ?: throw NotFoundException("Vedlegg '$kode' doesn't exist")
}