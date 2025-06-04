package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.Vedlegg
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggInnholdDTOSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTOSelectors.data

data class EtteroppgjoerBeregningVedleggInnholdDTO(
    val etteroppgjoersAar: Int,
)
data class EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO(
    val data: EtteroppgjoerBeregningVedleggInnholdDTO
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object EtteroppgjoerBeregningVedleggRedigerbartUtfall : EtterlatteTemplate<EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO>, Vedlegg {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_FORHAANDSVARSEL_BEREGNINGVEDLEGG_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Utfall beregning",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK, // TODO: ?
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "FORSLAG 1: ",
                    Nynorsk to "",
                    English to "",
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                textExpr(
                    Bokmal to "Du har hatt omstillingsstønad i deler av ".expr() + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde før du fikk innvilget stønaden. Vi har trukket fra <HER LEGGES TIL DET SOM TREKKES FRA I ETTEROPPGJØRET>.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har hatt andre inntekter som kan trekkes fra eller at opplysningene våre er feil, må du sende oss dokumentasjon på det innen tre uker.",
                    Nynorsk to "",
                    English to "",
                )
            }

            paragraph {
                text(
                    Bokmal to "FORSLAG 2: ",
                    Nynorsk to "",
                    English to "",
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                textExpr(
                    Bokmal to "Du har hatt omstillingsstønad i hele ".expr() + data.etteroppgjoersAar.format() + ". Det er kun omstillingsstønaden som ikke skal regnes med i inntekten som reduseres omstillingsstønaden din.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

            paragraph {
                text(
                    Bokmal to "FORSLAG 3: ",
                    Nynorsk to "",
                    English to "",
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                textExpr(
                    Bokmal to "Du har hatt omstillingsstønad i deler av ".expr() + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde etter stønaden ble opphørt. Vi har trukket fra <HER LEGGES TIL DET SOM TREKKES FRA I ETTEROPPGJØRET>.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

        }
    }
}