package no.nav.brev.brevbaker

import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.SPAN
import kotlinx.html.span
import no.nav.pensjon.brev.template.render.HTMLDocumentRendererBase

object HTMLDocumentRendererTest : HTMLDocumentRendererBase() {
    override fun FlowOrPhrasingContent.markerFritekst(function: SPAN.() -> Unit) {
        span(classes("text-blue"), function)
    }

    override fun FlowOrPhrasingContent.markerVariabel(function: SPAN.() -> Unit) {
        span(classes("data"), function)
    }

    override fun FlowOrPhrasingContent.markerRedigerbarData(function: SPAN.() -> Unit) {
        span(classes("redigerbarData"), function)
    }
}