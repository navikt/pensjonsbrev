package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.model.LetterMetadata
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.BrevmalService
import org.slf4j.LoggerFactory

fun Route.brevmal(brevbakerService: BrevbakerService, brevmalService: BrevmalService) {
    val logger = LoggerFactory.getLogger("brevbakerRoute")

    route("/brevmal") {

        get {
            val hasAccessToEblanketter = principal().isInGroup(ADGroups.pensjonUtland)
            call.respond(brevmalService.hentBrevmaler(hasAccessToEblanketter))
        }

        route("/{brevkode}") {
            get {
                val brevkode = call.parameters.getBrevkode()
                val brevmal = brevbakerService.getRedigerbarTemplate(brevkode)?.let { LetterMetadata.Brevbaker(it) }
                if (brevmal != null) {
                    call.respond(HttpStatusCode.OK, brevmal)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/modelSpecification") {
                val brevkode = call.parameters.getBrevkode()
                val modelSpec = brevbakerService.getModelSpecification(brevkode)

                if (modelSpec != null) {
                    call.respond(HttpStatusCode.OK, modelSpec)
                } else {
                    logger.info("Fant ikke modelSpecification for brevkode ${brevkode.kode()}")
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}

private fun Parameters.getBrevkode(): RedigerbarBrevkode =
    RedigerbarBrevkode(getOrFail("brevkode"))