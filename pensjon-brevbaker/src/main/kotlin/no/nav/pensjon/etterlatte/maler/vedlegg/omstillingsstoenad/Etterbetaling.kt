package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetalingSelectors.etterbetalingsperioder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetalingSelectors.fraDato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetalingSelectors.tilDato
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants


@TemplateModelHelpers
val etterbetalingOmstillingsstoenad = createAttachment<LangBokmalNynorskEnglish, OmstillingsstoenadEtterbetaling>(
    title = newText(
        Bokmal to "Etterbetaling",
        Nynorsk to "Etterbetaling",
        English to "Back Payments",
    ),
    includeSakspart = false
) {

    paragraph {
        textExpr(
            Bokmal to "Du får etterbetalt stønad fra ".expr() + fraDato.format() + " til " + tilDato.format() +
                    ". Vanligvis vil du få denne etterbetalingen i løpet av tre uker. ",
            Nynorsk to "Du får etterbetalt stønad frå ".expr() + fraDato.format() + " til " + tilDato.format() +
                    ". Vanlegvis får du denne etterbetalinga i løpet av tre veker.",
            English to "You will receive back payments on allowance from ".expr() + fraDato.format() + " until " +
                    tilDato.format() + ". You will usually receive this back payment within three weeks.",
        )
    }
    paragraph {
        text(
            Bokmal to "Det kan bli beregnet fradrag i etterbetalingen for skatt, ytelser du har mottatt fra " +
                    "NAV eller andre, som for eksempel tjenestepensjonsordninger. Hvis Skatteetaten eller andre " +
                    "ordninger har krav i etterbetalingen kan denne bli forsinket. Fradrag i etterbetalingen " +
                    "vil gå fram av utbetalingsmeldingen.",
            Nynorsk to "Det kan bli gjort frådrag i etterbetalinga for skatt, og for ytingar du har fått frå " +
                    "NAV eller andre (t.d. tenestepensjonsordningar). Dersom Skatteetaten eller andre ordningar har " +
                    "krav i etterbetalinga, kan denne bli forseinka. Frådrag i etterbetalinga vil gå fram av " +
                    "utbetalingsmeldinga.",
            English to "Deductions may be calculated from the back payment for tax, allowance you have received " +
                    "from NAV or others, such as occupational pension schemes. If the Norwegian Tax Administration " +
                    "or other schemes are entitled to the back payment, the payment to you may be delayed. " +
                    "Deductions from the back payment will be stated in the disbursement notice.  ",
        )
    }

    title2 {
        text(
            Bokmal to "Skatt på etterbetaling",
            Nynorsk to "Skatt på etterbetaling",
            English to "Taxes on back payments",
        )
    }
    paragraph {
        text(
            Bokmal to "Det trekkes vanligvis skatt av etterbetaling.",
            Nynorsk to "Det blir vanlegvis trekt skatt av etterbetaling.",
            English to "Tax is usually deducted from back payments.",
        )
    }
    paragraph {
        text(
            Bokmal to "Gjelder etterbetalingen tidligere år trekker NAV skatt etter Skatteetatens " +
                    "standardsatser. Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
            Nynorsk to "Viss etterbetalinga gjeld tidlegare år, vil NAV trekkje skatt etter standardsatsane " +
                    "til Skatteetaten. Du kan lese meir om satsane på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
            English to "If the back payment applies to previous years, NAV will deduct the tax at the Tax " +
                    "Administration's standard rates. You can read more about the rates here: " +
                    "${Constants.SKATTETREKK_ETTERBETALING_URL}.",
        )
    }
    title2 {
        text(
            Bokmal to "Etterbetaling av omstillingsstønad",
            Nynorsk to "Etterbetaling av omstillingsstønad",
            English to "Payment of adjustment allowance",
        )
    }

    includePhrase(BeregningsperiodetabellEtterbetaling(etterbetalingsperioder))

    paragraph {
        text(
            Bokmal to "Beløpene i tabellen er før skatt.",
            Nynorsk to "Beløpa i tabellen er før skatt.",
            English to "The amounts in the table are stated before tax.",
        )
    }
}
