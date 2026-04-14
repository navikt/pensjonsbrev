package no.nav.pensjon.brev.pdfbygger

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.BrevbakerFellesSelectors.dokumentDato
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

enum class LetterExampleBrevkode : Brevkode.Automatisk {
    TESTBREV;

    override fun kode() = name
}

@TemplateModelHelpers
object LetterExample : AutobrevTemplate<LetterExampleDto> {

    override val kode: Brevkode.Automatisk = LetterExampleBrevkode.TESTBREV

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk), // Data class containing the required data of this letter
        letterMetadata = LetterMetadata(
            displayTitle = "Dette er ett eksempel-brev", // Display title for external systems
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Eksempelbrev" },
                nynorsk { +"Eksempelbrev" }
            )
        }

        // Main letter content
        outline {
            // ── TITLE1 + intro-avsnitt ───────────────────────────────────────────────
            title1 {
                text(bokmal { +"Du har fått innvilget alderspensjon" }, nynorsk { +"Du har fått innvilga alderspensjon" })
            }

            paragraph {
                text(
                    bokmal { +"NAV har behandlet søknaden din og vedtatt at du har rett til alderspensjon fra " + LocalDate.of(2020,1,1).expr().format()+ ". Pensjonen er beregnet på grunnlag av dine opptjente rettigheter." },
                    nynorsk { +"NAV har handsama søknaden din og vedteke at du har rett til alderspensjon frå " + LocalDate.of(2020,1,1).expr().format() + ". Pensjonen er rekna ut på grunnlag av dei opptente rettane dine." }

                )
            }

            // ── TITLE2 + avsnitt med bold og italic ──────────────────────────────────
            title2 {
                text(bokmal { +"Slik er pensjonen din beregnet" }, nynorsk { +"Slik er pensjonen din rekna ut" })
            }

            paragraph {
                text(
                    bokmal { +"Pensjonen din består av tre deler:" },
                    nynorsk { +"Pensjonen din består av tre delar:" }
                )
            }

            // ── Punktliste ───────────────────────────────────────────────────────────
            paragraph {
                list {
                    item {
                        text(bokmal { +"Grunnpensjon" }, nynorsk { +"Grunnpensjon" }, FontType.BOLD)
                        text(
                            bokmal { +" – basert på trygdetid i Norge." },
                            nynorsk { +" – basert på trygdetid i Noreg." }
                        )
                    }
                    item {
                        text(bokmal { +"Tilleggspensjon" }, nynorsk { +"Tilleggspensjon" }, FontType.BOLD)
                        text(
                            bokmal { +" – basert på pensjonsgivende inntekt." },
                            nynorsk { +" – basert på pensjonsgivande inntekt." }
                        )
                    }
                    item {
                        text(bokmal { +"Særtillegg" }, nynorsk { +"Særtillegg" }, FontType.BOLD)
                        text(
                            bokmal { +" – gis dersom tilleggspensjonen er lav." },
                            nynorsk { +" – vert gjeve dersom tilleggspensjonen er låg." }
                        )
                    }
                    item {
                        // textOnlyPhrase kan inkluderes overalt der man skriver tekst
                        includePhrase(TextOnlyPhraseTest)
                    }
                }
            }

            // ── TITLE2 + nummerert liste ─────────────────────────────────────────────
            title2 {
                text(bokmal { +"Hva skjer videre?" }, nynorsk { +"Kva skjer vidare?" })
            }

            paragraph {
                numberedList {
                    item {
                        text(
                            bokmal { +"Du vil motta et informasjonsbrev innen 14 dager." },
                            nynorsk { +"Du vil få eit informasjonsbrev innan 14 dagar." }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Utbetalingen starter den " },
                            nynorsk { +"Utbetalinga startar den " }
                        )
                        text(bokmal { +"1. juni 2024" }, nynorsk { +"1. juni 2024" }, FontType.BOLD)
                        text(bokmal { +"." }, nynorsk { +"." })
                    }
                    item {
                        text(
                            bokmal { +"Du kan følge saken din på " },
                            nynorsk { +"Du kan følgje saka di på " }
                        )
                        text(bokmal { +"nav.no/minside" }, nynorsk { +"nav.no/minside" }, FontType.ITALIC)
                        text(bokmal { +"." }, nynorsk { +"." })
                    }
                }
            }

            title1 {
                text(bokmal { +"Utbetalingsoversikt" }, nynorsk { +"Utbetalingsoversikt" })
            }

            paragraph {
/*                text(
                    bokmal { +"Tabellen nedenfor viser de månedlige beløpene for perioden januar–juni 2024." },
                    nynorsk { +"Tabellen nedanfor viser dei månadlege beløpa for perioden januar–juni 2024." }
                )*/
                table(
                    header = {
                        column(3) { text(bokmal { +"Ytelse" }, nynorsk { +"Yting" }) }
                        column(1, RIGHT) { text(bokmal { +"Brutto (kr)" }, nynorsk { +"Brutto (kr)" }) }
                        column(1, RIGHT) { text(bokmal { +"Skatt (kr)" }, nynorsk { +"Skatt (kr)" }) }
                        column(1, RIGHT) { text(bokmal { +"Netto (kr)" }, nynorsk { +"Netto (kr)" }) }
                    }
                ) {
                    row {
                        cell { text(bokmal { +"Grunnpensjon" }, nynorsk { +"Grunnpensjon" }) }
                        cell { text(bokmal { +"11 306" }, nynorsk { +"11 306" }) }
                        cell { text(bokmal { +"2 826" }, nynorsk { +"2 826" }) }
                        cell { text(bokmal { +"8 480" }, nynorsk { +"8 480" }) }
                    }
                    row {
                        cell { text(bokmal { +"Tilleggspensjon" }, nynorsk { +"Tilleggspensjon" }) }
                        cell { text(bokmal { +"5 200" }, nynorsk { +"5 200" }) }
                        cell { text(bokmal { +"1 300" }, nynorsk { +"1 300" }) }
                        cell { text(bokmal { +"3 900" }, nynorsk { +"3 900" }) }
                    }
                    row {
                        cell { text(bokmal { +"Særtillegg" }, nynorsk { +"Særtillegg" }) }
                        cell { text(bokmal { +"0" }, nynorsk { +"0" }) }
                        cell { text(bokmal { +"0" }, nynorsk { +"0" }) }
                        cell { text(bokmal { +"0" }, nynorsk { +"0" }) }
                    }
                    row {
                        cell { text(bokmal { +"Sum" }, nynorsk { +"Sum" }, FontType.BOLD) }
                        cell { text(bokmal { +"16 506" }, nynorsk { +"16 506" }, FontType.BOLD) }
                        cell { text(bokmal { +"4 126" }, nynorsk { +"4 126" }, FontType.BOLD) }
                        cell { text(bokmal { +"12 380" }, nynorsk { +"12 380" }, FontType.BOLD) }
                    }
                }
            }

            // ── TITLE3 + tekst med newline ────────────────────────────────────────────
            title3 {
                text(bokmal { +"Merknader til beregningen" }, nynorsk { +"Merknader til utrekninga" })
            }

            paragraph {
                text(bokmal { +"Trygdetid" }, nynorsk { +"Trygdetid" }, FontType.BOLD)
                newline()
                text(
                    bokmal { +"Du er registrert med 40 års trygdetid i Norge, som gir full grunnpensjon." },
                    nynorsk { +"Du er registrert med 40 års trygdetid i Noreg, som gjev full grunnpensjon." }
                )
                newline()
                text(bokmal { +"Pensjonsgivende inntekt:" }, nynorsk { +"Pensjonsgivande inntekt:" }, FontType.BOLD)
                newline()
                text(
                    bokmal { +"Gjennomsnittlig pensjonsgivende inntekt de 20 beste inntektsårene er lagt til grunn." },
                    nynorsk { +"Gjennomsnittleg pensjonsgivande inntekt dei 20 beste inntektsåra er lagt til grunn." }
                )
            }

            // ── OutlinePhrase ────────────────────────────────────────────────────────
            includePhrase(OutlinePhraseTest(LocalDate.of(2020,1,1).expr(), true.expr()))

            // ── TITLE1 + klagerett ────────────────────────────────────────────────────
            title1 {
                text(bokmal { +"Du har rett til å klage" }, nynorsk { +"Du har rett til å klage" })
            }

            paragraph {
                text(
                    bokmal { +"Du kan klage på vedtaket innen " },
                    nynorsk { +"Du kan klage på vedtaket innan " }
                )
                text(bokmal { +"seks uker" }, nynorsk { +"seks veker" }, FontType.BOLD)
                text(
                    bokmal { +" fra du mottok dette brevet. Klagen skal sendes til det kontoret som har fattet vedtaket." },
                    nynorsk { +" frå du mottok dette brevet. Klaga skal sendast til det kontoret som har gjort vedtaket." }
                )
            }

            paragraph {
                text(
                    bokmal { +"Du kan lese mer om klagerett og saksbehandlingstid på " },
                    nynorsk { +"Du kan lese meir om klagerett og sakshandsamingstid på " }
                )
                text(bokmal { +"nav.no/klage" }, nynorsk { +"nav.no/klage" }, FontType.ITALIC)
                text(bokmal { +"." }, nynorsk { +"." })
            }

            // ── Noen ekstra avsnitt for å fylle siden ────────────────────────────────
            for (lipsum in lipsums.take(2)) {
                paragraph { text(bokmal { +lipsum }, nynorsk { +lipsum }) }
            }
        }
        includeAttachment(testVedlegg)
    }
}

// This data class should normally be in the api-model. Placed here for test-purposes.
data class LetterExampleDto(
    val pensjonInnvilget: Boolean,
    val datoInnvilget: LocalDate,
    val navneliste: List<String>,
    val tilleggEksempel: List<ExampleTilleggDto>,
    val datoAvslaatt: LocalDate?,
    val pensjonBeloep: Int?,
) : AutobrevData

data class ExampleTilleggDto(
    val navn: String,
    val tillegg1: Kroner? = null,
    val tillegg2: Kroner? = null,
    val tillegg3: Kroner? = null,
) {
    @Suppress("unused")
    constructor() : this(
        navn = "Navn",
        tillegg1 = Kroner(1234),
        tillegg2 = Kroner(1234),
        tillegg3 = Kroner(1234),
    )
}

data class OutlinePhraseTest(val datoInnvilget: Expression<LocalDate>, val pensjonInnvilget: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorsk>() {
    //The elements used in outline can also be used in outline phrases.
    //This is intended for use in the top-level outline scope

    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            showIf(pensjonInnvilget) {
                text(
                    bokmal { +"Du har fått innvilget pensjon fra " + datoInnvilget.format() + "." },
                    nynorsk { +"Du har fått innvilget pensjon fra " + datoInnvilget.format() + "." },
                )
            }
        }
}

@Suppress("unused")
object ParagraphPhraseTest : ParagraphPhrase<LangBokmalNynorsk>() {
    override fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.template() =
        list {
            item {
                text(bokmal { +"Test 1" }, nynorsk { +"Test 1" })
            }

            item {
                text(bokmal { +"Test 2" }, nynorsk { +"Test 2" })
            }

            item {
                text(bokmal { +"Test 3" }, nynorsk { +"Test 3" })
            }
        }
}

object TextOnlyPhraseTest : TextOnlyPhrase<LangBokmalNynorsk>() {
    override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() =
        text(bokmal { +"Dette er en tekstfrase" }, nynorsk { +"Dette er en tekstfrase" })
}

@TemplateModelHelpers
val testVedlegg = createAttachment<LangBokmalNynorsk, EmptyVedleggData>(
    title = {
        text(
            bokmal { +"Egenerklæring og tilleggsinformasjon" },
            nynorsk { +"Eigenerklæring og tilleggsinformasjon" },
        )
    },
    includeSakspart = true
) {
    val testVerdi1 = "testVerdi1".expr()
    val testVerdi2 = "testVerdi2".expr()
    // ── TITLE1 + intro ──────────────────────────────────────────────────────────
    title1 {
        text(bokmal { +"Om dette vedlegget" }, nynorsk { +"Om dette vedlegget" })
    }

    paragraph {
        val dokDato = felles.dokumentDato.format()
        text(
            bokmal { +"Dette vedlegget gjelder sak " + testVerdi1 + " (referanse: " + testVerdi2 + "), datert " + dokDato + ". Les det nøye før du fyller ut skjemaet." },
            nynorsk { +"Dette vedlegget gjeld sak " + testVerdi1 + " (referanse: " + testVerdi2 + "), datert " + dokDato + ". Les det nøye før du fyller ut skjemaet." },
        )
    }

    // ── TITLE2 + punktliste med bold/italic ─────────────────────────────────────
    title2 {
        text(bokmal { +"Dokumentasjon du må legge ved" }, nynorsk { +"Dokumentasjon du må leggje ved" })
    }

    paragraph {
        list {
            item {
                text(bokmal { +"Gyldig " }, nynorsk { +"Gyldig " })
                text(bokmal { +"legitimasjon" }, nynorsk { +"legitimasjon" }, FontType.BOLD)
                text(bokmal { +" (pass eller nasjonalt ID-kort)." }, nynorsk { +" (pass eller nasjonalt ID-kort)." })
            }
            item {
                text(bokmal { +"Dokumentasjon på " }, nynorsk { +"Dokumentasjon på " })
                text(bokmal { +"utenlandsk inntekt" }, nynorsk { +"utanlandsk inntekt" }, FontType.ITALIC)
                text(bokmal { +" de siste fem år." }, nynorsk { +" dei siste fem åra." })
            }
            item {
                text(
                    bokmal { +"Attest fra bostedskommune." },
                    nynorsk { +"Attest frå bustadkommunen." }
                )
            }
        }
    }

    // ── TITLE2 + nummerert liste ─────────────────────────────────────────────────
    title2 {
        text(bokmal { +"Slik sender du dokumentasjonen" }, nynorsk { +"Slik sender du dokumentasjonen" })
    }

    paragraph {
        numberedList {
            item {
                text(
                    bokmal { +"Skann eller fotografer dokumentene i god kvalitet." },
                    nynorsk { +"Skann eller fotografer dokumenta i god kvalitet." }
                )
            }
            item {
                text(
                    bokmal { +"Last opp filene på " },
                    nynorsk { +"Last opp filene på " }
                )
                text(bokmal { +"nav.no/minside" }, nynorsk { +"nav.no/minside" }, FontType.BOLD)
                text(bokmal { +" under «Send dokumentasjon»." }, nynorsk { +" under «Send dokumentasjon»." })
            }
            item {
                text(
                    bokmal { +"Du kan også sende dokumentene per post til adressen nedenfor." },
                    nynorsk { +"Du kan også sende dokumenta per post til adressa nedanfor." }
                )
            }
        }
    }

    // ── TITLE3 + tekst med newline ──────────────────────────────────────────────
    title3 {
        text(bokmal { +"Postadresse" }, nynorsk { +"Postadresse" })
    }

    paragraph {
        text(bokmal { +"NAV Pensjon" }, nynorsk { +"NAV Pensjon" }, FontType.BOLD)
        newline()
        text(bokmal { +"Postboks 6600 Etterstad" }, nynorsk { +"Postboks 6600 Etterstad" })
        newline()
        text(bokmal { +"0607 Oslo" }, nynorsk { +"0607 Oslo" })
    }

    // ── TITLE1 + tabell ─────────────────────────────────────────────────────────
    title1 {
        text(bokmal { +"Oppsummering av trygdetid" }, nynorsk { +"Oppsummering av trygdetid" })
    }

    paragraph {
        text(
            bokmal { +"Tabellen viser registrert trygdetid per land som er lagt til grunn ved beregningen." },
            nynorsk { +"Tabellen viser registrert trygdetid per land som er lagt til grunn ved utrekninga." }
        )
        table(
            header = {
                column(3) { text(bokmal { +"Land" }, nynorsk { +"Land" }) }
                column(1, RIGHT) { text(bokmal { +"Fra" }, nynorsk { +"Frå" }) }
                column(1, RIGHT) { text(bokmal { +"Til" }, nynorsk { +"Til" }) }
                column(1, RIGHT) { text(bokmal { +"År" }, nynorsk { +"År" }) }
            }
        ) {
            row {
                cell { text(bokmal { +"Norge" }, nynorsk { +"Noreg" }) }
                cell { text(bokmal { +"1984" }, nynorsk { +"1984" }) }
                cell { text(bokmal { +"2024" }, nynorsk { +"2024" }) }
                cell { text(bokmal { +"40" }, nynorsk { +"40" }) }
            }
            row {
                cell { text(bokmal { +"Sverige" }, nynorsk { +"Sverige" }) }
                cell { text(bokmal { +"1980" }, nynorsk { +"1980" }) }
                cell { text(bokmal { +"1984" }, nynorsk { +"1984" }) }
                cell { text(bokmal { +"4" }, nynorsk { +"4" }) }
            }
            row {
                cell { text(bokmal { +"Sum" }, nynorsk { +"Sum" }, FontType.BOLD) }
                cell { text(bokmal { +"" }, nynorsk { +"" }) }
                cell { text(bokmal { +"" }, nynorsk { +"" }) }
                cell { text(bokmal { +"44" }, nynorsk { +"44" }, FontType.BOLD) }
            }
        }
    }

    // ── TITLE1 + skjemadel ──────────────────────────────────────────────────────
    title1 {
        text(bokmal { +"Egenerklæring – fyll ut og returner" }, nynorsk { +"Eigenerklæring – fyll ut og returner" })
    }

    paragraph {
        text(
            bokmal { +"Fyll ut alle feltene og send skjemaet til NAV. Ufullstendig utfylt skjema kan forsinke saksbehandlingen." },
            nynorsk { +"Fyll ut alle felta og send skjemaet til NAV. Ufullstendig utfylt skjema kan forseinka sakshandsaminga." }
        )
    }

    // ── formText – ulike størrelser ──────────────────────────────────────────────
    paragraph {
        formText(
            size = Size.LONG,
            prompt = {
                text(bokmal { +"Saksnummer (" + testVerdi1 + "):" }, nynorsk { +"Saksnummer (" + testVerdi1 + "):" })
            },
            vspace = false
        )
        formText(
            size = Size.SHORT,
            prompt = {
                text(bokmal { +"Referanse (" + testVerdi2 + "):" }, nynorsk { +"Referanse (" + testVerdi2 + "):" })
            },
            vspace = false
        )
        formText(
            size = Size.FILL,
            prompt = {
                text(bokmal { +"Adresse:" }, nynorsk { +"Adresse:" })
            }
        )
        formText(
            size = Size.SHORT,
            vspace = false,
            prompt = {
                text(bokmal { +"Dato:" }, nynorsk { +"Dato:" })
            }
        )
        formText(
            size = Size.LONG,
            vspace = false,
            prompt = {
                text(bokmal { +"Underskrift:" }, nynorsk { +"Underskrift:" })
            }
        )
    }

    // ── formChoice ───────────────────────────────────────────────────────────────
    paragraph {
        formChoice(
            prompt = {
                text(
                    bokmal { +"Har du hatt inntekt fra utlandet de siste 12 månedene?" },
                    nynorsk { +"Har du hatt inntekt frå utlandet dei siste 12 månadene?" }
                )
            }
        ) {
            choice(bokmal { +"Ja" }, nynorsk { +"Ja" })
            choice(bokmal { +"Nei" }, nynorsk { +"Nei" })
            choice(
                bokmal { +"Vet ikke" },
                nynorsk { +"Veit ikkje" }
            )
        }
    }
    for (lipsum in lipsums.take(2)) {
        paragraph { text(bokmal { +lipsum }, nynorsk { +lipsum }) }
    }

    paragraph{

        formChoice(
            prompt = {
                text(
                    bokmal { +"Hvilken type bolig bor du i?" },
                    nynorsk { +"Kva for ein type bustad bur du i?" }
                )
            },
            vspace = true
        ) {
            choice(bokmal { +"Selveierbolig" }, nynorsk { +"Sjølveigarbustad" })
            choice(bokmal { +"Leiebolig" }, nynorsk { +"Leigebustad" })
            choice(bokmal { +"Institusjon / bofellesskap" }, nynorsk { +"Institusjon / bufellesskap" })
        }
    }
}