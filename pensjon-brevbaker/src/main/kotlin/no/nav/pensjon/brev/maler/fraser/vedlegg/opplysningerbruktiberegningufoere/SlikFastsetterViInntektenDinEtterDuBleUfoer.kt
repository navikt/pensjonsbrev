package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.InntektFoerUfoereBegrunnelse
import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdSelectors.harInntektEtterUfoereBegrunnelse
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

//TBU054V, TBU055V
// IF brevkode not(PE_UT_04_108) and not(PE_UT_04_109), THEN INCLUDE
data class SlikFastsetterViInntektenDinEtterDuBleUfoer(
    val inntektFoerUfoereBegrunnelse: Expression<InntektFoerUfoereBegrunnelse>,
    val ufoeretrygd: Expression<OpplysningerBruktIBeregningUTDto.Ufoeretrygd>,
    val kravAarsakType: Expression<KravAarsakType>,
    val harMinsteytelse: Expression<Boolean>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            ((inntektFoerUfoereBegrunnelse.notNull() or ufoeretrygd.harInntektEtterUfoereBegrunnelse) or
                    (kravAarsakType.isOneOf(KravAarsakType.SIVILSTANDSENDRING) and harMinsteytelse))
        ) {
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

            title1 {
                text(
                    Bokmal to "Slik fastsetter vi uføregraden din",
                    Nynorsk to "Slik fastset vi uføregraden din",
                    English to "This is how we establish your degree of disability"
                )
            }
            paragraph {
                text(
                    Bokmal to "Uføregraden din fastsetter vi ved å sammenligne inntekten din før og etter at du ble ufør. Har du ikke inntektsevne, setter vi uføregraden til 100 prosent. Har du fortsatt inntektsevne, vil uføregraden tilsvare inntektsevnen din som du har tapt. Uføregraden din graderes i trinn på fem prosentpoeng. Vi vurderer alltid om uføregraden skal settes lavere enn 100 prosent.",
                    Nynorsk to "Uføregraden din fastset vi ved å samanlikne inntekta di før og etter at du blei ufør. Dersom du ikkje har inntektsevne, set vi uføregraden til 100 prosent. Har du framleis inntektsevne, tilsvarer uføregraden den inntektsevna di som du har tapt. Uføregraden din blir gradert i trinn på fem prosentpoeng. Vi vurderer alltid om uføregraden skal setjast lågare enn 100 prosent.",
                    English to "Your degree of disability is established by comparing your income before and after your disability. If you have no earning ability, your degree of disability will be fixed at 100 percent. If you still have some earning ability, your degree of disability will correspond to your loss of earning ability. Your degree of disability is graduated on a scale in increments of 5 percentage points. We always evaluate whether the degree of disability should be reduced from 100 percent."
                )
            }
        }
    }
}

