package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDtoSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDtoSelectors.scenario
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — AFP etteroppgjør EO fase 2 (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_106`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker har
 * lagt fram nye opplysninger om inntekt før AFP-uttak, og ny beregning viser
 * at det ikke skal tilbakekreves noe. Forklaringen til brukeren avhenger av
 * et av tre gjensidig utelukkende scenarier
 * (se [VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto.Scenario]).
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_INGEN_ENDR_NYE_OPPL_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - AFP etteroppgjør (nye opplysninger)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + oppgjoersAar.format() },
            )
        }

        outline {
            includePhrase(AfpEtteroppgjoerInnhold.HarVaertRiktigIntro(oppgjoersAar))
            includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            showIf(scenario.equalTo(Scenario.HEL_AFP_ALL_INNTEKT_FOER_UTTAK)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                                "opplysninger i saken er hele din pensjonsgivende inntekt opptjent i " +
                                "perioden før du tok ut pensjon. Den vil derfor bli holdt utenfor " +
                                "etteroppgjøret."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                                "opplysningar i saka er heile den pensjonsgivande inntekta di opptent i " +
                                "perioden før du tok ut pensjon. Ho vil derfor bli halden utanfor " +
                                "etteroppgjeret."
                        },
                    )
                }
            }

            showIf(scenario.equalTo(Scenario.HEL_AFP_INNTEKT_INNEN_TOLERANSE)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                                "opplysninger i saken er " + ifu.format() + " opptjent i perioden " +
                                "før du tok ut AFP. Den faktiske arbeidsinntekten du har hatt sammen med " +
                                "pensjonen overstiger ikke toleransebeløpet som i " + oppgjoersAar.format() +
                                " var 15 000 kroner."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                                "opplysningar i saka er " + ifu.format() + " opptente i perioden " +
                                "før du tok ut AFP. Den faktiske arbeidsinntekta du har hatt saman med " +
                                "pensjonen, overstig ikkje toleransebeløpet som i " + oppgjoersAar.format() +
                                " var 15 000 kroner."
                        },
                    )
                }
            }

            showIf(scenario.equalTo(Scenario.GRADERT_AFP_INNTEKT_SVARER_TIL_FORVENTET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                                "opplysninger er " + ifu.format() + " opptjent i perioden før du " +
                                "tok ut AFP. Den faktiske arbeidsinntekten du har hatt sammen med " +
                                "pensjonen, svarer til det som tidligere har vært lagt til grunn ved " +
                                "utbetalingen av AFP."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                                "opplysningar er " + ifu.format() + " opptente i perioden før du " +
                                "tok ut AFP. Den faktiske arbeidsinntekta du har hatt saman med " +
                                "pensjonen, svarer til det som tidlegare har vore lagt til grunn ved " +
                                "utbetalinga av AFP."
                        },
                    )
                }
            }

            includePhrase(AfpEtteroppgjoerInnhold.NyBeregningFoererIkkeTilTilbakekreving(oppgjoersAar))

            // Avslutning — rettigheter, plikter og kontaktinformasjon (alltid).
            includePhrase(AfpEtteroppgjoerAvslutning)
        }
    }
}
