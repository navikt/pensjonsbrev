package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NavEnhetSelectors.nettside
import no.nav.pensjon.brevbaker.api.model.Year

object Omsorgsopptjening {
    // omsorgsopptjenHjelpestInnledn_001
    data class HjelpestoenadInnledn(val aarInnvilgetOmrsorgspoeng: Expression<Year>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                textExpr(
                    Bokmal to "Du har fått godkjent pensjonsopptjening for ".expr() + aarInnvilgetOmrsorgspoeng.format() + " fordi du utfører omsorgsarbeid for barn som har rett til forhøyet hjelpestønad etter sats 3 eller 4.",
                    Nynorsk to "Du har fått godkjent pensjonsopptening for ".expr() + aarInnvilgetOmrsorgspoeng.format() + " fordi du utfører omsorgsarbeid for barn som har rett til forhøgd hjelpestønad etter sats 3 eller 4.",
                    English to "You have been credited pension earnings for ".expr() + aarInnvilgetOmrsorgspoeng.format() + ", because you care for a child who is entitled to higher rate assistance allowance at rate 3 or rate 4."
                )
            }

    }

    // omsorgsopptjenHjelpestKap20Hjemmel_001
    object HjelpestKap20Hjemmel : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 3-16 første ledd bokstav b, 20-8 første ledd bokstav b og 20-21.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 3-16 første ledd bokstav b, 20-8 første ledd bokstav b og 20-21.",
                    English to "This decision was made pursuant to the provisions of sections 3-16, first paragraph, litra b, 20-8 first paragraph, litra b, and 20-21 of the National Insurance Act."
                )
            }
    }

    // omsorgsopptjenHjelpestKap3Hjemmel_001
    object HjelpestKap3Hjemmel : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 3-16 første ledd bokstav b.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 3-16 første ledd bokstav b.",
                    English to "This decision was made pursuant to the provisions of section 3-16, first paragraph, litra b of the National Insurance Act."
                )
            }
    }

    // omsorgsopptjenInfoOverskrift_001, omsorgsopptjenInfo_001
    object Info : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Hva er pensjonsopptjening for omsorgsarbeid?",
                    Nynorsk to "Kva er pensjonsopptening for omsorgsarbeid?",
                    English to "What are pension earnings for care work?"
                )
            }
            paragraph {
                val nettside = felles.avsenderEnhet.nettside
                textExpr(
                    Bokmal to "Pensjonsopptjeningen fra omsorgsarbeidet tilsvarer det du ville fått av en inntekt på 4,5 ganger grunnbeløpet for det aktuelle året. Du kan lese mer om dette på $NAV_URL, og få oversikt over pensjonsopptjeningen din på ".expr() +
                            nettside + ". Her finner du også omsorgsopptjening som du har fått godkjent.",
                    Nynorsk to "Pensjonsoppteninga frå omsorgsarbeidet svarer til det du ville ha fått av ei inntekt på 4,5 gonger grunnbeløpet for det aktuelle året. Du kan lese meir om dette på $NAV_URL, og få oversikt over pensjonsoppteninga di på ".expr() +
                            nettside + ". Her finn du også omsorgsopptening som du har fått godkjent.",
                    English to "The pension savings you earn for care work are equivalent to what you would have earned on an income of 4.5 times the National Insurance basic amount (${quoted("G")}) for the year in question. You can read more about this at $NAV_URL and see your earned pension savings at ".expr() +
                            nettside + ". Here you will also see your approved pension earnings for care work. "
                )
            }
        }
    }

    // omsorgsopptjenOverforingInfoOverskrift_001, omsorgsopptjenOverforingInfo_001
    object OverforingInfo : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Overføring av omsorgsopptjening til den andre forelderen",
                    Nynorsk to "Overføring av omsorgsopptening til den andre forelderen",
                    English to "Transferring pension earnings for care work to the other parent"
                )
            }
            paragraph {
                text(
                    Bokmal to "Den av foreldrene som får barnetrygden vil automatisk få omsorgsopptjeningen. Opptjeningen kan overføres til den andre forelderen hvis dere er sammen om omsorgen for barnet. Da må dere fylle ut skjemaet ${quoted("Overføring av omsorgsopptjening")} (Nav 03-16.10), som dere finner på ${Constants.SKJEMA_URL}.",
                    Nynorsk to "Den av foreldra som får barnetrygda, får automatisk omsorgsoppteninga. Oppteninga kan overførast til den andre forelderen dersom de er saman om omsorga for barnet. Då må de fylle ut skjemaet ${quoted("Overføring av omsorgsopptening")} (Nav 03-16.10), som de finn på ${Constants.SKJEMA_URL}.",
                    English to "The child benefit recipient will automatically be credited the pension earnings for care work. The pension earnings can be transferred to the other parent if you share the care of the child. To transfer pension earnings, complete the form ${quoted("Transfer of pension earnings for care work")} (form no. Nav 03-16.10), which you will find at ${Constants.SKJEMA_URL}."
                )
            }
        }
    }

    // omsorgsopptjenHjelpestonadAutoGodkjennInfoTittel_001, omsorgsopptjenHjelpestonadAutoGodkjennInfo_001
    object HjelpestonadAutoGodkjennInfo : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Automatisk godkjenning av omsorgsopptjening",
                    Nynorsk to "Automatisk godkjenning av omsorgsopptening",
                    English to "Automatic approval of pension earnings for care work"
                )
            }
            paragraph {
                text(
                    Bokmal to "Omsorgsopptjening blir godkjent automatisk hvert år, så lenge barnet har rett til forhøyet hjelpestønad sats 3 eller 4. Denne stønaden kan gis fram til barnet har fylt 18 år. Hvis du fortsatt utfører ulønnet omsorgsarbeid når hjelpestønaden opphører, må du selv sette fram søknad om omsorgsopptjening.",
                    Nynorsk to "Omsorgsopptening blir godkjent automatisk kvart år så lenge barnet har rett til forhøgd hjelpestønad sats 3 eller 4. Nav kan gi denne stønaden fram til barnet har fylt 18 år. Dersom du framleis utfører ulønt omsorgsarbeid når hjelpestønaden tek slutt, må du sjølv søkje om omsorgsopptening.",
                    English to "Pension earnings for care work are credited automatically each year for as long as the child is entitled to higher rate assistance allowance at rate 3 or rate 4. This allowance can be granted until the child turns 18 years. If you continue to care for the child after it is eligible for assistance allowance, you must submit a separate application to earn pension savings for care work."
                )
            }
        }
    }
}
