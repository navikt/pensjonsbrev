package no.nav.pensjon.brev.template.validation

import no.nav.pensjon.brev.template.OutlineElement


internal class RedigerbarTemplateValidator private constructor(blockIdentities: Set<Int>): BrevTemplateValidator {
    private val blockIdentities: MutableSet<Int> = blockIdentities.toMutableSet()

    constructor(): this(emptySet())

    override fun validate(e: OutlineElement<*>) {
        e.stableHashOfContent().forEach {
            if (!blockIdentities.add(it)) {
                throw DuplicateBlockIdentity("Det finnes allerede en blokk med denne identiteten i malen. Om det skal være slik så kan du overstyre med argumentet `uniqueness` til blokken.")
            }
        }
    }

    override fun subScope(): BrevTemplateValidator = RedigerbarTemplateValidator(blockIdentities)
}