package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterPDFRequest
import no.nav.brev.brevbaker.markup.MarkupInternalApi
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text

/**
 * Interne seams mot byggernes tilstand, brukt av `brevbaker:internal` for å bygge utvidede DSL-er
 * (id-er, `variable`, editBehaviour) oppå de samme byggerne.
 *
 * Seamene er bevisst *extension*-medlemmer og ikke medlemmer på byggerklassene: medlemmer ville vært
 * synlige i alle DSL-blokker og skygget for kallerens egne navn (f.eks. `items` eller `prompt`).
 * Extensions er kun i scope i filer som eksplisitt importerer dem. Seam-navn må heller aldri kollidere
 * med navnet på en offentlig DSL-funksjon (derav [tableHeader] og [promptTexts]), fordi ett `import`
 * av navnet ellers drar inn begge.
 */

// --- AbstractContentBuilder ---

@MarkupInternalApi
val AbstractContentBuilder.texts: MutableList<Text>
    get() = _texts

@MarkupInternalApi
fun AbstractContentBuilder.build(): List<Text> = _build()

// --- PlainTextBuilder ---

@MarkupInternalApi
val PlainTextBuilder.texts: MutableList<Text>
    get() = _texts

@MarkupInternalApi
fun PlainTextBuilder.build(): List<Text> = _build()

// --- OutlineBuilder ---

@MarkupInternalApi
val <C : AbstractContentBuilder> OutlineBuilder<C>.contentFactory: ContentFactory<C>
    get() = _contentFactory

@MarkupInternalApi
val <C : AbstractContentBuilder> OutlineBuilder<C>.blocks: MutableList<Block>
    get() = _blocks

@MarkupInternalApi
fun <C : AbstractContentBuilder> OutlineBuilder<C>.build(): List<Block> = _build()

// --- ItemsBuilder ---

@MarkupInternalApi
val <C : AbstractContentBuilder> ItemsBuilder<C>.contentFactory: ContentFactory<C>
    get() = _contentFactory

@MarkupInternalApi
val <C : AbstractContentBuilder> ItemsBuilder<C>.items: MutableList<Block.Item>
    get() = _items

@MarkupInternalApi
fun <C : AbstractContentBuilder> ItemsBuilder<C>.build(): List<Block.Item> = _build()

// --- TableBuilder ---

@MarkupInternalApi
val <C : AbstractContentBuilder> TableBuilder<C>.contentFactory: ContentFactory<C>
    get() = _contentFactory

@MarkupInternalApi
var <C : AbstractContentBuilder> TableBuilder<C>.tableHeader: Block.Table.Header?
    get() = _header
    set(value) {
        _header = value
    }

@MarkupInternalApi
val <C : AbstractContentBuilder> TableBuilder<C>.rows: MutableList<Block.Table.Row>
    get() = _rows

@MarkupInternalApi
fun <C : AbstractContentBuilder> TableBuilder<C>.build(id: Int): Block.Table = _build(id)

// --- HeaderBuilder ---

@MarkupInternalApi
val <C : AbstractContentBuilder> HeaderBuilder<C>.colSpec: MutableList<Block.Table.ColumnSpec>
    get() = _colSpec

@MarkupInternalApi
fun <C : AbstractContentBuilder> HeaderBuilder<C>.build(id: Int): Block.Table.Header = _build(id)

// --- RowBuilder ---

@MarkupInternalApi
val <C : AbstractContentBuilder> RowBuilder<C>.contentFactory: ContentFactory<C>
    get() = _contentFactory

@MarkupInternalApi
val <C : AbstractContentBuilder> RowBuilder<C>.cells: MutableList<Block.Table.Cell>
    get() = _cells

@MarkupInternalApi
fun <C : AbstractContentBuilder> RowBuilder<C>.build(id: Int): Block.Table.Row = _build(id)

// --- FormChoiceBuilder ---

@MarkupInternalApi
val <C : AbstractContentBuilder> FormChoiceBuilder<C>.contentFactory: ContentFactory<C>
    get() = _contentFactory

@MarkupInternalApi
var <C : AbstractContentBuilder> FormChoiceBuilder<C>.vspace: Boolean
    get() = _vspace
    set(value) {
        _vspace = value
    }

@MarkupInternalApi
val <C : AbstractContentBuilder> FormChoiceBuilder<C>.promptTexts: MutableList<Text>
    get() = _prompt

@MarkupInternalApi
val <C : AbstractContentBuilder> FormChoiceBuilder<C>.choices: MutableList<Block.FormChoice.Choice>
    get() = _choices

@MarkupInternalApi
fun <C : AbstractContentBuilder> FormChoiceBuilder<C>.build(id: Int): Block.FormChoice = _build(id)

// --- LetterMarkupBuilder / AttachmentBuilder / PDFRequestBuilder ---

@MarkupInternalApi
fun <C : AbstractContentBuilder> LetterMarkupBuilder<C>.setTitle(content: () -> List<Text>) = _setTitle(content)

@MarkupInternalApi
fun <C : AbstractContentBuilder> LetterMarkupBuilder<C>.build(): LetterMarkup = _build()

@MarkupInternalApi
fun <C : AbstractContentBuilder> AttachmentBuilder<C>.build(): Attachment = _build()

@MarkupInternalApi
fun PDFRequestBuilder.build(): LetterPDFRequest = _build()
