package no.nav.pensjon.brev.ufore.maler

import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.BrevbakerDSLInternal
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * Dokumenterer og verifiserer hvilke saksbehandlervalg (felter på SaksbehandlerValg, med datatype)
 * som er tilgjengelige for hvert redigerbare brev i ufoere-modulen. Testen gjør ingen rendring —
 * den sjekker kun strukturen på `saksbehandlerValg`-typen til hver mal, slik at endringer i
 * hvilke valg saksbehandler kan gjøre blir synlige i en PR-diff.
 */
class SaksbehandlervalgPerMalTest {

    @Test
    @OptIn(BrevbakerDSLInternal::class)
    fun `hvert redigerbart brev har forventede saksbehandlervalg`() {
        val faktiske = UfoereTemplates.hentRedigerbareMaler().associate { mal ->
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
            Redigerbar.UT_AVSLAG_ALDER to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I1 to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I2 to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_BEHANDLING to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_IFU_IKKE_VARIG to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_IFU_OKT_STILLING to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_INNTEKTSEVNE_30 to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
                "visUnntaksregelFremtidigInntekt: kotlin.Boolean",
                "visVurderingIEU: kotlin.Boolean",
                "visVurderingIFU: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_INNTEKTSEVNE_40 to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
                "visUnntaksregelFremtidigInntekt: kotlin.Boolean",
                "visVurderingIEU: kotlin.Boolean",
                "visVurderingIFU: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_INNTEKTSEVNE_50 to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
                "visUnntaksregelFremtidigInntekt: kotlin.Boolean",
                "visVurderingIEU: kotlin.Boolean",
                "visVurderingIFU: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_MANGLENDE_DOK to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_MEDLEMSKAP to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
                "visSupplerendeStonadUforeFlykninger: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_MEDLEMSKAP_UTLAND to listOf(
                "visBrukerIkkeOmfattesAvPersonkretsTrygdeforordning: kotlin.Boolean",
                "visInnvilgetPensjonEOSLand: kotlin.Boolean",
                "visSupplerendeStonadUforeFlykninger: kotlin.Boolean",
                "visTekstVedArtikkel57Avslag: kotlin.Boolean",
                "visVedtakFraAndreLand: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_ARB_TILTAK_I1 to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_ARB_TILTAK_I2 to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_OKT_GRAD_HENSIKTSMESSIG_BEHANDLING to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_OKT_GRAD_INNTEKTSEVNE to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
                "visUnntaksregelFremtidigInntekt: kotlin.Boolean",
                "visVurderingIEU: kotlin.Boolean",
                "visVurderingIFU: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_OKT_GRAD_MANGLENDE_DOK to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_OKT_GRAD_SYKDOM to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_SYKDOM to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_TESTMAL to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_UNG_UFOR_26 to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_UNG_UFOR_36 to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
                "visUforetidspunktEtter26: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_UNG_UFOR_VARIG to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
                "visForverrelseEtter26: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_YRKESSKADE_GODKJENT to listOf(
                "VisVurderingFraVilkarvedtak: kotlin.Boolean",
            ),
            Redigerbar.UT_AVSLAG_YRKESSKADE_IKKE_GODKJENT to emptyList(),
            Redigerbar.UT_FEILUTBETALING_VARSEL_BARN_FLYTTER to emptyList(),
            Redigerbar.UT_FEILUTBETALING_VARSEL_BARN_UTLAND to emptyList(),
            Redigerbar.UT_FEILUTBETALING_VARSEL_DODSBO to listOf(
                "kjentBobestyrer: kotlin.Boolean",
            ),
            Redigerbar.UT_FEILUTBETALING_VARSEL_INSTITUSJON to emptyList(),
            Redigerbar.UT_FEILUTBETALING_VARSEL_SIVILSTAND to emptyList(),
            Redigerbar.UT_FEILUTBETALING_VARSEL_SIVILSTAND_UU to emptyList(),
            Redigerbar.UT_FEILUTBETALING_VARSEL_SONING to emptyList(),
            Redigerbar.UT_INNH_OPPL_ANTATT_DOD to emptyList(),
            Redigerbar.UT_INNH_OPPL_BRUKER_LEGEERKLAERING to emptyList(),
            Redigerbar.UT_INNH_OPPL_EKTEFELLE_UTLAND to emptyList(),
            Redigerbar.UT_INNH_OPPL_FLERE_OPPL_FIRMAINNTEKT to emptyList(),
            Redigerbar.UT_INNH_OPPL_FLERE_OPPL_GENERELL to emptyList(),
            Redigerbar.UT_INNH_OPPL_LEGE_LEGEERKLAERING to emptyList(),
            Redigerbar.UT_INNH_OPPL_NAERINGSINNTEKTER to listOf(
                "ikkeMottattInntektsskjema: kotlin.Boolean",
            ),
            Redigerbar.UT_INNH_OPPL_NY_SIVILSTAND to emptyList(),
            Redigerbar.UT_INNH_OPPL_OPPGITT_SAMBOER to listOf(
                "ukjentSamboer: kotlin.Boolean",
            ),
            Redigerbar.UT_INNH_OPPL_SOKNAD_BARNETILLEGG to emptyList(),
            Redigerbar.UT_INNH_OPPL_SOKNAD_BARNETILLEGG_FOSTERFORELDER to emptyList(),
            Redigerbar.UT_INNH_OPPL_SOKNAD_BARNETILLEGG_UTLAND to emptyList(),
            Redigerbar.UT_INNH_OPPL_TRUKKET_KLAGE to emptyList(),
            Redigerbar.UT_INNH_OPPL_UTSATT_KLAGEFRIST to emptyList(),
            Redigerbar.UT_S_HVILENDE_RETT_INFO_4_AAR to emptyList(),
            Redigerbar.UT_S_HVILENDE_RETT_MIDL_OPPHOER to emptyList(),
            Redigerbar.UT_S_HVILENDE_RETT_OPPHOER to emptyList(),
            Redigerbar.UT_S_HVILENDE_RETT_VARSEL_OPPHOER to emptyList(),
            Redigerbar.UT_S_VARSEL_LAVERE_MINSTESATS_2026 to emptyList(),
            Redigerbar.UT_S_VARSEL_LAVERE_REDUKSJONSPROSENT to emptyList(),
            Redigerbar.UT_S_VARSEL_OKT_MINSTE_IFU to emptyList(),
            Redigerbar.UT_S_VARSEL_OKT_MINSTE_IFU_LAVERE_REDPROS to emptyList(),
            Redigerbar.UT_VARSEL_FEILUTBETALING to listOf(
                "rentetillegg: kotlin.Boolean",
            ),
            Redigerbar.UT_VEDTAK_FEILUTBETALING to emptyList(),
            Redigerbar.UT_VEDTAK_FEILUTBETALING_ETTERGITT_FORELDET to emptyList(),
            Redigerbar.UT_VEDTAK_FEILUTBETALING_INGEN_TILBAKEKREVING to emptyList(),
        )

        assertEquals(
            forventet.keys,
            faktiske.keys,
        ) { "Sjekk om det er nye eller fjernede redigerbare maler i ufoere-modulen: forventet=${forventet.keys}, faktisk=${faktiske.keys}" }
        assertEquals(forventet, faktiske)
    }
}
