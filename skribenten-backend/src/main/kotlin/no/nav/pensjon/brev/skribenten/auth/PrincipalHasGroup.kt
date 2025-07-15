package no.nav.pensjon.brev.skribenten.auth

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("PrincipalHasGroup")

class PrincipalHasGroupConfiguration {
    private val _groups = mutableListOf<Set<ADGroup>>()

    // requires at least one of the groups in each list to be present in the principal's groups
    val groupRequirements: List<Set<ADGroup>> get() = _groups

    fun require(group: ADGroup) {
        requireOneOf(setOf(group))
    }

    fun requireOneOf(groups: Set<ADGroup>) {
        if (groups.isNotEmpty()) {
            _groups.add(groups)
        }
    }
}

val PrincipalHasGroup = createRouteScopedPlugin("PrincipalHasGroup", ::PrincipalHasGroupConfiguration) {
    on(PrincipalInContext.Hook) { call ->
        val principal = PrincipalInContext.require()
        val requiredGroups = pluginConfig.groupRequirements

        // find the first group requirement that the principal does not have access to
        val firstMissingGroupRequirement = requiredGroups.firstOrNull { oneOfGroups -> oneOfGroups.all { !principal.isInGroup(it) } }

        if (firstMissingGroupRequirement != null) {
            logger.info("Tilgang avvist for bruker ${principal.navIdent} som mangler tilgang til en av gruppene: ${firstMissingGroupRequirement.joinToString(", ")}")
            call.respond(HttpStatusCode.Forbidden)
        }
    }
}