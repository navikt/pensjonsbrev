package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.model.maler.Brevkode

data class VedtaksbrevRequest(val kode: Brevkode.Vedtak, val letterData: Any, val felles: Felles, val language: LanguageCode)

data class RedigerbartbrevRequest(val kode: Brevkode.Redigerbar, val letterData: Any, val felles: Felles, val language: LanguageCode)