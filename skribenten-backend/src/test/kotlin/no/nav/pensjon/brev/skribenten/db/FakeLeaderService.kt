package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.services.LeaderElection
import no.nav.pensjon.brev.skribenten.services.LeaderService

class FakeLeaderService(
    override val isLeaderElectionEnabled: Boolean = true,
    val isThisInstanceLeader: Boolean
) : LeaderService {
    override suspend fun electedLeader() = LeaderElection(
        isThisInstanceLeader = isThisInstanceLeader,
        thisInstanceName = if (isThisInstanceLeader) { "leader-instance" } else { "not-leader-instance" },
        leaderName = "leader-instance"
    )
}