package no.nav.pensjon.brev.pdfbygger

import kotlin.reflect.jvm.internal.impl.load.java.UtilsKt

fun getScriptPath(name: String): String = UtilsKt::class.java.classLoader.getResource(name)!!.path
