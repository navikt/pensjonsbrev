package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_onsketvirkningsdato
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_virkningfom
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text

data class TBU1177_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1177EN, TBU1177, TBU1177NN]

		paragraph {
			text (
				bokmal { + "Du har fått innvilget uføretrygd fra " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kaller vi virkningstidspunktet. Du vil få sykepenger fram til <FRITEKST: dato for opphør>. I denne måneden får du utbetalt den delen av sykepengene som overstiger uføretrygden." },
				nynorsk { + "Du har fått innvilga uføretrygd frå " + pe.vedtaksdata_kravhode_onsketvirkningsdato().format() + ". Dette kallar vi verknadstidspunktet. Du får sjukepengar fram til <FRITEKST: Dato for opphør>. I denne månaden får du utbetalt den delen av sjukepengane som overstig uføretrygda." },
				english { + "You have been granted disability benefit from " + pe.vedtaksdata_virkningfom().format() + ". We call this the effective date. You will receive sickness benefit up to <FRITEKST: Dato for opphør>. In this month you will receive sickness benefit for the amount that exceeds the disability benefit." },
			)
		}
    }
}
        