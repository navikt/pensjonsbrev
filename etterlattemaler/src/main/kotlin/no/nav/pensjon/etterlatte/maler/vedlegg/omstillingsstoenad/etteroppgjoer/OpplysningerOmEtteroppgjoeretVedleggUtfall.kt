package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.Vedlegg
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.erVedtak
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggInnholdDTOSelectors.erVedtak
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggInnholdDTOSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTOSelectors.data

data class EtteroppgjoerBeregningVedleggInnholdDTO(
    val etteroppgjoersAar: Int,
    val erVedtak: Boolean,
)
data class EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO(
    val data: EtteroppgjoerBeregningVedleggInnholdDTO
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object EtteroppgjoerBeregningVedleggRedigerbartUtfall : EtterlatteTemplate<EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO>, Vedlegg {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_BEREGNINGVEDLEGG_INNHOLD

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Utfall beregning",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
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

        outline {
            title2 {
                text(
                    bokmal { +"Beløp trukket fra din pensjonsgivende inntekt - fradragsbeløp" },
                    nynorsk { +"Beløp som er trekt frå den pensjonsgivande inntekta di - frådragsbeløpet" },
                    english { +"Amounts deducted from your pensionable income - deductible amount" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fradragsbeløpet er den delen av inntekten din som ikke tas med når vi beregner omstillingsstønaden i etteroppgjørsåret." },
                    nynorsk { +"Frådragsbeløpet er den delen av inntekta di som ikkje blir teken med når vi reknar ut omstillingsstønaden i etteroppgjørsåret." },
                    english { +"The deductible amount is the part of your income that is not included when we calculate the adjustment allowance in the income settlement year." },
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
                    bokmal { +"Du har hatt omstillingsstønad i deler av " + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde før du fikk innvilget stønaden. Dette er fradragsbeløpet. Vi har trukket fra xxxxx kroner." },
                    nynorsk { +"Du har hatt omstillingsstønad i delar av " + data.etteroppgjoersAar.format() + ". Det betyr at vi trekkjer frå inntekt du hadde før du fekk innvilga stønaden. Dette er frådragsbeløpet. Vi har trekt frå xxxxx kroner." },
                    english { +"You received adjustment allowance for part of " + data.etteroppgjoersAar.format() + ". This means that income earned before the allowance was approved is deducted. We have deducted NOK xxxxx." },
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
                    bokmal { +"Du har hatt omstillingsstønad i hele " + data.etteroppgjoersAar.format() + ". Det er kun omstillingsstønaden som ikke skal regnes med i inntekten som reduserer omstillingsstønaden din." },
                    nynorsk { +"Du har hatt omstillingsstønad heile  " + data.etteroppgjoersAar.format() + ". Det er berre omstillingsstønaden som ikkje skal reknast med i inntekta som reduserer omstillingsstønaden din." },
                    english { +"You have received adjustment allowance for the whole of " + data.etteroppgjoersAar.format() + ". Only the adjustment allowance itself is excluded from the income used to reduce your benefit." },
                )
            }

            paragraph {
                text(
                    bokmal { +"EVENTUELT: " },
                    nynorsk { +"EVENTUELT: " },
                    english { +"EVENTUELT: " },
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                text(
                    bokmal { +"Du har mottatt omstillingsstønad gjennom hele " + data.etteroppgjoersAar.format() + ". Det er kun selve omstillingsstønaden som holdes utenfor når inntekten som påvirker stønadens størrelse beregnes. Det foreligger derfor ingen annen inntekt som skal trekkes fra i beregningen." },
                    nynorsk { +"Du har motteke omstillingsstønad gjennom heile " + data.etteroppgjoersAar.format() + ". Det er berre sjølve omstillingsstønaden som blir halden utanfor når vi reknar ut inntekta som påverkar storleiken på stønaden. Det er derfor ingen annan inntekt som skal trekkjast frå i utrekninga." },
                    english { +"You have received adjustment allowance throughout " + data.etteroppgjoersAar.format() + ". Only the adjustment allowance itself is excluded when calculating the income that affects the amount of the allowance. Therefore, there is no other income to be deducted in the calculation." },
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
                    bokmal { +"Du har hatt omstillingsstønad i deler av " + data.etteroppgjoersAar.format() + ". Det vil si at vi trekker fra inntekt som du hadde etter stønaden ble opphørt. Vi har trukket fra xxxxx kroner." },
                    nynorsk { +"Du har hatt omstillingsstønad i delar av " + data.etteroppgjoersAar.format() + ". Det betyr at vi trekkjer frå inntekt du hadde etter at stønaden vart avslutta. Vi har trekt frå xxxxx kroner." },
                    english { +"You have received adjustment allowance during part of " + data.etteroppgjoersAar.format() + ". This means we deduct any income you earned after your allowance ended. We have deducted NOK xxxxx." },
                )
            }

            showIf(data.erVedtak.not()) {
                paragraph {
                    text(
                        bokmal { +"Hvis du har hatt andre inntekter som kan trekkes fra eller at opplysningene våre er feil, må du sende oss dokumentasjon på det innen tre uker." },
                        nynorsk { +"Dersom du har hatt andre inntekter som kan trekkjast frå, eller viss opplysningane våre er feil, må du sende oss dokumentasjon på dette innan tre veker." },
                        english { +"If you have had other income that can be deducted or if our information is incorrect, you must send us documentation within three weeks." },
                    )
                }
            }

        }
    }
}