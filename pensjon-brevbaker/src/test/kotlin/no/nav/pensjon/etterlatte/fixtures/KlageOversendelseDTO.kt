package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.klage.Formkrav
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBlankettDTO
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTO
import no.nav.pensjon.etterlatte.maler.klage.VedtakKlagenGjelder
import no.nav.pensjon.etterlatte.maler.klage.VedtakType
import java.time.LocalDate

fun createKlageOversendelseBlankettDTO() = KlageOversendelseBlankettDTO(
    innhold = emptyList(),
    formkrav = Formkrav(
        vedtaketKlagenGjelder = VedtakKlagenGjelder(
            datoAttestert = LocalDate.now().minusWeeks(3),
            vedtakType = VedtakType.INNVILGELSE
        ),
        erKlagenSignert = false,
        gjelderKlagenNoeKonkretIVedtaket = false,
        erKlagerPartISaken = false,
        erKlagenFramsattInnenFrist = true,
        erFormkraveneOppfylt = true,
        begrunnelse = """
            Dette er en oversendelse med litt begrunnelse
            
            Legg merke til linjeskiftene. Helt utrolig.
        """.trimIndent()
    ),
    hjemmel = "§ 18-4", internKommentar = "Wow en intern kommentar", ovesendelseTekst = "Noe ekstra her",
    klager = "12312312312",
    klageDato = LocalDate.now().minusDays(3),
    sakType = SakType.BARNEPENSJON

    )

fun createKlageOversendelseBrukerDTO() = KlageOversendelseBrukerDTO(
    sakType = SakType.OMSTILLINGSSTOENAD,
    klageDato = LocalDate.now().minusWeeks(3),
    vedtakDato = LocalDate.now().minusMonths(2),
    innstillingTekst = """
        En innstillingstekst
        
        Med flere linjer
        
        og noe annet
        """.trimIndent(),
    under18Aar = false,
    harVerge = false,
    bosattIUtlandet = false
)