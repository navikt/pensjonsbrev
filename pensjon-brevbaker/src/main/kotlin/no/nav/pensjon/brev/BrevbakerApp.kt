package no.nav.pensjon.brev

import io.ktor.server.application.Application
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.etterlatte.EtterlatteMaler

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.brevbakerModulePensjon() = this.brevbakerModule(ProductionTemplates, EtterlatteMaler)