package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.db.MottakerType
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.ExternalAPI
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import no.nav.pensjon.brev.skribenten.services.toApi
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ExternalAPI")

fun Route.externalAPI(brevredigeringService: BrevredigeringService, brevbakerService: BrevbakerService) =
    route("/external/api/v1") {

        get("/brev") {
            val saksIder = call.queryParameters.getAll("saksId")
                ?.flatMap { it.split(",") }
                ?.mapNotNull { it.toLongOrNull() }
                ?: emptyList()

            val alleBrev = saksIder.flatMap { brevredigeringService.hentBrevForSak(it) }
            val maler = alleBrev.map { it.brevkode }.toSet().associateWith { brevbakerService.getRedigerbarTemplate(it) }

            call.respond(alleBrev.mapNotNull { it.toExternal(maler[it.brevkode]) })
        }
    }

private fun Dto.BrevInfo.toExternal(template: TemplateDescription.Redigerbar?) =
    if (template != null) {
        ExternalAPI.BrevInfo(
            url = TODO(),
            id = id,
            saksId = saksId,
            vedtaksId = vedtaksId,
            journalpostId = journalpostId,
            brevkode = brevkode,
            tittel = template.metadata.displayTitle,
            brevtype = template.metadata.brevtype,
            avsenderEnhetsId = avsenderEnhetId,
            spraak = spraak.toApi(),
            opprettetAv = opprettetAv,
            sistRedigertAv = sistredigertAv,
            redigeresAv = redigeresAv,
            opprettet = opprettet,
            sistRedigert = sistredigert,
            overstyrtMottaker = mottaker?.toExternal(),
            status = when (status) {
                Dto.BrevStatus.KLADD -> ExternalAPI.BrevStatus.KLADD
                Dto.BrevStatus.ATTESTERING -> ExternalAPI.BrevStatus.ATTESTERING
                Dto.BrevStatus.KLAR -> ExternalAPI.BrevStatus.KLAR
                Dto.BrevStatus.ARKIVERT -> ExternalAPI.BrevStatus.ARKIVERT
            },
        )
    } else {
        logger.warn("Fant ikke brevmal med brevkode $brevkode for brev med id $id")
        null
    }

private fun Dto.Mottaker.toExternal(): ExternalAPI.OverstyrtMottaker = when (type) {
    MottakerType.SAMHANDLER -> ExternalAPI.OverstyrtMottaker.Samhandler(tssId!!)
    MottakerType.NORSK_ADRESSE -> ExternalAPI.OverstyrtMottaker.NorskAdresse(
        navn!!,
        postnummer!!,
        poststed!!,
        adresselinje1,
        adresselinje2,
        adresselinje3
    )

    MottakerType.UTENLANDSK_ADRESSE -> ExternalAPI.OverstyrtMottaker.UtenlandskAdresse(
        navn!!,
        postnummer,
        poststed,
        adresselinje1!!,
        adresselinje2,
        adresselinje3,
        landkode!!
    )
}