package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.aars_trygdetid
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class TBU1130_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1130EN, TBU1130, TBU1130NN]

		paragraph {
			text (
				bokmal { + "For å ha rett til uføretrygd må du oppfylle disse vilkårene:" },
				nynorsk { + "For å ha rett til uføretrygd må du oppfylle desse vilkåra:" },
				english { + "To be eligible for disability benefit, you must meet the following requirements:" },
			)
			text (
				bokmal { + "Du må være mellom 18 og 67 år." },
				nynorsk { + "Du må vere mellom 18 og 67 år." },
				english { + "You must be between 18 and 67 years old." },
			)
			text (
				bokmal { + "Du må ha vært medlem av folketrygden i de siste " + pe.aars_trygdetid() + " årene fram til uføretidspunktet, eller oppfylle en av unntaksreglene." },
				nynorsk { + "Du må ha vore medlem av folketrygda i dei siste " + pe.aars_trygdetid() + " åra fram til uføretidspunktet, eller oppfylle ein av unntaksreglane." },
				english { + "You must have been a member of the Norwegian National Insurance Scheme for the last " + pe.aars_trygdetid() + " years up to the date of disability, or meet one of the exemption criteria." },
			)
			text (
				bokmal { + "Inntektsevnen din må være varig nedsatt med minst 50 prosent på grunn av sykdom og/eller skade, eller du må oppfylle en av unntaksreglene." },
				nynorsk { + "Inntektsevna di må vere varig sett ned med minst 50 prosent på grunn av sjukdom og/eller skade, eller oppfylle ein av unntaksreglane." },
				english { + "Your earning ability must be permanently reduced by at least 50 percent due to illness and / or injury or meet one of the exemption criteria." },
			)
			text (
				bokmal { + "Sykdommen eller skaden din må være hovedårsak til din nedsatte inntektsevne." },
				nynorsk { + "Sjukdommen eller skaden din må vere hovudårsaka til at inntektsevna di er sett ned." },
				english { + "Your illness or injury must be the main reason for your reduced earning ability." },
			)
			text (
				bokmal { + "Du må ha gjennomført hensiktsmessig behandling og arbeidsrettede tiltak." },
				nynorsk { + "Du må ha gjennomført formålstenleg behandling og arbeidsretta tiltak." },
				english { + "You have completed appropriate treatment and vocational rehabilitation." },
			)
		}
    }
}
