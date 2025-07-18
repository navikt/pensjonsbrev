package no.nav.pensjon.brev.pdfbygger

import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC

fun <R> mdc(vararg pairs: Pair<String, Any?>, block: () -> R): R {
    try {
        pairs.forEach { (k, v) ->
            MDC.put(k, v?.toString() ?: "-")
        }

        return block()
    } finally {
        pairs.map { it.first }.forEach(MDC::remove)
    }
}

suspend inline fun <R> withMdc(vararg pairs: Pair<String, String>, crossinline block: suspend () -> R): R =
    withContext(MDCContext()) {
        pairs.forEach { MDC.put(it.first, it.second) }
        block()
    }