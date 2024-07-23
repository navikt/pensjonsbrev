package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.Vedtaksbrev

data class PE(val Vedtaksbrev: Vedtaksbrev) : BrevbakerBrevdata
