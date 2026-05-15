package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.fpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.ieo
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.iiap
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.scenario
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — AFP etteroppgjør, ingen endring etter mottatt svar (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_103`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker
 * har lagt fram nye opplysninger om inntekt, og ny beregning fortsatt holder
 * seg innenfor toleransebeløpet — slik at det ikke blir tilbakekreving.
 * Forklaringen til brukeren har fem mulige varianter avhengig av hvilke
 * inntektsfelt (IFU/IEO) som er registrert og når AFP ble tatt ut, se
 * [VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto.Scenario].
 *
 * Konverterte avvik fra kilden (Step 7 i convert-exstream-letter-skill):
 *  - Originalen brukte fem overlappende `showIf`-blokker over rådata
 *    (`IFUregistrert_STRING`, `IEOregistrert_STRING`, `AFP_Uttaksdato` vs.
 *    01.01/01.02). To av blokkene feilet i konverteren (kommentar
 *    "Failed to convert with error: Unexpected character: !"). Logikken er
 *    løftet ut av malen til [Scenario]-diskriminatoren.
 *  - "Den faktiske arbeidsinntekten i den perioden..."-setningen er skilt ut
 *    i en egen paragraf for hvert scenario (jf. lærdom fra PE_AF_04_107).
 *  - Den hardkodede teksten "som i 2024 var på 15 000 kroner" er erstattet
 *    med "i {oppgjørsår} var 15 000 kroner" (samme tilpasning som i
 *    søsterbrevene).
 *  - "Vennlig hilsen" + avsenderenhet er fjernet — brevbaker-rammeverket
 *    setter signaturen selv via fellesAuto.
 *  - Innledningen («Vi viser til tidligere brev...») og konklusjonen
 *    («Ny beregning ... fører til at det ikke blir tilbakekreving»)
 *    deles med PE_AF_04_106 og er trukket ut til
 *    [AfpEtteroppgjoerInnhold.HarVaertRiktigIntro] og
 *    [AfpEtteroppgjoerInnhold.NyBeregningFoererIkkeTilTilbakekreving].
 *  - Hjemmelshenvisningen (lov om AFP for SPK § 3 d) gjenbrukes via
 *    [AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk]. Originalen for 103
 *    har samme nynorsk-komma-avvik som 107 mot fellesfrasen, men per
 *    nyere konvensjon ("bruk fellesfrasen ved små ordlydsavvik")
 *    harmoniseres dette bort.
 *  - Avslutning (Dine plikter / klage / innsyn / spørsmål) gjenbrukes som
 *    fellesfrase [AfpEtteroppgjoerAvslutning].
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndringEtterSvarAuto :
    AutobrevTemplate<VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_INGEN_ENDR_ETTER_SVAR_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ingen endring etter mottatt svar - AFP etteroppgjør",
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

            showIf(scenario.equalTo(Scenario.INGEN_NYE_OPPLYSNINGER)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. " +
                                "Dokumentasjonen som foreligger gir ikke tilstrekkelig grunnlag for å " +
                                "endre tidligere beregnet arbeidsinntekt før uttak av AFP. " +
                                "Arbeidsinntekten din blir etter dette satt til " + ifu.format() +
                                " i samsvar med den tidligere beregningen. Dette beløpet skal holdes " +
                                "utenfor etteroppgjøret for " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. " +
                                "Dokumentasjonen som ligg føre, gir ikkje tilstrekkeleg grunnlag for å " +
                                "endre tidlegare berekna arbeidsinntekt før uttak av AFP. " +
                                "Arbeidsinntekta di blir etter dette sett til " + ifu.format() +
                                " i samsvar med den tidlegare berekninga. Dette beløpet skal haldast " +
                                "utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                        },
                    )
                }
            }

            showIf(scenario.equalTo(Scenario.IFU_UTTAK_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                                "opplysninger har du hatt en høyere arbeidsinntekt før du tok ut AFP enn " +
                                "det som er beregnet tidligere. Arbeidsinntekten din er endret i samsvar " +
                                "med disse opplysningene til " + ifu.format() + ". Dette beløpet skal " +
                                "holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                                "opplysningar har du hatt ei høgare arbeidsinntekt før du tok ut AFP, enn " +
                                "det som er berekna tidlegare. Arbeidsinntekta di er endra i samsvar med " +
                                "desse opplysningane til " + ifu.format() + ". Dette beløpet skal haldast " +
                                "utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " +
                                iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                                "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekten din før uttak av AFP på " +
                                ifu.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " +
                                iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                                "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekta di før uttak av AFP på " +
                                ifu.format() + "."
                        },
                    )
                }
            }

            showIf(scenario.equalTo(Scenario.IFU_UTTAK_FOER_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                                "opplysninger har du hatt pensjonsgivende inntekt som er opptjent før " +
                                "uttak av AFP. Arbeidsinntekten som stammer fra tidligere arbeid, er " +
                                "satt til " + ifu.format() + ". Dette beløpet skal holdes utenfor " +
                                "etteroppgjøret av AFP for " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                                "opplysningar har du hatt pensjonsgivande inntekt som er opptent før " +
                                "uttak av AFP. Arbeidsinntekta som stammar frå tidlegare arbeid, er " +
                                "sett til " + ifu.format() + ". Dette beløpet skal haldast utanfor " +
                                "etteroppgjeret av AFP for " + oppgjoersAar.format() + "."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " +
                                iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                                "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekten din før uttak av AFP på " +
                                ifu.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " +
                                iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                                "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekta di før uttak av AFP på " +
                                ifu.format() + "."
                        },
                    )
                }
            }

            showIf(scenario.equalTo(Scenario.IFU_OG_IEO_REGISTRERT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                                "opplysninger har du hatt en høyere arbeidsinntekt enn tidligere beregnet " +
                                "i perioder hvor du ikke samtidig har hatt rett til AFP. " +
                                "Arbeidsinntekten din for denne perioden er endret i samsvar med disse " +
                                "opplysningene til henholdsvis " + ifu.format() + " for perioden før " +
                                "uttak av AFP og " + ieo.format() + " for perioden etter opphør av AFP. " +
                                "Disse beløpene skal holdes utenfor etteroppgjøret for " +
                                oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                                "opplysningar har du hatt ei høgare arbeidsinntekt enn tidlegare berekna " +
                                "i periodar der du ikkje samtidig har hatt rett til AFP. Arbeidsinntekta " +
                                "di for denne perioden er endra i samsvar med desse opplysningane til " +
                                "høvesvis " + ifu.format() + " for perioden før uttak av AFP og " +
                                ieo.format() + " for perioden etter opphør av AFP tok slutt. Desse " +
                                "beløpa skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() +
                                "."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " +
                                iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                                "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og summen av arbeidsinntektene før uttak av AFP på " +
                                ifu.format() + " og etter opphør av AFP på " + ieo.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " +
                                iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                                "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og summen av arbeidsinntektene før uttak av AFP på " +
                                ifu.format() + " og etter at AFP tok slutt, på " + ieo.format() + "."
                        },
                    )
                }
            }

            showIf(scenario.equalTo(Scenario.KUN_IEO_REGISTRERT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                                "opplysninger har du hatt en høyere arbeidsinntekt enn tidligere beregnet " +
                                "etter opphøret av AFP. Arbeidsinntekten din for denne perioden er " +
                                "endret i samsvar med disse opplysningene til " + ieo.format() + ". " +
                                "Dette beløpet skal holdes utenfor etteroppgjøret for " +
                                oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                                "opplysningar har du hatt ei høgare arbeidsinntekt enn tidlegare berekna " +
                                "etter at AFP tok slutt. Arbeidsinntekta di for denne perioden er endra " +
                                "i samsvar med desse opplysningane til " + ieo.format() + ". Dette " +
                                "beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() +
                                "."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " +
                                iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                                "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekten din etter opphøret av AFP på " +
                                ieo.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " +
                                iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                                "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekta di etter at AFP tok slutt, på " +
                                ieo.format() + "."
                        },
                    )
                }
            }

            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din for " + oppgjoersAar.format() + " la vi til " +
                            "grunn at du ville ha en forventet arbeidsinntekt på " + fpiberegnet.format() +
                            ". Differansen mellom denne tidligere medregnede arbeidsinntekten og den " +
                            "arbeidsinntekten du etter vår nye beregning har hatt i perioden, utgjør " +
                            avvik.format() + ". Denne differansen er ikke større enn toleransebeløpet " +
                            "som i " + oppgjoersAar.format() + " var 15 000 kroner."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din for " + oppgjoersAar.format() + " la vi til " +
                            "grunn at du ville ha ei forventa arbeidsinntekt på " + fpiberegnet.format() +
                            ". Differansen mellom denne tidlegare medrekna arbeidsinntekta og den " +
                            "arbeidsinntekta du etter vår nye berekning har hatt i perioden, utgjer " +
                            avvik.format() + ". Denne differansen er ikkje større enn toleransebeløpet " +
                            "som i " + oppgjoersAar.format() + " var 15 000 kroner."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.NyBeregningFoererIkkeTilTilbakekreving(oppgjoersAar))

            // Avslutning — rettigheter, plikter og kontaktinformasjon (alltid).
            includePhrase(AfpEtteroppgjoerAvslutning)
        }
    }
}
