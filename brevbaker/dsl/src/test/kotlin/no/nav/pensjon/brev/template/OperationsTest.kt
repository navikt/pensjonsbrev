package no.nav.pensjon.brev.template

import no.nav.brev.BrevLandmodell
import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.parallel.Isolated

class OperationsTest {

    @Test
    fun `an operation without fields are considered equal based on class`() {
        assertEquals(UnaryOperation.ToString, UnaryOperation.ToString)
    }

    @Test
    fun `different operations are not considered equal`() {
        assertNotEquals(BinaryOperation.Equal<Int>(), UnaryOperation.ToString)
    }

    @Test
    fun `LocalizedCollectionFormat uses comma and localized separator before last item`() {
        val list = listOf("Alexander", "Jeremy", "Håkon", "Agne")

        assertEquals(
            "Alexander, Jeremy, Håkon og Agne",
            LocalizedFormatter.CollectionFormat.apply(list, Language.Bokmal)
        )
        assertEquals(
            "Alexander, Jeremy, Håkon og Agne",
            LocalizedFormatter.CollectionFormat.apply(list, Language.Nynorsk)
        )
        assertEquals(
            "Alexander, Jeremy, Håkon and Agne",
            LocalizedFormatter.CollectionFormat.apply(list, Language.English)
        )
    }

    @Test
    fun `LocalizedCollectionFormat has no separator for only one item`() {
        val list = listOf("Agne")

        assertEquals("Agne", LocalizedFormatter.CollectionFormat.apply(list, Language.Bokmal))
        assertEquals("Agne", LocalizedFormatter.CollectionFormat.apply(list, Language.Nynorsk))
        assertEquals("Agne", LocalizedFormatter.CollectionFormat.apply(list, Language.English))
    }

    @Test
    fun `LocalizedCollectionFormat has no comma for only two items`() {
        val list = listOf("Agne", "Jeremy")

        assertEquals("Agne og Jeremy", LocalizedFormatter.CollectionFormat.apply(list, Language.Bokmal))
        assertEquals("Agne og Jeremy", LocalizedFormatter.CollectionFormat.apply(list, Language.Nynorsk))
        assertEquals("Agne and Jeremy", LocalizedFormatter.CollectionFormat.apply(list, Language.English))
    }


    @Nested
    @DisplayName("Absolute value operators")
    inner class AbsoluteValue {
        private val scope = ExpressionScope(2, FellesFactory.felles, Language.Bokmal)

        @Test
        fun `absoluteKronerValue returns positive value if negative value`() {
            val expr = Kroner(-123).expr().absoluteValue()
            assertEquals(expr.eval(scope), Kroner(123))
        }

        @Test
        fun `absoluteKronerValue returns positive value if positive value`() {
            val expr = Kroner(123).expr().absoluteValue()
            assertEquals(expr.eval(scope), Kroner(123))
        }

        @Test
        fun `absoluteValue returns positive value if negative value`() {
            val expr = (-123).expr().absoluteValue()
            assertEquals(expr.eval(scope), 123)
        }

        @Test
        fun `absoluteValue returns positive value if positive value`() {
            val expr = Kroner(123).expr().absoluteValue()
            assertEquals(expr.eval(scope), Kroner(123))
        }
    }

    @Nested
    @DisplayName("Landnavn-formatering")
    inner class LandnavnFormatering {

        @Test
        fun `Landnavn returnerer riktig namn på bokmål`() {
            assertEquals("Norge", BinaryOperation.Landnavn.apply(BrevLandmodell.Landkode("NO"), Language.Bokmal))
        }

        @Test
        fun `Landnavn returnerer riktig namn på nynorsk`() {
            assertEquals("Noreg", BinaryOperation.Landnavn.apply(BrevLandmodell.Landkode("NO"), Language.Nynorsk))
        }

        @Test
        fun `Landnavn returnerer riktig namn på engelsk`() {
            assertEquals("Norway", BinaryOperation.Landnavn.apply(BrevLandmodell.Landkode("NO"), Language.English))
        }

        @Test
        fun `Landnavn handterer lowercase landkode`() {
            assertEquals("Norge", BinaryOperation.Landnavn.apply(BrevLandmodell.Landkode("no"), Language.Bokmal))
            assertEquals("Noreg", BinaryOperation.Landnavn.apply(BrevLandmodell.Landkode("no"), Language.Nynorsk))
            assertEquals("Norway", BinaryOperation.Landnavn.apply(BrevLandmodell.Landkode("no"), Language.English))
        }

        @Test
        fun `format() på Landkode-expression brukar språket frå scope`() {
            val expr = BrevLandmodell.Landkode("SE").expr().format()
            assertEquals("Sverige", expr.eval(ExpressionScope(BrevLandmodell.Landkode("SE"), FellesFactory.felles, Language.Bokmal)))
            assertEquals("Sverige", expr.eval(ExpressionScope(BrevLandmodell.Landkode("SE"), FellesFactory.felles, Language.Nynorsk)))
            assertEquals("Sweden", expr.eval(ExpressionScope(BrevLandmodell.Landkode("SE"), FellesFactory.felles, Language.English)))
        }
    }

    @Nested
    @DisplayName("Feature-toggling")
    @Isolated
    inner class FunksjonsbryterEnabled {
        private val scope = ExpressionScope(2, FellesFactory.felles, Language.Nynorsk)

        @Test
        fun `enabled gir true viss funksjonen returnerer true`() {
            val toggle = FeatureToggle("t1")
            FeatureToggleSingleton.init(object : FeatureToggleService {
                override fun isEnabled(toggle: FeatureToggle) = true
                override fun verifiserAtAlleBrytereErDefinert(entries: List<FeatureToggle>) { }
            })
            val expr = toggle.expr().enabled()
            assertEquals(expr.eval(scope), true)
        }

        @Test
        fun `enabled gir false viss funksjonen returnerer false`() {
            val toggle = FeatureToggle("t2")
            FeatureToggleSingleton.init(object : FeatureToggleService {
                override fun isEnabled(toggle: FeatureToggle) = false
                override fun verifiserAtAlleBrytereErDefinert(entries: List<FeatureToggle>) { }
            })
            val expr = toggle.expr().enabled()
            assertEquals(expr.eval(scope), false)
        }
    }
}