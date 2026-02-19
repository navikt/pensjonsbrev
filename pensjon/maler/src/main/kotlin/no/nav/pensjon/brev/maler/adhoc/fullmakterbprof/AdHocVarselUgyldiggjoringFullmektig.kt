package no.nav.pensjon.brev.maler.adhoc.fullmakterbprof

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmektigBprofAutoDto
import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmektigBprofAutoDtoSelectors.navnFullmaktsgiver
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevFellesSelectors.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.BrevFellesSelectors.NavEnhetSelectors.telefonnummer

@TemplateModelHelpers
object AdHocVarselUgyldiggjoringFullmektig : AutobrevTemplate<FullmektigBprofAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.GEN_ADHOC_INFOBREV_BPROF_FULLMEKTIG
    override val template: LetterTemplate<*, FullmektigBprofAutoDto> = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om fullmakter for pensjon eller uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Du må fornye den digitale fullmakten for pensjon eller uføretrygd hos Nav " },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Nav får ny løsning for digitale fullmakter. Det betyr at gamle fullmakter blir slettet 15.03.2026." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi har registrert at du har digital fullmakt for " + navnFullmaktsgiver + ". En fullmakt gir deg rett til å logge inn og bruke Navs digitale løsninger på vegne av den som har gitt fullmakten." })
            }
            paragraph {
                text(
                    bokmal { +"Hvis du ikke trenger fullmakten lenger, trenger du ikke å gjøre noe." }
                )
            }

            title2 {
                text(
                    bokmal { +"Dette må du gjøre for å beholde fullmakten" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du har fått fullmakt til å handle på vegne av en annen:" }
                )
            }

            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"Be personen som har gitt deg fullmakten om å lage den på nytt." }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Personen må lage fullmakten på nav.no/fullmakt." }
                        )
                    }
                }
            }


            paragraph {
                text(
                    bokmal { +"Den som har gitt fullmakten, har også fått beskjed." }
                )
            }


            paragraph {
                text(
                    bokmal { +"Vi beklager ulempen!" }
                )
            }

            title2 {
                text(
                    bokmal { +"Har du spørsmål?" },
                )
            }
            paragraph {
                text(
                    bokmal { +"På nav.no/kontakt kan du chatte eller skrive til oss." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan ringe oss på telefon " + felles.avsenderEnhet.telefonnummer.format() + " hverdager kl.$NAV_KONTAKTSENTER_AAPNINGSTID." }
                )
            }

        }
    }
}