package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.services.Brevredigering.redigerbarBrevdata
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class BrevredigeringService {
    fun hentBrevredigering(opprettetAvNavident: String, saksid: String, brevkode: String): List<BrevredigeringDto> =
        transaction {
            Brevredigering.select {
                Brevredigering.opprettetAvNavident eq opprettetAvNavident
                Brevredigering.saksid eq saksid
                Brevredigering.brevkode eq brevkode
            }.map { 
                BrevredigeringDto(
                it[Brevredigering.saksid], 
                it[Brevredigering.opprettetAvNavident],
                it[Brevredigering.brevkode],
                RedigerbarBrevdataDto(it[redigerbarBrevdata].json))
            }
        }
    fun hentBrevredigering(navident: String): List<String> =
        transaction {
            Brevredigering.select { Brevredigering.opprettetAvNavident eq navident }.map { row -> row[Brevredigering.brevkode] }
        }

    fun lagreBrevredigering(
        saksid: String,
        navident: String,
        brevkode: String,
        redigeresAvNavident: String?,
        redigerbarBrevdata: RedigerbarBrevdata
    ) {
        transaction {
            Brevredigering.insert {
                it[eksternId] = UUID.randomUUID().toString()
                it[this.saksid] = saksid
                it[this.opprettetAvNavident] = navident
                it[this.brevkode] = brevkode
                it[this.redigerbarBrevdata] = redigerbarBrevdata
                it[laastForRedigering] = false
                it[this.redigeresAvNavident] = redigeresAvNavident
                it[this.opprettet] = LocalDateTime.now().toString()
                it[this.sistredigert] = LocalDateTime.now().toString()

            }
        }
    }

    fun slettBrevredigering(
        uuid: String,
        saksid: String,
        navident: String,
        brevkode: String,
        redigerbarBrevdata: RedigerbarBrevdata
    ) {
        transaction {
            Brevredigering.deleteWhere {
                this.eksternId eq uuid
                this.saksid eq saksid
                this.opprettetAvNavident eq navident
                this.brevkode eq brevkode
                this.redigerbarBrevdata eq redigerbarBrevdata
            }
        }
    }
}

data class BrevredigeringDto(
    val saksid: String,
    val navident: String,
    val brevkode: String,
    val redigerbarBrevdata: RedigerbarBrevdataDto
)

data class RedigerbarBrevdataDto(val json: String)