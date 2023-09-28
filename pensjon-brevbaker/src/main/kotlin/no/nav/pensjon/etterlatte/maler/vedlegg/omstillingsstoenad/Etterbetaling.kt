package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTO
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTOSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTOSelectors.fraDato
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTOSelectors.tilDato
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.stoenadFoerReduksjon
import no.nav.pensjon.etterlatte.maler.EtterbetalingsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import java.time.LocalDate


@TemplateModelHelpers
val etterbetaling = createAttachment<LangBokmalNynorskEnglish, EtterbetalingDTO>(
    title = newText(
        Bokmal to "Etterbetaling",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false
) {

    paragraph {
        textExpr(
            Bokmal to "Du får etterbetalt stønad fra ".expr() + fraDato.format() + " til " + tilDato.format()  +
                    ". Vanligvis vil du få denne etterbetalingen i løpet av tre uker. ",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }
    paragraph {
        text(
            Bokmal to "Har du mottatt andre ytelser fra NAV eller andre, som for eksempel " +
                    "tjenestepensjonsordninger, kan det bli trukket i etterbetalingen. Hvis Skatteetaten " +
                    "eller andre ordninger har krav i etterbetalingen kan utbetalingen bli forsinket. " +
                    "Hvis du får fradrag i etterbetalingen, vil det gå frem av utbetalingsmeldingen din. ",
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
            Bokmal to "Gjelder etterbetalingen tidligere år trekker NAV skatt etter Skatteetatens " +
                    "standardsatser. Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
            Nynorsk to "",
            English to "",
        )
    }

    paragraph {
        table(
            header = {
                column(2) {
                    text(Bokmal to "Periode",
                        Nynorsk to "",
                        English to "",
                    )
                }
                column(1) {
                    text(Bokmal to "Grunnbeløp (G)",
                        Nynorsk to "",
                        English to "",
                    )
                }
                column(2) {
                    text(Bokmal to "Stønad før reduksjon",
                    Nynorsk to "",
                    English to "",
                    )
                }
                column(2) {
                    text(Bokmal to "Brutto utbetaling per måned",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        ) {
            forEach(beregningsperioder) {
                row {
                    cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                    cell { includePhrase(Felles.KronerText(it.grunnbeloep)) }
                    cell { includePhrase(Felles.KronerText(it.stoenadFoerReduksjon)) }
                    cell { includePhrase(Felles.KronerText(it.utbetaltBeloep)) }
                }
            }
        }
    }
}


data class PeriodeITabell(val datoFOM: Expression<LocalDate>, val datoTOM: Expression<LocalDate?>) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        ifNotNull(datoTOM) { datoTOM ->
            textExpr(
                Bokmal to datoFOM.format(true) + " - " + datoTOM.format(true),
                Nynorsk to datoFOM.format(true) + " - " + datoTOM.format(true),
                English to datoFOM.format(true) + " - " + datoTOM.format(true),
            )
        } orShow {
            textExpr(
                Bokmal to datoFOM.format(true) + " - ",
                Nynorsk to datoFOM.format(true) + " - ",
                English to datoFOM.format(true) + " - ",
            )
        }
}