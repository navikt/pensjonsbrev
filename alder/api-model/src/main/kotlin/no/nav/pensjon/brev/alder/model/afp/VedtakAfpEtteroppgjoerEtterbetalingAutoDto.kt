package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Vedtak — AFP etteroppgjør med etterbetaling, fase 1 / varsel (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_101`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når avviket
 * mellom forventet og faktisk pensjonsgivende inntekt overstiger
 * toleransebeløpet og resulterer i **for lite utbetalt** AFP. Brevet
 * inviterer bruker til å sende inn dokumentasjon innen fire uker dersom
 * inntektsgrunnlaget er feil. Hvis ingen dokumentasjon kommer innen
 * fristen, blir etterbetalingen gjennomført automatisk; hvis ny
 * dokumentasjon mottas, sendes [VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDto]
 * (PE_AF_04_105) etter behandling.
 *
 * Motsatt finansiell retning av [VedtakAfpEtteroppgjoerTilbakekrevingAutoDto]
 * (PE_AF_04_107 — fase 1 for-mye-betalt).
 *
 * Hvilken periodevariant av forklaringen om inntektsfordeling som vises
 * bestemmes av [Periode] — samme inndeling som
 * [VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode]. Originalen brukte
 * fire overlappende `showIf`-blokker over rådata for uttaksdato /
 * opphorsdato; her er logikken løftet ut av malen til en diskriminator
 * levert av kalleren (jf. skill-step 7).
 */
data class VedtakAfpEtteroppgjoerEtterbetalingAutoDto(
    // PE_Vedtaksdata_Oppgjorsar
    val oppgjoersAar: Year,

    // PE_Vedtaksdata_AFPEO_forlitebetalt
    // For lite utbetalt AFP — totalbeløpet som etterbetales.
    val forlitebetalt: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
    // Samlet pensjonsgivende inntekt fra Skatteetaten for oppgjørsåret.
    val pgi: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
    // Inntekt opptjent før uttak av AFP. Brukes i [Periode.UTTAK_I_AARET]
    // og [Periode.UTTAK_OG_OPPHOER_I_AARET].
    val ifu: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IEO
    // Inntekt opptjent etter opphør av AFP. Brukes i
    // [Periode.OPPHOER_I_AARET] og [Periode.UTTAK_OG_OPPHOER_I_AARET].
    val ieo: Kroner,

    // PE_Vedtaksdata_APFEO_IIAP
    // Inntekt antatt opptjent i perioden med AFP.
    val iiap: Kroner,

    // PE_Vedtaksdata_AFPEO_fpiberegnet
    // Forventet pensjonsgivende inntekt som ble lagt til grunn ved
    // utbetalingen av pensjon — vises i toleransebeløp-paragrafen.
    val fpiberegnet: Kroner,

    // PE_Vedtaksdata_AFPEO_fullafp
    val fullafp: Kroner,

    // PE_Vedtaksdata_AFPEO_fradragberegnetai
    val fradragberegnetai: Kroner,

    // PE_Vedtaksdata_AFPEO_korrigertafp
    val korrigertafp: Kroner,

    // PE_Vedtaksdata_AFPEO_tpiberegnet
    // Tidligere arbeidsinntekt som ble lagt til grunn ved utbetaling av
    // pensjon; nevneren i formelen for inntektsfradraget.
    val tpiberegnet: Kroner,

    // PE_Vedtaksdata_AFPEO_utbetaltafp
    val utbetaltafp: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_Uttaksdato
    val uttaksdato: LocalDate,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_opphorsdato
    // Nullbart fordi Exstream-originalen sjekker mot DateValue("") for
    // pågående AFP. Brukes i [Periode.OPPHOER_I_AARET] og
    // [Periode.UTTAK_OG_OPPHOER_I_AARET].
    val opphorsdato: LocalDate?,

    val periode: Periode,
) : AutobrevData {

    /**
     * Periodevariant av forklaringen. Samme inndeling som
     * [VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode] (PE_AF_04_107).
     */
    enum class Periode {
        // Uttak < 01.02 AND (Opphor >= 31.12 OR Opphor tom) — AFP løp hele året.
        HEL_AFP_HELE_AARET,

        // Uttak >= 01.02 AND (Opphor >= 31.12 OR Opphor tom) — uttak i året, AFP løper videre.
        UTTAK_I_AARET,

        // Uttak < 01.02 AND Opphor < 31.12 — AFP fra årets start, opphor i året.
        OPPHOER_I_AARET,

        // Uttak >= 01.02 AND Opphor < 31.12 — både uttak og opphør i året.
        UTTAK_OG_OPPHOER_I_AARET,
    }
}
