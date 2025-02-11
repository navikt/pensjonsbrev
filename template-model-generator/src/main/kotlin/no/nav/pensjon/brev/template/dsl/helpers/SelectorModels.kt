package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.symbol.*

internal data class SelectorModels(private val visited: Set<KSClassDeclaration> = emptySet(), val needed: Map<KSClassDeclaration, Set<KSFile>> = emptyMap()) {
    fun withVisited(decl: KSClassDeclaration): SelectorModels =
        copy(visited = visited + decl)

    fun withDependency(
        decl: KSClassDeclaration,
        dependency: KSFile?,
    ): SelectorModels =
        if (needed.containsKey(decl)) {
            withNeeded(decl, dependency)
        } else {
            this
        }

    fun withNeeded(
        decl: KSClassDeclaration,
        dependency: KSFile?,
    ): SelectorModels =
        copy(needed = needed + (decl to needed.getOrDefault(decl, emptySet()) + setOfNotNull(dependency)))

    fun isVisited(decl: KSClassDeclaration): Boolean =
        visited.contains(decl) || needed.contains(decl)
}
