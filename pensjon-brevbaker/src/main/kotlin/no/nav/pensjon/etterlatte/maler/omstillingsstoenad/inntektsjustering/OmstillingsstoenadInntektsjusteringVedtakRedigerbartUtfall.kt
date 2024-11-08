package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTOSelectors.inntektsbeloep
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTOSelectors.opphoerDato
import java.time.LocalDate

data class OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO(
    override val innhold: List<Element>,
    val inntektsbeloep: Kroner,
    // Hvis denne settes, så viser vi mal for at du ikke er innvilget hele neste år. Så hvis opphør er i 2026 og vi
    // vi varsler for 2025 blir det feil å sende med datoen for 2026. Dette bør klart framgå av kode / navn på felt
    val opphoerDato: LocalDate?
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfall : EtterlatteTemplate<OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO>,
    Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNTEKTSJUSTERING_VEDTAK_UTFALL

    override val template =
        createTemplate(
            name = kode.name,
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
                    Bokmal to "",
                    Nynorsk to "",
                    English to "",
                )
            }
            outline {

                paragraph {
                    text(
                        Bokmal to "Omstillingsstønaden din skal reduseres basert på inntekten du forventer å ha neste kalenderår. Kun inntekt i måneder med innvilget omstillingsstønad blir medregnet.",
                        Nynorsk to "Omstillingsstønaden din skal reduserast basert på inntekta du forventar å ha neste kalenderår. Berre inntekt i månader med innvilga omstillingsstønad blir teken med.",
                        English to "Your adjustment allowance will be reduced based on the income you expect to earn in the next calendar year. Only income in months with granted adjustment allowance is included in the calculation.",
                    )
                }

                // opphør før desember neste år
                ifNotNull(opphoerDato) { kjentOpphoerDato ->
                    paragraph {
                        text(
                            Bokmal to "Du har ikke meldt om endring i inntekten for neste kalenderår. Vi har derfor basert beregningen på inntektsopplysningene du oppga for inneværende kalenderår. Inntekten er justert etter antall innvilgede måneder.",
                            Nynorsk to "Du har ikkje meldt om endring i inntekta for neste kalenderår. Vi har difor basert utrekninga vår på inntektsopplysningane du har oppgitt for inneverande kalenderår. Inntekta er justert ut frå talet på månader som er innvilga.",
                            English to "You have not notified us of any changes in your income for the next calendar year. Therefore we have based the calculation on the income information you provided for the current calendar year. Income has been adjusted according to the number of granted adjustment allowance months.",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har lagt til grunn at du har ".expr() + inntektsbeloep.format() + " kroner i forventet inntekt i månedene med innvilget omstillingsstønad neste år. Dette forutsetter at du vil motta omstillingsstønad frem til "+kjentOpphoerDato.format()+".",
                            Nynorsk to "Vi har lagt til grunn at du har ".expr() + inntektsbeloep.format() + " kroner i forventa inntekt i månadene med innvilga omstillingsstønad neste år. Dette føreset at du får omstillingsstønad fram til "+kjentOpphoerDato.format()+".",
                            English to "We have applied as a basis that you have NOK ".expr() + inntektsbeloep.format() + " in expected income in the months of granted adjustment allowance next year. This is on the premise that you will receive adjustment allowance up to "+kjentOpphoerDato.format()+".",
                        )
                    }

                // innvilget hele neste år
                }.orShow {
                    paragraph {
                        text(
                            Bokmal to "Du har ikke meldt om endring i inntekten for neste kalenderår. Vi har derfor basert beregningen på inntektsopplysningene du oppga for inneværende kalenderår. Inntekten er justert til å reflektere en årsinntekt.",
                            Nynorsk to "Du har ikkje meldt om endring i inntekta for neste kalenderår. Vi har difor basert utrekninga vår på inntektsopplysningane du har oppgitt for inneverande kalenderår. Inntekta er justert slik at ho reflekterer ei årsinntekt.",
                            English to "You have not notified us of any changes in your income for the next calendar year. Therefore we have based the calculation on the income information you provided for the current calendar year. Income is adjusted to reflect an annual income.",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har lagt til grunn at du har ".expr()+inntektsbeloep.format() + " kroner som forventet inntekt neste år. Dette forutsetter at du mottar omstillingsstønad hele året.",
                            Nynorsk to "Vi har lagt til grunn at du har ".expr()+inntektsbeloep.format() + " kroner som forventa inntekt neste år. Dette føreset at du får omstillingsstønad heile året.",
                            English to "We have applied as a basis that you have NOK ".expr()+inntektsbeloep.format() + " in expected income for next year. This is on the premise that you receive adjustment allowance for the entire year.",
                        )
                    }
                }

                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven §§ 17-9, 22-12 og 22-13.",
                        Nynorsk to "Vedtaket er gjort etter føresegnene om omstillingsstønad i folketrygdlova §§ 17-9, 22-12 og 22-13.",
                        English to "The decision has been made pursuant to the regulations on adjustment allowance in the National Insurance Act Sections 17-9, 22-12 and 22-13.",
                    )
                }
            }
        }

    }