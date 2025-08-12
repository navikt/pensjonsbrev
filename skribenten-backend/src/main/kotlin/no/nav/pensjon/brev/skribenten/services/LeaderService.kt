package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import java.net.InetAddress

data class LeaderElection(val isThisInstanceLeader: Boolean, val thisInstanceName: String, val leaderName: String)
private data class LeaderResponse(val name: String)

class LeaderService(
    private val url: String?,
    clientEngine: HttpClientEngine = CIO.create(),
) {
    constructor(config: Config, clientEngine: HttpClientEngine = CIO.create()) : this(
        url = config.tryGetString("leader.url"),
        clientEngine = clientEngine
    )

    private val client = HttpClient(clientEngine) {
        install(ContentNegotiation) {
            jackson {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }
    private val thisInstanceName by lazy { thisInstanceName() }

    val isLeaderElectionEnabled: Boolean
        get() = !url.isNullOrEmpty()

    private suspend fun fetchLeader(): LeaderResponse? =
        if (!url.isNullOrEmpty()) {
            client.get(url).body<LeaderResponse>()
        } else {
            null
        }

    suspend fun electedLeader(): LeaderElection? {
        return fetchLeader()?.name?.let {
            LeaderElection(
                isThisInstanceLeader = it == thisInstanceName,
                thisInstanceName = thisInstanceName,
                leaderName = it
            )
        }
    }

    companion object {
        fun thisInstanceName(): String = InetAddress.getLocalHost().hostName
    }
}
