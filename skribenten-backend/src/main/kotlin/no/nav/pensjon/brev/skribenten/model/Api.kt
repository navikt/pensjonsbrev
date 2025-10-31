package no.nav.pensjon.brev.skribenten.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.skribenten.db.EditLetterHash
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.ManueltAdressertTil
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.Duration
import java.time.Instant

typealias SaksbehandlerValg = Api.GeneriskBrevdata

object Api {
    class GeneriskBrevdata : LinkedHashMap<String, Any?>(), BrevbakerBrevdata, SaksbehandlerValgBrevdata

    data class OpprettBrevRequest(
        val brevkode: Brevkode.Redigerbart,
        val spraak: SpraakKode,
        val avsenderEnhetsId: String?,
        val saksbehandlerValg: SaksbehandlerValg,
        val reserverForRedigering: Boolean?,
        val mottaker: OverstyrtMottaker?,
        val vedtaksId: Long?,
    )

    data class OppdaterBrevRequest(
        val saksbehandlerValg: SaksbehandlerValg,
        val redigertBrev: Edit.Letter,
    )

    data class DelvisOppdaterBrevRequest(
        val laastForRedigering: Boolean? = null,
        val distribusjonstype: Distribusjonstype? = null,
        val mottaker: OverstyrtMottaker? = null
    )

    data class OppdaterAttesteringRequest(
        val saksbehandlerValg: SaksbehandlerValg,
        val redigertBrev: Edit.Letter,
    )

    data class BrevInfo(
        val id: Long,
        val saksId: Long,
        val opprettetAv: NavAnsatt,
        val opprettet: Instant,
        val sistredigertAv: NavAnsatt,
        val sistredigert: Instant,
        val brevkode: Brevkode.Redigerbart,
        val brevtittel: String,
        val brevtype: LetterMetadata.Brevtype,
        val status: BrevStatus,
        val distribusjonstype: Distribusjonstype,
        val mottaker: OverstyrtMottaker?,
        val avsenderEnhet: NavEnhet?,
        val spraak: SpraakKode,
        val journalpostId: Long?,
        val vedtaksId: Long?,
    )

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(BrevStatus.Kladd::class, name = "Kladd"),
        JsonSubTypes.Type(BrevStatus.UnderRedigering::class, name = "UnderRedigering"),
        JsonSubTypes.Type(BrevStatus.Attestering::class, name = "Attestering"),
        JsonSubTypes.Type(BrevStatus.Klar::class, name = "Klar"),
        JsonSubTypes.Type(BrevStatus.Arkivert::class, name = "Arkivert"),
    )
    sealed class BrevStatus {
        data object Kladd : BrevStatus()
        data class UnderRedigering(val redigeresAv: NavAnsatt) : BrevStatus()
        data object Attestering : BrevStatus()
        data class Klar(val attestertAv: NavAnsatt? = null) : BrevStatus()
        data object Arkivert : BrevStatus()
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(OverstyrtMottaker.Samhandler::class, name = "Samhandler"),
        JsonSubTypes.Type(OverstyrtMottaker.NorskAdresse::class, name = "NorskAdresse"),
        JsonSubTypes.Type(OverstyrtMottaker.UtenlandskAdresse::class, name = "UtenlandskAdresse"),
    )
    sealed class OverstyrtMottaker {
        data class Samhandler(val tssId: String, val navn: String?) : OverstyrtMottaker()
        data class NorskAdresse(
            val navn: String,
            val postnummer: String,
            val poststed: String,
            val adresselinje1: String?,
            val adresselinje2: String?,
            val adresselinje3: String?,
            val manueltAdressertTil: ManueltAdressertTil?,
            ) : OverstyrtMottaker()

        // landkode: To-bokstavers landkode ihht iso3166-1 alfa-2
        data class UtenlandskAdresse(
            val navn: String,
            val adresselinje1: String,
            val adresselinje2: String?,
            val adresselinje3: String?,
            val landkode: Landkode,
            val manueltAdressertTil: ManueltAdressertTil?
            ) : OverstyrtMottaker()
    }

    data class BrevResponse(
        val info: BrevInfo,
        val redigertBrev: Edit.Letter,
        val redigertBrevHash: EditLetterHash,
        val saksbehandlerValg: BrevbakerBrevdata,
        val propertyUsage: Set<LetterMarkupWithDataUsage.Property>?,
    )

    data class ReservasjonResponse(
        val vellykket: Boolean,
        val reservertAv: NavAnsatt,
        val timestamp: Instant,
        val expiresIn: Duration,
        val redigertBrevHash: EditLetterHash,
    )

    data class NavAnsatt(val id: NavIdent, val navn: String?)

    data class SakContext(
        val sak: Pen.SakSelection,
        val brevmalKoder: List<String>,
    )

    data class Brevmal(
        val name: String,
        val id: String,
        val brevsystem: BrevSystem,
        val spraak: List<SpraakKode>,
        val brevkategori: String?,
        val dokumentkategoriCode: BrevdataDto.DokumentkategoriCode?,
        val redigerbart: Boolean,
        val redigerbarBrevtittel: Boolean,
    )

    data class BestillDoksysBrevRequest(
        val brevkode: String,
        val spraak: SpraakKode,
        val vedtaksId: Long? = null,
        val enhetsId: String,
    )

    data class BestillExstreamBrevRequest(
        val brevkode: String,
        val spraak: SpraakKode,
        val isSensitive: Boolean,
        val vedtaksId: Long? = null,
        val idTSSEkstern: String? = null,
        val brevtittel: String? = null,
        val enhetsId: String,
    )

    data class BestillEblankettRequest(
        val brevkode: String,
        val landkode: String,
        val mottakerText: String,
        val isSensitive: Boolean,
        val enhetsId: String,
    )

    data class BestillOgRedigerBrevResponse(
        val url: String? = null,
        val journalpostId: String? = null,
        val failureType: FailureType? = null,
    ) {
        enum class FailureType {
            DOKSYS_BESTILLING_ADDRESS_NOT_FOUND,
            DOKSYS_BESTILLING_INTERNAL_SERVICE_CALL_FAILIURE,
            DOKSYS_BESTILLING_PERSON_NOT_FOUND,
            DOKSYS_BESTILLING_TPS_CALL_FAILIURE,
            DOKSYS_BESTILLING_UNAUTHORIZED,
            DOKSYS_BESTILLING_UNEXPECTED_DOKSYS_ERROR,
            EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT,
            EXSTREAM_REDIGERING_GENERELL,
            FERDIGSTILLING_TIMEOUT,
            SAF_ERROR,
            SKRIBENTEN_INTERNAL_ERROR,
            ENHET_UNAUTHORIZED,
            NAVANSATT_MANGLER_NAVN,
        }
    }
}