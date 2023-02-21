package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

//TBU054V
// IF brevkode not(PE_UT_04_108) and not(PE_UT_04_109), THEN INCLUDE
object SlikFastsetterViInntektenDinEtterDuBleUfoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Slik fastsetter vi inntekten din etter at du ble ufør",
                Nynorsk to "Slik fastset vi inntekta di etter at du blei ufør",
                English to "This is how we establish your income after your disability"
            )
        }
        paragraph {
            text(
                Bokmal to "Når vi fastsetter inntekten din etter at du ble ufør, tar vi utgangspunkt i den framtidige pensjonsgivende inntekten din. Er det dokumentert at du har inntektsmuligheter som du ikke benytter, skal også disse tas med ved fastsettelsen av inntekten din etter at du ble ufør.",
                Nynorsk to "Når vi fastset inntekta di etter at du blei ufør, tek vi utgangspunkt i den framtidige pensjonsgivande inntekta di. Er det dokumentert at du har inntektsmoglegheiter som du ikkje nyttar, skal også desse takast med når inntekta di etter at du blei ufør, skal fastsetjast.",
                English to "When we establish your income after your disability, we base our calculations on your future pensionable income. If it has been documented that you have options for gainful employment that you are not taking advantage of, these will be included when your income after your disability is calculated."
            )
        }
        paragraph {
            text(
                Bokmal to "Folketrygdens grunnbeløp endres hvert år, og inntekten din før og etter at du ble ufør blir justert ut fra dette.",
                Nynorsk to "Grunnbeløpet i folketrygda blir endra kvart år, og inntekta di før og etter at du blei ufør, blir justert ut frå dette.",
                English to "The National Insurance basic amount changes every year, and your income prior to and after your disability will be adjusted in accordance with this."
            )
        }
    }
}