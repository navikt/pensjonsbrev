package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.symbol.KSClassDeclaration

internal data class SelectorModels(private val visited: Set<KSClassDeclaration> = emptySet(), val needed: Set<KSClassDeclaration> = emptySet()) {
    fun withVisited(decl: KSClassDeclaration) = copy(visited = visited + decl)
    fun withNeeded(decl: KSClassDeclaration) = copy(needed = needed + decl)
    fun isVisited(decl: KSClassDeclaration) = visited.contains(decl) || needed.contains(decl)
}