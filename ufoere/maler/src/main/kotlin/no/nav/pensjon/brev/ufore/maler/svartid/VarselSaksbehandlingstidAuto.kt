package no.nav.pensjon.brev.ufore.maler.svartid

import no.nav.pensjon.brev.ufore.api.model.maler.svartid.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.enabled
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.svartid.selectors.varselSaksbehandlingstidAutoDto.dagensDatoMinus2Dager
import no.nav.pensjon.brev.ufore.api.model.maler.svartid.selectors.varselSaksbehandlingstidAutoDto.utvidetBehandlingstid
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.NAV_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.SAKSBEHANDLINGSTID_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.UFOERETRYGD_ENDRING_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselSaksbehandlingstidAuto : AutobrevTemplate<VarselSaksbehandlingstidAutoDto> {

    // PE_UT_06_200
    override val kode = Ufoerebrevkoder.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Automatisk varsel om saksbehandlingstid",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { + "Nav har mottatt søknaden din om uføretrygd" },
                nynorsk { + "Nav har motteke søknaden din om uføretrygd" },
            )
        }
        outline {
            // TBU3020
            paragraph {
                val mottattDato = dagensDatoMinus2Dager.format()
                text(
                    bokmal { + "Vi viser til søknaden din om uføretrygd som vi mottok " + mottattDato + "." },
                    nynorsk { + "Vi viser til søknaden din om uføretrygd som vi tok imot " + mottattDato + "." },
                )
            }
            // TBU3015
            paragraph {
                showIf(FeatureToggles.pl7231ForventetSvartid.toggle.expr().enabled()) {
                    text(
                        bokmal {
                            +"Søknaden din blir behandlet så snart som mulig. Når søknaden er ferdig behandlet, får du et svar fra oss på " + quoted(
                                "Min side"
                            ) + " på ${NAV_URL}. Du kan sjekke saksbehandlingstidene på ${SAKSBEHANDLINGSTID_URL}."
                        },
                        nynorsk {
                            +"Søknaden din vert handsama så snart som mogleg. Når søknaden er ferdig behandla, får du eit svar frå oss på " + quoted(
                                "Mi side"
                            ) + " på ${NAV_URL}. Du kan sjekke saksbehandlingstidene på $SAKSBEHANDLINGSTID_URL."
                        }
                    )
                } orShow {
                    text(
                        bokmal {
                            +"Søknaden din blir behandlet så snart som mulig, og senest innen "
                            + ifElse(utvidetBehandlingstid, ifFalse = "6", ifTrue = "20") + " måneder. "
                            + "Blir ikke saken din ferdigbehandlet innen denne fristen, vil vi gi deg beskjed om ny svartid." },
                        nynorsk {
                            +"Søknaden din vert handsama så snart som mogleg, og seinast innan "
                            + ifElse(utvidetBehandlingstid, ifFalse = "6", ifTrue = "20") + " månader. "
                            + "Vert ikkje saka di handsama innan denne fristen, vil vi gje deg melding om ny svartid." }
                    )
                }
            }

            title1 {
                text(
                    bokmal { + "Du må melde fra om endringer" },
                    nynorsk { + "Du må melde frå om endringar" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du må melde fra om endringer som kan påvirke søknaden din. Det kan være endringer som gjelder helse, arbeidssituasjon, inntekt, sivilstatus eller at du flytter til et annet land." },
                    nynorsk { + "Du må melde frå om endringar som kan påverke søknaden din. Det kan vere endringar som gjeld helse, arbeidssituasjon, inntekt, sivilstatus eller at du flyttar til eit anna land." }
                )
            }

            paragraph {
                text(
                    bokmal { + "For informasjon om hvordan du melder fra om endringer se: ${UFOERETRYGD_ENDRING_URL}" },
                    nynorsk { + "For informasjon om korleis du melder frå om endringar, sjå: $UFOERETRYGD_ENDRING_URL" }
                )
            }

            title1 {
                text(
                    bokmal { + "Du har rett til innsyn" },
                    nynorsk { + "Du har rett til innsyn" }
                )
            }

            paragraph {
                text(
                    bokmal { + "Du har rett til å se dokumentene i saken din. Du kan logge deg inn via $NAV_URL for å se dokumenter i saken din." },
                    nynorsk { + "Du har rett til å sjå dokumenta i saka di. Du kan logge deg inn via $NAV_URL for å sjå dokumenta i saka di." }
                )
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
