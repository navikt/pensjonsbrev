package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerForklaringer
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpTilbakekrevingBody
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggFolketrygden
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.formyebetalt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.fradragberegnetai
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.fullafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.ieo
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.iiap
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.korrigertafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.tpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.utbetaltafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — AFP etteroppgjør med tilbakekreving, fase 2 (redigerbar).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_104`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker
 * har lagt fram nye opplysninger om inntekt, men avviket fortsatt
 * overstiger toleransebeløpet og det blir tilbakekreving av for mye
 * utbetalt AFP. Saksbehandler fyller inn nettobeløp (etter fradrag for
 * skatt) og selve skattefradraget som fritekst i Skribenten.
 *
 * Forklaringen om hvilke nye inntektsopplysninger som er lagt fram har
 * seks gjensidig utelukkende varianter, se
 * [VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto.Scenario].
 *
 * Konverterte avvik fra kilden (Step 7 i convert-exstream-letter-skill):
 *  - Originalen brukte seks (delvis overlappende) `showIf`-blokker over
 *    rådata for `IFUregistrert_STRING`, `IEOregistrert_STRING` og
 *    `AFP_Uttaksdato`. Flere av blokkene feilet i konverteren med
 *    "Failed to convert with error: Unexpected character: !" og lot
 *    paragrafer stå uguarded i konverter-output. Logikken er løftet ut
 *    av malen til [Scenario]-diskriminatoren basert på `//IF`-kommentarene.
 *  - "Den faktiske arbeidsinntekten ..."-paragrafen er ifølge kilden en
 *    egen paragraf per scenario (jf. lærdom fra PE_AF_04_107).
 *  - Fem av seks scenarier deler ordlyd med PE_AF_04_102 og er trukket
 *    ut til [AfpEtteroppgjoerForklaringer]. Det sjette scenariet
 *    ([Scenario.INGEN_OVERSTYRING_HEL_AFP]) er unikt for 104 og
 *    inlinet her.
 *  - Tilbakekrevings-likningene, tilbakebetaling- og skatteoppgjør-
 *    seksjonene deler ordlyd med PE_AF_04_107 og er trukket ut til
 *    [AfpTilbakekrevingBody].
 *  - Hjemmelshenvisningen i nynorsk er meningsfullt forskjellig fra
 *    [AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk] (annen
 *    setningsstruktur) og holdes derfor inlinet.
 *  - "Vennlig hilsen" + avsenderenhet er fjernet — brevbaker-rammeverket
 *    setter signaturen selv.
 *  - Den hardkodede "i 2024 var 15 000 kroner" er erstattet med
 *    "i {oppgjørsår} var 15 000 kroner".
 *  - "I vedlegget får du vite mer om hvordan du går fram" etter
 *    klage-paragrafen er beholdt — referer til [vedleggFolketrygden].
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysninger :
    RedigerbarTemplate<VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_TILBAKEKREVING_NYE_OPPLYSNINGER

    override val kategori = Brevkategori.ETTEROPPGJOER

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - AFP etteroppgjør med tilbakekreving (nye opplysninger)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + pesysData.oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + pesysData.oppgjoersAar.format() },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til vårt forhåndsvarsel om etteroppgjør av avtalefestet pensjon " +
                            "(AFP) for " + pesysData.oppgjoersAar.format() + ". Resultatet av " +
                            "etteroppgjøret viser at du har fått for mye utbetalt."
                    },
                    nynorsk {
                        +"Vi viser til førehandsvarselet vårt om etteroppgjer av avtalefesta pensjon " +
                            "(AFP) for " + pesysData.oppgjoersAar.format() + ". Resultatet av " +
                            "etteroppgjeret viser at du har fått for mykje utbetalt."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Beløpet du skal betale tilbake er " + fritekst("nettobeløp") + " kroner."
                    },
                    nynorsk {
                        +"Beløpet du skal betale tilbake er " + fritekst("nettobeløp") + " kroner."
                    },
                )
            }

            // Hjemmelshenvisning inlinet — nynorsk har meningsfullt annen
            // setningsstruktur enn [AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk]
            // ("Vedtaket er fatta etter reglane om kombinasjon av AFP og
            // arbeidsinntekt i lov ...") og kan ikke harmoniseres uten å endre
            // meningen.
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3 " +
                            "bokstav d, og tilhørende forskrift om kombinasjon av avtalefestet pensjon " +
                            "for medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende " +
                            "inntekt)."
                    },
                    nynorsk {
                        +"Vedtaket er fatta etter reglane om kombinasjon av AFP og arbeidsinntekt i lov " +
                            "om AFP for medlemmer av Statens pensjonskasse paragraf 3 første ledd bokstav " +
                            "d og tilhøyrande forskrift om kombinasjon av AFP og arbeidsinntekt."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(pesysData.oppgjoersAar))

            // Scenariene følger rekkefølgen i kilden.
            showIf(pesysData.scenario.equalTo(Scenario.INGEN_OVERSTYRING_HEL_AFP)) {
                // Unik for 104 — ingen tilsvarende paragraf i 102.
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. " +
                                "Dokumentasjonen som foreligger gir ikke tilstrekkelig grunnlag for å " +
                                "godkjenne at din pensjonsgivende inntekt på " + pesysData.pgi.format() +
                                " for " + pesysData.oppgjoersAar.format() + " helt eller delvis stammer " +
                                "fra før uttak av AFP. Den faktiske arbeidsinntekten din i den perioden " +
                                "du har mottatt AFP, er satt til " + pesysData.iiap.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Dokumentasjonen " +
                                "som ligg føre, gir ikkje godt nok grunnlag for å godkjenne at den " +
                                "pensjonsgivande inntekta di på " + pesysData.pgi.format() + " for " +
                                pesysData.oppgjoersAar.format() + " heilt eller delvis stammar frå før du " +
                                "tok ut AFP. Den faktiske arbeidsinntekta di i den perioden du har fått " +
                                "AFP, er sett til " + pesysData.iiap.format() + "."
                        },
                    )
                }
            }

            showIf(pesysData.scenario.equalTo(Scenario.INGEN_OVERSTYRING_UTTAK_I_AARET)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.IngenNyeOpplysningerOmEndretInntektFoerUttak(
                        ifu = pesysData.ifu, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(
                        iiap = pesysData.iiap, oppgjoersAar = pesysData.oppgjoersAar, pgi = pesysData.pgi, ifu = pesysData.ifu,
                    )
                )
            }

            showIf(pesysData.scenario.equalTo(Scenario.IFU_OVERSTYRT_UTTAK_I_AARET)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakIAaret(
                        ifu = pesysData.ifu, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(
                        iiap = pesysData.iiap, oppgjoersAar = pesysData.oppgjoersAar, pgi = pesysData.pgi, ifu = pesysData.ifu,
                    )
                )
            }

            showIf(pesysData.scenario.equalTo(Scenario.IFU_OVERSTYRT_HEL_AFP)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakFoerAaret(
                        ifu = pesysData.ifu, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(
                        iiap = pesysData.iiap, oppgjoersAar = pesysData.oppgjoersAar, pgi = pesysData.pgi, ifu = pesysData.ifu,
                    )
                )
            }

            showIf(pesysData.scenario.equalTo(Scenario.IFU_OG_IEO_OVERSTYRT)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.IfuOgIeoOverstyrt(
                        ifu = pesysData.ifu, ieo = pesysData.ieo, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenIfuOgIeo(
                        iiap = pesysData.iiap, oppgjoersAar = pesysData.oppgjoersAar, pgi = pesysData.pgi, ifu = pesysData.ifu, ieo = pesysData.ieo,
                    )
                )
            }

            showIf(pesysData.scenario.equalTo(Scenario.KUN_IEO_OVERSTYRT)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.KunIeoOverstyrt(
                        ieo = pesysData.ieo, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIeo(
                        iiap = pesysData.iiap, oppgjoersAar = pesysData.oppgjoersAar, pgi = pesysData.pgi, ieo = pesysData.ieo,
                    )
                )
            }

            // Tilbakekrevings-likninger og forklaringer — identisk med 107.
            includePhrase(AfpTilbakekrevingBody.ToleransebeloepOverskrider(
                avvik = pesysData.avvik,
                oppgjoersAar = pesysData.oppgjoersAar
            ))
            includePhrase(AfpTilbakekrevingBody.NyPensjonsberegningEquation(
                fullafp = pesysData.fullafp,
                fradragberegnetai = pesysData.fradragberegnetai,
                korrigertafp = pesysData.korrigertafp
            ))
            includePhrase(AfpTilbakekrevingBody.InntektsfradragetFormel(
                fradragberegnetai = pesysData.fradragberegnetai,
                iiap = pesysData.iiap,
                tpiberegnet = pesysData.tpiberegnet,
                fullafp = pesysData.fullafp
            ))
            includePhrase(AfpTilbakekrevingBody.AfpForMyeEquation(
                utbetaltafp = pesysData.utbetaltafp,
                korrigertafp = pesysData.korrigertafp,
                formyebetalt = pesysData.formyebetalt
            ))

            // «Beløpet du skal betale tilbake etter fradrag for innbetalt skatt»
            // — unikt for 104 (107 har ingen netto/skatt-fradrag fordi det er
            // autobrev). Tre linjer i én paragraf, samme format som de andre
            // ligningene.
            title1 {
                text(
                    bokmal { +"Beløpet du skal betale tilbake etter fradrag for innbetalt skatt" },
                    nynorsk { +"Beløpet du skal betale tilbake etter fradrag for innbetalt skatt" },
                )
            }
            paragraph {
                text(
                    bokmal { +"For mye utbetalt AFP: " + pesysData.formyebetalt.format() },
                    nynorsk { +"For mykje utbetalt AFP: " + pesysData.formyebetalt.format() },
                )
                newline()
                text(
                    bokmal { +"− Fradrag for innbetalt skatt: " + fritekst("fradrag") + " kroner" },
                    nynorsk { +"− Frådrag for innbetalt skatt: " + fritekst("fradrag") + " kroner" },
                )
                newline()
                text(
                    bokmal { +"= Beløpet du skal betale tilbake: " + fritekst("nettobeløp") + " kroner" },
                    nynorsk { +"= Beløpet du skal betale tilbake: " + fritekst("nettobeløp") + " kroner" },
                )
            }

            // TODO: Avklar med fag om paragrafen om skatteoppgjør skal ha en
            //  egen "Skatteoppgjør"-title1 i dette brevet. Originalen
            //  (PE_AF_04_104) har ingen tittel her, men 107 har det.
            includePhrase(AfpTilbakekrevingBody.SkatteoppgjorParagraph(pesysData.oppgjoersAar))
            includePhrase(AfpTilbakekrevingBody.TilbakebetalingSection)

            // Avslutning — Dine plikter / klage / innsyn / Har du spørsmål.
            // Klage- og spørsmålsseksjonene gjenbruker fellesfrasene fra
            // [AfpEtteroppgjoerAvslutning]. Mindre ordlydsavvik fra
            // Exstream-originalen ("6 uker" → "seks uker" i bokmål/nynorsk,
            // og manglende komma etter "nav.no") harmoniseres mot fellesfrasen.
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
