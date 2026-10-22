package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class Institusjonsopphold(
    val erAnvendt: Boolean,//vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt
    val type: JustertPeriodeCode?,//vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype
    val fasteUtgifter: Kroner?,//vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter
    val reduksjonsperioder: List<Reduksjonsperiode>,//ut_forsorgeransvar_ingen_er_true
    val allePerioderHarForsorgeransvar: Boolean,// = reduksjonsperioder.all { it.forsorgeransvar }
    val ingenPerioderHarForsorgeransvar: Boolean// = reduksjonsperioder.none { it.forsorgeransvar }
) {

    data class Reduksjonsperiode(
        val forsorgeransvar: Boolean,
        val fomDato: LocalDate
    )

    enum class JustertPeriodeCode {
        EPS_INST,
        OKNING,
        REDUKSJON,
        REDUKSJON_FO,
        REDUKSJON_FRIPERIODE,
        REDUKSJON_HS
    }
}