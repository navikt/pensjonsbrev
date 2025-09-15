package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.databind.module.SimpleModule

internal inline fun <reified T, reified V : T> SimpleModule.addAbstractTypeMapping() =
    addAbstractTypeMapping(T::class.java, V::class.java)
