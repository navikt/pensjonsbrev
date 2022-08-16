package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.Brevkode

interface VedtaksbrevTemplate<LetterData : Any>: HasModel<LetterData> {
    val template: LetterTemplate<*, LetterData>
    val kode: Brevkode.Vedtak
}