package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

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
                Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                        "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae,\n" +
                        "imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum\n" +
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
            testpage1()
            //testpage3()
        }
        includeAttachment(attachment)
    }
}

private fun OutlineOnlyScope<LanguageSupport.Single<Bokmal>, LetterExampleDto>.testpage3() {
    repeat(16) {
        paragraph {
            text(
                Bokmal to "bla",
            )
        }
    }
    paragraph {
        text(
            Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                    "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae,\n" +
                    "imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum\n" +
                    "leo.",
        )
    }
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
            Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                    "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae,\n" +
                    "imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum\n" +
                    "leo.",
        )
    }
    title1 {
        text(
            Bokmal to "Har du spørsmål?",
        )
    }
    paragraph {
        text(
            Bokmal to "Du finner mer informasjon på nav.no/[snarvei-til-aktuelt-område]. På nav.no/kontakt kan du chatte\n" +
                    "eller skrive til oss. Hvis du ikke finner svar på nav.no kan du ringe oss på telefon 55 55 33 33, hverdager\n" +
                    "09.00-15.00.",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Single<Bokmal>, LetterExampleDto>.testpage1() {
    title1 {
        text(
            Bokmal to "Overskrift",
        )
    }
    paragraph {
        text(
            Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo. "
        )
    }
    title2 {
        text(
            Bokmal to "Underoverskrift nivå en",
        )
    }

    paragraph {
        text(
            Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                    "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae,\n" +
                    "imperdiet viverra tortor.",
        )
    }

    paragraph {
        text(
            Bokmal to "Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo.",
        )
    }
    title2 {
        text(
            Bokmal to "Underoverskrift nivå to",
        )
    }
    paragraph {
        text(
            Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                    "posuere porta nec eu elit. Integer nec vestibulum leo.",
        )
    }
}