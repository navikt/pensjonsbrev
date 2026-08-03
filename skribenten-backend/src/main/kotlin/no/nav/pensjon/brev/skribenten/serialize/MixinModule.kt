package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.databind.ObjectMapper

interface MixinModule {
    fun register(mapper: ObjectMapper): ObjectMapper
}

fun ObjectMapper.registerMixin(mixin: MixinModule): ObjectMapper = mixin.register(this)