package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.db.ValgteVedleggTable
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass

class ValgteVedlegg(brevredigeringId: EntityID<BrevId>) : Entity<BrevId>(brevredigeringId) {
    var valgteVedlegg by ValgteVedleggTable.valgteVedlegg
    companion object : EntityClass<BrevId, ValgteVedlegg>(ValgteVedleggTable)
}