package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.afpprivat.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageAfpPrivat
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggFolketrygden
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.selectors.avslagAfpPrivatAutoDto.*
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatDto.Begrunnelse
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Auto-vedtak — avslag på AFP i privat sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_116`. Dette er autobrev-varianten
 * av den redigerbare malen [AvslagAfpPrivat] (PE_AF_04_112) og bruker samme
 * [Begrunnelse]-enum. Originalen har kun bokmål og nynorsk.
 */
@TemplateModelHelpers
object AvslagAfpPrivatAuto : AutobrevTemplate<AvslagAfpPrivatAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_PRIVAT_AVSLAG_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Nav har avslått søknaden din om avtalefestet pensjon (AFP) i privat sektor  " },
                nynorsk { +"Nav har avslått søknaden din om avtalefesta pensjon (AFP) i privat sektor  " },
                english {+"Nav has declined your application for contractual pension (AFP) in the private sector. " },
            )
        }

        outline {
            includePhrase(
                AvslagAfpPrivatInnhold(
                    begrunnelse = begrunnelse,
                    kravMottattDato = kravMottattDato,
                )
            )
            includePhrase(HarDuSpoersmaal.afpPrivat)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageAfpPrivat)
    }
}
