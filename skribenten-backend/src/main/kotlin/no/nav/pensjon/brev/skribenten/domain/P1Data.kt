package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.db.P1DataTable
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass


class P1Data(brevredigeringId: EntityID<BrevId>) : Entity<BrevId>(brevredigeringId) {
    var p1data by P1DataTable.p1data
    companion object : EntityClass<BrevId, P1Data>(P1DataTable)
}