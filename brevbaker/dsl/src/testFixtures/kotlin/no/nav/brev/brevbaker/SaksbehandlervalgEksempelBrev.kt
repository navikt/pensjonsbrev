package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Testmal som bruker alle typene saksbehandlervalg som finnes i [SaksbehandlerValgBuilder]:
 * bool, int, tekst og enum, både med og uten default-verdi (dvs. både ikke-nullable og nullable expressions).
 */
object SaksbehandlervalgEksempelBrev : RedigerbarTemplate<SaksbehandlervalgTestDto> {
    @OptIn(InternKonstruktoer::class)
    override val kategori = TemplateDescription.Redigerbar.Brevkategori("Innhente opplysninger")
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(object : ISakstype {
        override val kode = "ALDER"
    })
    override val kode = SaksbehandlervalgTestBrevkode.TESTBREV_SAKSBEHANDLERVALG

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Testbrev saksbehandlervalg",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
        letterDataType = SaksbehandlervalgTestDto::class
    ) {
        // Bool har alltid en default-verdi (false om ikke annet er oppgitt), og er derfor aldri nullable.
        val bool = saksbehandlervalg("bool", "Boolsk valg").bool()

        val intUtenDefault = saksbehandlervalg("intUtenDefault", "Tall uten default").int()

        val tekstUtenDefault = saksbehandlervalg("tekstUtenDefault", "Tekst uten default").text()

        val enumUtenDefault = saksbehandlervalg("enumUtenDefault", "Enum uten default").enum<TestValgEnum>()

        title { text(bokmal { +"Test-tittel" }) }
        outline {
            paragraph {
                text(bokmal { +"Testbrev for saksbehandlervalg" })
            }
            showIf(bool) {
                paragraph {
                    text(bokmal { +"bool = true" })
                }
            }
            ifNotNull(intUtenDefault) {
                paragraph {
                    text(bokmal { +"intUtenDefault = " + it.format() })
                }
            }
            ifNotNull(tekstUtenDefault) {
                paragraph {
                    text(bokmal { +"tekstUtenDefault = " + it })
                }
            }
            ifNotNull(enumUtenDefault) {
                paragraph {
                    showIf(enumUtenDefault.equalTo(TestValgEnum.ALTERNATIV_EN)) {
                        text(bokmal { +"enumUtenDefault = ALTERNATIV_EN" })
                    }.orShowIf(enumUtenDefault.equalTo(TestValgEnum.ALTERNATIV_TO)) {
                        text(bokmal { +"enumUtenDefault = ALTERNATIV_TO" })
                    }
                }
            }
        }
    }
}

enum class SaksbehandlervalgTestBrevkode : Brevkode.Redigerbart {
    TESTBREV_SAKSBEHANDLERVALG;

    override fun kode() = name
}

enum class TestValgEnum(override val displayText: String) : SaksbehandlerValgEnum {
    ALTERNATIV_EN("Alternativ en"),
    ALTERNATIV_TO("Alternativ to"),
}

data class SaksbehandlervalgTestDto(
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : BrevdataMedSaksbehandlerValg<EmptyFagsystemdata>
