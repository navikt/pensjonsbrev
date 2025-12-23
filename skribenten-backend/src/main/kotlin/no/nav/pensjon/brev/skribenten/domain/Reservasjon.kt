package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.NavIdent
import java.time.Duration
import java.time.Instant

data class Reservasjon(
    val vellykket: Boolean,
    val reservertAv: NavIdent,
    val timestamp: Instant,
    val expiresIn: Duration,
    val redigertBrevHash: Hash<Edit.Letter>,
)