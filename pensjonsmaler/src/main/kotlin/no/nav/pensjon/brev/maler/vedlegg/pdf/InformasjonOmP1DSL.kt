package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import no.nav.pensjon.brev.api.model.maler.InformasjonOmP1Dto

fun InformasjonOmP1Dto.somPDFVedlegg() = PDFVedlegg.create {
    side("InformasjonOmP1.pdf") {
    }
}