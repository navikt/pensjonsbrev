package no.nav.pensjon.brev.ufore.maler.hvilenderett

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_S_HVILENDE_RETT_VARSEL_OPPHOER
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object HvilendeRettVarselOpphoer : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_S_HVILENDE_RETT_VARSEL_OPPHOER
    override val kategori = Brevkategori.ETTEROPPGJOER
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel om opphør av uføretrygden etter 10 år med hvilende rett",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Varsel om opphør av uføretrygden etter 10 år med hvilende rett" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Dette er et varsel om at uføretrygden din blir opphørt fra " + fritekst("dato") + " hvis du ikke har rett " +
                            "til utbetaling av uføretrygd i " + fritekst("år") + " og " + fritekst("år") + "." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har siden " + fritekst("år") + " ikke fått utbetaling av uføretrygd fordi din inntekt har vært over 80 prosent " +
                            "av oppjustert inntekt før uførhet. Du har derfor hatt innvilget en hvilende rett." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Det utbetales ikke uføretrygd når den pensjonsgivende inntekten utgjør mer enn 80 prosent av inntekt før uførhet. " +
                            "Dette står i folketrygdloven § 12-14 tredje ledd." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Dersom uføretrygden faller bort etter reglene i folketrygdloven § 12-14 tredje ledd, " +
                            "kan en beholde retten til uføretrygd med opprinnelig uføregrad i inntil ti år (hvilende rett) " +
                            "ved å melde fra til Arbeids- og velferdsetaten. Dette står i folketrygdloven § 12-10 tredje ledd." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har ikke hatt utbetaling av uføretrygd siden " + fritekst("år") + ". Har du heller ikke rett til utbetaling " +
                            "av uføretrygd i " + fritekst("år") + " og "+ fritekst("år") +", vil retten til uføretrygd opphøre fra " + fritekst("dato") + ". " +
                            "Ved opphør av uføretrygden, vil du få eget vedtak om dette." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Har du en årlig inntekt under 80 prosent av oppjustert inntekt før uførhet i " + fritekst("år") + " eller " + fritekst("år") + ", " +
                            "vil du igjen ha rett til utbetaling av uføretrygd. Du vil da opparbeide deg retten til en ny periode med hvilende rett." },
                )
            }
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}