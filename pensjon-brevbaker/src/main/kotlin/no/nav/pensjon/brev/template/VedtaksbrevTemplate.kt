package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.Brevkode

interface VedtaksbrevTemplate {
    val template: LetterTemplate<*, *>
    val kode: Brevkode.Vedtak
}