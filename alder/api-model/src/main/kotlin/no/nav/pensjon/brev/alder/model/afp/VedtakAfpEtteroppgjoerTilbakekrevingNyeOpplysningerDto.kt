package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

data class VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto.PesysData> {

    /**
     * Seks gjensidig utelukkende scenarier for forklaringen om hvilke
     * nye inntektsopplysninger som er lagt fram. Diskriminert ut fra
     * `IFUregistrert_STRING` (saksbehandler har overstyrt IFU),
     * `IEOregistrert_STRING` (saksbehandler har overstyrt IEO), og
     * `AFP_Uttaksdato` mot starten av oppgjørsåret.
     */
    enum class Scenario {
        // IFU == "" AND IEO == "" AND AFP_Uttaksdato <= 01.01
        // AFP løp hele året, ingen inntektsoverstyring — unik for 104.
        INGEN_OVERSTYRING_HEL_AFP,

        // IFU == "" AND IEO == "" AND AFP_Uttaksdato >= 01.02
        // AFP startet i året, ingen inntektsoverstyring.
        INGEN_OVERSTYRING_UTTAK_I_AARET,

        // IFU != "" AND IEO == "" AND AFP_Uttaksdato >= 01.02
        // Bare IFU oppjustert, uttak skjedde i etteroppgjørsåret.
        IFU_OVERSTYRT_UTTAK_I_AARET,

        // IFU != "" AND IEO == "" AND AFP_Uttaksdato <= 01.01
        // Bare IFU oppjustert, uttak skjedde før etteroppgjørsåret.
        IFU_OVERSTYRT_HEL_AFP,

        // IFU != "" AND IEO != ""
        // Både IFU og IEO oppjustert.
        IFU_OG_IEO_OVERSTYRT,

        // IFU == "" AND IEO != ""
        // Bare IEO oppjustert — inntekten kom etter at AFP tok slutt.
        KUN_IEO_OVERSTYRT,
    }

    data class PesysData(
        // PE_Vedtaksdata_Oppgjorsar
        val oppgjoersAar: Year,

        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
        // Samlet pensjonsgivende inntekt fra Skatteetaten for oppgjørsåret.
        val pgi: Kroner,

        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
        // Inntekt opptjent før uttak av AFP.
        val ifu: Kroner,

        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IEO
        // Inntekt opptjent etter opphør av AFP.
        val ieo: Kroner,

        // PE_Vedtaksdata_APFEO_IIAP
        // Faktisk arbeidsinntekt i perioden med AFP (= PGI − IFU − IEO).
        val iiap: Kroner,

        // PE_Vedtaksdata_AFPEO_AFP_avvik
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

        // PE_Vedtaksdata_AFPEO_formyebetalt
        // For mye utbetalt AFP — beløpet før fradrag for innbetalt skatt.
        val formyebetalt: Kroner,

        val scenario: Scenario,
    ) : FagsystemBrevdata
}
