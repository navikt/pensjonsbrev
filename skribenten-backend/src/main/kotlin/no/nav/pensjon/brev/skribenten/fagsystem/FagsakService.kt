package no.nav.pensjon.brev.skribenten.fagsystem

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.fagsystem.Behandlingsnummer.*
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.slf4j.LoggerFactory

class FagsakService(private val penClient: PenClient) {
    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun hentSak(saksId: SaksId): Pen.SakSelection? =
        penClient.hentSak(saksId)

    suspend fun hentAvtaleland(): List<Pen.Avtaleland> =
        penClient.hentAvtaleland()

    // TODO: Denne bør på sikt flyttes ut herifra og hentast frå f.eks. PEN heller
    private val behandlingsnummerMap = mapOf(
        "AFP" to B345,
        "AFP_PRIVAT" to B296,
        "ALDER" to B280,
        "BARNEP" to B359,
        "FAM_PL" to B150,
        "GAM_YRK" to B377,
        "GJENLEV" to B222,
        "KRIGSP" to B298,
        "OMSORG" to B300,
        "UFOREP" to B255,
    )

    fun finnBehandlingsnummer(sakstype: TemplateDescription.ISakstype): Behandlingsnummer? = behandlingsnummerMap[sakstype.kode] ?: null.also { logger.warn("Spurte om sakstype som ikke har behandlingsnummer: $sakstype") }

}

enum class Behandlingsnummer {
    B222,
    B255,
    B280,
    B359,
    B345,
    B296,
    B298,
    B150,
    B377,
    B300,
}