package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.AvkortningsinformasjonSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.AvkortningsinformasjonSelectors.oppjustertInntektFoerUfoere
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtterOppgjoerSelectors.avviksbeloep
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtterOppgjoerSelectors.endretPensjonOgAndreYtelserBruker
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtterOppgjoerSelectors.endretPensjonOgAndreYtelserEPS
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtterOppgjoerSelectors.endretPersonGrunnlagInntektBruker
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtterOppgjoerSelectors.endretPersonGrunnlagInntektEPS
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtterOppgjoerSelectors.harEtterbetaling
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtterOppgjoerSelectors.harTilbakePenger
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ResultatEtterOppgjoerSelectors.tidligereEOIverksatt
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.periodeFom
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.avkortningsinformasjon
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.resultatEtterOppgjoer
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDtoSelectors.ufoeretrygdEtteroppgjoer
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.*
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

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
                    resultatEtterOppgjoer.tidligereEOIverksatt and (resultatEtterOppgjoer.harEtterbetaling or resultatEtterOppgjoer.harTilbakePenger) and
                            (resultatEtterOppgjoer.endretPersonGrunnlagInntektBruker or resultatEtterOppgjoer.endretPersonGrunnlagInntektEPS
                                    or resultatEtterOppgjoer.endretPensjonOgAndreYtelserBruker or resultatEtterOppgjoer.endretPensjonOgAndreYtelserEPS)
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
                    Innledning(avviksbeloep = resultatEtterOppgjoer.avviksbeloep)
                )
                includePhrase(SjekkBeregning)
                includePhrase(HvordanDuBetaleTilbake)

                ifNotNull(avkortningsinformasjon.inntektsgrense, avkortningsinformasjon.oppjustertInntektFoerUfoere) {
                    inntektsgrense, oppjustertInntekt ->
                }
                showIf(avkortningsinformasjon.oppjustertInntektFoerUfoere.)

            }
        }
}
