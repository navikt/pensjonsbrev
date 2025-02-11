package no.nav.pensjon.brev.skribenten.auth

import io.ktor.server.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.withContext
import no.nav.pensjon.brev.skribenten.context.*
import no.nav.pensjon.brev.skribenten.principal
import kotlin.coroutines.CoroutineContext
import io.ktor.server.application.Hook as KtorHook

private val PrincipalInContextPhase: PipelinePhase = PipelinePhase("PrincipalInContext")
private val PrincipalContextPhase: PipelinePhase = PipelinePhase("PrincipalContext")

class PrincipalInContext {
    object Hook : KtorHook<suspend (ApplicationCall) -> Unit> {
        override fun install(
            pipeline: ApplicationCallPipeline,
            handler: suspend (ApplicationCall) -> Unit,
        ) {
            pipeline.insertPhaseBefore(ApplicationCallPipeline.Call, PrincipalContextPhase)
            pipeline.insertPhaseAfter(PrincipalContextPhase, PrincipalInContextPhase)
            pipeline.intercept(PrincipalInContextPhase) { handler(call) }
        }
    }

    companion object :
        ContextValue<UserPrincipal> by ContextValueProvider(ContextElement, "UserPrincipal", ContextElement::principal),
        BaseRouteScopedPlugin<Nothing, PrincipalInContext> {
        override val key = AttributeKey<PrincipalInContext>("PrincipalCoroutineContext")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Nothing.() -> Unit,
        ): PrincipalInContext {
            val plugin = PrincipalInContext()

            pipeline.insertPhaseBefore(ApplicationCallPipeline.Call, PrincipalContextPhase)
            pipeline.intercept(PrincipalContextPhase) {
                val principal = call.principal()

                withPrincipal(principal) {
                    proceed()
                }
            }
            return plugin
        }
    }
}

private class ContextElement(val principal: UserPrincipal) : CoroutineContext.Element {
    override val key: CoroutineContext.Key<*>
        get() = ContextElement

    companion object : CoroutineContext.Key<ContextElement>
}

suspend fun <T> withPrincipal(
    principal: UserPrincipal,
    block: suspend () -> T,
): T =
    withContext(ContextElement(principal)) {
        block()
    }
