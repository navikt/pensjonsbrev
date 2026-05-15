package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Vedtak — ingen endring (innenfor toleransebeløpet) — AFP etteroppgjør (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_100`. Brevet sendes når NAV har gjennomført
 * et AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) og forskjellen
 * mellom forventet og faktisk pensjonsgivende inntekt ikke er større enn det
 * fastsatte toleransebeløpet (15 000 kroner i 2024). Pensjonsberegningen skal
 * derfor ikke endres.
 *
 * Brevet inneholder en periodefordelingstekst som forklarer hvilke beløp som
 * ligger til grunn for opptjent inntekt før, under og etter AFP-uttak. Hvilken
 * av de fire variantene som vises bestemmes av [Periode] — en diskriminator
 * kalleren utleder fra `AFP_Uttaksdato`, `AFP_Opphorsdato` og 01.01 / 31.12 i
 * oppgjørsåret (se skill-step 7: Exstream-betingelsene avledes hos kalleren).
 *
 * Field comments carry the original `PE_…` source path so the mapping team kan
 * grep `pe_xml_mappinger(in).csv` for å finne tilsvarende PESYS-XML-node.
 */
data class VedtakAfpEtteroppgjoerToleransebeloepAutoDto(
    // PE_Vedtaksdata_Oppgjorsar
    val oppgjoersAar: Year,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
    // Samlet pensjonsgivende inntekt fra Skatteetaten for oppgjørsåret.
    val pgi: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
    // Inntekt opptjent før uttak av AFP. Brukes i [Periode.UTTAK_I_AARET] og
    // [Periode.UTTAK_OG_OPPHOER_I_AARET].
    val ifu: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IEO
    // Inntekt opptjent etter opphør av AFP. Brukes i [Periode.OPPHOER_I_AARET] og
    // [Periode.UTTAK_OG_OPPHOER_I_AARET].
    val ieo: Kroner,

    // PE_Vedtaksdata_APFEO_IIAP
    // Inntekt antatt opptjent i perioden med AFP.
    val iiap: Kroner,

    // PE_Vedtaksdata_AFPEO_FPIberegnet
    // Forventet pensjonsgivende inntekt brukt ved beregningen av pensjonen.
    val fpiberegnet: Kroner,

    // PE_Vedtaksdata_AFPEO_AFP_avvik
    // Forskjellen mellom forventet og faktisk pensjonsgivende inntekt. Skal
    // ligge innenfor toleransebeløpet — det er denne testen som gir grunnlaget
    // for «ingen endring» i dette brevet.
    val avvik: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_Uttaksdato
    // Vises i fritekst kun for [Periode.UTTAK_I_AARET]; alltid med i Dto for
    // enkel selektering.
    val uttaksdato: LocalDate,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_opphorsdato
    // Vises i fritekst kun for [Periode.OPPHOER_I_AARET]; null hvis AFP ikke
    // har opphørt i oppgjørsåret.
    val opphorsdato: LocalDate?,

    val periode: Periode,
) : AutobrevData {

    /**
     * Hvilken periodevariant av forklaringen som skal vises. Eksstream brukte
     * fire overlappende `showIf`-blokker over rådata for uttaksdato/opphorsdato
     * mot 01.01 og 31.12 i oppgjørsåret — her er logikken løftet ut av malen.
     */
    enum class Periode {
        // AFP_Uttaksdato < 01.01 AND (Opphorsdato >= 31.12 OR Opphorsdato tom)
        // Hadde AFP hele året; hele inntekten regnes som opptjent samtidig med AFP.
        HEL_AFP_HELE_AARET,

        // AFP_Uttaksdato >= 01.01 AND (Opphorsdato >= 31.12 OR Opphorsdato tom)
        // AFP startet i året, opphørte ikke; standardberegning fordeler inntekten
        // mellom periode før uttak og periode med AFP.
        UTTAK_I_AARET,

        // AFP_Opphorsdato < 31.12 AND Opphorsdato satt AND Uttaksdato < 01.01
        // AFP opphørte i året; standardberegning fordeler inntekten mellom
        // periode med AFP og periode etter opphør.
        OPPHOER_I_AARET,

        // AFP_Uttaksdato >= 01.01 AND Opphorsdato < 31.12 AND Opphorsdato satt
        // Både uttak og opphør i samme år; standardberegning fordeler inntekten
        // mellom periode før uttak, periode med AFP og periode etter opphør.
        UTTAK_OG_OPPHOER_I_AARET,
    }
}
