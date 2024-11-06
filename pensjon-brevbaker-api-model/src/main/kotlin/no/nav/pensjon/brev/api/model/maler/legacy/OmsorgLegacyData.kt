package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

data class OmsorgLegacyData(
    val vedtaksbrev: Vedtaksbrev,
) : BrevbakerBrevdata

data class Vedtaksbrev(
    val grunnlag: Grunnlag,
    val vedtaksdata: Vedtaksdata,
)

data class Grunnlag(val omsorgGodskrGrunnlagListe: List<OmsorgGodskrGrunnlag>)

data class OmsorgGodskrGrunnlag(val aar: Year)

data class Vedtaksdata(val kravhode: Kravhode)

data class Kravhode(val kravmottattdato: LocalDate)