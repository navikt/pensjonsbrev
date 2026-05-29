package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.alder.model.vedlegg.HvordanPensjonenErBeregnetAfpOffentligDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import java.time.LocalDate

data class InnvilgelseAvAfpOffentligSektorDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, InnvilgelseAvAfpOffentligSektorDto.PesysData> {

    data class PesysData(
        val kravMottattDato: LocalDate,
        val virkningFom: LocalDate,
        val beregningVirkDatoFom: LocalDate,
        val afpPensjonsgrad: Percent,
        val grunnbeloep: Kroner,
        val framtidigArligInntekt: Kroner,
        val tidligereArbeidsinntekt: Kroner,
        val flerePerioder: Boolean,
        val etterbetaling: Boolean,
        val sivilstand: AfpOffentligSektor.Sivilstand,
        val beregning: AfpOffentligSektor.Beregning,
        val hvordanPensjonenErBeregnet: HvordanPensjonenErBeregnetAfpOffentligDto,
        val opplysningerOmBeregningen: OpplysningerOmBeregningenAfpDto,
        val oversiktOverPensjonen: OversiktOverPensjonenAfpDto?,
        val toleranseBeloep: Kroner,
    ) : FagsystemBrevdata
}
