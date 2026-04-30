package no.nav.pensjon.brev.skribenten.fagsystem

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
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
        "AFP" to Behandlingsnummer("B345"),
        "AFP_PRIVAT" to Behandlingsnummer("B296"),
        "ALDER" to Behandlingsnummer("B280"),
        "BARNEP" to Behandlingsnummer("B359"),
        "FAM_PL" to Behandlingsnummer("B150"),
        "GAM_YRK" to Behandlingsnummer("B377"),
        "GJENLEV" to Behandlingsnummer("B222"),
        "KRIGSP" to Behandlingsnummer("B298"),
        "OMSORG" to Behandlingsnummer("B300"),
        "UFOREP" to Behandlingsnummer("B255"),
    )

    fun finnBehandlingsnummer(sakstype: TemplateDescription.ISakstype): Behandlingsnummer? = behandlingsnummerMap[sakstype.kode] ?: null.also { logger.warn("Spurte om sakstype som ikke har behandlingsnummer: $sakstype") }

}

@JvmInline
value class Behandlingsnummer(val value: String)