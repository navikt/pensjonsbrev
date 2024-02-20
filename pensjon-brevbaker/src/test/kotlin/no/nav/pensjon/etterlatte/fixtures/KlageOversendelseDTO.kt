package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.klage.Formkrav
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseDTO
import no.nav.pensjon.etterlatte.maler.klage.VedtakKlagenGjelder
import no.nav.pensjon.etterlatte.maler.klage.VedtakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.SakType
import java.time.LocalDate

fun createKlageOversendelseDTO() = KlageOversendelseDTO(
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