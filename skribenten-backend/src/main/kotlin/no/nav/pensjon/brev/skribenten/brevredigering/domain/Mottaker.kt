package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.db.MottakerTable
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass

enum class MottakerType { SAMHANDLER, NORSK_ADRESSE, UTENLANDSK_ADRESSE }

class Mottaker(brevredigeringId: EntityID<BrevId>) : Entity<BrevId>(brevredigeringId) {
    var type by MottakerTable.type
    var tssId by MottakerTable.tssId
    var navn by MottakerTable.navn
    var postnummer by MottakerTable.postnummer
    var poststed by MottakerTable.poststed
    var adresselinje1 by MottakerTable.adresselinje1
    var adresselinje2 by MottakerTable.adresselinje2
    var adresselinje3 by MottakerTable.adresselinje3
    var manueltAdressertTil by MottakerTable.manueltAdressertTil
    var landkode by MottakerTable.landkode

    var navnKryptert by MottakerTable.navnKryptert
    var postnummerKryptert by MottakerTable.postnummerKryptert
    var poststedKryptert by MottakerTable.poststedKryptert
    var adresselinje1Kryptert by MottakerTable.adresselinje1Kryptert
    var adresselinje2Kryptert by MottakerTable.adresselinje2Kryptert
    var adresselinje3Kryptert by MottakerTable.adresselinje3Kryptert
    var manueltAdressertTilKryptert by MottakerTable.manueltAdressertTilKryptert
    var landkodeKryptert by MottakerTable.landkodeKryptert

    companion object : EntityClass<BrevId, Mottaker>(MottakerTable) {

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

        navnKryptert = mottaker.navn
        postnummerKryptert = mottaker.postnummer
        poststedKryptert = mottaker.poststed
        adresselinje1Kryptert = mottaker.adresselinje1
        adresselinje2Kryptert = mottaker.adresselinje2
        adresselinje3Kryptert = mottaker.adresselinje3
        landkodeKryptert = mottaker.landkode
        manueltAdressertTilKryptert = mottaker.manueltAdressertTil

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