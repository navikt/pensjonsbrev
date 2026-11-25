package no.nav.pensjon.brev.brevoppskrift

import com.auth0.jwk.JwkProvider
import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.getAs
import no.nav.pensjon.brev.brevoppskrift.lagring.DatabaseConfig
import no.nav.pensjon.brev.brevoppskrift.api.*
import no.nav.pensjon.brev.brevoppskrift.model.*
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.PrService

fun Application.testModule(
    azure: AzureConfig,
    jwks: JwkProvider,
    database: DatabaseConfig,
    productionAuthoring: Boolean = false,
    integrations: IntegrationsConfig = IntegrationsConfig(),
    prService: PrService? = null,
) {
    val config = ApplicationConfig("application-test.conf").config("brevoppskrift").getAs<BrevoppskriftConfig>()
    val testConfig = config.copy(azure = azure, database = database, integrations = integrations)
    if (productionAuthoring) {
        configureDependencies(testConfig, jwks, prService = prService)
    } else {
        configureDependencies(testConfig, jwks, prService = prService) {
            provide<SourceCatalogue> {
                object : SourceCatalogue {
                    override fun list() = emptyList<TemplateSource>()
                    override fun import(sourceId: SourceId): Draft? = null
                    override suspend fun checkSource(draft: Draft) = emptyList<ValidationIssue>()
                }
            }
            provide<DraftGenerator> {
                object : DraftGenerator {
                    override suspend fun generate(draft: Draft): GenerationResult = error("Use production authoring services for generation")
                }
            }
            provide<DraftPreview> {
                object : DraftPreview {
                    override fun render(request: PreviewRequest): PreviewResult = error("Use production authoring services for preview")
                }
            }
        }
    }
    configureApplication()
}
