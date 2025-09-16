package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTOSelectors.inntektsaar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTOSelectors.inntektsbeloep

data class OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO(
    val inntektsbeloep: Kroner,
    val inntektsaar: Int
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfall : EtterlatteTemplate<OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO>,
    Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNTEKTSJUSTERING_VEDTAK_UTFALL

    override val template =
        createTemplate(
            letterDataType = OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Vedtaksbrev - inntektsjustering",
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

                paragraph {
                    text(
                        bokmal { +"Omstillingsstønaden din skal reduseres basert på inntekten du forventer å ha neste kalenderår. Kun inntekt i måneder med innvilget omstillingsstønad blir medregnet." },
                        nynorsk { +"Omstillingsstønaden din skal reduserast basert på inntekta du forventar å ha neste kalenderår. Berre inntekt i månader med innvilga omstillingsstønad blir teken med." },
                        english { +"Your adjustment allowance will be reduced based on the income you expect to earn in the next calendar year. Only income in months with granted adjustment allowance is included in the calculation." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Du har ikke meldt om endring i inntekten for neste kalenderår. Vi har derfor basert beregningen på inntektsopplysningene du oppga for inneværende kalenderår. Inntekten er justert til å reflektere inntekt i innvilga måneder i "+inntektsaar.format()+"." },
                        nynorsk { +"Du har ikkje meldt om endring i inntekta for neste kalenderår. Vi har difor basert utrekninga vår på inntektsopplysningane du har oppgitt for inneverande kalenderår. Inntekta er justert slik at ho reflekterer inntekt i innvilga månader i "+inntektsaar.format()+"." },
                        english { +"You have not notified us of any changes in your income for the next calendar year. Therefore we have based the calculation on the income information you provided for the current calendar year. Income is adjusted to reflect income in granted months in "+inntektsaar.format()+"." },
                    )
                }

                showIf(inntektsbeloep.notEqualTo(0)){
                    paragraph {
                        text(
                            bokmal { +"Vi har lagt til grunn at du har "+inntektsbeloep.format() + " som forventet inntekt i innvilgede måneder i  "+inntektsaar.format()+"." },
                            nynorsk { +"Vi har lagt til grunn at du har "+inntektsbeloep.format() + " som forventa inntekt i innvilga månader i "+inntektsaar.format()+"." },
                            english { +"We have assumed a basis of "+inntektsbeloep.format() + " in expected income for the granted months in "+inntektsaar.format()+"." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Vi har lagt til grunn at du ikke har inntekt som omstillingsstønaden skal reduseres etter i innvilgede måneder i "+inntektsaar.format()+"." },
                            nynorsk { +"Vi har lagt til grunn at du ikkje har inntekt som omstillingsstønaden skal reduserast etter i innvilga månader i "+inntektsaar.format()+"." },
                            english { +"We have assumed that you will have no income from which the adjustment allowance will be reduced in granted months in "+inntektsaar.format()+"." },
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven §§ 17-9, 22-12 og 22-13." },
                        nynorsk { +"Vedtaket er gjort etter føresegnene om omstillingsstønad i folketrygdlova §§ 17-9, 22-12 og 22-13." },
                        english { +"The decision has been made pursuant to the regulations on adjustment allowance in the National Insurance Act Sections 17-9, 22-12 and 22-13." },
                    )
                }
            }
        }
    }