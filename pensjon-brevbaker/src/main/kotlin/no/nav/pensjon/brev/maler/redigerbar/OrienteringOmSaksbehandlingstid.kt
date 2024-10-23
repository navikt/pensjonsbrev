package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDtoSelectors.SaksbehandlerValgSelectors.mottattSoeknad
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDtoSelectors.SaksbehandlerValgSelectors.soeknadOversendesTilUtlandet
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object OrienteringOmSaksbehandlingstid : RedigerbarTemplate<OrienteringOmSaksbehandlingstidDto> {

    override val kode = Brevkode.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID
    override val kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OrienteringOmSaksbehandlingstidDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering om saksbehandlingstid (uføretrygd)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            //[PE_UP_07_105_overskrift]
            text(
                Bokmal to "Orientering om svartid",
                Nynorsk to "Orientering om svartid",
            )
        }

        outline {
            //[PE_UP_07_105_TB3018-3021,TB124]

            paragraph {
                val mottattDato = saksbehandlerValg.mottattSoeknad.format()
                textExpr(
                    Bokmal to "Vi har ".expr() + mottattDato + " mottatt søknaden din om uføretrygd.",
                    Nynorsk to "Vi har ".expr() + mottattDato + " mottatt søknaden din om uførepensjon.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Søknaden vil bli behandlet så snart som mulig og senest innen 4 måneder. ",
                    Nynorsk to "Søknaden vil bli behandla så snart som mogleg og seinast innan 4 månader. ",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis søknaden ikke blir avgjort i løpet av denne tiden, vil du høre nærmere fra oss. ",
                    Nynorsk to "Viss søknaden ikkje vert avgjort i løpet av denne tida, vil du høyre nærmare frå oss. ",
                )
            }

            showIf(saksbehandlerValg.soeknadOversendesTilUtlandet) {
                paragraph {
                    text(
                        Bokmal to "Søknaden din vil også bli oversendt utlandet fordi du har opplyst at du har bodd/arbeidet i et land Norge har trygdeavtale med.",
                        Nynorsk to "Søknaden din vil også bli send til utlandet fordi du har opplyst at du har budd/arbeidd i eit land Noreg har trygdeavtale med. ",
                    )
                }
            }

            title1 {
                text(
                    Bokmal to "Meld fra om endringer",
                    Nynorsk to "Meld frå om endringar",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av søknaden din. Det kan være endringer i medisinske forhold, arbeid, inntekt, sivilstand og lignende. ",
                    Nynorsk to "Vi ber om at du held oss orientert om forhold som kan ha noko å seie for avgjerda av søknaden din. Det kan vere endringar i medisinske forhold, arbeid, inntekt, sivilstand og liknande. ",
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(Constants.UFOERETRYGD_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }
    }
}