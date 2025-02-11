@file:Suppress("unused")

package no.nav.pensjon.brev.template.somepkg

import no.nav.pensjon.brev.template.otherpkg.TypeParameterModel
import no.nav.pensjon.brev.template.thirdpkg.*

data class ModelOutsideOfTestPackage(val thing: TypeParameterModel<OtherWithTypeParameter<SimpleModel>>)
