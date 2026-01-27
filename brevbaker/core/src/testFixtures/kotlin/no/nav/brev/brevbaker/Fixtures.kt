package no.nav.brev.brevbaker

import kotlin.reflect.KClass

interface LetterDataFactory {
    fun <T : Any> create(letterDataType: KClass<T>): T
    fun <T : Any> createVedlegg(letterDataType: KClass<T>): T
}