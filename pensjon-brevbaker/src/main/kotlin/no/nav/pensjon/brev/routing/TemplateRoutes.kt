package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.AutobrevTemplateResource
import no.nav.pensjon.brev.api.model.maler.AutomatiskBrevkode
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.TemplateModelSpecificationFactory
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer
import no.nav.pensjon.brevbaker.api.model.LanguageCode

inline fun <reified Kode : Brevkode<Kode>, T : BrevTemplate<BrevbakerBrevdata, Kode>> Route.templateRoutes(resource: AutobrevTemplateResource<Kode, T>) =
    route("/${resource.name}") {

        get {
            if (call.parameters["includeMetadata"] == "true") {
                call.respond(resource.listTemplatesWithMetadata())
            } else {
                call.respond(resource.listTemplatekeys())
            }
        }

        route("/{kode}") {
            get {
                val template = call.kode(resource)
                    .let { resource.getTemplate(it) }
                    ?.description()

                if (template != null) {
                    call.respond(template)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/doc/{language}") {
                val language = call.parameters.getOrFail<LanguageCode>("language").toLanguage()

                val template = call.kode(resource)
                    .let { resource.getTemplate(it)?.template }
                    ?.takeIf { it.language.supports(language) }

                if (template != null) {
                    call.respond(TemplateDocumentationRenderer.render(template, language, template.modelSpecification()))
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/modelSpecification") {
                val template = call.kode(resource)
                    .let { resource.getTemplate(it)?.template }

                if (template != null) {
                    call.respond(template.modelSpecification())
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }

fun LetterTemplate<*, *>.modelSpecification() = TemplateModelSpecificationFactory(this.letterDataType).build()

// TODO: Med riktig typing burde heile denne metoden vera unødvendig
fun <Kode: Brevkode<Kode>> ApplicationCall.kode(resource: AutobrevTemplateResource<Kode,*>): Kode = parameters.getOrFail<String>("kode").let {
    if (resource.name == "autobrev") {
        AutomatiskBrevkode(it)
    } else {
        RedigerbarBrevkode(it)
    } as Kode
}