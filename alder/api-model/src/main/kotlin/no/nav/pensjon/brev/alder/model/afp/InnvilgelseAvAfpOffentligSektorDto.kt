package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.alder.model.vedlegg.HvordanPensjonenErBeregnetAfpOffentligDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Vedtak — innvilgelse av avtalefestet pensjon (AFP) i offentlig sektor (gammel AFP).
 *
 * Ported from the Exstream brevkode `PE_AF_04_001`. Den nye, privat-sektor varianten
 * (PE_AF_04_111 / 04_115) finnes i [InnvilgelseAvAfpDto] / [InnvilgelseAvAfpAutoDto].
 *
 * Feltkommentarene refererer til den originale Pesys-modellen (PE_…) slik at
 * mapping-teamet kan slå opp riktig XML-node i `pe_xml_mappinger(in).csv`.
 */
data class InnvilgelseAvAfpOffentligSektorDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, InnvilgelseAvAfpOffentligSektorDto.PesysData> {

    data class PesysData(
        // PE_Vedtaksdata_Kravhode_KravMottatdato
        // (rtv-brev brev Vedtaksdata Kravhode KravMottatdato)
        val kravMottattDato: LocalDate,

        // PE_Vedtaksdata_VirkningFOM
        // (rtv-brev brev Vedtaksdata VirkningFOM)
        val virkningFom: LocalDate,

        // PE_Vedtaksdata_BeregningsData_Beregning_VirkDatoFOM
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning VirkDatoFOM)
        val beregningVirkDatoFom: LocalDate,

        // PE_Vedtaksdata_BeregningsData_Beregning_AFPpensjonsgrad
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning AFPpensjonsgrad)
        val afpPensjonsgrad: Int,

        // PE_Vedtaksdata_BeregningsData_Beregning_Grunnbelop
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning Grunnbelop)
        val grunnbeloep: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFPI
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragBruker BrukerFPI)
        // Framtidig pensjonsgivende inntekt benyttet i beregningen (per år).
        val framtidigArligInntekt: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo1_SPT_Poengrekke_PoengarUtenOKTPI
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningNokkelinfo BeregningNokkelinfo1 SPT Poengrekke PoengarUtenOKTPI)
        // Tidligere arbeidsinntekt, beregnet snitt — vises som beløp i brevet.
        val tidligereArbeidsinntekt: Kroner,

        // PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder
        // (rtv-brev brev Vedtaksdata BeregningsData BeregningAntallPerioder)
        // Brevet henviser bare til vedlegg når antall perioder > 1; modellert som
        // scalar Boolean per convert-exstream-letter skill Step 3.
        val flerePerioder: Boolean,

        // PE_VedtaksData_Etterbetaling
        // (rtv-brev brev VedtaksData Etterbetaling)
        val etterbetaling: Boolean,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningSivilstandAnvendt)
        // Pesys-rådatabasen leverer en av ~15 sivilstand-strenger; brevet bruker
        // dem kun som 6 grupperinger for "Du må også melde fra hvis"-listen.
        val sivilstand: AfpOffentligSektor.Sivilstand,

        val beregning: AfpOffentligSektor.Beregning,

        // Data til vedlegget «Hvordan pensjonen er beregnet» (PE_AF_hvordan_pensjonen_beregnes).
        val hvordanPensjonenErBeregnet: HvordanPensjonenErBeregnetAfpOffentligDto,

        // Data til vedlegget «Opplysninger om beregningen» (PE_AF_opplysninger_om_beregningen_MR71).
        val opplysningerOmBeregningen: OpplysningerOmBeregningenAfpDto,

        // Data til vedlegget «Oversikt over pensjonens størrelse» (PE_AF_oversikt_over_pensjonen_MR71).
        // null når antallPerioder == 1 (vedlegget skal kun produseres ved flere perioder).
        val oversiktOverPensjonen: OversiktOverPensjonenAfpDto?,
    ) : FagsystemBrevdata

    // Sivilstandsgrupperinger og Beregning-detaljer er flyttet til [AfpOffentligSektor]
    // for deling med [VedtakEndringAfpOffentligSektorDto].
}
