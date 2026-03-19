package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonUtlandDto
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

@TemplateModelHelpers
object AvslagGjenlevendepensjonUtland : RedigerbarTemplate<AvslagGjenlevendepensjonUtlandDto> {

    //override val featureToggle = FeatureToggles.brevmalAvslagGjenlevendepensjonUtland.toggle

    override val kode = Pesysbrevkoder.Redigerbar.GP_OPPHOER_GJENLEVENDEPENSJON
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.GJENLEV)



}