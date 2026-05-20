package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

data class VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDto(
    // PE_Vedtaksdata_Oppgjorsar
    val oppgjoersAar: Year,

    // PE_Vedtaksdata_AFPEO_forlitebetalt
    // For lite utbetalt AFP — totalbeløpet som etterbetales.
    val forlitebetalt: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
    val pgi: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
    // Inntekt opptjent før uttak av AFP. Vises i alle scenarier unntatt
    // [Scenario.KUN_IEO_OVERSTYRT].
    val ifu: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IEO
    // Inntekt opptjent etter opphør av AFP. Vises i scenariene
    // [Scenario.IFU_OG_IEO_OVERSTYRT] og [Scenario.KUN_IEO_OVERSTYRT].
    val ieo: Kroner,

    // PE_Vedtaksdata_APFEO_IIAP
    // Inntekt antatt opptjent i perioden med AFP.
    val iiap: Kroner,

    // PE_Vedtaksdata_AFPEO_AFP_avvik
    // Forskjellen mellom forventet og faktisk pensjonsgivende inntekt;
    // overstiger toleransebeløpet (15 000 kr). Beløpet er positivt og
    // omtales som "lavere" i teksten — bruker har hatt mindre inntekt
    // enn forventet, og skal derfor ha etterbetaling.
    val avvik: Kroner,

    // PE_Vedtaksdata_AFPEO_fullafp
    val fullafp: Kroner,

    // PE_Vedtaksdata_AFPEO_fradragberegnetai
    val fradragberegnetai: Kroner,

    // PE_Vedtaksdata_AFPEO_korrigertafp
    val korrigertafp: Kroner,

    // PE_Vedtaksdata_AFPEO_tpiberegnet
    val tpiberegnet: Kroner,

    // PE_Vedtaksdata_AFPEO_utbetaltafp
    val utbetaltafp: Kroner,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_Uttaksdato
    val uttaksdato: LocalDate,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_AFP_opphorsdato
    // Nullbart fordi Exstream-originalen sjekker mot DateValue("") for
    // pågående AFP. Vises bare i to av periode-variantene.
    val opphorsdato: LocalDate?,

    val scenario: Scenario,

    val periode: NyPensjonsberegningPeriode,
) : AutobrevData {

    /**
     * Hvilken inntektsforklaring som skal vises. Fem gjensidig utelukkende
     * varianter.
     */
    enum class Scenario {
        // IFUregistrert = "" AND IEOregistrert = "" AND uttaksdato >= 01.02
        INGEN_OVERSTYRING_UTTAK_I_AARET,

        // IFUregistrert != "" AND IEOregistrert = "" AND uttaksdato >= 01.02
        IFU_OVERSTYRT_UTTAK_I_AARET,

        // IFUregistrert != "" AND IEOregistrert = "" AND uttaksdato <= 01.01
        IFU_OVERSTYRT_HEL_AFP,

        // IFUregistrert != "" AND IEOregistrert != ""
        IFU_OG_IEO_OVERSTYRT,

        // IFUregistrert = "" AND IEOregistrert != ""
        KUN_IEO_OVERSTYRT,
    }

    /**
     * Periodevariant av «Ny pensjonsberegning …»-introen. Fire varianter
     * basert på om uttak skjedde før eller i året (vs. 01.02) og om opphør
     * skjedde i året eller etter (vs. 31.12 / tom dato).
     */
    enum class NyPensjonsberegningPeriode {
        // Uttak < 01.02 AND (opphor >= 31.12 OR opphor tom) — AFP løp hele året.
        HELE_AARET,

        // Uttak >= 01.02 AND (opphor >= 31.12 OR opphor tom) — uttak i året, AFP løper videre.
        UTTAK_I_AARET_LOEPENDE,

        // Uttak < 01.02 AND opphor < 31.12 — AFP fra årets start, opphor i året.
        UTTAK_FOER_AARET_OPPHOR_I_AARET,

        // Uttak >= 01.02 AND opphor < 31.12 — både uttak og opphor i året.
        UTTAK_OG_OPPHOR_I_AARET,
    }
}
