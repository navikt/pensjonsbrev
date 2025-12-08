package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.template.AutobrevTemplate

interface EtterlatteTemplate<LetterData : AutobrevData> : AutobrevTemplate<LetterData>