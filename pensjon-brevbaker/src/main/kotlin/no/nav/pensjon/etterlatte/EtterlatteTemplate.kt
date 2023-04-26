package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.template.*

interface EtterlatteTemplate<LetterData : Any>: HasModel<LetterData> {
    val template: LetterTemplate<*, LetterData>
    val kode: EtterlatteBrevKode
}