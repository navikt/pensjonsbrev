package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Varsel (fase 1) om foreløpig beregning av AFP-etteroppgjør (offentlig sektor /
 * Statens pensjonskasse) som viser at bruker kan ha fått for mye utbetalt AFP.
 *
 * Konvertert fra Exstream-malen `PE_AF_03_100` (Vedtak_afp_EO_fase1). Brevet er
 * et autobrev som sendes før det endelige etteroppgjørsvedtaket: bruker varsles
 * om den foreløpige beregningen og får fire ukers frist til å legge fram nye
 * inntektsopplysninger. Det endelige vedtaket (ev. tilbakekreving) kommer i et
 * eget brev — se [VedtakAfpEtteroppgjoerTilbakekrevingAuto] (`PE_AF_04_107`).
 *
 * Innholdet deler de aller fleste paragrafene med PE_AF_04-familien gjennom
 * frasene i `AfpEtteroppgjoerInnhold`, `AfpTilbakekrevingBody` og
 * `AfpEtteroppgjoerAvslutning`.
 */
data class VarselAfpEtteroppgjoerForeloepigAutoDto(
    val oppgjoersAar: Year,
    val formyebetalt: Kroner,
    val uttaksdato: LocalDate,
    val opphorsdato: LocalDate?,
    val pensjonsgivendeInntekt: Kroner,
    val inntektFoerUttak: Kroner,
    val inntektEtterOpphoer: Kroner,
    val inntektIAfpPerioden: Kroner,
    val forventetInntekt: Kroner,
    val fullAfp: Kroner,
    val fradragBeregnetArbeidsInntekt: Kroner,
    val korrigertAfp: Kroner,
    val tidligereArbeidsInntektBeregnet: Kroner,
    val utbetaltAfp: Kroner,
    val periode: Periode,
) : AutobrevData {

    /**
     * Periodevariant som styrer både tidsrommet i den foreløpige beregningen
     * («du kan ha fått … for mye») og fordelingen av inntekten før/etter uttak
     * og opphør av AFP. Exstream uttrykte dette som fire overlappende
     * `showIf`-blokker over rådata for uttaksdato/opphorsdato mot 01.01 og 31.12
     * i oppgjørsåret — her er logikken løftet ut av malen, identisk med
     * [VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode].
     */
    enum class Periode {
        // Uttaksdato < 01.01 AND (Opphorsdato >= 31.12 OR tom): AFP løp hele året.
        HEL_AFP_HELE_AARET,

        // Uttak i året, ingen opphør: inntekten fordeles før uttak og i AFP-perioden.
        UTTAK_I_AARET,

        // Både uttak og opphør i året: inntekten fordeles før uttak, i AFP-perioden og etter opphør.
        UTTAK_OG_OPPHOER_I_AARET,

        // Opphør i året: inntekten fordeles i AFP-perioden og etter opphør.
        OPPHOER_I_AARET,
    }
}
