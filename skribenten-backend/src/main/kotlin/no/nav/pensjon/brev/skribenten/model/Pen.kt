package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.B222
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.B255
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.B280
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.B359
import java.time.LocalDate
import java.time.LocalDateTime
import no.nav.pensjon.brev.api.model.Sakstype as BrevbakerSakstype

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

        fun toBrevbaker(): BrevbakerSakstype = when(this) {
            AFP -> BrevbakerSakstype.AFP
            AFP_PRIVAT -> BrevbakerSakstype.AFP_PRIVAT
            ALDER -> BrevbakerSakstype.ALDER
            BARNEP -> BrevbakerSakstype.BARNEP
            FAM_PL -> BrevbakerSakstype.FAM_PL
            GAM_YRK -> BrevbakerSakstype.GAM_YRK
            GENRL -> BrevbakerSakstype.GENRL
            GJENLEV -> BrevbakerSakstype.GJENLEV
            GRBL -> BrevbakerSakstype.GRBL
            KRIGSP -> BrevbakerSakstype.KRIGSP
            OMSORG -> BrevbakerSakstype.OMSORG
            UFOREP -> BrevbakerSakstype.UFOREP
        }
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

    data class SendRedigerbartBrevRequest(
        val templateDescription: TemplateDescription.Redigerbar,
        val dokumentDato: LocalDate,
        val saksId: Long,
        val brevkode: Brevkode.Redigerbar,
        val enhetId: String?,
        val pdf: ByteArray,
        val eksternReferanseId: String,
        val mottaker: Mottaker?
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SendRedigerbartBrevRequest

            if (templateDescription != other.templateDescription) return false
            if (dokumentDato != other.dokumentDato) return false
            if (saksId != other.saksId) return false
            if (brevkode != other.brevkode) return false
            if (enhetId != other.enhetId) return false
            if (!pdf.contentEquals(other.pdf)) return false
            if (eksternReferanseId != other.eksternReferanseId) return false
            if (mottaker != other.mottaker) return false

            return true
        }

        override fun hashCode(): Int {
            var result = templateDescription.hashCode()
            result = 31 * result + dokumentDato.hashCode()
            result = 31 * result + saksId.hashCode()
            result = 31 * result + brevkode.hashCode()
            result = 31 * result + enhetId.hashCode()
            result = 31 * result + pdf.contentHashCode()
            result = 31 * result + eksternReferanseId.hashCode()
            result = 31 * result + mottaker.hashCode()
            return result
        }

        data class Mottaker(val type: Type, val tssId: String? = null, val norskAdresse: NorskAdresse? = null, val utenlandskAdresse: UtenlandsAdresse? = null) {
            enum class Type { TSS_ID, NORSK_ADRESSE, UTENLANDSK_ADRESSE }
            data class NorskAdresse(val navn: String, val postnummer: String, val poststed: String, val adresselinje1: String?, val adresselinje2: String?, val adresselinje3: String?)
            // landkode: To-bokstavers landkode ihht iso3166-1 alfa-2
            data class UtenlandsAdresse(val navn: String, val landkode: String, val postnummer: String?, val poststed: String?, val adresselinje1: String, val adresselinje2: String?, val adresselinje3: String?)
        }
    }

    data class BestillBrevResponse(
        val journalpostId: Long?,
        val error: Error?,
    ) {
        data class Error(val brevIkkeStoettet: String?, val tekniskgrunn: String?, val beskrivelse: String?)
    }
}