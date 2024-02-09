package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions
import com.typesafe.config.ConfigResolveOptions
import com.typesafe.config.ConfigValueFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File

fun main() {
    val tjenestebussIntegrasjonConfig: Config =
        ConfigFactory.load(ConfigParseOptions.defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
            .getConfig("tjenestebussintegrasjon")
            .resolveWith(getVaultSecretConfig(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
            .resolveWithMultiple("sts/auth", "azuread", "pensjonsbrev/auth")
    embeddedServer(Netty, port = tjenestebussIntegrasjonConfig.getInt("port"), host = "0.0.0.0") {
        tjenestebussIntegrationApi(tjenestebussIntegrasjonConfig)
    }.start(wait = true)
}

/**
 * Mapper innholdet i filer under /secrets/xyz/...
 * F.eks innholdet i secrets/sts/username mappes til STS_USERNAME
 */
fun getVaultSecretConfig(): Config {
    val secrets = File("/secrets")

    return if (secrets.isDirectory) {
        secrets.listFiles()?.flatMap { secretDir ->
            secretDir.listFiles()
                ?.map {
                    "${secretDir.name.uppercase()}_${it.name.uppercase()}" to ConfigValueFactory.fromAnyRef(
                        it.readText().trim(), it.absolutePath
                    )
                }
                ?: emptyList()
        }?.fold(ConfigFactory.empty()) { config, value -> config.withValue(value.first, value.second) }
            ?: ConfigFactory.empty()
    } else ConfigFactory.empty()
}

fun Config.resolveWithMultiple(vararg paths: String): Config =
    paths.foldIndexed(this) { index, current, path ->
        current.resolveWith(ConfigFactory.load(path), ConfigResolveOptions.defaults().setAllowUnresolved(index != paths.lastIndex))
    }