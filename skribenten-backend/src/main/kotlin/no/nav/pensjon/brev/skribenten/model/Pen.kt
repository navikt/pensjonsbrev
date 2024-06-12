package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.*
import java.time.LocalDate
import java.time.LocalDateTime

object Pen {
    enum class SakType(val behandlingsnummer: Behandlingsnummer?) {
        AFP(null),
        AFP_PRIVAT(null),
        ALDER(B280),
        BARNEP(B359),
        FAM_PL(null),
        GAM_YRK(null),
        GENRL(null),
        GJENLEV(B222),
        GRBL(null),
        KRIGSP(null),
        OMSORG(null),
        UFOREP(B255);
    }

    data class SakSelection(
        val saksId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: SakType,
        val enhetId: String,
    )

    data class Avtaleland(val navn: String, val kode: String)

    data class BestillDoksysBrevResponse(val journalpostId: String?, val failure: FailureType? = null) {
        enum class FailureType {
            ADDRESS_NOT_FOUND,
            UNAUTHORIZED,
            PERSON_NOT_FOUND,
            UNEXPECTED_DOKSYS_ERROR,
            INTERNAL_SERVICE_CALL_FAILIURE,
            TPS_CALL_FAILIURE,
        }
    }

    data class BestillExstreamBrevResponse(
        val journalpostId: String,
    ) {
        data class Error(
            val type: String,
            val message: String?,
        )
    }

    data class BestillExstreamBrevRequest(
        val brevGruppe: String? = null,
        val brevKode: String? = null,
        val brevMottakerNavn: String? = null,
        val redigerbart: Boolean? = null,
        val sakskontekst: Sakskontekst? = null,
        val soknadsInformasjon: String? = null,
        val sprakKode: String? = null,
        val vedtaksInformasjon: String? = null,
    ) {
        data class Sakskontekst(
            val dokumentdato: LocalDateTime? = null,
            val dokumenttype: String? = null,
            val fagomradeKode: String? = null,
            val fagspesifikkgradering: String? = null,
            val fagsystem: String? = null,
            val gjelder: String? = null,
            val innhold: String? = null,
            val journalenhet: String? = null,
            val kategori: String? = null,
            val kravtype: String? = null,
            val land: String? = null,
            val merknad: String? = null,
            val mottaker: String? = null,
            val referanse: String? = null,
            val saksbehandlernavn: String? = null,
            val saksbehandlerid: String? = null,
            val sensitivt: Boolean? = null,
            val saksid: String? = null,
            val tillattelektroniskvarsling: Boolean? = null,
            val tilleggsbeskrivelse: String? = null,
        )
    }

    data class RedigerDokumentResponse(
        val uri: String,
    )
}