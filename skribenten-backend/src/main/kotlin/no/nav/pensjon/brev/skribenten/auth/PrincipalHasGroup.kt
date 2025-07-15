package no.nav.pensjon.brev.skribenten.auth

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("PrincipalHasGroup")

class PrincipalHasGroupConfiguration {
    // requires at least one of the groups in each list to be present in the principal's groups
    private val _groups = mutableListOf<Set<ADGroup>>()
    val groupRequirements: List<Set<ADGroup>> get() = _groups

    var responseHandler: suspend ApplicationCall.() -> Unit =
        { respond(HttpStatusCode.Forbidden, "Access denied: You do not have the required group access.") }
        private set

    fun require(group: ADGroup) {
        requireOneOf(setOf(group))
    }

    fun requireOneOf(groups: Set<ADGroup>) {
        if (groups.isNotEmpty()) {
            _groups.add(groups)
        }
    }

    fun onRejection(responseHandler: suspend ApplicationCall.() -> Unit) {
        this.responseHandler = responseHandler
    }
}

val PrincipalHasGroup = createRouteScopedPlugin("PrincipalHasGroup", ::PrincipalHasGroupConfiguration) {
    on(PrincipalInContext.Hook) { call ->
        val principal = PrincipalInContext.require()
        val requiredGroupSets = pluginConfig.groupRequirements

        // find the first group requirement that the principal does not have access to
        val firstMissingGroupRequirement = requiredGroupSets.firstOrNull { oneOfGroups: Set<ADGroup> ->
            oneOfGroups.none { group -> principal.isInGroup(group) }
        }

        if (firstMissingGroupRequirement != null) {
            logger.info("Tilgang avvist for bruker ${principal.navIdent} mangler tilgang til en av gruppene: ${firstMissingGroupRequirement.joinToString(", ")}")
            pluginConfig.responseHandler(call)
        }
    }
}