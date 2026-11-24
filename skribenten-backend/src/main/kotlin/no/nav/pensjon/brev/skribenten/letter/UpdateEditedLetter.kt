@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl

class UpdateEditedLetterException(message: String) : RuntimeException(message)

/**
 * Update a letter edited in Skribenten (originally rendered by brevbaker) with a fresh
 * rendering from brevbaker. In the new rendering from brevbaker we may receive elements (blocks/content)
 * that wasn't present in previous renders, e.g. if Saksbehandler has modified the template options (saksbehandlerValg) or
 * if Sak-data has changed in pesys. Or elements (blocks/content) present in previous renders may no longer be
 * present.
 */
fun Edit.Letter.updateEditedLetter(renderedLetter: LetterMarkup): Edit.Letter =
    renderedLetter.toEdit().let { rendered ->
        UpdateEditedLetter(VariableValuesUpdater(rendered)).mergeLetter(
            this,
            rendered
        )
    }


fun Edit.Attachment.updateEditedAttachment(renderedAttachment: LetterMarkup.Attachment): Edit.Attachment =
    renderedAttachment.toEdit().let { rendered ->
        UpdateEditedLetter(VariableValuesUpdater(rendered)).mergeAttachment(
            this,
            rendered
        )
    }

fun Edit.Letter.updateSakspartOgSignatur(renderedLetter: LetterMarkup): Edit.Letter =
    copy(
        sakspart = renderedLetter.sakspart,
        signatur = mergeSignatur(edited = signatur, rendered = renderedLetter.signatur),
    )

private fun mergeSignatur(edited: LetterMarkup.Signatur, rendered: LetterMarkup.Signatur): LetterMarkup.Signatur =
    LetterMarkupImpl.SignaturImpl(
        // Template-controlled fields: always use rendered values
        hilsenTekst = rendered.hilsenTekst,
        navAvsenderEnhet = rendered.navAvsenderEnhet,
        // User-editable fields: preserve edited values
        saksbehandlerNavn = edited.saksbehandlerNavn,
        attesterendeSaksbehandlerNavn = edited.attesterendeSaksbehandlerNavn,
    )

class UpdateEditedLetter(private val variableValues: VariableValuesUpdater) {

    fun mergeLetter(edited: Edit.Letter, rendered: Edit.Letter): Edit.Letter =
        mergeList(null, edited.blocks, rendered.blocks, edited.deletedBlocks, ::mergeBlock).let { merged ->
            edited.copy(
                title = mergeTitle(edited.title, rendered.title),
                sakspart = rendered.sakspart,
                signatur = mergeSignatur(edited.signatur, rendered.signatur),
                blocks = merged.items,
                deletedBlocks = merged.deleted,
            )
        }

    fun mergeAttachment(edited: Edit.Attachment, rendered: Edit.Attachment): Edit.Attachment =
        mergeList(null, edited.blocks, rendered.blocks, edited.deletedBlocks, ::mergeBlock).let { merged ->
            edited.copy(
                title = mergeTitle(edited.title, rendered.title),
                blocks = merged.items,
                deletedBlocks = merged.deleted,
                includeSakspart = rendered.includeSakspart,
            )
        }

    private data class Merged<E>(val items: List<E>, val deleted: List<Int>)

    /**
     * Merges a list of [edited] elements with a list of freshly [rendered] elements, and returns the merged elements
     * together with the deletion markers from [deleted] that are still relevant.
     * For each edited element we attempt to find a corresponding rendered element, and if we find it they are merged using the provided [merge]-function.
     * Both lists may contain elements not present in the other, and we try to zip the lists together based on elements present in both. The edited-list
     * may contain new elements, e.g. a new paragraph not present in the template or elements no longer present in the fresh render. While the rendered-list
     * may contain elements that weren't included in the previous render.
     *
     * Example 1: No edits other than new elements.
     * ```
     *    edited:   [A, C, New1, E, New2]
     *    rendered: [A, B, D, E]
     *    result:   [A, New1, D, E, New2]
     * ```
     *
     * Example 2: Edited elements marked with ', e.g. A'.
     * ```
     *    edited    [A, B', D]
     *    rendered: [A, B, C, D]
     *    result:   [A, B', C, D]
     * ```
     */
    private fun <E : Edit.Identifiable> mergeList(
        parent: Edit.Identifiable?,
        edited: List<E>,
        rendered: List<E>,
        deleted: List<Int>,
        merge: (E, E) -> E,
    ): Merged<E> {
        val aligned = ListAlignment.align(edited, rendered, deleted)
        return Merged(mergeAligned(parent, edited, aligned.remainingRendered, merge), aligned.deleted)
    }

    private fun <E : Edit.Identifiable> mergeAligned(
        parent: Edit.Identifiable?,
        edited: List<E>,
        rendered: List<E>,
        merge: (E, E) -> E,
    ): List<E> = buildList {
        val remainingRendered = rendered.toMutableList()

        // We zip-merge the two lists with edited as basis, then we pick matching elements of remainingRendered.
        edited.forEach { currentEdited ->
            // If the currentEdited element is new, i.e. was added manually by Saksbehandler.
            if (currentEdited.isNew()) {
                add(variableValues.withUpdatedVariables(currentEdited))
            } else {
                val renderedIndex = remainingRendered.indexOfFirst { it.id == currentEdited.id }

                if (renderedIndex >= 0) {
                    // The currentEdited element is present in the fresh render.

                    // We add any new elements from the fresh render that precedes currentEdited in the fresh render.
                    repeat((0 until renderedIndex).count()) { add(remainingRendered.removeFirst()) }

                    // If the currentEdited element actually has any edits we merge them, otherwise we simply pick the rendered one.
                    if (currentEdited.isEdited()) {
                        add(merge(currentEdited, remainingRendered.removeFirst()))
                    } else {
                        add(remainingRendered.removeFirst())
                    }
                } else if (currentEdited.isEdited()) {
                    // The currentEdited element is not present in the fresh render, but it is edited by the Saksbehandler.
                    // We include it so that no potentially important text is lost.
                    add(
                        withTemplateStatus(
                            variableValues.withUpdatedVariables(currentEdited),
                            Edit.Block.DecisionRequest(
                                type = Edit.Block.DecisionRequest.Type.MISSING_FROM_RENDERED,
                                status = Edit.Block.DecisionRequest.Decision.PENDING
                            )
                        )
                    )
                } else if (currentEdited.parentId != parent?.id) {
                    // The currentEdited element is moved to another parent, and thus cannot currently be tracked.
                    // But it is moved by intent, so we do not wish to remove it.
                    add(variableValues.withUpdatedVariables(currentEdited))
                }
            }
        }
        addAll(remainingRendered)
    }

    private fun mergeTitle(edited: Edit.Title, rendered: Edit.Title): Edit.Title =
        mergeList(null, edited.text, rendered.text, edited.deletedContent, ::mergeTextContent).let { merged ->
            edited.copy(text = merged.items, deletedContent = merged.deleted)
        }

    private fun mergeBlock(edited: Edit.Block, rendered: Edit.Block): Edit.Block =
        when (edited) {
            is Edit.Block.Paragraph ->
                mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeParagraphContent).let { merged ->
                    edited.copy(content = merged.items, deletedContent = merged.deleted)
                }

            is Edit.Block.Title1 ->
                mergeList(edited, edited.content, rendered.textContent(), edited.deletedContent, ::mergeTextContent).let { merged ->
                    edited.copy(content = merged.items, deletedContent = merged.deleted)
                }

            is Edit.Block.Title2 ->
                mergeList(edited, edited.content, rendered.textContent(), edited.deletedContent, ::mergeTextContent).let { merged ->
                    edited.copy(content = merged.items, deletedContent = merged.deleted)
                }

            is Edit.Block.Title3 ->
                mergeList(edited, edited.content, rendered.textContent(), edited.deletedContent, ::mergeTextContent).let { merged ->
                    edited.copy(content = merged.items, deletedContent = merged.deleted)
                }
        }

    private fun Edit.Block.textContent(): List<Edit.ParagraphContent.Text> =
        when (this) {
            is Edit.Block.Title1 -> content
            is Edit.Block.Title2 -> content
            is Edit.Block.Title3 -> content
            is Edit.Block.Paragraph -> content.filterIsInstance<Edit.ParagraphContent.Text>()
        }

    private fun mergeTextContent(edited: Edit.ParagraphContent.Text, rendered: Edit.ParagraphContent.Text): Edit.ParagraphContent.Text =
        when (edited) {
            is Edit.ParagraphContent.Text.Literal -> when (rendered) {
                is Edit.ParagraphContent.Text.Literal -> rendered.copy(editedText = edited.editedText, editedFontType = edited.editedFontType)
                is Edit.ParagraphContent.Text.Variable -> if (edited.editedText != null) edited else rendered
                is Edit.ParagraphContent.Text.NewLine -> throw UpdateEditedLetterException("Edited literal and rendered newLine has same ID, cannot merge: $edited - $rendered")
            }
            is Edit.ParagraphContent.Text.Variable -> throw UpdateEditedLetterException("Variable should never be considered edited: $edited")
            is Edit.ParagraphContent.Text.NewLine -> when (rendered) {
                is Edit.ParagraphContent.Text.NewLine -> edited
                is Edit.ParagraphContent.Text.Literal -> throw UpdateEditedLetterException("Edited newLine and rendered literal has same ID, cannot merge: $edited - $rendered")
                is Edit.ParagraphContent.Text.Variable -> throw UpdateEditedLetterException("Edited newLine and rendered variable has same ID, cannot merge: $edited - $rendered")
            }
        }

    private fun mergeParagraphContent(edited: Edit.ParagraphContent, rendered: Edit.ParagraphContent): Edit.ParagraphContent =
        when (edited) {
            is Edit.ParagraphContent.ItemList ->
                if (rendered is Edit.ParagraphContent.ItemList) {
                    mergeList(edited, edited.items, rendered.items, edited.deletedItems, ::mergeItems).let { merged ->
                        edited.copy(items = merged.items, deletedItems = merged.deleted)
                    }
                } else {
                    throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
                }

            is Edit.ParagraphContent.Text ->
                if (rendered is Edit.ParagraphContent.Text) {
                    mergeTextContent(edited, rendered)
                } else {
                    throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
                }

            is Edit.ParagraphContent.Table ->
                if (rendered is Edit.ParagraphContent.Table) {
                    mergeList(edited, edited.rows, rendered.rows, edited.deletedRows, ::mergeRows).let { merged ->
                        edited.copy(header = mergeTableHeader(edited.header, rendered.header), rows = merged.items, deletedRows = merged.deleted)
                    }
                } else {
                    throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
                }
        }

    private fun mergeTableHeader(edited: Edit.ParagraphContent.Table.Header, rendered: Edit.ParagraphContent.Table.Header): Edit.ParagraphContent.Table.Header =
        mergeList(edited, edited.colSpec, rendered.colSpec, edited.deletedColSpecs, ::mergeColumnSpec).let { merged ->
            edited.copy(colSpec = merged.items, deletedColSpecs = merged.deleted)
        }

    private fun mergeColumnSpec(
        edited: Edit.ParagraphContent.Table.ColumnSpec,
        rendered: Edit.ParagraphContent.Table.ColumnSpec,
    ): Edit.ParagraphContent.Table.ColumnSpec =
        edited.copy(headerContent = mergeCell(edited.headerContent, rendered.headerContent))

    private fun mergeCell(edited: Edit.ParagraphContent.Table.Cell, rendered: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
        mergeList(edited, edited.text, rendered.text, edited.deletedContent, ::mergeTextContent).let { merged ->
            edited.copy(text = merged.items, deletedContent = merged.deleted)
        }

    private fun mergeRows(edited: Edit.ParagraphContent.Table.Row, rendered: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row =
        mergeList(edited, edited.cells, rendered.cells, edited.deletedCells, ::mergeCell).let { merged ->
            edited.copy(cells = merged.items, deletedCells = merged.deleted)
        }

    private fun mergeItems(edited: Edit.ParagraphContent.ItemList.Item, rendered: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
        mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeTextContent).let { merged ->
            edited.copy(content = merged.items, deletedContent = merged.deleted)
        }


    @Suppress("UNCHECKED_CAST")
    private fun <E : Edit.Identifiable> withTemplateStatus(element: E, decisionRequest: Edit.Block.DecisionRequest): E =
        when (element) {
            is Edit.Block.Title1 -> element.copy(decision = decisionRequest) as E
            is Edit.Block.Title2 -> element.copy(decision = decisionRequest) as E
            is Edit.Block.Title3 -> element.copy(decision = decisionRequest) as E
            is Edit.Block.Paragraph -> element.copy(decision = decisionRequest) as E
            is Edit.ParagraphContent,
            is Edit.ParagraphContent.ItemList.Item,
            is Edit.ParagraphContent.Table.Header,
            is Edit.ParagraphContent.Table.ColumnSpec,
            is Edit.ParagraphContent.Table.Row,
            is Edit.ParagraphContent.Table.Cell,
                -> element
        }
}
