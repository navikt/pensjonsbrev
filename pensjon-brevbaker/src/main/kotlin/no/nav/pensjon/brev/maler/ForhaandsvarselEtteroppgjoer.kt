package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.ResultatEtteroppgjoerSelectors.avviksbeloep
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.ResultatEtteroppgjoerSelectors.harNyttEtteroppgjoer
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.aarPeriodeFom
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.inntektOverInntektstak
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.inntektsgrensebeloepAar
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.oppjustertInntektFoerUfoerhet
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.UfoeretrygdEtteroppgjoerSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.resultatEtteroppgjoer
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDtoSelectors.ufoeretrygdEtteroppgjoer
import no.nav.pensjon.brev.maler.fraser.ufoer.*
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.*


// PE_UT_23_001 Varsel - etteroppgjør av uføretrygd ved feilutbetaling (auto)
// Brevet bestilles av BPEN092 (Etteroppgjør Uføre), brevet går ut til de som har fått for mye uføretrygd utbetalt.
// The conditional for showing the letter is: ResultatEO/ResultatForrigeEO = 'tilbakekr'

@TemplateModelHelpers
object ForhaandsvarselEtteroppgjoerAuto : AutobrevTemplate<ForhaandsvarselEtteroppgjoerAutoDto> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_EO_FORHAANDSVARSEL_AUTO

    override val template: LetterTemplate<LanguageSupport.Triple<Bokmal, Nynorsk, English>, ForhaandsvarselEtteroppgjoerAutoDto>
        get() = createTemplate(
            name = ForhaandsvarselEtteroppgjoerAuto.kode.name,
            letterDataType = ForhaandsvarselEtteroppgjoerAutoDto::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata = LetterMetadata(
                displayTitle = "Varsel - etteroppgjør av uføretrygd ved feilutbetaling (automatisk)",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            )
        ) {
            title {
                val aarPeriodeFom = ufoeretrygdEtteroppgjoer.aarPeriodeFom.format()
                showIf(resultatEtteroppgjoer.harNyttEtteroppgjoer) {
                    textExpr(
                        Bokmal to "Nytt forhåndsvarsel om etteroppgjør av uføretrygd for ".expr() + aarPeriodeFom,
                        Nynorsk to "Nytt førehandsvarsel om etteroppgjer av uføretrygd for ".expr() + aarPeriodeFom,
                        English to "New advance notice of settlement of disability benefit for ".expr() + aarPeriodeFom
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Forhåndsvarsel om etteroppgjør av uføretrygd for ".expr() + aarPeriodeFom,
                        Nynorsk to "Førehandsvarsel om etteroppgjer av uføretrygd for ".expr() + aarPeriodeFom,
                        English to "Advance notice of settlement of disability benefit for ".expr() + aarPeriodeFom
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
                        InntektOverInntektstak(
                            oppjustertInntektFoerUfoerhet = ufoeretrygdEtteroppgjoer.oppjustertInntektFoerUfoerhet,
                            aarPeriodeFom = ufoeretrygdEtteroppgjoer.aarPeriodeFom
                        )
                    )
                }

                // showIf arbeidstaker AND
                showIf(
                    ufoeretrygdEtteroppgjoer.ufoeregrad.greaterThan(0)
                            and ufoeretrygdEtteroppgjoer.ufoeregrad.lessThan(100)
                ) {
                    includePhrase(
                        SoekOmNyInntektsgrense(
                            inntektsgrensebeloepAar = ufoeretrygdEtteroppgjoer.inntektsgrensebeloepAar,
                            aarPeriodeFom = ufoeretrygdEtteroppgjoer.aarPeriodeFom
                        )
                    )
                }

                showIf(resultatEtteroppgjoer.harNyttEtteroppgjoer) {
                    includePhrase(FlereVedtakOmEtteroppgjoer)
                }

                includePhrase(MeldeFraOmEndringerEtteroppgjoer)
                includePhrase(FristerOpplysningerKlage)
                includePhrase(HarDuSpoersmaal)
            }
            includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
        }
}
