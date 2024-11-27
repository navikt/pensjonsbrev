package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.LetterTemplate

interface EtterlatteTemplate<LetterData : BrevbakerBrevdata>: HasModel<LetterData> {
    val template: LetterTemplate<*, LetterData>
    val kode: Brevkode.Automatisk
}