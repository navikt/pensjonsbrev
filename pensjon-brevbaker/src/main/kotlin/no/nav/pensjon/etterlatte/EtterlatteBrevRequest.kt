package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode

data class EtterlatteBrevRequest(
    val kode: Brevkode.Automatisk,
    val letterData: BrevbakerBrevdata,
    val felles: Felles,
    val language: LanguageCode,
)
