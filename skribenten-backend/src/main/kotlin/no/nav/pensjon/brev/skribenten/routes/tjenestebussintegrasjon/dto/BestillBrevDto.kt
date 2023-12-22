package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

import javax.xml.datatype.XMLGregorianCalendar

sealed class BestillBrevResponseDto {
    data class Success(val journalpostId: String): BestillBrevResponseDto()
    data class Failure(
        val message: String,
        val type: String,
    ) : BestillBrevResponseDto()
}
data class BestillBrevRequestDto(
    val brevKode: String,
    val brevGruppe: String,
    val isRedigerbart: Boolean,
    val sprakkode: String,
    val sakskontekstDto: SakskontekstDto,
)


data class SakskontekstDto(
    val journalenhet: String,
    val gjelder: String,
    val dokumenttype: String,
    val dokumentdato: XMLGregorianCalendar,
    val fagsystem: String,
    val fagomradekode: String,
    val innhold: String,
    val kategori: String,
    val saksid: String,
    val saksbehandlernavn: String,
    val saksbehandlerId: String,
    val sensitivitet: String
)