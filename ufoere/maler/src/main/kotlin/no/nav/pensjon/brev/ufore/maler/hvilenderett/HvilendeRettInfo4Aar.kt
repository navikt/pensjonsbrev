package no.nav.pensjon.brev.ufore.maler.hvilenderett

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_S_HVILENDE_RETT_INFO_4_AAR
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object HvilendeRettInfo4Aar : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_S_HVILENDE_RETT_INFO_4_AAR
    override val kategori = TemplateDescription.Brevkategori.ETTEROPPGJOER
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om hvilende rett til uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Informasjon om hvilende rett til uføretrygd" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Du har siden " + fritekst("år") + " ikke fått utbetaling av uføretrygd fordi inntekten din har vært over 80 prosent av oppjustert inntekt før du ble ufør. Du har derfor fått innvilget en hvilende rett." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Uføretrygden utbetales ikke når den pensjonsgivende inntekten utgjør mer enn 80 prosent av inntekten før du ble ufør. Dette står i folketrygdloven § 12-14 tredje ledd." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Når uføretrygden faller bort etter reglene i folketrygdloven § 12-14 tredje ledd, kan en beholde retten til uføretrygd med opprinnelig uføregrad i inntil ti år ved å melde fra til Arbeids- og velferdsetaten. Dette kalles hvilende rett og står i folketrygdloven § 12-10 tredje ledd." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har ikke hatt utbetaling av uføretrygd siden " + fritekst("år") + ". Du kan derfor ha en hvilende rett til uføretrygd i ytterligere fem år før uføretrygden opphøres. Ved opphør av uføretrygden vil du få et eget varsel og vedtak om dette." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du ønsker en ny periode på 5 år, trenger du ikke å kontakte oss om dette. Vi forutsetter da at du ønsker å beholde retten til uføretrygd i ytterligere fem år." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Om du senere har en årlig inntekt under 80 prosent av oppjustert inntekt før uførhet, vil du igjen ha rett til utbetaling av uføretrygd. Du vil da opparbeide deg rett til en ny periode med hvilende rett." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du ønsker å motta uføretrygd igjen eller forventer lavere inntekt fremover gjelder følgende:" },
                )
                list { 
                    item {
                        text(
                            bokmal { + "Du korrigerer selv forventet inntekt i Inntektsplanleggeren din. Denne finner du på Ditt NAV // Din Uføretrygd // Inntektsplanlegger." },
                        )
                    }
                    item {
                        text(
                            bokmal { + "Når du registrerer annen forventet inntekt via Inntektsplanleggeren, som gir ny beregning, vil det dannes et nytt brev i innboksen din." },
                        )
                    }
                    item {
                        text(
                            bokmal { + "Endringen vil gjelde fra kalendermåneden etter endringen av inntekt." },
                        )
                    }
                }
            }
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}