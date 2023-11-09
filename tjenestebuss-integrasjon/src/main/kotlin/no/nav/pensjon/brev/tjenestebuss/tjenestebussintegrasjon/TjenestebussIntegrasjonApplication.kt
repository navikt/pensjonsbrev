package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon
import com.typesafe.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
	val tjenestebussIntegrasjonConfig: Config = ConfigFactory.load(ConfigParseOptions.defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
		.getConfig("tjenestebussintegrasjon")
		.resolveWith(ConfigFactory.load("sts/auth"))
	embeddedServer(Netty, port = tjenestebussIntegrasjonConfig.getInt("port"), host = "0.0.0.0") {
		tjenestebussIntegrationApi(tjenestebussIntegrasjonConfig)
	}.start(wait = true)
}