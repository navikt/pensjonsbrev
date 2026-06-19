package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.db.RedigertVedleggTable
import no.nav.pensjon.brev.skribenten.db.writeHashTo
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.dao.CompositeEntityClass

class RedigertVedlegg(id: EntityID<CompositeID>) : CompositeEntity(id) {
    var brevredigering by RedigertVedleggTable.brevredigering
    var vedleggId by RedigertVedleggTable.vedleggId
    var redigertVedlegg by RedigertVedleggTable.redigertVedleggKryptert
        .writeHashTo(RedigertVedleggTable.redigertVedleggKryptertHash)
    val redigertVedleggHash by RedigertVedleggTable.redigertVedleggKryptertHash

    companion object : CompositeEntityClass<RedigertVedlegg>(RedigertVedleggTable)
}
