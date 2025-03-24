package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.util.Locale

internal val norskBokmål: Locale = Locale.of("NB", "NO")
internal val landKoder: Set<String> = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2)
internal val land = landKoder.map {
    LandJson(it, Locale.of("", it).getDisplayCountry(norskBokmål))
}

internal data class LandJson(
    val kode: String,
    val navn: String
)

//TODO - test
fun Route.landRoute() {
    get("/land") {
        call.respond(land)
    }
}