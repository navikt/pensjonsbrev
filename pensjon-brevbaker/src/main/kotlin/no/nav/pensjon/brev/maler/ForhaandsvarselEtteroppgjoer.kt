package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtteroppgjoerSelectors.avviksbeloep
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtteroppgjoerSelectors.endretPensjonOgAndreYtelserBruker
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtteroppgjoerSelectors.endretPensjonOgAndreYtelserEPS
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtteroppgjoerSelectors.endretPersonGrunnlagInntektBruker
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtteroppgjoerSelectors.endretPersonGrunnlagInntektEPS
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtteroppgjoerSelectors.harEtterbetaling
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtteroppgjoerSelectors.harTilbakePenger
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtteroppgjoerSelectors.tidligereEOIverksatt
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.inntektOverInntektstak
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.inntektsgrensebeloepAar
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.oppjustertInntektFoerUfoere
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.periodeFom
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.periodeFomAar
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.resultatEtteroppgjoer
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ufoeretrygdEtteroppgjoer
import no.nav.pensjon.brev.maler.fraser.ufoer.*
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.*


// PE_UT_23_001 Varsel - etteroppgjør av uføretrygd ved feilutbetaling (auto)
// Brevet bestilles av BPEN092 (Etteroppgjør Uføre), brevet går ut til de som har fått for mye uføretrygd utbetalt

@TemplateModelHelpers
object ForhaandsvarselEtteroppgjoer : AutobrevTemplate<ForhaandsvarselEtteroppgjoerDto> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_FORHAANDSVARSEL_EO_AUTO

    override val template: LetterTemplate<LanguageSupport.Triple<Bokmal, Nynorsk, English>, ForhaandsvarselEtteroppgjoerDto>
        get() = createTemplate(
            name = ForhaandsvarselEtteroppgjoer.kode.name,
            letterDataType = ForhaandsvarselEtteroppgjoerDto::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata = LetterMetadata(
                displayTitle = "Varsel - etteroppgjør av uføretrygd ved feilutbetaling (automatisk)",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            )
        ) {
            title {
                val periodeFom = ufoeretrygdEtteroppgjoer.periodeFom.format()
                showIf(
                    resultatEtteroppgjoer.tidligereEOIverksatt and (resultatEtteroppgjoer.harEtterbetaling or resultatEtteroppgjoer.harTilbakePenger)
                            and (resultatEtteroppgjoer.endretPersonGrunnlagInntektBruker or resultatEtteroppgjoer.endretPersonGrunnlagInntektEPS
                            or resultatEtteroppgjoer.endretPensjonOgAndreYtelserBruker or resultatEtteroppgjoer.endretPensjonOgAndreYtelserEPS)
                ) {
                    textExpr(
                        Bokmal to "Nytt forhåndsvarsel om etteroppgjør av uføretrygd for ".expr() + periodeFom,
                        Nynorsk to "Nytt førehandsvarsel om etteroppgjer av uføretrygd for ".expr() + periodeFom,
                        English to "New advance notice of settlement of disability benefit for ".expr() + periodeFom
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Forhåndsvarsel om etteroppgjør av uføretrygd for ".expr() + periodeFom,
                        Nynorsk to "Førehandsvarsel om etteroppgjer av uføretrygd for ".expr() + periodeFom,
                        English to "Advance notice of settlement of disability benefit for ".expr() + periodeFom
                    )
                }
            }
            outline {
                includePhrase(
                    Innledning(avviksbeloep = resultatEtteroppgjoer.avviksbeloep)
                )
                includePhrase(SjekkBeregning)
                includePhrase(HvordanDuBetaleTilbake)

                showIf(ufoeretrygdEtteroppgjoer.inntektOverInntektstak) {
                    includePhrase(
                        InntektOverInntektsgrense(
                            oppjustertInntektFoerUfoere = ufoeretrygdEtteroppgjoer.oppjustertInntektFoerUfoere,
                            periodeFomAar = ufoeretrygdEtteroppgjoer.periodeFomAar
                        )
                    )
                }

                // showIf arbeidstaker AND
                showIf(ufoeretrygdEtteroppgjoer.ufoeregrad.greaterThan(0)
                        and ufoeretrygdEtteroppgjoer.ufoeregrad.lessThan(100)) {
                    includePhrase(
                        SoekOmNyInntektsgrense(
                            inntektsgrensebeloepAar = ufoeretrygdEtteroppgjoer.inntektsgrensebeloepAar,
                            periodeFomAar = ufoeretrygdEtteroppgjoer.periodeFomAar
                        )
                    )
                }

                showIf(
                    resultatEtteroppgjoer.tidligereEOIverksatt and (resultatEtteroppgjoer.harEtterbetaling or resultatEtteroppgjoer.harTilbakePenger)
                            and (resultatEtteroppgjoer.endretPersonGrunnlagInntektBruker or resultatEtteroppgjoer.endretPersonGrunnlagInntektEPS
                            or resultatEtteroppgjoer.endretPensjonOgAndreYtelserBruker or resultatEtteroppgjoer.endretPensjonOgAndreYtelserEPS)
                ) {
                    includePhrase(FlereVedtakOmEtteroppgjoer)
                }

                includePhrase(MeldeFraOmEndringerEtteroppgjoer)
                includePhrase(FristerOpplysningerKlage)
                includePhrase(HarDuSpoersmaal)
            }
        }
}
