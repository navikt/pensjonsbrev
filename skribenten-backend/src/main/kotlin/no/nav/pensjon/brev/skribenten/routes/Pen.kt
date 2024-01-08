package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getClaim
import no.nav.pensjon.brev.skribenten.services.*

data class OrderLetterRequest(
    val brevkode: String,
    val spraak: SpraakKode,
    val sakId: Long,
    val gjelderPid: String,
    val landkode: String? = null,
    val mottakerText: String? = null,
)

fun Route.penRoute(penService: PenService, safService: SafService) {
    route("/pen") {
        post("/extream") {
//            // TODO skal vi validere metadata?
//            val request = call.receive<OrderLetterRequest>()
//            //TODO try to get extra claims when authorizing user instead of using graph service.
//            val name = getClaim("name") ?: throw UnauthorizedException("Could not find name of user")
//
//            // TODO create respond on error or similar function to avoid boilerplate. RespondOnError?
//            val onPremisesSamAccountName: String =
//                when (val response = microsoftGraphService.getOnPremisesSamAccountName(call)) {
//                    is ServiceResult.Ok -> response.result
//                    is ServiceResult.Error, is ServiceResult.AuthorizationError -> {
//                        respondWithResult(response)
//                        return@post
//                    }
//                }
//
//            //TODO better error handling.
//            // TODO access controls for e-blanketter
//            penService.bestillExtreamBrev(call, request, name, onPremisesSamAccountName).map { journalpostId ->
//                val error = safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)
//                if (error != null) {
//                    if (error.type == SafService.JournalpostLoadingError.ErrorType.TIMEOUT) {
//                        call.respond(HttpStatusCode.RequestTimeout, error.error)
//                    } else {
//                        call.respondText(text = error.error, status = HttpStatusCode.InternalServerError)
//                    }
//                } else {
//                    respondWithResult(penService.redigerExtreamBrev(call, journalpostId))
//                }
//            }
        }

        post("/doksys") {
//            val name = getClaim("name") ?: throw UnauthorizedException("Could not find name of user")
//            val request = call.receive<OrderLetterRequest>()
//            val onPremisesSamAccountName: String =
//                when (val response = microsoftGraphService.getOnPremisesSamAccountName(call)) {
//                    is ServiceResult.Ok -> response.result
//                    is ServiceResult.Error, is ServiceResult.AuthorizationError -> {
//                        respondWithResult(response)
//                        return@post
//                    }
//                }
//            respondWithResult(
//                penService.bestillDoksysBrev(call, request, name, onPremisesSamAccountName),
//                onError = { _, _ -> },
//            )
//            when (val response = penService.bestillDoksysBrev(call, request, name, onPremisesSamAccountName)) {
//                is ServiceResult.Ok -> {
//                    val journalpostId = response.result
//                    respondWithResult(penService.redigerDoksysBrev(call, journalpostId))
//                }
//
//                is ServiceResult.Error, is ServiceResult.AuthorizationError -> {
//                    respondWithResult(response)
//                    return@post
//                }
//            }
        }

        //TODO Check access using /tilganger(?). Is there an on behalf of endpoint which checks access?
        get("/sak/{sakId}") {
            val sakId = call.parameters.getOrFail("sakId")
            respondWithResult(penService.hentSak(call, sakId))
        }
    }


}