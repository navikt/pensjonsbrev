package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.FeatureToggleService
import no.nav.pensjon.brev.skribenten.UnleashToggle
import no.nav.pensjon.brev.skribenten.model.Api

context(app: Application)
fun Route.featureToggleRoute() {
    val featureToggleService: FeatureToggleService by app.dependencies

    route("/features") {
        // Tar imot toggle-navnet uten prefiks ("featureName"). UnleashService legger på
        // "pensjonsbrev.skribenten."-prefikset, så frontend skal ikke ha noe forhold til det.
        // Det finnes bevisst ingen statisk liste over gyldige toggles her: hvilke toggles som
        get("/{featureName}") {
            val featureName = call.parameters["featureName"]
            if (featureName.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "featureName mangler")
                return@get
            }

            val enabled = featureToggleService.isEnabled(UnleashToggle(featureName))
            call.respond(Api.FeatureToggleResponse(enabled = enabled))
        }
    }
}
