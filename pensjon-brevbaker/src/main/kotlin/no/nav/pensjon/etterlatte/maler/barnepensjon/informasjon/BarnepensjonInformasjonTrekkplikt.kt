package no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon

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
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO

@TemplateModelHelpers
object BarnepensjonInformasjonTrekkplikt : EtterlatteTemplate<ManueltBrevDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INFORMASJON_TREKKPLIKT

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = ManueltBrevDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Skatt på barnepensjon fra 1. januar 2025",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Skatt på barnepensjon fra 1. januar 2025",
                    Nynorsk to "",
                    English to "",
                )
            }
            outline {
                title2 {
                    text(
                        Bokmal to "Barnepensjonen blir trekkpliktig",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Barnepensjonen blir trekkpliktig fra 1. januar 2025. Det betyr at vi trekker " +
                                "skatt av alle utbetalinger av barnepensjon. Dette gjelder også hvis du får en " +
                                "etterbetaling av barnepensjon.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Det blir trukket 17 prosent skatt av barnepensjonen din.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Fra 1. januar 2025 skal du ikke registrere barnepensjon i skattekortet ditt. " +
                                "Alle andre inntekter, fradrag, formue og gjeld skal fortsatt inngå i skattekortet. " +
                                "Hvis du er usikker på om opplysningene i skattekortet er riktige og gir korrekt skattetrekk, " +
                                "kan du kontakte Skatteetaten for veiledning.",
                        Nynorsk to "",
                        English to "",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Har du tidligere hatt frivillig skattetrekk på barnepensjonen?",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi erstatter det frivillige skattetrekket med 17 prosent skattetrekk fra og med 1. januar 2025.",
                        Nynorsk to "",
                        English to "",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Bor du i utlandet?",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du mener at du ikke er skattepliktig til Norge, må du søke om fritak hos " +
                                "Skatteetaten. For at vi ikke skal trekke skatt på barnepensjonen, må du sende oss vedtaket om fritak.",
                        Nynorsk to "",
                        English to "",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Har du spørsmål?",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Nav kan ikke svare på spørsmål om skatt. Du kan kontakte Skatteetaten eller " +
                                "bruke denne lenken for mer informasjon: " +
                                "https://www.skatteetaten.no/person/skatt/hjelp-til-riktig-skatt/familie-og-helse/barn/barn-og-ungdom-med-egen-inntekt-eller-formue/.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Har du spørsmål om barnepensjonen finner du mer informasjon på nav.no/barnepensjon.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
}
