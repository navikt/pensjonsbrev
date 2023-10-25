package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

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
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTOSelectors.etterbetalingsperioder
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTOSelectors.fraDato
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTOSelectors.tilDato
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

@TemplateModelHelpers
val etterbetalingAvBarnepensjon = createAttachment(
    title = newText(
        Bokmal to "Etterbetaling av barnepensjon",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false,
) {
    paragraph {
        textExpr(
            Bokmal to "Du får etterbetalt stønad fra ".expr() + fraDato.format() + " til " + tilDato.format() +
                    ". Vanligvis vil du få denne etterbetalingen i løpet av tre uker.",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }
    paragraph {
        text(
            Bokmal to "Det kan bli beregnet fradrag i etterbetalingen for skatt, ytelser du har mottatt fra NAV eller andre, som for eksempel tjenestepensjonsordninger. " +
                    "Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan denne bli forsinket. " +
                    "Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen.",
            Nynorsk to "",
            English to "",
        )
    }
    title2 {
        text(
            Bokmal to "Skatt på etterbetaling",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Det trekkes vanligvis skatt av etterbetaling.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Gjelder etterbetalingen tidligere år, trekker NAV skatt etter Skatteetatens standardsatser. " +
                    "Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
            Nynorsk to "",
            English to "",
        )
    }
    includePhrase(Beregningsperiodetabell(etterbetalingsperioder))
}
