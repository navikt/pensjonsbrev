package no.nav.pensjon.brev.skribenten.model

import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.domain.MottakerType
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.Companion.norskAdresse
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.Companion.samhandler
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.Companion.utenlandskAdresse
import no.nav.pensjon.brev.skribenten.services.BrevdataResponse
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import java.time.Instant
import java.time.LocalDate

object Dto {
    data class Brevredigering(
        val info: BrevInfo,
        val redigertBrev: Edit.Letter,
        val redigertBrevHash: Hash<Edit.Letter>,
        val saksbehandlerValg: SaksbehandlerValg,
        val propertyUsage: Set<LetterMarkupWithDataUsage.Property>?,
        val valgteVedlegg: List<AlltidValgbartVedleggKode>?
    )

    data class BrevInfo(
        val id: BrevId,
        val saksId: SaksId,
        val vedtaksId: VedtaksId?,
        val opprettetAv: NavIdent,
        val opprettet: Instant,
        val sistredigertAv: NavIdent,
        val sistredigert: Instant,
        val redigeresAv: NavIdent?,
        val sistReservert: Instant?,
        val brevkode: Brevkode.Redigerbart,
        val laastForRedigering: Boolean,
        val distribusjonstype: Distribusjonstype,
        val mottaker: Mottaker?,
        val avsenderEnhetId: EnhetId,
        val spraak: LanguageCode,
        val journalpostId: JournalpostId?,
        val attestertAv: NavIdent?,
        val status: BrevStatus,
    )

    enum class BrevStatus {
        KLADD, ATTESTERING, KLAR, ARKIVERT
    }

    data class Document(
        val brevredigeringId: BrevId,
        val dokumentDato: LocalDate,
        val pdf: ByteArray,
        val redigertBrevHash: Hash<Edit.Letter>,
        val brevdataHash: Hash<BrevdataResponse.Data>?,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Document

            if (brevredigeringId != other.brevredigeringId) return false
            if (dokumentDato != other.dokumentDato) return false
            if (!pdf.contentEquals(other.pdf)) return false
            if (redigertBrevHash != other.redigertBrevHash) return false
            if (brevdataHash != other.brevdataHash) return false

            return true
        }

        override fun hashCode(): Int {
            var result = brevredigeringId.hashCode()
            result = 31 * result + dokumentDato.hashCode()
            result = 31 * result + pdf.contentHashCode()
            result = 31 * result + redigertBrevHash.hashCode()
            result = 31 * result + brevdataHash.hashCode()
            return result
        }
    }

    @ConsistentCopyVisibility
    data class Mottaker private constructor(
        val type: MottakerType,
        val tssId: String? = null,
        val navn: String? = null,
        val postnummer: NorskPostnummer? = null,
        val poststed: String? = null,
        val adresselinje1: String? = null,
        val adresselinje2: String? = null,
        val adresselinje3: String? = null,
        val landkode: Landkode? = null,
        val manueltAdressertTil: ManueltAdressertTil,
    ) {
        companion object {
            fun samhandler(tssId: String) = Mottaker(
                type = MottakerType.SAMHANDLER,
                tssId = tssId,
                manueltAdressertTil = ManueltAdressertTil.IKKE_RELEVANT
            )

            fun norskAdresse(
                navn: String,
                postnummer: NorskPostnummer,
                poststed: String,
                adresselinje1: String?,
                adresselinje2: String?,
                adresselinje3: String?,
                manueltAdressertTil: ManueltAdressertTil
            ) = Mottaker(
                type = MottakerType.NORSK_ADRESSE,
                navn = navn,
                postnummer = postnummer,
                poststed = poststed,
                adresselinje1 = adresselinje1,
                adresselinje2 = adresselinje2,
                adresselinje3 = adresselinje3,
                manueltAdressertTil = manueltAdressertTil,
            )

            fun utenlandskAdresse(
                navn: String,
                adresselinje1: String,
                adresselinje2: String?,
                adresselinje3: String?,
                landkode: Landkode,
                manueltAdressertTil: ManueltAdressertTil,
            ) = Mottaker(
                type = MottakerType.UTENLANDSK_ADRESSE,
                navn = navn,
                adresselinje1 = adresselinje1,
                adresselinje2 = adresselinje2,
                adresselinje3 = adresselinje3,
                landkode = landkode,
                manueltAdressertTil = manueltAdressertTil,
            )
        }

        enum class ManueltAdressertTil{
            BRUKER,
            ANNEN,
            IKKE_RELEVANT
        }
    }
}

fun Api.OverstyrtMottaker.toDto() =
    when (this) {
        is Api.OverstyrtMottaker.Samhandler -> samhandler(tssId)
        is Api.OverstyrtMottaker.NorskAdresse -> norskAdresse(
            navn = navn,
            postnummer = postnummer,
            poststed = poststed,
            adresselinje1 = adresselinje1,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
            manueltAdressertTil = manueltAdressertTil?: Dto.Mottaker.ManueltAdressertTil.BRUKER,
        )
        is Api.OverstyrtMottaker.UtenlandskAdresse -> utenlandskAdresse(
            navn = navn,
            adresselinje1 = adresselinje1,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
            landkode = landkode,
            manueltAdressertTil = manueltAdressertTil?: Dto.Mottaker.ManueltAdressertTil.BRUKER,
        )
    }

fun Dto.Mottaker.toPen(): Pen.SendRedigerbartBrevRequest.Mottaker = when (type) {
    MottakerType.SAMHANDLER -> Pen.SendRedigerbartBrevRequest.Mottaker(type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.TSS_ID, tssId = tssId!!)
    MottakerType.NORSK_ADRESSE -> Pen.SendRedigerbartBrevRequest.Mottaker(
        type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.NORSK_ADRESSE,
        norskAdresse = Pen.SendRedigerbartBrevRequest.Mottaker.NorskAdresse(
            navn = navn!!,
            postnummer = postnummer!!,
            poststed = poststed!!,
            adresselinje1 = adresselinje1,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
        ),
    )

    MottakerType.UTENLANDSK_ADRESSE -> Pen.SendRedigerbartBrevRequest.Mottaker(
        type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.UTENLANDSK_ADRESSE,
        utenlandskAdresse = Pen.SendRedigerbartBrevRequest.Mottaker.UtenlandsAdresse(
            navn = navn!!,
            landkode = landkode!!,
            adresselinje1 = adresselinje1!!,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
        ),
    )
}


@JvmInline
value class NorskPostnummer(val value: String) {
    init {
        valider()
    }

    fun valider() = require(value.matches(regex)) {
        "Norske postnummer skal være fire siffer, men dette var ${value.length}: $value"
    }

    companion object {
        private val regex = Regex("^[0-9]{4}$")
    }
}