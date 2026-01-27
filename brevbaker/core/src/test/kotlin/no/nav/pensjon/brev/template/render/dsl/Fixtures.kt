package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

@TemplateModelHelpers
object SomeDtoHelperGen : HasModel<SomeDto>
data class SomeDto(val name: String, val pensjonInnvilget: Boolean, val kortNavn: String? = null) : AutobrevData