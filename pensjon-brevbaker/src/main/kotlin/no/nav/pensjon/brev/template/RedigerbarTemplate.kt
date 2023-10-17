package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode

interface RedigerbarTemplate<LetterData: BrevbakerBrevdata>: HasModel<LetterData> {
    val template: LetterTemplate<*, LetterData>
    val kode: Brevkode.Redigerbar
}