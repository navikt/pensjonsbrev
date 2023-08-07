package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.LetterTemplate

interface EtterlatteTemplate<LetterData : Any>: HasModel<LetterData> {
    val template: LetterTemplate<*, LetterData>
    val kode: EtterlatteBrevKode
}