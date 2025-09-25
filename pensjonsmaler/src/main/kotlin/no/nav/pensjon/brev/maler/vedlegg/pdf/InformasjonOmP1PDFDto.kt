package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.api.model.maler.InformasjonOmP1Dto
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.vedlegg.createAttachmentPDF

val informasjonOmP1Vedlegg = createAttachmentPDF<LangBokmalEnglish, InformasjonOmP1Dto> {
    side("InformasjonOmP1.pdf") {
    }
}