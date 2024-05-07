package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.services.Brevredigering.redigerbarBrevdata
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class BrevredigeringService {
    fun hentBrevredigering(navident: String, saksid: String, brevkode: String): List<BrevredigeringDto> =
        transaction {
            Brevredigering.select {
                Brevredigering.navident eq navident
                Brevredigering.saksid eq saksid
                Brevredigering.brevkode eq brevkode
            }.map { 
                BrevredigeringDto(
                it[Brevredigering.saksid], 
                it[Brevredigering.navident], 
                it[Brevredigering.brevkode],
                RedigerbarBrevdataDto(it[redigerbarBrevdata].json))
            }
        }
    fun hentBrevredigering(navident: String): List<String> =
        transaction {
            Brevredigering.select { Brevredigering.navident eq navident }.map { row -> row[Brevredigering.brevkode] }
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
                it[uuid] = UUID.randomUUID().toString()
                it[this.saksid] = saksid
                it[this.navident] = navident
                it[this.brevkode] = brevkode
                it[this.redigerbarBrevdata] = redigerbarBrevdata
                it[laastForRedigering] = false
                it[this.redigeresAvNavident] = redigeresAvNavident

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
                this.uuid eq uuid
                this.saksid eq saksid
                this.navident eq navident
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