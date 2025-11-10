package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*

data class TBU1134_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1134, TBU1134EN, TBU1134NN]

		paragraph {
			text (
				bokmal { + "Du har vært medlem av folketrygden fra " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().format() + " til " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidtom().format() + ". Vi har fått opplyst at du har vært medlem av den <FRITEKST: nasjonalitet> trygdeordningen fra " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral().format() + " til " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidtombilateral().format() + ". Uføretidspunktet ditt er satt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + ". Du har derfor vært medlem av folketrygden og den <FRITEKST: nasjonalitet> trygdeordningen sammenhengende i tre år eller mer fram til uføretidspunktet ditt. Fordi vi har lagt sammen perioder med medlemskap i folketrygden og i <FRITEKST: land>, får du unntak fra vilkåret om medlemskap i folketrygden." },
				nynorsk { + "Du har vore medlem av den norske folketrygda frå " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().format() + " til " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidtom().format() + ". Vi har fått opplyst at du har vore medlem av den <FRITEKST: Nasjonalitet> trygdeordninga frå " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral().format() + " til " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidtombilateral().format() + ". Uføretidspunktet ditt er sett til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt().format() + ". Du har derfor vore medlem av den norske folketrygda og den <FRITEKST: Nasjonalitet> trygdeordninga samanhengande i tre år eller meir fram til uføretidspunktet ditt. Fordi vi har lagt saman periodar med medlemstid i Noreg og i <FRITEKST: Land>, får du unntak frå vilkåret om medlemskap i folketrygda." },
				english { + "You have been a member of the Norwegian National Insurance Scheme from " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().format() + " to " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidtom().format() + ". We have been informed that you have been a member of the <FRITEKST: Nasjonalitet> social security scheme from " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral().format() + " to " + pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidtombilateral().format() + ". Your date of disability has been determined to be " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().format() + ". Therefore, you have been a member of the Norwegian National Insurance Scheme and the <FRITEKST: Nasjonalitet> social security scheme continuously for three years or more up to the time of your disability. As we have added together the periods of membership in Norway and <FRITEKST: Land>, you have been exempted from the provision regarding membership of the Norwegian National Insurance Scheme." },
			)
		}
    }
}
        