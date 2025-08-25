package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.brevbakerModulePensjon() = this.brevbakerModule(pensjonOgUfoereProductionTemplates)

val pensjonOgUfoereProductionTemplates = object : AllTemplates {
    override fun hentAutobrevmaler() = ProductionTemplates.hentAutobrevmaler() + UfoereProductionTemplates.hentAutobrevmaler()

    override fun hentRedigerbareMaler() = ProductionTemplates.hentRedigerbareMaler() + UfoereProductionTemplates.hentRedigerbareMaler()

}