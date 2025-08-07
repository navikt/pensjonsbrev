package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import java.time.LocalDate
import java.time.Month

fun createSamletMeldingOmPensjonsvedtakDto() = SamletMeldingOmPensjonsvedtakDto(
    sakstype = Sakstype.ALDER,
    vedlegg = createP1Dto()
)

fun createP1Dto() = P1Dto(
    kravMottattDato = LocalDate.of(2025, Month.MAY, 1)
)