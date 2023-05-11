package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brevbaker.api.model.*

data class AutobrevRequest(val kode: Brevkode.AutoBrev, val letterData: Any, val felles: Felles, val language: LanguageCode)

data class RedigerbartbrevRequest(val kode: Brevkode.Redigerbar, val letterData: Any, val felles: Felles, val language: LanguageCode)