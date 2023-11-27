package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.fixtures

import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.BestillBrevRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv.SakskontekstDto
import java.util.*
import javax.xml.datatype.DatatypeFactory

fun bestillBrevDto(): BestillBrevRequestDto {
    val gc = GregorianCalendar()
    val xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc)

    val sakskontekstDto = SakskontekstDto(
        journalenhet = "4405",
        gjelder = "12345678910",
        dokumenttype = "N",
        dokumentdato = xgc,
        fagsystem = "PEN",
        fagomradekode = "PEN",
        innhold = "Posteringsgrunnlag",
        kategori = "B",
        saksid = "22972355",
        saksbehandlernavn = "F_Z999999",
        saksbehandlerId = "Z999999",
        sensitivitet = "false"
    )

    return BestillBrevRequestDto(
        brevKode = "PE_OK_06_101",
        brevGruppe = "brevgr001",
        isRedigerbart = true,
        sprakkode = "NB",
        sakskontekstDto = sakskontekstDto
    )
}