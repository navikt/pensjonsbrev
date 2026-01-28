package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakGodskrivingPensjonsopptjeningDto
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL_INNLOGGET
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// PE_IY_05_200_201_godskriving_omsorgspoeng

@TemplateModelHelpers
object VedtakGodskrivingPensjonsopptjening : RedigerbarTemplate<VedtakGodskrivingPensjonsopptjeningDto> {

    //override val featureToggle = FeatureToggles.vedtakGodskrivingPensjonsopptjening.toggle
    override val kode = Pesysbrevkoder.Redigerbar.PE_VEDTAK_GODSKRIVING_PENSJONSOPPTJENING
    override val kategori = TemplateDescription.Brevkategori.OMSORGSOPPTJENING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.OMSORG)

    override val template = createTemplate(
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av omsorgsopptjening",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Godskriving av pensjonsopptjening (omsorgsopptjening) fordi du har omsorg for små barn" },
                nynorsk { +"" },
                english { +"Accredited pension earning (for care work) because you care for young children" } //Pension accrual credits granted for caring for young children
            )
            text(
                bokmal { +"Godskriving av pensjonsopptjening for omsorg for barn under sju år før 1992 - melding om vedtak" },
                nynorsk { +"" },
                english { +"Accreditation of acquired rights for the care of children below the age of seven prior to 1992 - notification of decision" } //Pension accrual credits for care of children under seven before 1992 - notice of decision
            )
            text(
                bokmal { +"Pensjonsopptjening for omsorgsarbeid (omsorgsopptjening) - orientering om godskriving" },
                nynorsk { +"" },
                english { +"Acquired rights for care work - guidance about credits" } // Pension accrual for caregiving (care credits) - notice of crediting
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            paragraph {}
        }
    }
}