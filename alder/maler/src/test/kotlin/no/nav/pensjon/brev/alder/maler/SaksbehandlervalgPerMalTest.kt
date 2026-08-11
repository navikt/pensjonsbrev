package no.nav.pensjon.brev.alder.maler

import no.nav.pensjon.brev.alder.model.Aldersbrevkoder.Redigerbar
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.BrevbakerDSLInternal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * Dokumenterer og verifiserer hvilke saksbehandlervalg (felter på SaksbehandlerValg, med datatype)
 * som er tilgjengelige for hvert redigerbare brev i alder-modulen. Testen gjør ingen rendring —
 * den sjekker kun strukturen på `saksbehandlerValg`-typen til hver mal, slik at endringer i
 * hvilke valg saksbehandler kan gjøre blir synlige i en PR-diff.
 */
class SaksbehandlervalgPerMalTest {

    @Test
    @OptIn(BrevbakerDSLInternal::class)
    fun `hvert redigerbart brev har forventede saksbehandlervalg`() {
        val faktiske = AlderTemplates.hentRedigerbareMaler().associate { mal ->
            val saksbehandlerValgType = mal.template.letterDataType.members
                .single { it.name == "saksbehandlerValg" }
                .returnType.classifier as KClass<*>

            // Maler migrert til saksbehandlervalg-DSL-en deklarerer valgene sine i template-body
            // (`saksbehandlervalg("id", "...")`) i stedet for som felter på en egen SaksbehandlerValg-type,
            // og har `saksbehandlerValg: SaksbehandlervalgIDSL` på Dto-en. De registrerte valgene finnes da i
            // `mal.template.saksbehandlervalg`. Ikke-migrerte maler har fremdeles en typet SaksbehandlerValg-klasse,
            // som vi da faller tilbake til å reflektere over.
            val valg = if (saksbehandlerValgType == SaksbehandlervalgIDSL::class) {
                mal.template.saksbehandlervalg.orEmpty().entries.map { (id, verdi) -> "$id: ${verdi.typename}" }
            } else {
                saksbehandlerValgType.memberProperties.map { "${it.name}: ${it.returnType}" }
            }

            (mal.kode as Redigerbar) to valg.sorted()
        }

        val forventet = mapOf(
            Redigerbar.INFO_BEKREFTELSE_UTSENDING_KRAV_TIL_UTLANDET to emptyList(),
            Redigerbar.INFO_BRUKER_AFP_PRIVAT_SOKER_UFORETRYGD to listOf("harSoktUforeTrygd: kotlin.Boolean"),
            Redigerbar.INFO_BRUKER_UFORETRYGD_SOKER_AFP_PRIVAT to listOf("brukerHarSoktAfpPrivat: kotlin.Boolean"),
            Redigerbar.PE_AFP_AVSLAG to emptyList(),
            Redigerbar.PE_AFP_AVSLAG_GAMMEL to emptyList(),
            Redigerbar.PE_AFP_ETTEROPPGJOER_ETTERBETALING to emptyList(),
            Redigerbar.PE_AFP_ETTEROPPGJOER_ETTERBETALING_ETTER_SVAR to emptyList(),
            Redigerbar.PE_AFP_ETTEROPPGJOER_INGEN_ENDRING_ANDRE_AVVIK to emptyList(),
            Redigerbar.PE_AFP_ETTEROPPGJOER_INGEN_ENDR_ETTER_SVAR to emptyList(),
            Redigerbar.PE_AFP_ETTEROPPGJOER_INGEN_ENDR_AVVIK_ETTER_SVAR to emptyList(),
            Redigerbar.PE_AFP_ETTEROPPGJOER_TILBAKEKREV_NYE_OPPL to emptyList(),
            Redigerbar.PE_AFP_ETTEROPPGJOER_INGEN_ENDRING to emptyList(),
            Redigerbar.PE_AFP_ETTEROPPGJOER_VARSEL_FORELOEPIG to emptyList(),
            Redigerbar.PE_AFP_INNVILGELSE to emptyList(),
            Redigerbar.PE_AFP_PRIVAT_ENDRING to emptyList(),
            Redigerbar.PE_AF_INNVILGELSE_OFFENTLIG to emptyList(),
            Redigerbar.PE_AF_VEDTAK_ENDRING_OFFENTLIG to emptyList(),
            Redigerbar.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER to emptyList(),
            Redigerbar.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_AP2016 to listOf("visInfoOmUttakFoer67: kotlin.Boolean?"),
            Redigerbar.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_ETT_AAR to emptyList(),
            Redigerbar.PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER to listOf("visInfoOmUttakFoer67: Boolean"),
            Redigerbar.PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER_AP2016 to listOf("visInfoOmUttakFoer67: kotlin.Boolean?"),
            Redigerbar.PE_AP_ENDRING_AV_ALDERSPENSJON_GARANTITILLEGG to emptyList(),
            Redigerbar.PE_AP_ENDRING_AV_ALDERSPENSJON_SAERSKILT_SATS to listOf(
                "aarligKontrollEPS: kotlin.Boolean",
                "eps: no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSatsDto.SaksbehandlerValg.EPS?",
                "etterbetaling: kotlin.Boolean?",
                "feilutbetaling: kotlin.Boolean",
            ),
            Redigerbar.PE_AP_ENDRING_AV_ALDERSPENSJON_SIVILSTAND to listOf(
                "etterbetaling: kotlin.Boolean?",
                "feilutbetaling: kotlin.Boolean?",
                "sivilstandsendringsaarsak: no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandDto.SaksbehandlerValg.Sivilstandsendringsaarsak?",
            ),
            Redigerbar.PE_AP_OMREGNING_ALDER_UFORE_2016 to emptyList(),
            Redigerbar.PE_AP_STANS_FLYTTING_MELLOM_LAND to listOf("feilutbetaling: kotlin.Boolean"),
        )

        assertEquals(
            forventet.keys,
            faktiske.keys,
        ) { "Sjekk om det er nye eller fjernede redigerbare maler i alder-modulen: forventet=${forventet.keys}, faktisk=${faktiske.keys}" }
        assertEquals(forventet, faktiske)
    }
}
