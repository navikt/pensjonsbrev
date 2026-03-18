package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.LoependeInntektsAvkortningSelectors.forventetInntektAar
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.LoependeInntektsAvkortningSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.UforetrygdSelectors.ufoeregrad
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.UforetrygdSelectors.utbetalingsgrad
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.loependeInntektsAvkortning
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDtoSelectors.uforetrygdInnevarendeAr
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object InfoEndretUforetrygdPgaInntekt : AutobrevTemplate<InfoEndretUTPgaInntektDto> {
    override val kode = Ufoerebrevkoder.AutoBrev.UT_INFO_ENDRET_PGA_INNTEKT

    override val template = createTemplate(
        letterDataType = InfoEndretUTPgaInntektDto::class, languages = languages(Language.Bokmal), letterMetadata = LetterMetadata(
            displayTitle = "Varsel - endring av uføretrygd på grunn av inntekt",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {

        val forventetInntekt = loependeInntektsAvkortning.forventetInntektAar
        val inntektsgrense = loependeInntektsAvkortning.inntektsgrenseAar

        val utbetalinggradErLikUfoeregrad = uforetrygdInnevarendeAr.utbetalingsgrad.equalTo(uforetrygdInnevarendeAr.ufoeregrad)
        val avkortet = uforetrygdInnevarendeAr.utbetalingsgrad.lessThan(uforetrygdInnevarendeAr.ufoeregrad) or (utbetalinggradErLikUfoeregrad and forventetInntekt.greaterThan(inntektsgrense))
        val belopsgrense = if (avkortet.equals(true) ) forventetInntekt else inntektsgrense

        title {
            text(
                bokmal { +"Inntekten din kan påvirke utbetalingen av uføretrygd" }
            )
        }

        outline {
            title1 {
                text (
                    bokmal { +"Vi har fått opplysninger om inntekten din" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du fortsetter å ha like mye inntekt som du har hatt hittil i år, vil du få utbetalt for mye uføretrygd. Hvis inntekten din blir høyere enn " + belopsgrense.format() + " kroner i år, vil du få en lavere utbetaling av uføretrygd."}
                )
            }
            paragraph {
                text(
                    bokmal { +"Har du barnetillegg, kan inntekt under inntektsgrensen føre til endring i barnetillegget. Bor du sammen med barnets andre forelder, kan både din og den andre forelderens inntekt påvirke barnetillegget. Derfor må du melde fra om både din og den andre forelderens inntekt."}
                )
            }
            paragraph {
                text(
                    bokmal { +"Du beholder uføregraden din selv om du får en lavere utbetaling."}
                )
            }

            title1 {
                text (
                    bokmal { +"Dette kan du gjøre" }
                )
            }
            paragraph {
                list {
                    item { text(bokmal { +"Hvis du ikke forventer at inntekten din blir høyere enn " + belopsgrense.format() + " kroner i år, trenger du ikke gjøre noe." }) }
                }
                list {
                    item { text(bokmal { +"Forventer du at inntekten din blir høyere enn " + belopsgrense.format() + " kroner, bør du melde fra til oss så fort som mulig." }) }
                }
                list {
                    item { text(bokmal { +"Har du barnetillegg og bor sammen med barnets andre forelder, bør du melde fra om både din og den andre forelderens inntekt." }) }
                }
            }

            title1 {
                text (
                    bokmal { +"For deg som har fått feriepenger" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan få dette brevet fordi du har fått en større utbetaling av feriepenger. Så lenge inntekten din for året ikke blir høyere enn " + belopsgrense.format() + " kroner blir ikke utbetalingen av uføretrygden din påvirket."}
                )
            }
            paragraph {
                text(
                    bokmal { +"Feriepenger skal som hovedregel tas med som inntekt. Unntaket er hvis feriepengene er opptjent før du fikk innvilget uføretrygd eller økt uføregraden din."}
                )
            }
            paragraph {
                text(
                    bokmal { +"Husk at en lavere inntekt kan ha betydning hvis du har barnetillegg."}
                )
            }

            title1 {
                text (
                    bokmal { +"Slik melder du fra om inntekt" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan melde fra om endringer i inntektsplanleggeren, som du finner ved å logge inn på Din uføretrygd. Her kan du melde fra om hvor mye du forventer å tjene, og se hvordan det vil påvirke din utbetaling."}
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du ikke melder fra om inntekt, øker risikoen for at du får utbetalt feil beløp i uføretrygd. Derfor er det viktig at du melder fra om inntekt. "}
                )
            }
            paragraph {
                text(
                    bokmal { +"Selv om vi reduserer uføretrygden din, kan du ha fått for mye utbetalt til at vi kan rette det opp samme år. Hvis du har fått for lite eller for mye utbetalt i uføretrygd et år, får du et etteroppgjør. Etteroppgjøret kommer på høsten når skatteoppgjøret for året før er klart."}
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får et vedtak fra oss hvis uføretrygden endres på grunn av inntekt."}
                )
            }

            title1 {
                text (
                    bokmal { +"Slik henter vi opplysninger om inntekt" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi henter inntektsopplysninger automatisk fra Skatteetatens register, hvor arbeidsgivere melder inn sine ansattes inntekt. Hvis du mener arbeidsgiver har rapportert inn feil inntekt, må du kontakte arbeidsgiver."}
                )
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}