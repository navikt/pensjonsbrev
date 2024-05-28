package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brevbaker.api.model.*

data class AutobrevRequest(val kode: Brevkode.AutoBrev, val letterData: BrevbakerBrevdata, val felles: Felles, val language: LanguageCode)

data class RedigerbartbrevRequest(val kode: Brevkode.Redigerbar, val letterData: BrevbakerBrevdata, val felles: Felles, val language: LanguageCode)

data class BestillBrevRequest(val letterData: BrevbakerBrevdata, val felles: Felles, val language: LanguageCode)