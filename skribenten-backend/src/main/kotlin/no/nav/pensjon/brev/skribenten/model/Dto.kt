package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.db.EditLetterHash
import no.nav.pensjon.brev.skribenten.db.MottakerType
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.Companion.norskAdresse
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.Companion.samhandler
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.Companion.utenlandskAdresse
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.time.Instant
import java.time.LocalDate

object Dto {
    data class Brevredigering(
        val info: BrevInfo,
        val redigertBrev: Edit.Letter,
        val redigertBrevHash: EditLetterHash,
        val saksbehandlerValg: SaksbehandlerValg,
    )

    data class BrevInfo(
        val id: Long,
        val saksId: Long,
        val opprettetAv: NavIdent,
        val opprettet: Instant,
        val sistredigertAv: NavIdent,
        val sistredigert: Instant,
        val redigeresAv: NavIdent?,
        val sistReservert: Instant?,
        val brevkode: Brevkode.Redigerbar,
        val laastForRedigering: Boolean,
        val distribusjonstype: Distribusjonstype,
        val mottaker: Mottaker?,
        val avsenderEnhetId: String?,
        val spraak: LanguageCode,
        val signaturSignerende: String,
    )

    data class Document(
        val brevredigeringId: Long,
        val dokumentDato: LocalDate,
        val pdf: ByteArray,
        val redigertBrevHash: EditLetterHash,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Document

            if (brevredigeringId != other.brevredigeringId) return false
            if (dokumentDato != other.dokumentDato) return false
            if (!pdf.contentEquals(other.pdf)) return false
            if (redigertBrevHash != other.redigertBrevHash) return false

            return true
        }

        override fun hashCode(): Int {
            var result = brevredigeringId.hashCode()
            result = 31 * result + dokumentDato.hashCode()
            result = 31 * result + pdf.contentHashCode()
            result = 31 * result + redigertBrevHash.hashCode()
            return result
        }
    }

    @ConsistentCopyVisibility
    data class Mottaker private constructor(
        val type: MottakerType,
        val tssId: String? = null,
        val navn: String? = null,
        val postnummer: String? = null,
        val poststed: String? = null,
        val adresselinje1: String? = null,
        val adresselinje2: String? = null,
        val adresselinje3: String? = null,
        val landkode: String? = null,
    ) {
        companion object {
            fun samhandler(tssId: String) = Mottaker(
                type = MottakerType.SAMHANDLER,
                tssId = tssId,
            )

            fun norskAdresse(navn: String, postnummer: String, poststed: String, adresselinje1: String?, adresselinje2: String?, adresselinje3: String?) =
                Mottaker(
                    type = MottakerType.NORSK_ADRESSE,
                    navn = navn,
                    postnummer = postnummer,
                    poststed = poststed,
                    adresselinje1 = adresselinje1,
                    adresselinje2 = adresselinje2,
                    adresselinje3 = adresselinje3,
                )

            fun utenlandskAdresse(
                navn: String,
                postnummer: String?,
                poststed: String?,
                adresselinje1: String,
                adresselinje2: String?,
                adresselinje3: String?,
                landkode: String,
            ) = Mottaker(
                type = MottakerType.UTENLANDSK_ADRESSE,
                navn = navn,
                postnummer = postnummer,
                poststed = poststed,
                adresselinje1 = adresselinje1,
                adresselinje2 = adresselinje2,
                adresselinje3 = adresselinje3,
                landkode = landkode,
            )
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
        )
        is Api.OverstyrtMottaker.UtenlandskAdresse -> utenlandskAdresse(
            navn = navn,
            postnummer = postnummer,
            poststed = poststed,
            adresselinje1 = adresselinje1,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
            landkode = landkode,
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
            postnummer = postnummer,
            poststed = poststed,
            adresselinje1 = adresselinje1!!,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
        ),
    )
}