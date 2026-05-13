package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Vedtak — melding om endring av avtalefestet pensjon (AFP) i offentlig sektor (gammel AFP).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_020`. Brevet er svært likt
 * innvilgelsesvarianten [InnvilgelseAvAfpOffentligSektorDto] (PE_AF_04_001), men
 * mangler vedlegget «Hvordan pensjonen er beregnet» og introduksjonen er en
 * endringsmelding i stedet for en innvilgelse.
 */
data class VedtakEndringAfpOffentligSektorDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakEndringAfpOffentligSektorDto.PesysData> {

    data class PesysData(
        // PE_Vedtaksdata_VirkningFOM
        val virkningFom: LocalDate,

        // PE_Vedtaksdata_BeregningsData_Beregning_VirkDatoFOM
        val beregningVirkDatoFom: LocalDate,

        // PE_Vedtaksdata_BeregningsData_Beregning_AFPpensjonsgrad
        val afpPensjonsgrad: Int,

        // PE_Vedtaksdata_BeregningsData_Beregning_Grunnbelop
        val grunnbeloep: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFPI
        val framtidigArligInntekt: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo1_SPT_Poengrekke_PoengarUtenOKTPI
        val tidligereArbeidsinntekt: Kroner,

        // PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder > 1
        val flerePerioder: Boolean,

        // PE_VedtaksData_Etterbetaling
        val etterbetaling: Boolean,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt grupperinger
        val sivilstand: AfpOffentligSektor.Sivilstand,

        val beregning: AfpOffentligSektor.Beregning,

        // Data til vedlegget «Opplysninger om beregningen» (PE_AF_opplysninger_om_beregningen_MR71).
        val opplysningerOmBeregningen: OpplysningerOmBeregningenAfpDto,

        // Data til vedlegget «Oversikt over pensjonens størrelse» (PE_AF_oversikt_over_pensjonen_MR71).
        // null når antallPerioder == 1 (vedlegget skal kun produseres ved flere perioder).
        val oversiktOverPensjonen: OversiktOverPensjonenAfpDto?,
    ) : FagsystemBrevdata

    // Sivilstandsgrupperinger og Beregning-detaljer ligger i [AfpOffentligSektor]
    // og deles med [InnvilgelseAvAfpOffentligSektorDto].
}
