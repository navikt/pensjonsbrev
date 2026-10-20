package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object KlageBeslutningOmAaIkkeGjoereOmVedtaketUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageBeslutningOmAaIkkeGjoereOmVedtaketUT.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_KLAGE_BESLUTNING_AA_IKKE_GJOERE_OM_VEDTAKET
    override val kategori = Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - beslutning om å ikke gjøre om vedtaket",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    )

}

