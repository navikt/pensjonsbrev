package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_GP_04_025 Vedtak innvilgelse av gjenlevendepensjon utland
//Brevgruppe 2

@TemplateModelHelpers
object InnvilgelseGjenlevendepensjonBosattNorgeEtterUtland : RedigerbarTemplate<InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto> {

    override val featureToggle = FeatureToggles.brevmalAvslagGjenlevendepensjonUtland.toggle

    override val kode = Pesysbrevkoder.Redigerbar.GP_INNVILGELSE_BOSATT_NORGE_ETTER_UTLAND
    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.GJENLEV)

    override val template = createTemplate(
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av ytelse til gjenlevende (bosatt Norge - etter utland) "
        )
    )
}