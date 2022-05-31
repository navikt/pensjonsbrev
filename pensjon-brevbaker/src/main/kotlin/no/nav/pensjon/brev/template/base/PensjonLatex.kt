package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.latex.LatexPrintWriter
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.pensjonlatex.pensjonLatexSettings
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object PensjonLatex : BaseTemplate() {
    val letterResourceFiles: Map<String, ByteArray> = hashMapOf(
        "nav-logo.pdf" to getResource("latex/nav-logo.pdf"),
        "nav-logo.pdf_tex" to getResource("latex/nav-logo.pdf_tex"),
        "pensjonsbrev_v3.cls" to getResource("latex/pensjonsbrev_v3.cls"),
        "firstpage.tex" to getResource("latex/firstpage.tex"),
        "attachment.tex" to getResource("latex/attachment.tex"),
        "closing.tex" to getResource("latex/closing.tex"),
        "content.tex" to getResource("latex/content.tex"),
        "tabularray.sty" to getResource("latex/tabularray.sty"),
    )

    private fun getResource(fileName: String): ByteArray =
        this::class.java.getResourceAsStream("/$fileName")
            ?.use { it.readAllBytes() }
            ?: throw IllegalStateException("""Could not find latex resource /$fileName""")

    override val languageSettings: LanguageSettings = pensjonLatexSettings

    override fun render(letter: Letter<*>): RenderedLetter =
        RenderedLatexLetter().apply {
            newFile("params.tex").use { masterTemplateParameters(letter, LatexPrintWriter(it)) }
            newFile("letter.xmpdata").use { xmpData(letter, LatexPrintWriter(it)) }
            newFile("letter.tex").use { renderLetter(letter, LatexPrintWriter(it)) }
            letter.template.attachments
                .filter { it.predicate.eval(letter.toScope()) }
                .forEachIndexed { index, attachment ->
                    newFile("attachment_$index.tex").use { renderAttachment(letter, attachment, LatexPrintWriter(it)) }
                }
            addFiles(letterResourceFiles)
        }

    private fun xmpData(letter: Letter<*>, latexPrintWriter: LatexPrintWriter) {
        with(latexPrintWriter) {
            printCmd("Title", renderTitle(letter))
            printCmd("Language", letter.language.locale().toLanguageTag())
            printCmd("Publisher", letter.felles.avsenderEnhet.navn)
            printCmd("Date", letter.felles.dokumentDato.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
    }

    private fun pdfMetadata(letter: Letter<*>, latexPrintWriter: LatexPrintWriter) =
        latexPrintWriter.print(
            """
               \pdfinfo{
                    /Creator (${letter.felles.avsenderEnhet.navn.latexEscape()})
                    /Title  (${renderTitle(letter)})
                    /Language (${letter.language.locale().toLanguageTag().latexEscape()})
                    /Producer (${letter.felles.avsenderEnhet.navn.latexEscape()})
                }
            """.trimIndent(), escape = false
        )

    private fun renderAttachment(
        letter: Letter<*>,
        attachment: IncludeAttachment<*, *>,
        printWriter: LatexPrintWriter
    ) =
        with(printWriter) {
            printCmd("startvedlegg", attachment.template.title.text(letter.language))
            if (attachment.template.includeSakspart) {
                printCmd("sakspart")
            }
            val scope = letter.toScope().let {
                ExpressionScope(attachment.data.eval(it), it.felles, it.language)
            }
            attachment.template.outline.forEach { renderElement(scope, it, printWriter) }
            printCmd("sluttvedlegg")
        }

    private fun renderLetter(letter: Letter<*>, printWriter: LatexPrintWriter): Unit =
        with(printWriter) {
            println("""\documentclass{pensjonsbrev_v3}""", escape = false)
            pdfMetadata(letter, printWriter)
            printCmd("begin", "document")
            printCmd("firstpage")
            printCmd("tittel", renderTitle(letter))
            contents(letter, printWriter)
            printCmd("closing")
            letter.template.attachments
                .filter { it.predicate.eval(letter.toScope()) }
                .forEachIndexed { index, _ ->
                    printCmd(
                        "input",
                        "attachment_$index",
                        escape = false
                    )
                }
            printCmd("end", "document")
        }

    private fun masterTemplateParameters(letter: Letter<*>, printWriter: LatexPrintWriter) {
        languageSettings.writeLanguageSettings { settingName, settingValue ->
            printWriter.printNewCmd("felt$settingName") { bodyWriter ->
                val scope = letter.toScope()
                settingValue.forEach { renderElement(scope, it, bodyWriter) }
            }
        }

        with(printWriter) {
            println("\\def\\pdfcreationdate{\\string ${pdfCreationTime()}}", escape = false)
            printNewCmd("feltsaksnummer", letter.felles.saksnummer)
        }
        vedleggCommand(letter, printWriter)

        with(letter.felles) {
            brukerCommands(bruker, printWriter)
            mottakerCommands(mottaker, printWriter)
            navEnhetCommands(avsenderEnhet, printWriter)
            datoCommand(dokumentDato, letter.language, printWriter)
            saksbehandlerCommands(signerendeSaksbehandlere, printWriter)
        }
    }

    private fun pdfCreationTime(): String {
        val now = ZonedDateTime.now()
        val formattedTime = now.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssxxx"))
        return "D:${formattedTime.replace(":", "’")}’"
    }

    private fun saksbehandlerCommands(saksbehandlere: SignerendeSaksbehandlere?, printWriter: LatexPrintWriter) {
        if (saksbehandlere != null) {
            printWriter.printNewCmd("closingbehandlet", """\closingsaksbehandlet""", escape = false)
            printWriter.printNewCmd("feltclosingsaksbehandlerfirst", saksbehandlere.saksbehandler)
            printWriter.printNewCmd("feltclosingsaksbehandlersecond", saksbehandlere.attesterendeSaksbehandler)
        } else {
            printWriter.printNewCmd("closingbehandlet", """\closingautomatiskbehandlet""", escape = false)
        }
    }

    private fun datoCommand(dato: LocalDate, language: Language, printWriter: LatexPrintWriter) {
        printWriter.printNewCmd("feltdato", dato.format(dateFormatter(language, FormatStyle.LONG)))
    }

    private fun brukerCommands(bruker: Bruker, printWriter: LatexPrintWriter) =
        with(bruker) {
            printWriter.printNewCmd("feltfoedselsnummerbruker", foedselsnummer.format())
            printWriter.printNewCmd("feltfornavnbruker", fornavn)
            printWriter.printNewCmd("feltetternavnbruker", etternavn)
        }

    private fun mottakerCommands(mottaker: Adresse, printWriter: LatexPrintWriter) =
        with(mottaker) {
            printWriter.printNewCmd("feltmottakeradresselineone", linje1)
            printWriter.printNewCmd("feltmottakeradresselinetwo", linje2)
            printWriter.printNewCmd("feltmottakeradresselinethree", linje3 ?: "")
            printWriter.printNewCmd("feltmottakeradresselinefour", linje4 ?: "")
            printWriter.printNewCmd("feltmottakeradresselinefive", linje5 ?: "")
        }


    private fun navEnhetCommands(navEnhet: NAVEnhet, printWriter: LatexPrintWriter) =
        with(navEnhet.returAdresse) {
            printWriter.printNewCmd("feltnavenhet", navEnhet.navn)
            printWriter.printNewCmd("feltnavenhettlf", navEnhet.telefonnummer.format())
            printWriter.printNewCmd("feltnavenhetnettside", navEnhet.nettside)
            printWriter.printNewCmd("feltreturadressepostnrsted", "$postNr $postSted")
            printWriter.printNewCmd("feltreturadresse", adresseLinje1)
            printWriter.printNewCmd("feltpostadressepostnrsted", "$postNr $postSted")
            printWriter.printNewCmd("feltpostadresse", adresseLinje1)
        }

    private fun vedleggCommand(letter: Letter<*>, printWriter: LatexPrintWriter) {
        printWriter.printNewCmd("feltclosingvedlegg") { bodyWriter ->
            val attachments = letter.template.attachments.filter { it.predicate.eval(letter.toScope()) }
            if (attachments.isNotEmpty()) {
                bodyWriter.printCmd("begin", "attachmentList")
                attachments.forEach {
                    bodyWriter.print("""\item """, escape = false)
                    bodyWriter.println(it.template.title.text(letter.language))
                }
                bodyWriter.printCmd("end", "attachmentList")
            }
        }
    }

    private fun renderTitle(letter: Letter<*>): String {
        val output = ByteArrayOutputStream()

        LatexPrintWriter(output).use { printWriter ->
            val scope = letter.toScope()
            letter.template.title.forEach { renderElement(scope, it, printWriter) }
        }

        return output.toString(Charsets.UTF_8)
    }

    private fun contents(letter: Letter<*>, printWriter: LatexPrintWriter) {
        val scope = letter.toScope()
        letter.template.outline.forEach { renderElement(scope, it, printWriter) }
    }

}
