package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.db.RedigertVedleggTable
import no.nav.pensjon.brev.skribenten.db.writeHashTo
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class RedigertVedlegg(id: EntityID<Long>) : LongEntity(id) {
    var brevredigering by RedigertVedleggTable.brevredigering
    var vedleggId by RedigertVedleggTable.vedleggId
    var redigertVedlegg by RedigertVedleggTable.redigertVedleggKryptert
        .writeHashTo(RedigertVedleggTable.redigertVedleggKryptertHash)

    companion object : LongEntityClass<RedigertVedlegg>(RedigertVedleggTable)
}
