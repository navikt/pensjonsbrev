package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.TemplateResourceV2
import no.nav.pensjon.brev.api.description
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer
import no.nav.pensjon.brevbaker.api.model.LanguageCode

inline fun <reified Kode : Enum<Kode>, T : BrevTemplate<BrevbakerBrevdata, Kode>> Route.templateRoutes(resourceV2: TemplateResourceV2<Kode, T>) =
    route("/${resourceV2.name}") {

        get { call.respond(resourceV2.templates.keys) }

        route("/{kode}") {
            get {
                val template = call.parameters.getOrFail<Kode>("kode")
                    .let { resourceV2.templates[it] }
                    ?.template
                    ?.description()

                if (template != null) {
                    call.respond(template)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/doc/{language}") {
                val language = call.parameters.getOrFail<LanguageCode>("language").toLanguage()

                val template = call.parameters.getOrFail<Kode>("kode")
                    .let { resourceV2.templates[it]?.template }
                    ?.takeIf { it.language.supports(language) }

                if (template != null) {
                    call.respond(TemplateDocumentationRenderer.render(template, language))
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/modelSpecification") {
                val template = call.parameters.getOrFail<Kode>("kode")
                    .let { resourceV2.templates[it]?.template }

                if (template != null) {
                    call.respond(template.modelSpecification)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }