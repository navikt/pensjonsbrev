package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.maler.fraser.ufoer.InnledningVarselSaksbehandlingstid
import no.nav.pensjon.brev.maler.fraser.ufoer.SaksbehandlingstidUfoere
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

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
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
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
            includePhrase(
                InnledningVarselSaksbehandlingstid(
                    mottattDatoMinus2Dager =
                )
            )

            includePhrase(
                SaksbehandlingstidUfoere(
                    utvidetBehandlingstid =
                )
            )

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)

            includePhrase(Ufoeretrygd.HarDuSpoersmaalUfoeretrygd)
        }
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere)
    }
}
