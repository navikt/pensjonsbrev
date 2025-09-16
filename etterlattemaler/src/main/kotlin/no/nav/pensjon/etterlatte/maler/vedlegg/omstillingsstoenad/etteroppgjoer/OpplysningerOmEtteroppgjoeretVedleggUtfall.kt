package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
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
                bokmal { +"" },
                nynorsk { +"" },
                english { +"" },
            )
        }

        // TODO: NN og EN senere
        outline {
            title2 {
                text(
                    bokmal { +"Beløp trukket fra din pensjonsgivende inntekt" },
                    nynorsk { +"Beløp som er trekt frå den pensjonsgivande inntekta di" },
                    english { +"Amounts deducted from your pensionable income" },
                )
            }
            paragraph {
                text(
                    bokmal { +"FORSLAG 1: " },
                    nynorsk { +"FORSLAG 1: " },
                    english { +"FORSLAG 1: " },
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                text(
                    bokmal { +"Du har hatt omstillingsstønad i deler av " + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde før du fikk innvilget stønaden. Vi har trukket fra <HER LEGGES TIL DET SOM TREKKES FRA I ETTEROPPGJØRET>." },
                    nynorsk { +"Du har hatt omstillingsstønad i deler av " + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde før du fikk innvilget stønaden. Vi har trukket fra <HER LEGGES TIL DET SOM TREKKES FRA I ETTEROPPGJØRET>." },
                    english { +"Du har hatt omstillingsstønad i deler av " + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde før du fikk innvilget stønaden. Vi har trukket fra <HER LEGGES TIL DET SOM TREKKES FRA I ETTEROPPGJØRET>." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du har hatt andre inntekter som kan trekkes fra eller at opplysningene våre er feil, må du sende oss dokumentasjon på det innen tre uker." },
                    nynorsk { +"Hvis du har hatt andre inntekter som kan trekkes fra eller at opplysningene våre er feil, må du sende oss dokumentasjon på det innen tre uker." },
                    english { +"Hvis du har hatt andre inntekter som kan trekkes fra eller at opplysningene våre er feil, må du sende oss dokumentasjon på det innen tre uker." },
                )
            }

            paragraph {
                text(
                    bokmal { +"FORSLAG 2: " },
                    nynorsk { +"FORSLAG 2: " },
                    english { +"FORSLAG 2: " },
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                text(
                    bokmal { +"Du har hatt omstillingsstønad i hele " + data.etteroppgjoersAar.format() + ". Det er kun omstillingsstønaden som ikke skal regnes med i inntekten som reduseres omstillingsstønaden din." },
                    nynorsk { +"Du har hatt omstillingsstønad i hele " + data.etteroppgjoersAar.format() + ". Det er kun omstillingsstønaden som ikke skal regnes med i inntekten som reduseres omstillingsstønaden din." },
                    english { +"Du har hatt omstillingsstønad i hele " + data.etteroppgjoersAar.format() + ". Det er kun omstillingsstønaden som ikke skal regnes med i inntekten som reduseres omstillingsstønaden din." },
                )
            }

            paragraph {
                text(
                    bokmal { +"FORSLAG 3: " },
                    nynorsk { +"FORSLAG 3: " },
                    english { +"FORSLAG 3: " },
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                text(
                    bokmal { +"Du har hatt omstillingsstønad i deler av " + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde etter stønaden ble opphørt. Vi har trukket fra <HER LEGGES TIL DET SOM TREKKES FRA I ETTEROPPGJØRET>." },
                    nynorsk { +"Du har hatt omstillingsstønad i deler av " + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde etter stønaden ble opphørt. Vi har trukket fra <HER LEGGES TIL DET SOM TREKKES FRA I ETTEROPPGJØRET>." },
                    english { +"Du har hatt omstillingsstønad i deler av " + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde etter stønaden ble opphørt. Vi har trukket fra <HER LEGGES TIL DET SOM TREKKES FRA I ETTEROPPGJØRET>." },
                )
            }

        }
    }
}