package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonUtlandDto
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_GP_04_025 Vedtak innvilgelse av gjenlevendepensjon utland
//Brevgruppe 2

@TemplateModelHelpers
object InnvilgelseGjenlevendepensjonUtland : RedigerbarTemplate<InnvilgelseGjenlevendepensjonUtlandDto> {

    override  val featureToggle = FeatureToggles.brevmalAvslagGjenlevendepensjonUtland.toggle
}