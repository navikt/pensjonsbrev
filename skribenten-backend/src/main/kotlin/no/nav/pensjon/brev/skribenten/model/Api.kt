package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.skribenten.services.LetterMetadata
import no.nav.pensjon.brev.skribenten.services.SpraakKode

object Api {
    data class SakContext(
        val sak: Pen.SakSelection,
        val brevMetadata: List<LetterMetadata>
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
            DOKSYS_REDIGERING_IKKE_FUNNET,
            DOKSYS_REDIGERING_IKKE_REDIGERBART,
            DOKSYS_REDIGERING_IKKE_TILGANG,
            DOKSYS_REDIGERING_LUKKET,
            DOKSYS_REDIGERING_UFORVENTET,
            DOKSYS_REDIGERING_UNDER_REDIGERING,
            DOKSYS_REDIGERING_VALIDERING_FEILET,
            EXSTREAM_BESTILLING_ADRESSE_MANGLER,
            EXSTREAM_BESTILLING_HENTE_BREVDATA,
            EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT,
            EXSTREAM_BESTILLING_OPPRETTE_JOURNALPOST,
            EXSTREAM_REDIGERING_GENERELL,
            FERDIGSTILLING_TIMEOUT,
            SAF_ERROR,
            SKRIBENTEN_INTERNAL_ERROR,
            ENHETSID_MANGLER,
            ENHET_UNAUTHORIZED,
            NAVANSATT_MANGLER_NAVN,
        }
    }
}