package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen

fun Mottaker.toApi(): Api.OverstyrtMottaker = when (type) {
    MottakerType.SAMHANDLER -> Api.OverstyrtMottaker.Samhandler(tssId!!)
    MottakerType.NORSK_ADRESSE -> Api.OverstyrtMottaker.NorskAdresse(navn!!, postnummer!!, poststed!!, adresselinje1, adresselinje2, adresselinje3)
    MottakerType.UTENLANDSK_ADRESSE -> Api.OverstyrtMottaker.UtenlandskAdresse(
        navn!!,
        postnummer,
        poststed,
        adresselinje1!!,
        adresselinje2,
        adresselinje3,
        landkode!!
    )
}

fun Mottaker.toPen(): Pen.SendRedigerbartBrevRequest.Mottaker = when (type) {
    MottakerType.SAMHANDLER -> Pen.SendRedigerbartBrevRequest.Mottaker(type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.TSS_ID, tssId = tssId!!)
    MottakerType.NORSK_ADRESSE -> Pen.SendRedigerbartBrevRequest.Mottaker(
        type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.NORSK_ADRESSE,
        norskAdresse = Pen.SendRedigerbartBrevRequest.Mottaker.NorskAdresse(
            navn = navn!!,
            postnummer = postnummer!!,
            poststed = poststed!!,
            adresselinje1 = adresselinje1,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
        ),
    )

    MottakerType.UTENLANDSK_ADRESSE -> Pen.SendRedigerbartBrevRequest.Mottaker(
        type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.UTENLANDSK_ADRESSE,
        utenlandskAdresse = Pen.SendRedigerbartBrevRequest.Mottaker.UtenlandsAdresse(
            navn = navn!!,
            landkode = landkode!!,
            postnummer = postnummer,
            poststed = poststed,
            adresselinje1 = adresselinje1!!,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
        ),
    )
}