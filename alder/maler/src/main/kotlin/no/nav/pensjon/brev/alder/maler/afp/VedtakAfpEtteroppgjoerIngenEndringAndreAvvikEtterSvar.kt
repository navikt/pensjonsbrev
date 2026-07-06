package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto.pesysData.*
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto.*
import no.nav.pensjon.brev.api.model.TemplateDescription
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
 * Vedtak — AFP etteroppgjør EO fase 2.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_106`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker har
 * lagt fram nye opplysninger om inntekt før AFP-uttak, og ny beregning viser
 * at det ikke skal tilbakekreves noe. Forklaringen til brukeren avhenger av
 * et av tre gjensidig utelukkende scenarier
 * (se [VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto.Scenario]).
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvar : RedigerbarTemplate<VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_INGEN_ENDR_AVVIK_ETTER_SVAR

    override val kategori = Brevkategori.ETTEROPPGJOER
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.AFP)

    override val featureToggle = FeatureToggles.vedtakAfpEtteroppgjoerIngenEndringNyeOpplysninger.toggle

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
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + pesysData.oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + pesysData.oppgjoersAar.format() },
            )
        }

        outline {
            includePhrase(AfpEtteroppgjoerInnhold.HarVaertRiktigIntro(pesysData.oppgjoersAar))
            showIf(pesysData.medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(pesysData.oppgjoersAar))

            showIf(pesysData.scenario.equalTo(Scenario.HEL_AFP_ALL_INNTEKT_FOER_UTTAK)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntekten din. Ifølge nye opplysninger i saken er hele din pensjonsgivende inntekt opptjent i perioden før du tok ut pensjon. Den vil derfor bli holdt utenfor etteroppgjøret."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntekta di. Ifølgje nye opplysningar i saka er heile den pensjonsgivande inntekta di opptent i perioden før du tok ut pensjon. Ho vil derfor bli halden utanfor etteroppgjeret."
                        },
                    )
                }
            }

            showIf(pesysData.scenario.equalTo(Scenario.HEL_AFP_INNTEKT_INNEN_TOLERANSE)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntekten din. Ifølge nye opplysninger i saken er " + pesysData.inntektFoerUttak.format() + " opptjent i perioden før du tok ut AFP. Den faktiske arbeidsinntekten du har hatt sammen med pensjonen er ikke høyere enn toleransebeløpet som i " + pesysData.oppgjoersAar.format() + " var " + pesysData.toleranseBeloep.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntekta di. Ifølgje nye opplysningar i saka er " + pesysData.inntektFoerUttak.format() + " opptente i perioden før du tok ut AFP. Den faktiske arbeidsinntekta du har hatt saman med pensjonen er ikkje høgare enn toleransebeløpet som i " + pesysData.oppgjoersAar.format() + " var " + pesysData.toleranseBeloep.format() + "."
                        },
                    )
                }
            }

            showIf(pesysData.scenario.equalTo(Scenario.GRADERT_AFP_INNTEKT_SVARER_TIL_FORVENTET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntekten din. Ifølge nye opplysninger er " + pesysData.inntektFoerUttak.format() + " opptjent i perioden før du tok ut AFP. Den faktiske arbeidsinntekten du har hatt sammen med pensjonen er den samme som tidligere har vært lagt til grunn ved utbetalingen av AFP."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntekta di. Ifølgje nye opplysningar er " + pesysData.inntektFoerUttak.format() + " opptente i perioden før du tok ut AFP. Den faktiske arbeidsinntekta du har hatt saman med pensjonen er den same som tidlegare har vore lagt til grunn ved utbetalinga av AFP."
                        },
                    )
                }
            }

            includePhrase(AfpEtteroppgjoerInnhold.NyBeregningFoererIkkeTilTilbakekreving(pesysData.oppgjoersAar))

            includePhrase(AfpEtteroppgjoerAvslutning)
        }
    }
}
