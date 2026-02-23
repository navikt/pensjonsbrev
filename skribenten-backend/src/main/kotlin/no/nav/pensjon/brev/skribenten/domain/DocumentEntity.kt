package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.db.DocumentTable
import no.nav.pensjon.brev.skribenten.model.Dto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class DocumentEntity(id: EntityID<Long>) : LongEntity(id) {
    var brevredigering by BrevredigeringEntity referencedOn DocumentTable.brevredigering
    var dokumentDato by DocumentTable.dokumentDato
    var pdf by DocumentTable.pdfKryptert
    var redigertBrevHash by DocumentTable.redigertBrevHash
    var brevdataHash by DocumentTable.brevdataHash

    companion object : LongEntityClass<DocumentEntity>(DocumentTable)

    fun toDto(): Dto.Document =
        Dto.Document(
            dokumentDato = dokumentDato,
            pdf = pdf,
            redigertBrevHash = redigertBrevHash,
            brevdataHash = brevdataHash
        )
}


