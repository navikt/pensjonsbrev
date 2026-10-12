package no.nav.pensjon.brev.ufore.maler.svartid

import no.nav.pensjon.brev.ufore.api.model.maler.svartid.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.svartid.selectors.varselSaksbehandlingstidAutoDto.dagensDatoMinus2Dager
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.NAV_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.SAKSBEHANDLINGSTID_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OrienteringOmSaksbehandlingstidAuto : AutobrevTemplate<VarselSaksbehandlingstidAutoDto> {

    // PE_UT_06_200
    override val kode = Ufoerebrevkoder.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering om saksbehandlingstid av søknad om uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Orientering om svartid" },
                nynorsk { +"Orientering om svartid" },
            )
        }
        outline {
            paragraph {
                val mottattDato = dagensDatoMinus2Dager.format()
                text(
                    bokmal { +"Vi viser til søknaden din om uføretrygd som vi mottok " + mottattDato + "." },
                    nynorsk { +"Vi viser til søknaden din om uføretrygd som vi tok imot " + mottattDato + "." },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Søknaden din blir behandlet så snart som mulig. Når søknaden er ferdig behandlet, får du et svar fra oss på " + quoted(
                            "Min side"
                        ) + " på ${NAV_URL}. Du kan sjekke saksbehandlingstidene på $SAKSBEHANDLINGSTID_URL."
                    },
                    nynorsk {
                        +"Søknaden din vert behandla så snart som mogleg. Når søknaden er ferdig behandla, får du eit svar frå oss på " + quoted(
                            "Mi side"
                        ) + " på $NAV_URL. Du kan sjekke saksbehandlingstidene på $SAKSBEHANDLINGSTID_URL."
                    },
                )
            }
            includePhrase(OrienteringOmSaksbehandlingstidFraser.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
