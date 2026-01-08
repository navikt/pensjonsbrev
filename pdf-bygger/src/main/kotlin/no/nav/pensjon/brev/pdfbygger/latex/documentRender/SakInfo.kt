package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dateFormatter
import no.nav.pensjon.brev.template.render.LanguageSetting
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import java.time.format.FormatStyle

internal fun LatexAppendable.sakspart(sakspart: LetterMarkup.Sakspart, language: Language) {
    appendNewCmd("feltdato", sakspart.dokumentDato.format(dateFormatter(language, FormatStyle.LONG)))
    appendNewCmd("feltsaksnummer", sakspart.saksnummer)
    appendNewCmd("feltfoedselsnummerbruker", sakspart.gjelderFoedselsnummer.format())
    appendNewCmd("feltnavnbruker", sakspart.gjelderNavn)
    val annenMottaker = sakspart.annenMottakerNavn?.also { appendNewCmd("feltannenmottakernavn", it) }

    appendNewCmd("saksinfomottaker") {
        appendCmd("begin", "saksinfotable", "")

        if (annenMottaker != null) {
            appendln("""\felt${LanguageSetting.Sakspart.annenMottaker} & \feltannenmottakernavn \\""", escape = false)
            appendln("""\felt${LanguageSetting.Sakspart.gjelderNavn} & \feltnavnbruker \\""", escape = false)
        } else {
            appendln("""\felt${LanguageSetting.Sakspart.navn} & \feltnavnbruker \\""", escape = false)
        }
        appendln(
            """\felt${LanguageSetting.Sakspart.foedselsnummer} & \feltfoedselsnummerbruker \\""",
            escape = false
        )
        appendln(
            """\felt${LanguageSetting.Sakspart.saksnummer} & \feltsaksnummer \hfill \letterdate\\""",
            escape = false
        )

        appendCmd("end", "saksinfotable")
    }
}
