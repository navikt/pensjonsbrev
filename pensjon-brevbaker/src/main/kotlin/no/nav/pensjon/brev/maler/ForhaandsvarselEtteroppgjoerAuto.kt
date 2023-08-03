package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.erNyttEtteroppgjoer
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.harTjentOver80prosentAvOIFU
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.kanSoekeOmNyInntektsgrense
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.oppjustertInntektFoerUfoerhet
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.opplysningerOmEtteroppgjoret
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.pensjonsgivendeInntektBruktIBeregningen
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.periode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.periodeSelector
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

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO

    override val template: LetterTemplate<LanguageSupport.Triple<Bokmal, Nynorsk, English>, ForhaandsvarselEtteroppgjoerUfoeretrygdDto>
        get() = createTemplate(
            name = ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.kode.name,
            letterDataType = ForhaandsvarselEtteroppgjoerUfoeretrygdDto::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata = LetterMetadata(
                displayTitle = "Varsel - etteroppgjør av uføretrygd ved feilutbetaling (automatisk)",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            )
        ) {
            title {
                val periode = opplysningerOmEtteroppgjoret.periode.format()
                showIf(erNyttEtteroppgjoer) {
                    textExpr(
                        Bokmal to "Nytt forhåndsvarsel om etteroppgjør av uføretrygd for ".expr() + periode,
                        Nynorsk to "Nytt førehandsvarsel om etteroppgjer av uføretrygd for ".expr() + periode,
                        English to "New advance notice of settlement of disability benefit for ".expr() + periode
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Forhåndsvarsel om etteroppgjør av uføretrygd for ".expr() + periode,
                        Nynorsk to "Førehandsvarsel om etteroppgjer av uføretrygd for ".expr() + periode,
                        English to "Advance notice of settlement of disability benefit for ".expr() + periode
                    )
                }
            }
            outline {
                includePhrase(
                    Innledning(opplysningerOmEtteroppgjoret.totaltAvvik)
                )
                includePhrase(SjekkBeregning)
                includePhrase(HvordanDuBetaleTilbake)

                showIf(harTjentOver80prosentAvOIFU) {
                    includePhrase(
                        InntektOverInntektstak(
                            oppjustertInntektFoerUfoerhet = oppjustertInntektFoerUfoerhet,
                            pensjonsgivendeInntektBruktIBeregningen = opplysningerOmEtteroppgjoret.pensjonsgivendeInntektBruktIBeregningen,
                            periode = opplysningerOmEtteroppgjoret.periode,
                        )
                    )
                }

                showIf(kanSoekeOmNyInntektsgrense) {
                    includePhrase(SoekOmNyInntektsgrense)
                }

                showIf(erNyttEtteroppgjoer) {
                    includePhrase(FlereVedtakOmEtteroppgjoer)
                }

                includePhrase(MeldeFraOmEndringerEtteroppgjoer)
                includePhrase(FristerOpplysningerKlage)
                includePhrase(HarDuSpoersmaal)
            }
            includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
            // TODO: Include a total of 4 attachments in the order of:
            // Beregning av etteroppgjøret, Opplysninger om beregningen, Praktisk informasjon om tilbakebetaling av etteroppgjøret, Dine rettigheter og plikter
        }
}
