package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.pensjon.brev.maler.ProductionTemplates

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.brevbakerModulePensjon() = this.brevbakerModule(ProductionTemplates)