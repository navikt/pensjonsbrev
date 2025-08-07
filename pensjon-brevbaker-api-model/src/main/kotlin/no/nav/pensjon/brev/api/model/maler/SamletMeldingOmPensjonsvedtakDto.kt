package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Sakstype
import java.time.LocalDate

data class SamletMeldingOmPensjonsvedtakDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData
) : RedigerbarBrevdata<EmptyBrevdata, SamletMeldingOmPensjonsvedtakDto.PesysData> {
    data class PesysData(
        val sakstype: Sakstype,
        val vedlegg: P1Dto
    ) : BrevbakerBrevdata
}

data class P1Dto(
    val kravMottattDato: LocalDate
) : BrevbakerBrevdata // TODO: Innhaldet i denne kjem i ein seinare PR