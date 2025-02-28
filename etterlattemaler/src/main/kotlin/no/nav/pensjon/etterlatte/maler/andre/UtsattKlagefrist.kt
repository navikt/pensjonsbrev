package no.nav.pensjon.etterlatte.maler.andre

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO

@TemplateModelHelpers
object UtsattKlagefrist : EtterlatteTemplate<ManueltBrevDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.UTSATT_KLAGEFRIST

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadataEtterlatte(
            displayTitle = "Utsatt klagefrist",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Vi viser til vårt forhåndsvarsel og utkast til vedtak – endring av barnepensjonen.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "På grunn av feil hos oss er det blitt en forsinkelse fra produksjon til utsendelse av “forhåndsvarsel om økt barnepensjon” og “utkast til vedtak – endring av barnepensjon”. Klagefristen er derfor utsatt.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har klagefrist på seks uker fra 20. januar 2024 på utkast til vedtak, som er å regne som et vedtak fra 20. januar 2024.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi viser ellers til informasjon i tidligere utsendt forhåndsvarsel og utkast til vedtak.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}