package no.nav.pensjon.brev.pdfbygger

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

