package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.stoenadFoerReduksjon
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EtterbetalingDTOSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
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
        text(
            Bokmal to "Du får etterbetalt stønad fra <fra og til-dato >. Vanligvis vil du få denne etterbetalingen i løpet av tre uker.",
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
            Bokmal to "Gjelder etterbetalingen tidligere år trekker NAV skatt etter Skatteetatens standardsatser. Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        table(
            header = {
                column(2) {
                    text(Bokmal to "Periode", Nynorsk to "", English to "")
                }
                column(1) {
                    text(Bokmal to "Grunnbeløp (G)", Nynorsk to "", English to "")
                }
                column(2) {
                    text(Bokmal to "Stønad før reduksjon", Nynorsk to "", English to "")
                }
                column(2) {
                    text(Bokmal to "Brutto utbetaling per måned", Nynorsk to "", English to "")
                }
            },
        ) {
            forEach(beregningsperioder) {
                row {
                    cell { includePhrase(Barnepensjon.PeriodeITabell(it.datoFOM, it.datoTOM)) }
                    cell { includePhrase(Felles.KronerText(it.grunnbeloep)) }
                    cell { includePhrase(Felles.KronerText(it.stoenadFoerReduksjon)) }
                    cell { includePhrase(Felles.KronerText(it.utbetaltBeloep)) }
                }
            }
        }
    }
}
