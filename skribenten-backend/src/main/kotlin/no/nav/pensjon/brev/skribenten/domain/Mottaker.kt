package no.nav.pensjon.brev.skribenten.domain

import no.nav.brev.Landkode
import no.nav.pensjon.brev.skribenten.db.MottakerTable
import no.nav.pensjon.brev.skribenten.db.wrap
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NorskPostnummer
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

enum class MottakerType { SAMHANDLER, NORSK_ADRESSE, UTENLANDSK_ADRESSE }

class Mottaker(brevredigeringId: EntityID<Long>) : LongEntity(brevredigeringId) {
    var type by MottakerTable.type
    var tssId by MottakerTable.tssId
    var navn by MottakerTable.navn
    var postnummer by MottakerTable.postnummer.wrap(::NorskPostnummer, NorskPostnummer::value)
    var poststed by MottakerTable.poststed
    var adresselinje1 by MottakerTable.adresselinje1
    var adresselinje2 by MottakerTable.adresselinje2
    var adresselinje3 by MottakerTable.adresselinje3
    var manueltAdressertTil by MottakerTable.manueltAdressertTil
    var landkode by MottakerTable.landkode.wrap(::Landkode, Landkode::landkode)

    companion object : LongEntityClass<Mottaker>(MottakerTable) {
        fun opprettMottaker(brevredigering: Brevredigering, mottaker: Dto.Mottaker): Mottaker {
            return new(brevredigering.id.value) {
                oppdater(mottaker)
            }
        }
    }

    fun oppdater(mottaker: Dto.Mottaker): Mottaker {
        type = mottaker.type
        tssId = mottaker.tssId
        navn = mottaker.navn
        postnummer = mottaker.postnummer
        poststed = mottaker.poststed
        adresselinje1 = mottaker.adresselinje1
        adresselinje2 = mottaker.adresselinje2
        adresselinje3 = mottaker.adresselinje3
        landkode = mottaker.landkode
        manueltAdressertTil = mottaker.manueltAdressertTil

        return this
    }

    fun toDto(): Dto.Mottaker =
        when (type) {
            MottakerType.SAMHANDLER -> Dto.Mottaker.samhandler(tssId!!)
            MottakerType.NORSK_ADRESSE -> Dto.Mottaker.norskAdresse(
                navn = navn!!,
                postnummer = postnummer!!,
                poststed = poststed!!,
                adresselinje1 = adresselinje1,
                adresselinje2 = adresselinje2,
                adresselinje3 = adresselinje3,
                manueltAdressertTil = manueltAdressertTil,
            )

            MottakerType.UTENLANDSK_ADRESSE -> Dto.Mottaker.utenlandskAdresse(
                navn = navn!!,
                adresselinje1 = adresselinje1!!,
                adresselinje2 = adresselinje2,
                adresselinje3 = adresselinje3,
                landkode = landkode!!,
                manueltAdressertTil = manueltAdressertTil,
            )
        }
}