package no.nav.pensjon.brev.skribenten.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.services.SpraakKode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.Instant

object ExternalAPI {
    data class BrevInfo(
        val url: String,
        val id: Long,
        val saksId: Long,
        val vedtaksId: Long?,
        val journalpostId: Long?,
        val brevkode: Brevkode.Redigerbart,
        val tittel: String,
        val brevtype: LetterMetadata.Brevtype,
        val avsenderEnhetsId: String?,
        val spraak: SpraakKode,
        val opprettetAv: NavIdent,
        val sistRedigertAv: NavIdent,
        val redigeresAv: NavIdent?,
        val opprettet: Instant,
        val sistRedigert: Instant,
        val overstyrtMottaker: OverstyrtMottaker?,
        val status: BrevStatus,
    )

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(OverstyrtMottaker.Samhandler::class, name = "Samhandler"),
        JsonSubTypes.Type(OverstyrtMottaker.NorskAdresse::class, name = "NorskAdresse"),
        JsonSubTypes.Type(OverstyrtMottaker.UtenlandskAdresse::class, name = "UtenlandskAdresse"),
    )
    sealed class OverstyrtMottaker {
        data class Samhandler(val tssId: String) : OverstyrtMottaker()
        data class NorskAdresse(
            val navn: String,
            val postnummer: String,
            val poststed: String,
            val adresselinje1: String?,
            val adresselinje2: String?,
            val adresselinje3: String?
        ) : OverstyrtMottaker()

        // landkode: To-bokstavers landkode ihht iso3166-1 alfa-2
        data class UtenlandskAdresse(
            val navn: String,
            val postnummer: String?,
            val poststed: String?,
            val adresselinje1: String,
            val adresselinje2: String?,
            val adresselinje3: String?,
            val landkode: Landkode,
        ) : OverstyrtMottaker()
    }

    enum class BrevStatus {
        KLADD,
        ATTESTERING,
        KLAR,
        ARKIVERT,
    }
}