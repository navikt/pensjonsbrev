package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.alder.maler.AlderTemplates
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.planleggepensjon.PlanleggePensjonTemplates
import no.nav.pensjon.brev.ufore.maler.UfoereTemplates
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.BrevbakerApp")

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.brevbakerModulePensjon() = try {
    this.brevbakerModule(pensjonOgUfoereProductionTemplates)
} catch (e: Exception) {
    logger.error(e.message, e)
    throw e
}

val pensjonOgUfoereProductionTemplates = object : AllTemplates {
    private val alleMaler = setOf(
        ProductionTemplates, AlderTemplates, UfoereTemplates, PlanleggePensjonTemplates
    )

    override fun hentAutobrevmaler() = alleMaler.flatMap { it.hentAutobrevmaler() }.toSet()

    override fun hentRedigerbareMaler() = alleMaler.flatMap { it.hentRedigerbareMaler() }.toSet()

    override fun hentAlltidValgbareVedlegg() = alleMaler.flatMap { it.hentAlltidValgbareVedlegg() }.toSet()
}