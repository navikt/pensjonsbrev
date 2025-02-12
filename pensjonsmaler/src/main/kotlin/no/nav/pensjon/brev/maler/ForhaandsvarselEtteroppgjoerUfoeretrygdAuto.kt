package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.erNyttEtteroppgjoer
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.harTjentOver80prosentAvOIFU
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.kanSoekeOmNyInntektsgrense
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.oppjustertInntektFoerUfoerhet
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.opplysningerOmEtteroppgjoeretUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.pensjonsgivendeInntektBruktIBeregningen
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.periode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.totaltAvvik
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
// The conditional for showing the letter is: ResultatEO or ResultatForrigeEO = 'tilbakekr'

@TemplateModelHelpers
object ForhaandsvarselEtteroppgjoerUfoeretrygdAuto : AutobrevTemplate<ForhaandsvarselEtteroppgjoerUfoeretrygdDto> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = ForhaandsvarselEtteroppgjoerUfoeretrygdDto::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Varsel - etteroppgjør av uføretrygd ved feilutbetaling (automatisk)",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                val periode = opplysningerOmEtteroppgjoeretUfoeretrygd.periode.format()
                showIf(erNyttEtteroppgjoer) {
                    textExpr(
                        Bokmal to "Nytt forhåndsvarsel om etteroppgjør av uføretrygd for ".expr() + periode,
                        Nynorsk to "Nytt førehandsvarsel om etteroppgjer av uføretrygd for ".expr() + periode,
                        English to "New notice of settlement for disability benefit for ".expr() + periode,
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Forhåndsvarsel om etteroppgjør av uføretrygd for ".expr() + periode,
                        Nynorsk to "Førehandsvarsel om etteroppgjer av uføretrygd for ".expr() + periode,
                        English to "Notice of settlement for disability benefit for ".expr() + periode,
                    )
                }
            }
            outline {
                includePhrase(
                    Innledning(opplysningerOmEtteroppgjoeretUfoeretrygd.totaltAvvik),
                )
                includePhrase(SjekkBeregning)
                includePhrase(HvordanDuBetalerTilbake)

                showIf(harTjentOver80prosentAvOIFU) {
                    includePhrase(
                        InntektOverInntektstak(
                            oppjustertInntektFoerUfoerhet = oppjustertInntektFoerUfoerhet,
                            pensjonsgivendeInntektBruktIBeregningen = opplysningerOmEtteroppgjoeretUfoeretrygd.pensjonsgivendeInntektBruktIBeregningen,
                            periode = opplysningerOmEtteroppgjoeretUfoeretrygd.periode,
                        ),
                    )
                }

                showIf(kanSoekeOmNyInntektsgrense) {
                    includePhrase(SoekOmNyInntektsgrense)
                }

                showIf(erNyttEtteroppgjoer) {
                    includePhrase(FlereVedtakOmEtteroppgjoer)
                }

                includePhrase(MeldeFraOmEndringerEtteroppgjoer)
                includePhrase(FristNyeOpplysninger)
                includePhrase(FristKlageOpplysninger)
                includePhrase(HarDuSpoersmaalEtteroppgjoer)
            }
            includeAttachment(vedleggOpplysningerOmEtteroppgjoeret, opplysningerOmEtteroppgjoeretUfoeretrygd)
            includeAttachment(vedleggPraktiskInformasjonEtteroppgjoerUfoeretrygd)
            includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
        }
}
