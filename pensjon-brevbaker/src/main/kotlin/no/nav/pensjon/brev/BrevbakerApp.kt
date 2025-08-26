package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.maler.ProductionTemplates

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.brevbakerModulePensjon() = this.brevbakerModule(pensjonOgUfoereProductionTemplates)

val pensjonOgUfoereProductionTemplates = object : AllTemplates {
    override fun hentAutobrevmaler() = ProductionTemplates.hentAutobrevmaler() + UfoereTemplates.hentAutobrevmaler()

    override fun hentRedigerbareMaler() = ProductionTemplates.hentRedigerbareMaler() + UfoereTemplates.hentRedigerbareMaler()

}