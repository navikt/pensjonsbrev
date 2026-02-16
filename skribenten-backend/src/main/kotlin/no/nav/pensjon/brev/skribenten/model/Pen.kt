package no.nav.pensjon.brev.skribenten.model

import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.B222
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.B255
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.B280
import no.nav.pensjon.brev.skribenten.model.Pdl.Behandlingsnummer.B359
import no.nav.pensjon.brev.skribenten.serialize.Sakstype
import no.nav.pensjon.brev.skribenten.services.BrevdataDto
import no.nav.pensjon.brev.skribenten.services.EnhetId
import java.time.LocalDate
import java.time.LocalDateTime

object Pen {
    // TODO: Denne bør på sikt flyttes ut herifra
    private val behandlingsnummerMap = mapOf(
        "ALDER" to B280,
        "BARNEP" to B359,
        "GJENLEV" to B222,
        "UFOREP" to B255
    )

    fun finnBehandlingsnummer(sakstype: ISakstype) = behandlingsnummerMap[sakstype.kode]

    data class SakSelection(
        val saksId: SaksId,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val navn: Navn,
        val sakType: ISakstype,
    ) {
        data class Navn(val fornavn: String, val mellomnavn: String?, val etternavn: String)
    }

    data class Avtaleland(val navn: String, val kode: String)

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
            val journalenhet: EnhetId? = null,
            val kategori: String? = null,
            val kravtype: String? = null,
            val land: String? = null,
            val merknad: String? = null,
            val mottaker: String? = null,
            val referanse: String? = null,
            val saksbehandlernavn: String? = null,
            val saksbehandlerid: String? = null,
            val sensitivt: Boolean? = null,
            val saksid: SaksId? = null,
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
        val saksId: SaksId,
        val brevkode: Brevkode.Redigerbart,
        val enhetId: EnhetId,
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
            data class NorskAdresse(val navn: String, val postnummer: NorskPostnummer, val poststed: String, val adresselinje1: String?, val adresselinje2: String?, val adresselinje3: String?)
            data class UtenlandsAdresse(val navn: String, val landkode: Landkode, val adresselinje1: String, val adresselinje2: String?, val adresselinje3: String?)
        }
    }

    data class BestillBrevResponse(
        val journalpostId: Long?,
        val error: Error?,
    ) {
        data class Error(val brevIkkeStoettet: String?, val tekniskgrunn: String?, val beskrivelse: String?)
    }

    fun isRelevantRegelverk(sakstype: ISakstype, brevregeltype: BrevdataDto.BrevregeltypeCode?, forGammeltRegelverk: Boolean?): Boolean = when (sakstype.kode) {
        "ALDER" if forGammeltRegelverk == true -> brevregeltype?.gjelderGammeltRegelverk() ?: true
        "ALDER" -> brevregeltype?.gjelderNyttRegelverk() ?: true
        "UFOREP" -> brevregeltype?.gjelderGammeltRegelverk() ?: true
        else -> true
    }

    val sakstypeForLegacybrev = Sakstype("GENRL")

    // TODO: Dette bør flyttes over en annen plass, feks i brevbaker-appen, og serveres derifra
    private val brevkategoriTilVisningstekst = mapOf(
        "ETTEROPPGJOER" to "Etteroppgjør",
        "FEILUTBETALING" to "Feilutbetaling",
        "FOERSTEGANGSBEHANDLING" to "Førstegangsbehandling",
        "FRITEKSTBREV" to "Fritekstbrev",
        "INFORMASJONSBREV" to "Informasjonsbrev",
        "INNHENTE_OPPLYSNINGER" to "Innhente opplysninger",
        "KLAGE_OG_ANKE" to "Klage og anke",
        "LEVEATTEST" to "Leveattest",
        "OMSORGSOPPTJENING" to "Omsorgsopptjening",
        "POSTERINGSGRUNNLAG" to "Posteringsgrunnlag",
        "SLUTTBEHANDLING" to "Sluttbehandling",
        "UFOEREPENSJON" to "Uførepensjon",
        "VARSEL" to "Varsel",
        "VEDTAK_EKSPORT" to "Vedtak - eksport",
        "VEDTAK_ENDRING_OG_REVURDERING" to "Vedtak - endring og revurdering",
        "VEDTAK_FLYTTE_MELLOM_LAND" to "Vedtak - flytte mellom land"
    )

    fun finnVisningstekst(brevkategori: TemplateDescription.IBrevkategori) = brevkategoriTilVisningstekst[brevkategori.kode]
}