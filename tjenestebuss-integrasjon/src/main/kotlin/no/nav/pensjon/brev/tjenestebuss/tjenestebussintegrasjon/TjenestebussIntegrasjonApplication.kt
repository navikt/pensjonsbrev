package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon
import com.typesafe.config.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth.tjenestebusJwt
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth.requireAzureADConfig

fun main() {
	val tjenestebussIntegrasjonConfig: Config = ConfigFactory.load(ConfigParseOptions.defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
		.getConfig("tjenestebussintegrasjon")
		.resolveWith(ConfigFactory.load("sts/auth"), ConfigResolveOptions.defaults().setAllowUnresolved(true))
		.resolveWith(ConfigFactory.load("azuread"))
	embeddedServer(Netty, port = tjenestebussIntegrasjonConfig.getInt("port"), host = "0.0.0.0") {

		val azureADConfig = tjenestebussIntegrasjonConfig.requireAzureADConfig()
		install(Authentication) {
			tjenestebusJwt(azureADConfig)
		}

		tjenestebussIntegrationApi(azureADConfig, tjenestebussIntegrasjonConfig)
	}.start(wait = true)
}