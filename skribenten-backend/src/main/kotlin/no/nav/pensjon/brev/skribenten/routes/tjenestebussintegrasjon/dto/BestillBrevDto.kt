package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

import javax.xml.datatype.XMLGregorianCalendar

sealed class BestillExtreamBrevResponseDto {
    data class Success(val journalpostId: String): BestillExtreamBrevResponseDto()
    data class Failure(
        val message: String,
        val type: String,
    ) : BestillExtreamBrevResponseDto()
}
data class BestillBrevExtreamRequestDto(
    val brevKode: String,
    val brevGruppe: String,
    val isRedigerbart: Boolean,
    val sprakkode: String,
    val sakskontekstDto: SakskontekstDto,
    val brevMottakerNavn: String?,
){
    data class SakskontekstDto(
        val dokumentdato: XMLGregorianCalendar,
        val dokumenttype: String,
        val fagomradekode: String,
        val fagsystem: String,
        val gjelder: String,
        val innhold: String,
        val journalenhet: String,
        val kategori: String,
        val kravtype: String?,
        val land: String?,
        val mottaker: String?,
        val saksbehandlerId: String,
        val saksbehandlernavn: String,
        val saksid: String,
    )
}


