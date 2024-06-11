package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.*
import java.time.LocalDate

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

}