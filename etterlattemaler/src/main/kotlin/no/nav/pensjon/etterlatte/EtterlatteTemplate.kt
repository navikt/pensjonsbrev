package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate

interface EtterlatteTemplate<LetterData : BrevbakerBrevdata> : AutobrevTemplate<LetterData>
