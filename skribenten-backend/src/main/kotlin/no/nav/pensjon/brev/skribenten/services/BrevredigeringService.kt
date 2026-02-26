package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.letter.alleFritekstFelterErRedigert
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

data class GeneriskRedigerbarBrevdata(
    override val pesysData: FagsystemBrevdata,
    override val saksbehandlerValg: SaksbehandlerValgBrevdata,
) : RedigerbarBrevdata<SaksbehandlerValgBrevdata, FagsystemBrevdata>

sealed class BrevredigeringException(override val message: String) : Exception() {
    class BrevIkkeKlartTilSendingException(message: String) : BrevredigeringException(message)
    class NyereVersjonFinsException(message: String) : BrevredigeringException(message)
    class BrevmalFinnesIkke(message: String) : BrevredigeringException(message)
}

interface HentBrevService {
    fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo>
}

class BrevredigeringService(
    private val brevbakerService: BrevbakerService,
    private val penService: PenService,
) : HentBrevService {
    private val brevreservasjonPolicy = BrevreservasjonPolicy()


    /**
     * Slett brev med id.
     * @return `true` om brevet ble slettet, false om brevet ikke eksisterer,
     */
    fun slettBrev(saksId: SaksId, brevId: BrevId): Boolean {
        return transaction {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
            if (brev != null) {
                brev.delete()
                true
            } else {
                false
            }
        }
    }

    override fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId inList saksIder }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }

     suspend fun sendBrev(saksId: SaksId, brevId: BrevId): Pen.BestillBrevResponse? {
        val (brev, document) = transaction {
            BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
                .let { it?.toDto(brevreservasjonPolicy, null) to it?.document }
        }

        return if (brev != null && document != null) {
            if (!brev.info.laastForRedigering) {
                throw BrevIkkeKlartTilSendingException("Brev må være markert som klar til sending")
            }
            brev.validerErFerdigRedigert()
            if (document.redigertBrevHash != brev.redigertBrevHash) {
                throw NyereVersjonFinsException("Det finnes en nyere versjon av brevet enn den som er generert til PDF")
            }

            val template = brevbakerService.getRedigerbarTemplate(brev.info.brevkode)

            if (template == null) {
                throw BrevmalFinnesIkke("Mangler TemplateDescription for ${brev.info.brevkode}")
            } else {
                validerVedtaksbrevAttestert(brev, template.metadata.brevtype)
                penService.sendbrev(
                    sendRedigerbartBrevRequest = Pen.SendRedigerbartBrevRequest(
                        dokumentDato = document.dokumentDato,
                        saksId = brev.info.saksId,
                        enhetId = brev.info.avsenderEnhetId,
                        templateDescription = template,
                        brevkode = brev.info.brevkode,
                        pdf = document.pdf,
                        eksternReferanseId = "skribenten:${brev.info.id.id}",
                        mottaker = brev.info.mottaker?.toPen(),
                    ),
                    distribuer = brev.info.distribusjonstype == Distribusjonstype.SENTRALPRINT,
                ).also {
                    transaction {
                        if (it.journalpostId != null) {
                            if (it.error == null) {
                                BrevredigeringEntity[brevId].delete()
                            } else {
                                BrevredigeringEntity[brevId].journalpostId = it.journalpostId
                            }
                        }
                    }
                }
            }
        } else null
    }

    private fun validerVedtaksbrevAttestert(brev: Dto.Brevredigering, brevtype: LetterMetadata.Brevtype) {
        if (brevtype == LetterMetadata.Brevtype.VEDTAKSBREV && brev.info.attestertAv == null) {
            throw BrevIkkeKlartTilSendingException("Brev med id ${brev.info.id} er ikke attestert.")
        }
    }

}

private fun Dto.Brevredigering.validerErFerdigRedigert(): Boolean =
    redigertBrev.alleFritekstFelterErRedigert() || throw BrevIkkeKlartTilSendingException("Brevet inneholder fritekst-felter som ikke er endret")
