package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

import javax.xml.datatype.XMLGregorianCalendar

data class BestillExtreamBrevResponseDto(
    val journalpostId: String?,
    val failureType: FailureType?
) {
    enum class FailureType {
        ADRESSE_MANGLER,
        HENTE_BREVDATA,
        MANGLER_OBLIGATORISK_INPUT,
        OPPRETTE_JOURNALPOST,
    }
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
        val vedtaksId: String?,
    )
}

