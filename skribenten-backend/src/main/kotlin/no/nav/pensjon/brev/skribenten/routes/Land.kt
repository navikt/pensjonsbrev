package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.brev.Landkoder

fun Route.landRoute() {
    get("/land") {
        call.respond(Landkoder.landkoderMedNavn)
    }
    get("/landForP1") {
        call.respond((eoesLand + nordiskTrygdeavtaleland + trygdeavtaleMedStorbritanniaLand + sveits).distinct())
    }
}


private val eoesLand = Landkoder.landkoderMedNavn.filter {
    it.kode.landkode in setOf(
        // EU-land
        "AT", // Østerrike
        "BE", // Belgia
        "BG", // Bulgaria
        "CY", // Kypros
        "CZ", // Tsjekkia
        "DE", // Tyskland
        "DK", // Danmark
        "EE", // Estland
        "ES", // Spania
        "FI", // Finland
        "FR", // Frankrike
        "GR", // Hellas
        "HR", // Kroatia
        "HU", // Ungarn
        "IE", // Irland
        "IT", // Italia
        "LT", // Litauen
        "LU", // Luxembourg
        "LV", // Latvia
        "MT", // Malta
        "NL", // Nederland
        "PL", // Polen
        "PT", // Portugal
        "RO", // Romania
        "SE", // Sverige
        "SI", // Slovenia
        "SK", // Slovakia

        // EØS
        "IS", // Island
        "LI", // Liechtenstein
        "NO", // Norge
    )
}

private val nordiskTrygdeavtaleland = Landkoder.landkoderMedNavn.filter {
    it.kode.landkode in setOf(
        "DK", // Danmark
        "FI", // Finland
        "IS", // Island
        "SE", // Sverige

        "FO", // Færøyene
        "GL", // Grønland
        "AX", // Åland, del av Finland
    )
}

private val trygdeavtaleMedStorbritanniaLand = Landkoder.landkoderMedNavn.filter {
    it.kode.landkode in setOf(
        "IM", // Isle of Man
        "JE", // Jersey
        "GB", // Storbritannia
    )
}

private val sveits = Landkoder.landkoderMedNavn.filter {
    it.kode.landkode == "CH" // Sveits
}