package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brevbaker.api.model.Felles


interface VedleggData

class FellesVedleggData(val felles: Felles) : VedleggData