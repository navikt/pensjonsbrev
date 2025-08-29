package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDtoSelectors.dagensDatoMinus2Dager
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SAKSBEHANDLINGSTID_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERETRYGD_ENDRING_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Gammel brevkode: PE_UT_06_200
@TemplateModelHelpers
object VarselSaksbehandlingstidAutoV2 : AutobrevTemplate<VarselSaksbehandlingstidAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = VarselSaksbehandlingstidAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Automatisk varsel om saksbehandlingstid",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { + "Nav har mottatt søknaden din om uføretrygd" },
                nynorsk { + "Nav har motteke søknaden din om uføretrygd" },
                english { + "Nav has received your application for disability benefit" }
            )
        }
        outline {
            // TBU3020
            paragraph {
                val mottattDato = dagensDatoMinus2Dager.format()
                text(
                    bokmal { + "Vi viser til søknaden din om uføretrygd som vi mottok " + mottattDato + "." },
                    nynorsk { + "Vi viser til søknaden din om uføretrygd som vi tok imot " + mottattDato + "." },
                    english { + "We refer to your application for disability benefit that we received " + mottattDato + "." }
                )
            }
            // TBU3015
            paragraph {
                text(
                    bokmal { + "Søknaden din blir behandlet så snart som mulig. Når søknaden er ferdig behandlet, får du et svar fra oss på " + quoted("Min side") +" på $NAV_URL. Du kan sjekke saksbehandlingstidene på $SAKSBEHANDLINGSTID_URL." },
                    nynorsk { + "Søknaden din vert handsama så snart som mogleg. Når søknaden er ferdig behandla, får du eit svar frå oss på " + quoted("Mi side") +" på $NAV_URL. Du kan sjekke saksbehandlingstidene på $SAKSBEHANDLINGSTID_URL." },
                    english { + "Your application will be processed as soon as possible. When your application has been processed, you will receive a response from us on " + quoted("My Page") +" at $NAV_URL. You can check the processing times at $SAKSBEHANDLINGSTID_URL." }
                )
            }

            title1 {
                text(
                    bokmal { + "Du må melde fra om endringer" },
                    nynorsk { + "Du må melde frå om endringar" },
                    english { + "You must notify any changes" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du må melde fra om endringer som kan påvirke søknaden din. Det kan være endringer som gjelder helse, arbeidssituasjon, inntekt, sivilstatus eller at du flytter til et annet land." },
                    nynorsk { + "Du må melde frå om endringar som kan påverke søknaden din. Det kan vere endringar som gjeld helse, arbeidssituasjon, inntekt, sivilstatus eller at du flyttar til eit anna land." },
                    english { + "You must notify us of any changes that may impact your application. These changes might relate to your health, employment, income, marital status, or moving abroad." }
                )
            }
            paragraph {
                text(
                    bokmal { + "For informasjon om hvordan du melder fra om endringer se: $UFOERETRYGD_ENDRING_URL" },
                    nynorsk { + "For informasjon om korleis du melder frå om endringar, sjå: $UFOERETRYGD_ENDRING_URL" },
                    english { + "For information on how to report changes, see: $UFOERETRYGD_ENDRING_URL" }
                )
            }

            title1 {
                text(
                    bokmal { + "Du har rett til innsyn" },
                    nynorsk { + "Du har rett til innsyn" },
                    english { + "You have the right to access your file" },
                )
            }

            paragraph {
                text(
                    bokmal { + "Du har rett til å se dokumentene i saken din. Du kan logge deg inn via $NAV_URL for å se dokumenter i saken din." },
                    nynorsk { + "Du har rett til å sjå dokumenta i saka di. Du kan logge deg inn via $NAV_URL for å sjå dokumenta i saka di." },
                    english { + "You are entitled to see your case documents. You can log in via $NAV_URL to view documents related to your case." }
                )
            }

            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}
