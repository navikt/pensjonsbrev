package no.nav.pensjon.brev.brevoppskrift.auth

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.isHandled
import io.ktor.server.auth.AuthenticationChecked
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext
import no.nav.pensjon.brev.brevoppskrift.api.ApiFailure

val EditorAccess = createRouteScopedPlugin("EditorAccess") {
    on(AuthenticationChecked) { call ->
        if (call.isHandled) return@on
        val user = call.principal<DraftUser>() ?: throw ApiFailure(HttpStatusCode.Unauthorized, "UNAUTHORIZED", "Innlogging kreves.")
        if (!user.canEdit) throw ApiFailure(HttpStatusCode.Forbidden, "FORBIDDEN", "Du har ikke tilgang til å endre brevmaler.")
    }
}

fun Route.editorRoutes(build: Route.() -> Unit): Route = createChild(EditorAccessRouteSelector()).apply {
    install(EditorAccess)
    build()
}

private class EditorAccessRouteSelector : RouteSelector() {
    override suspend fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation = RouteSelectorEvaluation.Transparent
    override fun toString(): String = "(editor)"
}
