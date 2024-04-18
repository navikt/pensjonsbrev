package no.nav.pensjon.brev.skribenten.services

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


class BrevredigeringService {
    fun hentBrevredigering(navident: String, sakid: String, brevkode: String): List<BrevredigeringDto> =
        transaction {
            Brevredigering.select {
                Brevredigering.navident eq navident
                Brevredigering.sakid eq sakid
                Brevredigering.brevkode eq brevkode
            }.map { 
                BrevredigeringDto(
                it[Brevredigering.sakid], 
                it[Brevredigering.navident], 
                it[Brevredigering.brevkode],
                RedigerbarBrevdataDto(it[Brevredigering.redigerbarBrevdata].json))
            }
        }
    fun hentBrevredigering(navident: String): List<String> =
        transaction {
            Brevredigering.select { Brevredigering.navident eq navident }.map { row -> row[Brevredigering.brevkode] }
        }

    fun lagreBrevredigering(
        sakid: String,
        navident: String,
        brevkode: String,
        redigerbarBrevdata: RedigerbarBrevdata
    ) {
        transaction {
            Brevredigering.insert {
                it[uuid] = UUID.randomUUID().toString()
                it[this.sakid] = sakid
                it[this.navident] = navident
                it[this.brevkode] = brevkode
                it[this.redigerbarBrevdata] = redigerbarBrevdata
                it[laastForRedigering] = false

            }
        }
    }

    fun slettBrevredigering(
        uuid: String,
        sakid: String,
        navident: String,
        brevkode: String,
        redigerbarBrevdata: RedigerbarBrevdata
    ) {
        transaction {
            Brevredigering.deleteWhere {
                this.uuid eq uuid
                this.sakid eq sakid
                this.navident eq navident
                this.brevkode eq brevkode
                this.redigerbarBrevdata eq redigerbarBrevdata
            }
        }
    }
}

data class BrevredigeringDto(
    val sakid: String,
    val navident: String,
    val brevkode: String,
    val redigerbarBrevdata: RedigerbarBrevdataDto
)

data class RedigerbarBrevdataDto(val json: String)