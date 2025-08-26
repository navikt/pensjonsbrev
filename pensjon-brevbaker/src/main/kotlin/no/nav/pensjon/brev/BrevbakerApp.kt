package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.maler.AlderTemplates

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.brevbakerModulePensjon() = this.brevbakerModule(pensjonOgUfoereProductionTemplates)

val pensjonOgUfoereProductionTemplates = object : AllTemplates {
    override fun hentAutobrevmaler() = AlderTemplates.hentAutobrevmaler() + UfoereTemplates.hentAutobrevmaler()

    override fun hentRedigerbareMaler() = AlderTemplates.hentRedigerbareMaler() + UfoereTemplates.hentRedigerbareMaler()

}