package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiode
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class OmstillingsstoenadInnvilgelseTest {

    @Test
    fun `lavEllerIngenInntekt brukes som default hvis omsRettUtenTidsbegrensning ikke er satt`() {
        val dto = OmstillingsstoenadInnvilgelseDTO(
            innhold = emptyList(),
            data = OmstillingsstoenadInnvilgelseData(
                beregning = lagOmstillingsstoenadBeregning(),
                innvilgetMindreEnnFireMndEtterDoedsfall = false,
                lavEllerIngenInntekt = true,
                harUtbetaling = false,
                etterbetaling = null,
                tidligereFamiliepleier = false,
                bosattUtland = false,
                erSluttbehandling = false,
                datoVedtakOmgjoering = null,
            )
        )

        assertEquals(true, dto.data.omsRettUtenTidsbegrensning)
    }

    @Test
    fun `omsRettUtenTidsbegrensning overstyrer lavEllerIngenInntekt`() {
        val dto = OmstillingsstoenadInnvilgelseDTO(
            innhold = emptyList(),
            data = OmstillingsstoenadInnvilgelseData(
                beregning = lagOmstillingsstoenadBeregning(),
                innvilgetMindreEnnFireMndEtterDoedsfall = false,
                lavEllerIngenInntekt = true,
                harUtbetaling = false,
                etterbetaling = null,
                omsRettUtenTidsbegrensning = false,
                tidligereFamiliepleier = false,
                bosattUtland = false,
                erSluttbehandling = false,
                datoVedtakOmgjoering = null
            )
        )

        assertEquals(false, dto.data.omsRettUtenTidsbegrensning)
    }

}


fun lagOmstillingsstoenadBeregning(): OmstillingsstoenadBeregning = OmstillingsstoenadBeregning(
    innhold = listOf(),
    virkningsdato = vilkaarligDato,
    beregningsperioder = listOf(),
    sisteBeregningsperiode = OmstillingsstoenadBeregningsperiode(
        datoFOM = LocalDate.of(2024, 2, 1),
        datoTOM = null,
        inntekt = Kroner(100000),
        oppgittInntekt = Kroner(600000),
        fratrekkInnAar = Kroner(100000),
        innvilgaMaaneder = 12,
        grunnbeloep = Kroner(118000),
        utbetaltBeloep = Kroner(5000),
        ytelseFoerAvkorting = Kroner(22000),
        restanse = Kroner(1000),
        trygdetid = 40,
        sanksjon = false,
        institusjon = false,
    ),
    sisteBeregningsperiodeNesteAar = null,
    trygdetid = Trygdetid(
        beregnetTrygdetidAar = 40,
        prorataBroek = null,
        beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
        beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
        mindreEnnFireFemtedelerAvOpptjeningstiden = true,
        navnAvdoed = "Elvis Presley",
        trygdetidsperioder = emptyList(),
    ),
    oppphoersdato = vilkaarligDato,
    opphoerNesteAar = false,
    erYrkesskade = false
)