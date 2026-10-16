package no.nav.pensjon.brev.api.model.maler.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class Institusjonsopphold(
    val erAnvendt: Boolean,//vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt
    val type: JustertPeriodeCode?,//vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype
    val fasteUtgifter: Kroner?,//vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter
    val reduksjonsperioder: List<Reduksjonsperiode>//ut_forsorgeransvar_ingen_er_true
) {
    fun allePerioderHarForsorgeransvar() = reduksjonsperioder.all { it.forsorgeransvar }

    fun ingenPerioderHarForsorgeransvar() = reduksjonsperioder.none { it.forsorgeransvar }

    fun sistePeriodeHarForsorgeransvar() = reduksjonsperioder.maxBy {it.fomDato}.forsorgeransvar

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