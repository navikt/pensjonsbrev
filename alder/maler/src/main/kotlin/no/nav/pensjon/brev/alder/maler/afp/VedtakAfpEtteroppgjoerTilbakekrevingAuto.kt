package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpTilbakekrevingBody
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggFolketrygden
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.formyebetalt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.fradragBeregnetArbeidsInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.fullAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.inntektIAfpPerioden
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.korrigertAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.tidligereArbeidsInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.utbetaltAfp
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
 * Vedtak — AFP etteroppgjør med tilbakekreving (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_107`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker
 * ikke har lagt fram nye dokumenterte opplysninger og avviket mellom
 * forventet og faktisk pensjonsgivende inntekt overstiger toleransebeløpet,
 * slik at det blir tilbakekreving av for mye utbetalt AFP. Forklaringen til
 * brukeren har fire periodevarianter avhengig av når AFP er tatt ut / opphørt
 * — se [VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode].
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerTilbakekrevingAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerTilbakekrevingAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_TILBAKEKREVING_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - AFP etteroppgjør med tilbakekreving",
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
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til vårt forhåndsvarsel om etteroppgjør av avtalefestet pensjon (AFP) for " + oppgjoersAar.format() + ". Resultatet av etteroppgjøret viser at du har fått " + formyebetalt.format() + " for mye utbetalt. Dette er hele det feilutbetalte " + "beløpet. I beløpet som kreves tilbake vil innbetalt skatt bli trukket fra."
                    },
                    nynorsk {
                        +"Vi viser til førehandsvarselet vårt om etteroppgjer av avtalefesta pensjon (AFP) for " + oppgjoersAar.format() + ". Resultatet av etteroppgjeret viser at du har fått " + formyebetalt.format() + " for mykje utbetalt. Dette er heile det feilutbetalte " + "beløpet. I beløpet som Nav krev tilbake vil innbetalt skatt bli trekt frå."
                    },
                )
            }

            // Inlinet hjemmelshenvisning. Bokmål er identisk med
            // [AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk], men nynorsk i
            // originalen har et ekstra komma før "og tilhøyrande forskrift"
            // som fellesfrasen normaliserer bort. Holdes inlinet med samme
            // ordlyd som Exstream-kilden.
            includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            showIf(periode.equalTo(Periode.HEL_AFP_HELE_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke lagt fram nye dokumenterte opplysninger som viser at din " + "pensjonsgivende inntekt på " + pensjonsgivendeInntekt.format() + " helt eller delvis " + "stammer fra tidligere arbeid før uttak av AFP, eller fra frivillig eller " + "beordret tjeneste i helsevesenet, skole eller barnehage i forbindelse med " + "covid-19-pandemien. Du har heller ikke lagt fram nye opplysninger om " + "inntekt gitt som pensjonistavlønning i forbindelse med arbeid med " + "fordrevne fra Ukraina. I samsvar med den tidligere beregningen har vi " + "derfor lagt til grunn at hele denne inntekten skal gi avkorting av AFP."
                        },
                        nynorsk {
                            +"Du har ikkje lagt fram nye dokumenterte opplysningar som viser at den " + "pensjonsgivande inntekta di på " + pensjonsgivendeInntekt.format() + " heilt eller delvis " + "stammar frå tidlegare arbeid før uttak av AFP, eller frå frivillig eller " + "beordra teneste i helsesektoren, skule eller barnehage i samband med " + "covid-19-pandemien. Du har heller ikkje lagt fram nye opplysningar om " + "inntekt frå arbeid med fordrivne frå Ukraina. I samsvar med den tidlegare " + "berekninga har vi derfor lagt til grunn at denne inntekta skal gi " + "avkorting av AFP."
                        },
                    )
                }
            }

            showIf(periode.equalTo(Periode.UTTAK_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke lagt fram nye dokumenterte opplysninger om arbeidsinntekten din " + "før du tok ut AFP. Du har heller ikke lagt frem opplysninger om eventuell " + "inntekt fra frivillig eller beordret tjeneste i helsevesenet, skole eller " + "barnehage i forbindelse med covid-19-pandemien. Du har heller ikke lagt " + "fram nye opplysninger om inntekt gitt som pensjonistavlønning i " + "forbindelse med arbeid med fordrevne fra Ukraina. Arbeidsinntekten som " + "skal gi avkorting av AFP er derfor satt til " + inntektFoerUttak.format() + " i samsvar " + "med den tidligere beregningen. Dette beløpet skal holdes utenfor " + "etteroppgjøret for " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har ikkje lagt fram nye dokumenterte opplysningar om arbeidsinntekta di " + "før du tok ut AFP. Du har heller ikkje lagt fram opplysningar om " + "eventuell inntekt frå frivillig eller beordra teneste i helsesektoren, " + "skule eller barnehage i samband med covid-19-pandemien. Du har heller " + "ikkje lagt fram nye opplysningar om inntekt frå arbeid med fordrivne frå " + "Ukraina. Arbeidsinntekta som skal gi avkorting av AFP er derfor sett til " + inntektFoerUttak.format() + " i samsvar med den tidlegare berekninga. Dette beløpet " + "skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten din i den perioden du har mottatt AFP er satt " + "til " + inntektIAfpPerioden.format() + ". Dette beløpet utgjør differansen mellom din " + "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og arbeidsinntekten din før uttak av AFP på " + inntektFoerUttak.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta di i den perioden du har fått AFP er sett til " + inntektIAfpPerioden.format() + ". Dette beløpet utgjer differansen mellom den " + "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og arbeidsinntekta di før uttak av AFP på " + inntektFoerUttak.format() + "."
                        },
                    )
                }
            }

            showIf(periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke lagt fram nye dokumenterte opplysninger om arbeidsinntekten din " + "før du tok ut AFP eller om arbeidsinntekten din etter opphør av AFP. Du " + "har heller ikke lagt frem opplysninger om eventuell inntekt fra frivillig " + "eller beordret tjeneste i helsevesenet, skole eller barnehage i " + "forbindelse med covid-19-pandemien. Du har heller ikke lagt fram nye " + "opplysninger om inntekt gitt som pensjonistavlønning i forbindelse med " + "arbeid med fordrevne fra Ukraina. Arbeidsinntekten din for perioden før " + "uttak av AFP er derfor satt til " + inntektFoerUttak.format() + ". Arbeidsinntekten " + "din for perioden etter opphør av AFP er satt til " + inntektEtterOpphoer.format() + ". " + "Disse beløpene skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + " i samsvar med den tidligere beregningen."
                        },
                        nynorsk {
                            +"Du har ikkje lagt fram nye dokumenterte opplysningar om arbeidsinntekta di " + "før du tok ut AFP eller om arbeidsinntekta di etter at AFP tok slutt. Du " + "har heller ikkje lagt fram opplysningar om eventuell inntekt frå " + "frivillig eller beordra teneste i helsesektoren, skule eller barnehage i " + "samband med covid-19-pandemien. Du har heller ikkje lagt fram nye " + "opplysningar om inntekt frå arbeid med fordrivne frå Ukraina. " + "Arbeidsinntekta di for perioden før uttak av AFP er sett til " + inntektFoerUttak.format() + ". Arbeidsinntekta di for perioden etter opphøyr av AFP er " + "sett til " + inntektEtterOpphoer.format() + ". Desse beløpa skal haldast utanfor " + "etteroppgjeret for " + oppgjoersAar.format() + " i samsvar med den " + "tidlegare berekninga."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " + inntektIAfpPerioden.format() + ". Dette beløpet utgjør differansen mellom din " + "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og summen av arbeidsinntektene før uttak av AFP på " + inntektFoerUttak.format() + " og etter opphør av AFP på " + inntektEtterOpphoer.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " + inntektIAfpPerioden.format() + ". Dette beløpet utgjer differansen mellom den " + "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og summen av arbeidsinntektene før uttak av AFP på " + inntektFoerUttak.format() + " og etter at AFP tok slutt på " + inntektEtterOpphoer.format() + "."
                        },
                    )
                }
            }

            showIf(periode.equalTo(Periode.OPPHOER_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke lagt fram nye opplysninger om inntektsforholdene dine. Du har " + "heller ikke lagt frem opplysninger om eventuell inntekt fra frivillig " + "eller beordret tjeneste i helsevesenet, skole eller barnehage i " + "forbindelse med covid-19-pandemien. Du har heller ikke lagt fram nye " + "opplysninger om inntekt gitt som pensjonistavlønning i forbindelse med " + "arbeid med fordrevne fra Ukraina. Arbeidsinntekten din etter opphør av " + "AFP, er derfor satt til " + inntektEtterOpphoer.format() + " i samsvar med den tidligere " + "beregningen. Dette beløpet skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har ikkje lagt fram nye opplysningar om inntektsforholda dine. Du har " + "heller ikkje lagt fram opplysningar om eventuell inntekt frå frivillig " + "eller beordra teneste i helsesektoren, skule eller barnehage i samband " + "med covid-19-pandemien. Du har heller ikkje lagt fram nye opplysningar " + "om inntekt frå arbeid med fordrivne frå Ukraina. Arbeidsinntekta di " + "etter at AFP tok slutt, er derfor sett til " + inntektEtterOpphoer.format() + " i " + "samsvar med den tidlegare berekninga. Dette beløpet skal haldast utanfor " + "etteroppgjeret for " + oppgjoersAar.format() + "."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP er satt til " + inntektIAfpPerioden.format() + ". Dette beløpet utgjør differansen mellom din " + "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og arbeidsinntekten din etter opphør av AFP på " + inntektEtterOpphoer.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta i den perioden du har fått AFP er sett til " + inntektIAfpPerioden.format() + ". Dette beløpet utgjer differansen mellom den " + "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og arbeidsinntekta di etter at AFP tok slutt på " + inntektEtterOpphoer.format() + "."
                        },
                    )
                }
            }

            includePhrase(AfpTilbakekrevingBody.ToleransebeloepOverskrider(avvik = avvik, oppgjoersAar = oppgjoersAar))
            includePhrase(AfpTilbakekrevingBody.NyPensjonsberegningEquation(fullAfp = fullAfp, fradragBeregnetArbeidsInntekt = fradragBeregnetArbeidsInntekt, korrigertAfp = korrigertAfp))
            includePhrase(AfpTilbakekrevingBody.InntektsfradragetFormel(fradragBeregnetArbeidsInntekt = fradragBeregnetArbeidsInntekt, inntektIAfpPerioden = inntektIAfpPerioden, tidligereArbeidsInntektBeregnet = tidligereArbeidsInntektBeregnet, fullAfp = fullAfp))
            includePhrase(AfpTilbakekrevingBody.AfpForMyeEquation(utbetaltAfp = utbetaltAfp, korrigertAfp = korrigertAfp, formyebetalt = formyebetalt))
            includePhrase(AfpTilbakekrevingBody.TilbakebetalingSection)
            title1 {
                text(
                    bokmal { +"Skatteoppgjør" },
                    nynorsk { +"Skatteoppgjer" },
                )
            }
            includePhrase(AfpTilbakekrevingBody.SkatteoppgjorParagraph(oppgjoersAar))

            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageSeksUker)
            paragraph {
                text(
                    bokmal { +"I vedlegget får du vite mer om hvordan du går fram." },
                    nynorsk { +"I vedlegget får du vite meir om korleis du går fram." },
                )
            }

            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
        includeAttachment(vedleggFolketrygden)
    }
}
