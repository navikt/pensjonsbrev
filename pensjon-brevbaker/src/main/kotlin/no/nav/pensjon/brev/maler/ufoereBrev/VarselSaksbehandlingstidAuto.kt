package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDtoSelectors.mottattDatoMinus2Dager
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDtoSelectors.utvidetBehandlingstid
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselSaksbehandlingstidAuto : AutobrevTemplate<VarselSaksbehandlingstidAutoDto> {

    override val kode = Brevkode.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO

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
                Bokmal to "NAV har mottatt søknaden din om uføretrygd",
                Nynorsk to "NAV har motteke søknaden din om uføretrygd",
                English to "NAV has received your application for disability benefit"
            )
        }
        outline {
            // TBU3020
            paragraph {
                val mottattDato = mottattDatoMinus2Dager.format()
                textExpr(
                    Bokmal to "Vi viser til søknaden din om uføretrygd som vi mottok ".expr() + mottattDato + ".",
                    Nynorsk to "Vi viser til søknaden din om uføretrygd som vi tok imot ".expr() + mottattDato + ".",
                    English to "We refer to your application for disability benefit that we received ".expr() + mottattDato + "."
                )
            }
            // TBU3015
            paragraph {
                textExpr(
                    Bokmal to "Søknaden din blir behandlet så snart som mulig, og senest innen ".expr()
                            + ifElse(utvidetBehandlingstid, ifFalse = "4", ifTrue = "12") + " måneder. "
                            + "Blir ikke saken din ferdigbehandlet innen denne fristen, vil vi gi deg beskjed om ny svartid.",
                    Nynorsk to "Søknaden din vert handsama så snart som mogleg, og seinast innan ".expr()
                            + ifElse(utvidetBehandlingstid, ifFalse = "4", ifTrue = "12") + " månader. "
                            + "Vert ikkje saka di handsama innan denne fristen, vil vi gje deg melding om ny svartid.",
                    English to "Your application will be processed as soon as possible, and no later than within ".expr()
                            + ifElse(utvidetBehandlingstid, ifFalse = "4", ifTrue = "12") + " months. "
                            + "If your case is not processed within this deadline, we will notify you of a new response time."
                )
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)

            includePhrase(Felles.RettTilInnsynPesys)

            includePhrase(Ufoeretrygd.HarDuSpoersmaalUfoeretrygd)
        }
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}
