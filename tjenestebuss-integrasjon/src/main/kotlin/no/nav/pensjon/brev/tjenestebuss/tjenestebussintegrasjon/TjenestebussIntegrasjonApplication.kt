package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon
import com.typesafe.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
private data class ResourceNotFoundException(override val message: String?): RuntimeException(message)

fun main() {
	val tjenestebussIntegrasjonConfig: Config = ConfigFactory.load(ConfigParseOptions.defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
		.getConfig("tjenestebussintegrasjon")
		.resolveWith(ConfigFactory.parseResources("sts/password", ConfigParseOptions.defaults().apply {
			classLoader.getResources("sts/username")
		}))
	embeddedServer(Netty, port = tjenestebussIntegrasjonConfig.getInt("port"), host = "0.0.0.0") {
		tjenestebussIntegrationApi(tjenestebussIntegrasjonConfig)
	}.start(wait = true)
}
fun getResourceAsText(path: String): String? =
	object {}.javaClass.getResource(path)?.readText()