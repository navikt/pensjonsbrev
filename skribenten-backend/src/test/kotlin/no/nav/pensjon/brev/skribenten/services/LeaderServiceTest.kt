package no.nav.pensjon.brev.skribenten.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LeaderServiceTest {

    val leaderResponse = LeaderResponse("the-elected-leader")

    @Test
    fun `given a url leader election is enabled`() {
        val leaderService = NaisLeaderService(url = "http://mocked-leader-service", mockEngine(leaderResponse))
        assertThat(leaderService.isLeaderElectionEnabled).isTrue()
    }

    @Test
    fun `given no url leader election is disabled`() {
        val leaderService = NaisLeaderService(url = null, mockEngine(leaderResponse))
        assertThat(leaderService.isLeaderElectionEnabled).isFalse()
    }

    @Test
    fun `answers that current instance is elected leader`(): Unit =
        httpClientTest(LeaderResponse(NaisLeaderService.thisInstanceName())) { engine ->
            val leaderService = NaisLeaderService(url = "http://mocked-leader-service", engine)

            val leaderElection = leaderService.electedLeader()
            assertThat(leaderElection).isNotNull
            assertThat(leaderElection?.isThisInstanceLeader).isTrue()
            assertThat(leaderElection?.leaderName).isEqualTo(leaderElection?.thisInstanceName)
            assertThat(leaderElection?.thisInstanceName).isEqualTo(NaisLeaderService.thisInstanceName())
        }

    @Test
    fun `answers that current instance is not elected leader`(): Unit = httpClientTest(leaderResponse) { engine ->
        val leaderService = NaisLeaderService(url = "http://mocked-leader-service", engine)

        val leaderElection = leaderService.electedLeader()
        assertThat(leaderElection).isNotNull
        assertThat(leaderElection?.isThisInstanceLeader).isFalse()
        assertThat(leaderElection?.leaderName).isEqualTo("the-elected-leader")
        assertThat(leaderElection?.thisInstanceName).isEqualTo(NaisLeaderService.thisInstanceName())
    }

    @Test
    fun `answers with null if leader election is disabled`(): Unit = httpClientTest(leaderResponse) { engine ->
        val leaderService = NaisLeaderService(url = null, engine)

        val leaderElection = leaderService.electedLeader()
        assertThat(leaderElection).isNull()
    }
}