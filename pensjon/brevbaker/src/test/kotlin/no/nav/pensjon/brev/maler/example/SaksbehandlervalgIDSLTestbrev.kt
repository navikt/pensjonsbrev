package no.nav.pensjon.brev.maler.example

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.SimpleSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

/**
 * Testmal som demonstrerer [SaksbehandlervalgIDSL] — den flytende saksbehandlervalg-DSL-en (`saksbehandlervalg(id, displayText).bool()/.int()/.text()/.enum()`)
 * — i motsetning til det vanlige mønsteret med en håndskrevet, nøstet `Saksbehandlervalg`-dataklasse.
 * Dekker alle fire typene, både med og uten default-verdi, samt et enkelt fagsystem-Dto (`PesysData`) med to felt.
 *
 * Merk: **ingen** `@TemplateModelHelpers` her. [SaksbehandlervalgIDSL] er et `Map`, og KSP-selector-generatoren
 * klarer ikke å generere selectors for et map-typet felt — legger man `@TemplateModelHelpers` på denne malen feiler
 * *hele* den genererte selector-filen (også selectors for `pesysData`), ikke bare selectoren for saksbehandlerValg.
 * `pesysData`-selectorene under er derfor skrevet for hånd, etter samme mønster som KSP normalt genererer.
 */
object SaksbehandlervalgIDSLTestbrev : RedigerbarTemplate<SaksbehandlervalgIDSLTestbrevDto> {

    @OptIn(InternKonstruktoer::class)
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = Sakstype.all
    override val kode: Brevkode.Redigerbart = SaksbehandlervalgIDSLTestbrevBrevkode.TESTBREV_SAKSBEHANDLERVALG_IDSL

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Testbrev saksbehandlervalg (IDSL)",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
        letterDataType = SaksbehandlervalgIDSLTestbrevDto::class,
    ) {
        // Bool har alltid en default-verdi (false om ikke annet er oppgitt), og er derfor aldri nullable.
        val bool = saksbehandlervalg("bool", "Boolsk valg").bool()
        val boolMedDefault = saksbehandlervalg("boolMedDefault", "Boolsk valg med default").bool(default = true)

        val intUtenDefault = saksbehandlervalg("intUtenDefault", "Tall uten default").int()
        val intMedDefault = saksbehandlervalg("intMedDefault", "Tall med default").int(default = 42)

        val tekstUtenDefault = saksbehandlervalg("tekstUtenDefault", "Tekst uten default").text()
        val tekstMedDefault = saksbehandlervalg("tekstMedDefault", "Tekst med default").text(default = "standardtekst")

        val enumUtenDefault = saksbehandlervalg("enumUtenDefault", "Enum uten default").enum<SaksbehandlervalgIDSLTestValg>()
        val enumMedDefault = saksbehandlervalg("enumMedDefault", "Enum med default").enum(default = SaksbehandlervalgIDSLTestValg.ALTERNATIV_EN)

        title {
            text(
                bokmal { +"Testbrev saksbehandlervalg (IDSL)" },
                nynorsk { +"Testbrev saksbehandlarval (IDSL)" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Testbrev for saksbehandlervalg via SaksbehandlervalgIDSL" },
                    nynorsk { +"Testbrev for saksbehandlarval via SaksbehandlervalgIDSL" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Saksnummer: " + pesysData.saksnummer + ", opprettet " + pesysData.opprettet.format() },
                    nynorsk { +"Saksnummer: " + pesysData.saksnummer + ", oppretta " + pesysData.opprettet.format() },
                )
            }
            showIf(bool) {
                paragraph { text(bokmal { +"bool = true" }, nynorsk { +"bool = true" }) }
            }
            showIf(boolMedDefault) {
                paragraph { text(bokmal { +"boolMedDefault = true" }, nynorsk { +"boolMedDefault = true" }) }
            }
            ifNotNull(intUtenDefault) {
                paragraph {
                    text(
                        bokmal { +"intUtenDefault = " + it.format() },
                        nynorsk { +"intUtenDefault = " + it.format() },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"intMedDefault = " + intMedDefault.format() },
                    nynorsk { +"intMedDefault = " + intMedDefault.format() },
                )
            }
            ifNotNull(tekstUtenDefault) {
                paragraph {
                    text(bokmal { +"tekstUtenDefault = " + it }, nynorsk { +"tekstUtenDefault = " + it })
                }
            }
            paragraph {
                text(bokmal { +"tekstMedDefault = " + tekstMedDefault }, nynorsk { +"tekstMedDefault = " + tekstMedDefault })
            }
            ifNotNull(enumUtenDefault) {
                paragraph {
                    showIf(enumUtenDefault.equalTo(SaksbehandlervalgIDSLTestValg.ALTERNATIV_EN)) {
                        text(bokmal { +"enumUtenDefault = ALTERNATIV_EN" }, nynorsk { +"enumUtenDefault = ALTERNATIV_EN" })
                    }.orShowIf(enumUtenDefault.equalTo(SaksbehandlervalgIDSLTestValg.ALTERNATIV_TO)) {
                        text(bokmal { +"enumUtenDefault = ALTERNATIV_TO" }, nynorsk { +"enumUtenDefault = ALTERNATIV_TO" })
                    }
                }
            }
            paragraph {
                showIf(enumMedDefault.equalTo(SaksbehandlervalgIDSLTestValg.ALTERNATIV_EN)) {
                    text(bokmal { +"enumMedDefault = ALTERNATIV_EN" }, nynorsk { +"enumMedDefault = ALTERNATIV_EN" })
                }.orShowIf(enumMedDefault.equalTo(SaksbehandlervalgIDSLTestValg.ALTERNATIV_TO)) {
                    text(bokmal { +"enumMedDefault = ALTERNATIV_TO" }, nynorsk { +"enumMedDefault = ALTERNATIV_TO" })
                }
            }
        }
    }
}

enum class SaksbehandlervalgIDSLTestbrevBrevkode : Brevkode.Redigerbart {
    TESTBREV_SAKSBEHANDLERVALG_IDSL;

    override fun kode() = name
}

enum class SaksbehandlervalgIDSLTestValg(override val displayText: String) : SaksbehandlerValgEnum {
    ALTERNATIV_EN("Alternativ en"),
    ALTERNATIV_TO("Alternativ to"),
}

data class SaksbehandlervalgIDSLTestbrevDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdataMedSaksbehandlerValg<SaksbehandlervalgIDSLTestbrevDto.PesysData> {
    data class PesysData(
        val saksnummer: String,
        val opprettet: LocalDate,
    ) : FagsystemBrevdata
}

// Håndskrevne selectors for pesysData — se merknaden ovenfor for hvorfor KSP (@TemplateModelHelpers) ikke kan brukes her.
private val pesysDataSelector = SimpleSelector(
    className = SaksbehandlervalgIDSLTestbrevDto::class.qualifiedName!!,
    propertyName = "pesysData",
    propertyType = SaksbehandlervalgIDSLTestbrevDto.PesysData::class.qualifiedName!!,
    selector = SaksbehandlervalgIDSLTestbrevDto::pesysData,
)

@OptIn(InternKonstruktoer::class)
private val TemplateGlobalScope<SaksbehandlervalgIDSLTestbrevDto>.pesysData: Expression<SaksbehandlervalgIDSLTestbrevDto.PesysData>
    get() = Expression.UnaryInvoke(Expression.FromScope.Argument(), UnaryOperation.Select(pesysDataSelector))

private val saksnummerSelector = SimpleSelector(
    className = SaksbehandlervalgIDSLTestbrevDto.PesysData::class.qualifiedName!!,
    propertyName = "saksnummer",
    propertyType = String::class.qualifiedName!!,
    selector = SaksbehandlervalgIDSLTestbrevDto.PesysData::saksnummer,
)

private val Expression<SaksbehandlervalgIDSLTestbrevDto.PesysData>.saksnummer: Expression<String>
    get() = Expression.UnaryInvoke(this, UnaryOperation.Select(saksnummerSelector))

private val opprettetSelector = SimpleSelector(
    className = SaksbehandlervalgIDSLTestbrevDto.PesysData::class.qualifiedName!!,
    propertyName = "opprettet",
    propertyType = LocalDate::class.qualifiedName!!,
    selector = SaksbehandlervalgIDSLTestbrevDto.PesysData::opprettet,
)

private val Expression<SaksbehandlervalgIDSLTestbrevDto.PesysData>.opprettet: Expression<LocalDate>
    get() = Expression.UnaryInvoke(this, UnaryOperation.Select(opprettetSelector))
