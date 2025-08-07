package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Sakstype
import java.time.LocalDate

data class SamletMeldingOmPensjonsvedtakDto(
    val sakstype: Sakstype,
    val vedlegg: P1Dto,
) : BrevbakerBrevdata

data class P1Dto(
    val kravMottattDato: LocalDate
) : BrevbakerBrevdata // TODO: Innhaldet i denne kjem i ein seinare PR