package no.nav.brev.brevbaker.document

import no.nav.brev.brevbaker.markup.cleanBlocks

fun Document.clean(): Document = copy(blocks = blocks.cleanBlocks())
