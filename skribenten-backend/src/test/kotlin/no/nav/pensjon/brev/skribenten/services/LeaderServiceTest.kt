package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LeaderServiceTest {

    private val mockClient = MockEngine {
        respond(
            content = """{"name": "the-elected-leader"}""",
            status = HttpStatusCode.OK,
            headers = headersOf("Content-Type", "application/json")
        )
    }

    @Test
    fun `given a url leader election is enabled`() {
        val leaderService = NaisLeaderService(url = "http://mocked-leader-service", clientEngine = mockClient)
        assertThat(leaderService.isLeaderElectionEnabled).isTrue()
    }

    @Test
    fun `given no url leader election is disabled`() {
        val leaderService = NaisLeaderService(url = null, clientEngine = mockClient)
        assertThat(leaderService.isLeaderElectionEnabled).isFalse()
    }

    @Test
    fun `answers that current instance is elected leader`(): Unit = runBlocking {
        val mockClient = MockEngine {
            respond(
                content = """{"name": "${NaisLeaderService.thisInstanceName()}"}""",
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }

        val leaderService = NaisLeaderService(url = "http://mocked-leader-service", clientEngine = mockClient)

        val leaderElection = leaderService.electedLeader()
        assertThat(leaderElection).isNotNull
        assertThat(leaderElection?.isThisInstanceLeader).isTrue()
        assertThat(leaderElection?.leaderName).isEqualTo(leaderElection?.thisInstanceName)
        assertThat(leaderElection?.thisInstanceName).isEqualTo(NaisLeaderService.thisInstanceName())
    }

    @Test
    fun `answers that current instance is not elected leader`(): Unit = runBlocking {
        val leaderService = NaisLeaderService(url = "http://mocked-leader-service", clientEngine = mockClient)

        val leaderElection = leaderService.electedLeader()
        assertThat(leaderElection).isNotNull
        assertThat(leaderElection?.isThisInstanceLeader).isFalse()
        assertThat(leaderElection?.leaderName).isEqualTo("the-elected-leader")
        assertThat(leaderElection?.thisInstanceName).isEqualTo(NaisLeaderService.thisInstanceName())
    }

    @Test
    fun `answers with null if leader election is disabled`(): Unit = runBlocking {
        val leaderService = NaisLeaderService(url = null, clientEngine = mockClient)

        val leaderElection = leaderService.electedLeader()
        assertThat(leaderElection).isNull()
    }
}