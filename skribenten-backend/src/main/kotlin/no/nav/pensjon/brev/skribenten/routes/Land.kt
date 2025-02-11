package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

internal val norskBokmål: Locale = Locale("NB", "NO")
internal val landKoder: Set<String> = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2)
internal val land =
    landKoder.map {
        LandJson(it, Locale("", it).getDisplayCountry(norskBokmål))
    }

internal data class LandJson(
    val kode: String,
    val navn: String,
)

// TODO - test
fun Route.landRoute() {
    get("/land") {
        call.respond(land)
    }
}
