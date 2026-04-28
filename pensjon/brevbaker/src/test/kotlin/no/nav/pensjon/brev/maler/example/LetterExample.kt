package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.maler.example.LetterExampleDtoSelectors.datoInnvilget
import no.nav.pensjon.brev.maler.example.LetterExampleDtoSelectors.pensjonInnvilget
import no.nav.pensjon.brev.maler.example.TestVedleggDtoSelectors.testVerdi1
import no.nav.pensjon.brev.maler.example.TestVedleggDtoSelectors.testVerdi2
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
                    bokmal { +"NAV har behandlet søknaden din og vedtatt at du har rett til alderspensjon fra " + datoInnvilget.format() + ". Pensjonen er beregnet på grunnlag av dine opptjente rettigheter." },
                    nynorsk { +"NAV har handsama søknaden din og vedteke at du har rett til alderspensjon frå " + datoInnvilget.format() + ". Pensjonen er rekna ut på grunnlag av dei opptente rettane dine." }
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
                text(
                    bokmal { +"Tabellen nedenfor viser de månedlige beløpene for perioden januar–juni 2024." },
                    nynorsk { +"Tabellen nedanfor viser dei månadlege beløpa for perioden januar–juni 2024." }
                )
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
                        cell { text(bokmal { +"2 836" }, nynorsk { +"2 836" }) }
                        cell { text(bokmal { +"709" }, nynorsk { +"709" }) }
                        cell { text(bokmal { +"2 127" }, nynorsk { +"2 127" }) }
                    }
                    row {
                        cell { text(bokmal { +"Sum" }, nynorsk { +"Sum" }, FontType.BOLD) }
                        cell { text(bokmal { +"19 342" }, nynorsk { +"19 342" }, FontType.BOLD) }
                        cell { text(bokmal { +"4 835" }, nynorsk { +"4 835" }, FontType.BOLD) }
                        cell { text(bokmal { +"14 507" }, nynorsk { +"14 507" }, FontType.BOLD) }
                    }
                }
            }

            // ── TITLE3 + tekst med newline ────────────────────────────────────────────
            title2 {
                text(bokmal { +"Merknader til beregningen" }, nynorsk { +"Merknader til utrekninga" })
            }

            title3 { text(bokmal { +"Trygdetid" }, nynorsk { +"Trygdetid" }) }
            paragraph {
                text(
                    bokmal { +"Du er registrert med 40 års trygdetid i Norge, som gir full grunnpensjon." },
                    nynorsk { +"Du er registrert med 40 års trygdetid i Noreg, som gjev full grunnpensjon." }
                )

            }
            title3 { text(bokmal { +"Pensjonsgivende inntekt" }, nynorsk { +"Pensjonsgivande inntekt" }) }
            paragraph {
                text(
                    bokmal { +"Gjennomsnittlig pensjonsgivende inntekt de 20 beste inntektsårene er lagt til grunn." },
                    nynorsk { +"Gjennomsnittleg pensjonsgivande inntekt dei 20 beste inntektsåra er lagt til grunn." }
                )

            }


            // ── OutlinePhrase ────────────────────────────────────────────────────────
            includePhrase(OutlinePhraseTest(datoInnvilget, pensjonInnvilget))

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

        }
        includeAttachment(testVedlegg, TestVedleggDto("test1", "test2").expr())
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
                val dato = datoInnvilget.format()
                text(
                    bokmal { +"Du har fått innvilget pensjon fra " + dato + "." },
                    nynorsk { +"Du har fått innvilget pensjon fra " + dato + "." },
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


data class TestVedleggDto(val testVerdi1: String, val testVerdi2: String) : VedleggData

@TemplateModelHelpers
val testVedlegg = createAttachment<LangBokmalNynorsk, TestVedleggDto>(
    title = {
        text(
            bokmal { +"Egenerklæring og tilleggsinformasjon" },
            nynorsk { +"Eigenerklæring og tilleggsinformasjon" },
        )
    },
    includeSakspart = true
) {
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
/*
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
            }
        )
        formText(
            size = Size.SHORT,
            prompt = {
                text(bokmal { +"Referanse (" + testVerdi2 + "):" }, nynorsk { +"Referanse (" + testVerdi2 + "):" })
            }
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
    }*/
}


val lipsums = listOf(
    "Etiam porta turpis et eros ullamcorper sodales. Cras et eleifend leo. Aenean vehicula nunc sit amet quam tincidunt, id aliquam arcu cursus. Morbi non imperdiet augue, nec placerat tellus. Aenean imperdiet auctor porta. Morbi in lacus nec purus commodo sodales non in ligula. Praesent euismod mollis elit, mollis finibus massa pretium eget. Fusce mollis tempus nisl vitae suscipit. Morbi in elementum tortor. Aenean varius odio non sem convallis, at venenatis arcu ullamcorper. Duis porttitor nulla facilisis mattis porttitor. Quisque pharetra hendrerit tellus, id consequat sapien maximus sit amet. Vestibulum vehicula pellentesque nulla, sit amet egestas felis pellentesque ac. Ut viverra vel magna eget mollis. Aliquam dictum aliquet tortor vitae efficitur. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
    "Fusce sed diam ac dui luctus venenatis sit amet sit amet sapien. Praesent non congue metus. Morbi rutrum pellentesque rhoncus. Duis semper dictum rutrum. Curabitur iaculis, magna sit amet varius dignissim, sapien augue pellentesque mi, id malesuada arcu risus et justo. Vivamus fermentum neque ac purus faucibus, non viverra massa pulvinar. Suspendisse ornare erat hendrerit, condimentum lorem vel, fringilla dui. Donec non tortor dignissim, ornare metus nec, malesuada nulla. Nulla convallis arcu ultricies augue consectetur, eu mattis neque tristique. Sed suscipit lacus vel risus lobortis, sed dignissim orci posuere. Aenean ut magna eget tellus viverra tincidunt non quis lectus. Donec elementum molestie tellus, tincidunt tincidunt urna tincidunt in. Nunc eget lorem non enim rhoncus consequat. Vivamus laoreet semper facilisis.",
    "Donec consequat nibh vitae faucibus blandit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut vel fermentum urna. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla dictum justo in egestas venenatis. Sed vel eros quis turpis blandit accumsan. Nulla sed velit euismod, aliquet nibh eu, finibus mauris.",
    "Cras efficitur tincidunt eleifend. Vestibulum auctor diam in tortor tincidunt dapibus. Nulla id nunc luctus, sodales tellus sed, pulvinar turpis. Etiam vel vulputate ex, nec efficitur nunc. Morbi vel maximus quam. Pellentesque id iaculis ipsum. Sed facilisis dui et arcu aliquam rhoncus. Nullam id ex dictum, porttitor elit sed, hendrerit risus. Duis convallis sed magna sit amet porttitor. Phasellus neque ligula, cursus id tristique et, facilisis in magna. Sed bibendum tempus neque.",
    "Suspendisse faucibus lorem ante, vel gravida enim dignissim sit amet. Pellentesque a tempor ligula. Nunc eu nisl massa. Nullam ut semper magna. Aliquam condimentum massa dui. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ac ex pretium, pretium mi ut, congue nulla. Integer eget neque id orci tristique hendrerit quis quis quam. Praesent quis nunc nunc. Cras luctus maximus mi quis dapibus. Nullam ultricies bibendum velit quis pulvinar. Cras a turpis elit. Mauris placerat rhoncus metus ac varius. Etiam rhoncus mi sit amet sollicitudin posuere. Vivamus scelerisque, ex vitae imperdiet luctus, metus tortor fringilla lacus, non facilisis dui tortor vel ante. Cras efficitur lacus felis, in imperdiet enim feugiat a.",
    "In suscipit, velit convallis feugiat semper, dolor urna vehicula arcu, ornare hendrerit quam ex non dui. Donec consectetur, urna et faucibus viverra, mi est consectetur enim, ac luctus lectus eros nec mauris. Fusce quam sapien, elementum ut neque a, tempus ultrices nunc. Integer id aliquet lacus. Nam rhoncus ligula et diam gravida, eget auctor urna vehicula. Phasellus vel dignissim metus, sed consectetur risus. Proin in diam in sapien condimentum tristique. Duis ultrices est eu nisl scelerisque porta. Sed vulputate quis felis id semper. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Etiam eleifend, ante in porta aliquam, velit sem faucibus libero, quis rhoncus elit libero nec diam. Suspendisse vel faucibus orci.",
    "Etiam luctus, velit eu semper cursus, orci justo porttitor leo, sed tincidunt purus nunc commodo quam. Integer sed varius sapien. Vestibulum felis dolor, tristique eget mauris id, semper fermentum tortor. Ut imperdiet auctor sodales. Proin ut tincidunt nulla, at lobortis nulla. Vestibulum quis justo vitae mi tempor ultricies a sit amet dolor. Praesent vitae metus viverra, consectetur purus ut, consequat mi. Integer vitae tempus eros. Curabitur orci nisi, varius eget orci quis, pulvinar rhoncus quam. Fusce non dui id augue semper vulputate. Morbi aliquet blandit ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur tempus massa dui, quis malesuada enim suscipit sed.",
    "Aenean vel mauris massa. Aliquam vel tortor in mi posuere suscipit et vitae mauris. Aenean tortor nulla, tempus quis suscipit vel, ornare non orci. Phasellus non semper tellus. Fusce finibus neque eu accumsan efficitur. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Cras dictum euismod volutpat. Praesent quis rutrum massa. Nullam iaculis, erat vel sodales fringilla, leo tellus ullamcorper risus, a bibendum nulla nisl in dui. Duis ac nisi felis.",
    "Ut congue ac orci a porttitor. Nullam ut ex erat. Nullam at venenatis augue. Morbi luctus fringilla ex, ut ultrices enim auctor ac. Maecenas eget blandit justo, quis euismod nunc. Cras venenatis nunc non euismod hendrerit. Suspendisse facilisis mi dui, in ultricies tellus dictum ut. Nulla id elit eu arcu viverra iaculis non at est. Phasellus leo nunc, posuere vitae est vel, sagittis lobortis tellus. Vivamus viverra eu tortor at placerat. Sed vehicula congue magna vitae ultrices. Nulla facilisi. Vivamus bibendum metus vitae lacus consequat, eget ullamcorper sem blandit.",
    "Fusce tincidunt porta orci, ac pretium erat mattis vel. Suspendisse quis turpis sit amet enim lacinia interdum at et dui. Nulla fringilla bibendum magna sed interdum. Donec congue orci non malesuada dignissim. Mauris pulvinar nibh et varius fermentum. Proin efficitur bibendum viverra. Nam sollicitudin euismod tempor. Sed dapibus ac dolor ut dignissim. Quisque sagittis, sem eget suscipit venenatis, odio elit finibus eros, vel faucibus nibh libero vitae nisi.",
    "Nulla in bibendum tellus. Praesent sit amet luctus ligula. Nulla facilisi. Ut laoreet arcu dignissim arcu dignissim, efficitur porta risus lobortis. Suspendisse elit nunc, tempus id maximus nec, faucibus non ipsum. Cras semper neque quis est facilisis, sed egestas neque accumsan. Nulla facilisi.",
    "Aliquam erat volutpat. Integer ac euismod augue. Phasellus venenatis lorem in ipsum vestibulum volutpat. Proin eu sapien id lectus egestas placerat vel quis dui. Fusce dapibus risus urna, eu feugiat orci auctor id. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porttitor nisl non mi finibus, ut malesuada enim congue. Nullam auctor enim sollicitudin orci viverra tempor. Etiam placerat volutpat nisl vitae vehicula. Donec quis rutrum dui. Nunc congue, sapien quis pharetra gravida, nisl tortor vehicula arcu, a mattis leo turpis ac massa. Duis sagittis augue in arcu sollicitudin, sit amet dapibus turpis dignissim. Sed id efficitur risus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut cursus eget augue vitae faucibus.",
    "Nullam rhoncus faucibus nisi, at tristique ante fermentum et. Donec hendrerit, sem eu euismod convallis, nisi risus dapibus quam, id egestas metus justo ac tortor. Nulla sagittis mauris eleifend odio efficitur, gravida rutrum turpis aliquet. Sed a turpis sed ex molestie viverra. Quisque maximus tincidunt dui, ut faucibus quam sollicitudin ut. Mauris nec rhoncus ipsum. Fusce in enim ac risus tincidunt interdum et vitae mauris. Aliquam non sodales elit, sit amet pharetra lectus. Nulla facilisi. Quisque vitae nisl eu nulla eleifend sagittis. Praesent rutrum elit nec est viverra, a feugiat nibh dignissim. Donec sed scelerisque leo. Donec sed pulvinar eros, nec mollis nunc. Curabitur ac erat et justo porttitor auctor. Donec sed condimentum dui, at sodales magna. Vivamus quis lacinia risus.",
    "Sed sed nunc vitae nunc convallis placerat quis non quam. Sed vulputate augue nulla, in finibus tortor consectetur in. Vestibulum mollis sagittis feugiat. Donec a interdum diam, nec pretium justo. Fusce congue lacus ac euismod rutrum. Vivamus nulla nisl, ornare vel tempor eget, porta eget dui. Praesent vulputate dui vitae venenatis porttitor. Sed aliquam ligula libero, id rhoncus mauris pharetra vel. Donec et pulvinar ante. Curabitur id malesuada leo. Vestibulum vel augue vehicula, rutrum magna id, consectetur erat. Maecenas condimentum felis varius, eleifend ex ac, interdum augue. Nam quis ex sed urna ornare suscipit sed quis ante. Curabitur vitae est pharetra, condimentum purus et, mollis ante. Morbi id feugiat dui, ullamcorper fermentum ligula.",
    "Etiam dictum molestie nunc eget vehicula. Suspendisse et interdum est. Ut sed luctus massa. Phasellus aliquet, felis vel tempus aliquet, velit est vestibulum orci, nec hendrerit nunc odio vitae magna. In mi est, lobortis nec suscipit in, auctor vel nisi. Duis ante neque, dictum non consequat non, ornare sed mauris. Quisque eu lorem convallis, sodales magna eget, dictum ante. Aliquam ac pharetra tortor. Mauris dictum gravida vestibulum. Duis lacinia lacinia sem a ultrices. Phasellus posuere fermentum ex, vel iaculis diam lobortis eget. Integer et efficitur ipsum, id hendrerit nulla. Phasellus dapibus, lacus et euismod rutrum, mi ante convallis massa, sit amet fermentum ante ante sed orci. Aliquam sapien dolor, imperdiet blandit odio nec, laoreet euismod nisl.",
    "Pellentesque at est tempor, maximus velit vitae, ullamcorper felis. Quisque non sodales turpis. Vestibulum tincidunt et orci vel auctor. Pellentesque eu consequat est. Nulla condimentum venenatis ex, non vehicula sem pulvinar et. Quisque aliquet semper eros. Donec dictum nibh in libero molestie ultrices. Sed quis libero neque. Integer vehicula quam at sollicitudin dignissim. Pellentesque maximus arcu dolor, id gravida elit hendrerit quis.",
    "Donec sed rutrum metus. Sed at est a orci iaculis tristique. Ut non bibendum nisl. Integer ante eros, suscipit sit amet nisl ut, ultricies faucibus ante. Vivamus hendrerit felis arcu, et convallis velit mattis porta. Curabitur varius, leo et lacinia pellentesque, eros massa tempor felis, at gravida leo augue et nulla. Donec efficitur quam ac elit sagittis maximus. Duis in rhoncus nunc, eu aliquet risus. Curabitur posuere mattis mi, sed placerat justo pellentesque vel. Suspendisse sed nulla sapien. Praesent feugiat neque in diam pharetra mattis. Nullam laoreet finibus arcu sit amet eleifend. In mollis dictum ultricies. Donec id justo a sapien tempor scelerisque. Ut varius, sapien vitae semper commodo, velit leo mollis orci, id efficitur velit augue vel neque. In rutrum laoreet semper.",
    "Nulla dignissim non ante sit amet mattis. Sed condimentum tincidunt ligula sed sodales. Quisque volutpat et nisl at molestie. Curabitur consectetur, massa efficitur tempus euismod, enim velit lacinia nulla, eu vehicula purus justo et mauris. Curabitur in dui tempor, porttitor lorem vitae, tincidunt ligula. Maecenas ultrices quam nec mattis hendrerit. Aenean pretium aliquet ipsum, sed iaculis felis tristique dapibus.",
    "Ut aliquam magna non elit molestie feugiat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut blandit, augue in tristique accumsan, arcu felis ornare elit, et luctus nulla odio in libero. Vestibulum commodo eleifend sapien. Vivamus tempor ac nulla non pulvinar. Nulla pulvinar at odio ultrices semper. Donec tempor mauris eu tortor rhoncus lobortis. Fusce egestas velit congue massa eleifend blandit. Maecenas egestas fringilla urna a dapibus. Nulla id leo a diam rhoncus tempus.",
)