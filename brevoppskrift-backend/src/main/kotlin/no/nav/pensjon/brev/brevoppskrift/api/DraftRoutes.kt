package no.nav.pensjon.brev.brevoppskrift.api

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.request.receive
import no.nav.pensjon.brev.brevoppskrift.ValidationResult
import no.nav.pensjon.brev.brevoppskrift.application.DraftHandler
import no.nav.pensjon.brev.brevoppskrift.application.PrJobHandler
import no.nav.pensjon.brev.brevoppskrift.auth.DraftUser
import no.nav.pensjon.brev.brevoppskrift.auth.editorRoutes
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import no.nav.pensjon.brev.brevoppskrift.model.Draft

fun Route.draftRoutes() {
    val handler: DraftHandler by application.dependencies
    val repository: DraftRepository by application.dependencies
    val prs: PrJobHandler by application.dependencies
    post("/utkast/valider") {
        validate(call.receive<Draft>())
        call.respond(ValidationResult(valid = true))
    }
    get("/utkast") { call.respond(repository.list()) }
    editorRoutes {
        post("/utkast") {
            val user = requireNotNull(call.principal<DraftUser>())
            val record = handler.create(call.receive<CreateDraftRequest>(), user.name)
            call.response.headers.append(HttpHeaders.Location, "/api/v1/utkast/${record.id.value}")
            call.respond(HttpStatusCode.Created, record)
        }
    }
    route("/utkast/{id}") {
        get { call.respond(handler.get(call.draftId())) }
        get("/bruk") { call.respond(handler.attachmentUses(call.draftId())) }
        get("/pr") { call.respond(prs.latest(call.draftId())) }
        editorRoutes {
            put {
                val user = requireNotNull(call.principal<DraftUser>())
                val request = call.receive<UpdateDraftRequest>()
                call.respond(handler.update(call.draftId(), request, user.name))
            }
            post("/status") {
                val user = requireNotNull(call.principal<DraftUser>())
                val request = call.receive<StatusRequest>()
                call.respond(handler.status(call.draftId(), request, user.name))
            }
            post("/grunnlinje") {
                val user = requireNotNull(call.principal<DraftUser>())
                val request = call.receive<RevisionRequest>()
                call.respond(handler.approve(call.draftId(), request, user.name))
            }
            post("/arkiver") {
                val user = requireNotNull(call.principal<DraftUser>())
                val request = call.receive<ArchiveRequest>()
                handler.archive(call.draftId(), request, user.name)
                call.respond(HttpStatusCode.NoContent)
            }
            post("/pr") {
                prs.requireEnabled()
                val request = call.receive<RevisionRequest>()
                call.respond(HttpStatusCode.Accepted, prs.submit(call.draftId(), request))
            }
        }
    }
}
