package no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang

import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.db.Transactional
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FerdigRedigertPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.SendBrevPolicy

fun testBrevtilgang(
    ferdigRedigertPolicy: FerdigRedigertPolicy = FerdigRedigertPolicy(),
) = Brevtilgang(
    redigerBrevPolicy = RedigerBrevPolicy(),
    attesterBrevPolicy = AttesterBrevPolicy(),
    sendBrevPolicy = SendBrevPolicy(ferdigRedigertPolicy),
    brevreservasjonPolicy = BrevreservasjonPolicy(),
    transactional = Transactional(SharedPostgres.database),
)
