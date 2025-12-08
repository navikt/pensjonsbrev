package no.nav.pensjon.etterlatte.maler.andre

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO

@TemplateModelHelpers
object UtsattKlagefrist : EtterlatteTemplate<ManueltBrevDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.UTSATT_KLAGEFRIST

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Utsatt klagefrist",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"" },
                nynorsk { +"" },
                english { +"" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Vi viser til vårt forhåndsvarsel og utkast til vedtak – endring av barnepensjonen." },
                    nynorsk { +"" },
                    english { +"" },
                )
            }
            paragraph {
                text(
                    bokmal { +"På grunn av feil hos oss er det blitt en forsinkelse fra produksjon til utsendelse av “forhåndsvarsel om økt barnepensjon” og “utkast til vedtak – endring av barnepensjon”. Klagefristen er derfor utsatt." },
                    nynorsk { +"" },
                    english { +"" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har klagefrist på seks uker fra 20. januar 2024 på utkast til vedtak, som er å regne som et vedtak fra 20. januar 2024." },
                    nynorsk { +"" },
                    english { +"" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi viser ellers til informasjon i tidligere utsendt forhåndsvarsel og utkast til vedtak." },
                    nynorsk { +"" },
                    english { +"" },
                )
            }
        }
    }
}