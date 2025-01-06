package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.text.Typography.paragraph

object DesignReferenceLetter : AutobrevTemplate<LetterExampleDto> {
    enum class Kode : Brevkode.Automatisk {
        KODE;

        override fun kode() = name
    }

    override val kode = Kode.KODE
    private val attachment = createAttachment<LangBokmal, EmptyBrevdata>(
        title = newText(
            Bokmal to "Tittel vedlegg"
        ),
        includeSakspart = false,
    ){
        title1 {
            text(
                Bokmal to "Overskrift",
            )
        }
        paragraph {
            text(
                Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                        "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae,\n" +
                        "imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum\n" +
                        "leo.",
            )
        }
        title2 {
            text(
                Bokmal to "Underoverskrift",
            )
        }
        paragraph {
            text(
                Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam" +
                        "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae," +
                        "imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum" +
                        "leo.",
            )
        }
        paragraph {
            table(header = {
                column { text(Bokmal to "Type inntekt") }
                column { text(Bokmal to "Mottatt av") }
                column(alignment = RIGHT) {text(Bokmal to "Inntekt") }
            }){
                row { cell{ text(Bokmal to "Arbeidsinntekt")}; cell{ text(Bokmal to "<RegistreringsKilde>")}; cell{ text(Bokmal to "44 520 kr")}}
                row { cell{ text(Bokmal to "Næringsinntekt")}; cell{ text(Bokmal to "<RegistreringsKilde>")}; cell{ text(Bokmal to "8 000 kr")}}
                row { cell{ text(Bokmal to "Næringsinntekt")}; cell{ text(Bokmal to "<RegistreringsKilde>")}; cell{ text(Bokmal to "8 000 kr")}}
                row { cell{ text(Bokmal to "Arbeidsinntekt")}; cell{ text(Bokmal to "<RegistreringsKilde>")}; cell{ text(Bokmal to "44 520 kr")}}
                row { cell{ text(Bokmal to "Arbeidsinntekt")}; cell{ text(Bokmal to "<RegistreringsKilde>")}; cell{ text(Bokmal to "44 520 kr")}}
                row { cell{ text(Bokmal to "Arbeidsinntekt")}; cell{ text(Bokmal to "<RegistreringsKilde>")}; cell{ text(Bokmal to "44 520 kr")}}
            }
        }
    }

    override val template = createTemplate(
        name = "EKSEMPEL_BREV", //Letter ID
        letterDataType = LetterExampleDto::class, // Data class containing the required data of this letter
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Dette er ett eksempel-brev", // Display title for external systems
            isSensitiv = false, // If this letter contains sensitive information requiring level 4 log-in
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Brevtittel"
            )
        }

        outline {
            paragraph {
                text(
                    Bokmal to lipsums[1],
                )
            }
            title1 {
                text(
                    Bokmal to "Tittel under avsnitt",
                )
            }
            paragraph {
                text(
                    Bokmal to "Sed suscipit lacus vel risus lobortis, sed\n" +
                            "dignissim orci posuere. Aenean ut magna eget tellus viverra tincidunt non quis lectus. Donec\n" +
                            "elementum molestie tellus, tincidunt tincidunt urna tincidunt in. Nunc eget lorem non enim rhoncus\n" +
                            "consequat. Vivamus laoreet semper facilisis.",
                )
            }
            repeat(10) {
                title1 {
                    text(
                        Bokmal to "tittel",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Sed suscipit lacus vel risus lobortis, sed\n" +
                                "dignissim orci posuere. Aenean ut magna eget tellus viverra tincidunt non quis lectus. Donec\n" +
                                "elementum molestie tellus, tincidunt tincidunt urna tincidunt in. Nunc eget lorem non enim rhoncus\n" +
                                "consequat. Vivamus laoreet semper facilisis.",
                    )
                }
            }
        }
        includeAttachment(attachment)
    }
}

