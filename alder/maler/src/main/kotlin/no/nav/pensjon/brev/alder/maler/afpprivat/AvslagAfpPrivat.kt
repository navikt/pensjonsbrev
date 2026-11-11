package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afpprivat.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.Vedtak
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageAfpPrivat
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggFolketrygden
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder.AlltidValgbareVedlegg.UTTAKSSKJEMA
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatDto
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatDto.Begrunnelse
import no.nav.pensjon.brev.alder.model.afpprivat.selectors.avslagAfpPrivatDto.pesysData.*
import no.nav.pensjon.brev.alder.model.afpprivat.selectors.avslagAfpPrivatDto.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — avslag på AFP i privat sektor (redigerbar).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_112`. Begrunnelsen for avslaget
 * kommer fra vilkårsvedtaket i PESYS (rådatastrengen
 * `PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse[1]`) og er
 * representert som [Begrunnelse]. Brevkroppen viser standardteksten for
 * den begrunnelsen pluss tilhørende hjemmelshenvisning til
 * AFP-tilskottsloven.
 */
@TemplateModelHelpers
object AvslagAfpPrivat : RedigerbarTemplate<AvslagAfpPrivatDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_AVSLAG
    override val valgbareVedlegg = setOf(UTTAKSSKJEMA, SKJEMA_FOR_BANKOPPLYSNINGER)

    override val featureToggle = FeatureToggles.avslagAfpPrivat.toggle

    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP_PRIVAT)

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
                bokmal { +"Nav har avslått søknaden din om avtalefestet pensjon (AFP) i privat sektor" },
                nynorsk { +"Nav har avslått søknaden din om avtalefesta pensjon (AFP) i privat sektor  " },
                english { +"Nav has declined your application for contractual pension (AFP) in the private sector. " },
            )
        }

        outline {
            includePhrase(
                AvslagAfpPrivatInnhold(
                begrunnelse = pesysData.begrunnelse,
                kravMottattDato = pesysData.kravMottattDato,
                )
            )
            includePhrase(HarDuSpoersmaal.afpPrivat)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageAfpPrivat)
    }
}
