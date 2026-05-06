package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Vedlegg "Oversikt over pensjonens størrelse" som henges på vedtak om gjenlevendepensjon
 * (nasjonal og utland).
 *
 * Konvertert fra Exstream-malen `PE_GP_Nasj_Utland_oversikt_over_pensjonen_pensjon_HORISONT`.
 *
 * Vedlegget kan ikke redigeres av saksbehandler. Exstream-kilden hadde flere
 * `<FRITEKST: …>`-markører for fri saksbehandlerforklaring per periodeårsak; disse er bevisst
 * utelatt – kun de tre periodeårsakene som hadde fast tekst beholdes
 * (se [EndringAarsak]). Bestiller må derfor selv filtrere bort årsaker som ikke kan
 * representeres uten saksbehandlerforklaring før de leveres i [BeregningPeriode.aarsaker]
 * eller [Beregning.aarsaker].
 *
 * Hver felt-kommentar peker tilbake til opphavet i PESYS XML
 * (`Name` / `rtv-brev brev …`-kolonnene i
 * `pensjonsbrev-utils/exstreamConverter/src/main/resources/pe_xml_mappinger(in).csv`).
 */
@Suppress("unused")
data class OversiktOverPensjonensStoerrelseGjenlevendepensjonDto(
    val pesysData: PesysData,
) : VedleggData {

    /**
     * Periodeårsaker fra Exstream `PE_Vedtaksdata_BeregningsData_BeregningPeriode_PeriodeArsakListe_PeriodeArsakKode`
     * (og tilsvarende `Beregning_PeriodeArsakListe_PeriodeArsakKode`) som har en fast tekst i
     * Exstream-malen.
     *
     * Følgende koder fra Exstream-kilden hadde kun `<FRITEKST: …>`-tekster og er **utelatt**
     * (kan ikke gjengis uten saksbehandlerforklaring i et ikke-redigerbart vedlegg):
     * `endr_vedtakhistorikk`, `endr_regel`, `endr_personinfo`, `endr_inntekt`,
     * `endr_inst_justering`, `endr_aldersovergang`.
     */
    enum class EndringAarsak {
        /** Exstream-koden `endr_inst_fasteutgif` – "faste utgifter ved institusjonsopphold er endret". */
        FASTE_UTGIFTER_INSTITUSJONSOPPHOLD,

        /** Exstream-koden `endr_uttaksgrad` – "uttaksgraden er endret". */
        UTTAKSGRAD,

        /** Exstream-koden `endr_opptjening` – "opptjeningsgrunnlaget er endret". */
        OPPTJENING,
    }

    data class PesysData(
        // PE_Vedtaksdata_VirkningFom
        // (rtv-brev brev Vedtaksdata VirkningFom)
        val virkningFom: LocalDate,
        // PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder > 1 ⇔ beregningPerioder.size > 1
        // (rtv-brev brev Vedtaksdata BeregningsData BeregningAntallPerioder)
        val beregningPerioder: List<BeregningPeriode>,
        val beregning: Beregning,
    )

    /**
     * En periodisert linje i oversikten – tilsvarer Exstream-løkka
     * `PE_Vedtaksdata_BeregningsData_BeregningPeriode(SYS_TableRow)`.
     */
    data class BeregningPeriode(
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_VirkDatoFom
        // (rtv-brev brev Vedtaksdata BeregningsData BeregningPeriode VirkDatoFom)
        val virkDatoFom: LocalDate,
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_VirkDatoTom
        // (rtv-brev brev Vedtaksdata BeregningsData BeregningPeriode VirkDatoTom)
        val virkDatoTom: LocalDate,
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_GrunnBelop
        // (rtv-brev brev Vedtaksdata BeregningsData BeregningPeriode GrunnBelop)
        val grunnbeloep: Kroner,
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_BeregningsSammendragBruker_BrukerFpi
        // (rtv-brev brev Vedtaksdata BeregningsData BeregningPeriode BeregningsSammendragBruker BrukerFpi)
        val forventetAarligInntekt: Kroner,
        // (filtrert) PE_Vedtaksdata_BeregningsData_BeregningPeriode_PeriodeArsakListe_PeriodeArsakKode
        // (rtv-brev brev Vedtaksdata BeregningsData BeregningPeriode PeriodeArsakListe PeriodeArsakKode)
        val aarsaker: List<EndringAarsak>,
        val ytelser: BeregningYtelser,
    )

    /** Hovedberegningen – Exstream `PE_Vedtaksdata_BeregningsData_Beregning`. */
    data class Beregning(
        // PE_Vedtaksdata_BeregningsData_Beregning_VirkDatoFom
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning VirkDatoFom)
        val virkDatoFom: LocalDate,
        // PE_Vedtaksdata_BeregningsData_Beregning_GrunnBelop
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning GrunnBelop)
        val grunnbeloep: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFpi
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragBruker BrukerFpi)
        val forventetAarligInntekt: Kroner,
        // (filtrert) PE_Vedtaksdata_BeregningsData_Beregning_PeriodeArsakListe_PeriodeArsakKode
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning PeriodeArsakListe PeriodeArsakKode)
        val aarsaker: List<EndringAarsak>,
        val ytelser: BeregningYtelser,
    )

    /**
     * Felles ytelsesoppdeling for både [Beregning] og [BeregningPeriode].
     * `brutto`/`netto` på topp-nivå er totalsum (Exstream `…_Brutto` / `…_Netto`); dersom
     * `brutto != netto` rendres en tabell med begge kolonnene, ellers kun nettokolonnen.
     */
    data class BeregningYtelser(
        // PE_Vedtaksdata_BeregningsData_Beregning(Periode)_Brutto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning(Periode) Brutto)
        val brutto: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning(Periode)_Netto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning(Periode) Netto)
        val netto: Kroner,
        val grunnpensjon: Komponent,
        val tilleggspensjon: KomponentValgfri,
        val saertillegg: KomponentValgfri,
        val fasteUtgifter: KomponentValgfri,
        val familietillegg: KomponentValgfri,
    )

    /** Alltid innvilget komponent (grunnpensjon). */
    data class Komponent(
        // PE_Vedtaksdata_BeregningsData_Beregning(Periode)_BeregningYtelseKomp_<Komponent>_<…>brutto
        val brutto: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning(Periode)_BeregningYtelseKomp_<Komponent>_<…>netto
        val netto: Kroner,
    )

    /**
     * Komponent som kun rendres når `innvilget = true`
     * (tilleggspensjon, særtillegg, faste utgifter, familietillegg).
     */
    data class KomponentValgfri(
        // PE_Vedtaksdata_BeregningsData_Beregning(Periode)_BeregningYtelseKomp_<Komponent>_<…>innvilget
        val innvilget: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning(Periode)_BeregningYtelseKomp_<Komponent>_<…>brutto
        val brutto: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning(Periode)_BeregningYtelseKomp_<Komponent>_<…>netto
        val netto: Kroner,
    )
}

