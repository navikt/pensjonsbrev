@file:Suppress("unused")

package no.nav.pensjon.brev.template.somepkg

import no.nav.pensjon.brev.template.otherpkg.QualifiedNamesAndTypeParameters_Model2
import no.nav.pensjon.brev.template.thirdpkg.*

@Suppress("ClassName")
data class QualifiedNamesAndTypeParameters_Model1(val thing: QualifiedNamesAndTypeParameters_Model2<QualifiedNamesAndTypeParameters_Model3<ActualModel>>) {
    data class ActualModel(val name: String)
}