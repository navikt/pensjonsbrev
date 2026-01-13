package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.template.dsl.helpers.testcases.IterableItemsSelectors.CollectionItemSelectors.name
import no.nav.pensjon.brev.template.dsl.helpers.testcases.IterableItemsSelectors.IterableItemSelectors.name
import no.nav.pensjon.brev.template.dsl.helpers.testcases.IterableItemsSelectors.ListItemSelectors.name

/**
 * Verify that selectors are generated for iterable item-types.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
@OptIn(InternKonstruktoer::class)
object IterableItems : HasModel<IterableItems.TheModel> {
    data class ListItem(val name: String)
    data class CollectionItem(val name: String)
    data class IterableItem(val name: String)
    data class TheModel(val theList: List<ListItem>, val theCollection: Collection<CollectionItem>, val theIterable: Iterable<IterableItem>)

    fun someUsage() {
        val listItemName: Expression<String> = Expression.Literal(ListItem("list name")).name
        val collectionItemName: Expression<String> = Expression.Literal(CollectionItem("collection name")).name
        val iterableItemName: Expression<String> = Expression.Literal(IterableItem("iterable name")).name
    }
}