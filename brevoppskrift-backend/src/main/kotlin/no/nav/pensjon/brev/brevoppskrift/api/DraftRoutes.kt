package no.nav.pensjon.brev.brevoppskrift.api

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import no.nav.pensjon.brev.brevoppskrift.ValidationResult
import no.nav.pensjon.brev.brevoppskrift.application.DraftHandler
import no.nav.pensjon.brev.brevoppskrift.application.PrJobHandler
import no.nav.pensjon.brev.brevoppskrift.auth.requireEditor
import no.nav.pensjon.brev.brevoppskrift.lagring.*
import no.nav.pensjon.brev.brevoppskrift.model.Draft

fun Route.draftRoutes() {
    val handler: DraftHandler by application.dependencies
    val repository: DraftRepository by application.dependencies
    val prs: PrJobHandler by application.dependencies
    post("/utkast/valider") {
        validate(call.readJson<Draft>())
        call.respond(ValidationResult(valid = true))
    }
    get("/utkast") { call.respond(repository.list()) }
    post("/utkast") {
        val user = call.requireEditor()
        val record = handler.create(call.readJson<CreateDraftRequest>(), user.name)
        call.response.headers.append(HttpHeaders.Location, "/api/v1/utkast/${record.id.value}")
        call.respond(HttpStatusCode.Created, record)
    }
    route("/utkast/{id}") {
        get { call.respond(handler.get(call.draftId())) }
        get("/bruk") { call.respond(handler.attachmentUses(call.draftId())) }
        put {
            val user = call.requireEditor()
            val request = call.readJson<UpdateDraftRequest>()
            call.respond(handler.update(call.draftId(), request, user.name))
        }
        post("/status") {
            val user = call.requireEditor()
            val request = call.readJson<StatusRequest>()
            call.respond(handler.status(call.draftId(), request, user.name))
        }
        post("/grunnlinje") {
            val user = call.requireEditor()
            val request = call.readJson<RevisionRequest>()
            call.respond(handler.approve(call.draftId(), request, user.name))
        }
        post("/arkiver") {
            val user = call.requireEditor()
            val request = call.readJson<ArchiveRequest>()
            handler.archive(call.draftId(), request, user.name)
            call.respond(HttpStatusCode.NoContent)
        }
        post("/pr") {
            call.requireEditor()
            prs.requireEnabled()
            val request = call.readJson<RevisionRequest>()
            call.respond(HttpStatusCode.Accepted, prs.submit(call.draftId(), request))
        }
        get("/pr") {
            call.respond(prs.latest(call.draftId()))
        }
    }
}
