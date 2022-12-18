package ee.ut.cs.powerwise.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.glance.unit.ColorProvider

import ee.ut.cs.powerwise.ui.theme.*


object GlanceTheme {
    val colors: WidgetColorProviders
        @Composable
        @ReadOnlyComposable
        get() = LocalColorProviders.current
}

internal val LocalColorProviders = staticCompositionLocalOf { lightColors }


@Composable
fun GlanceTheme(
    colors: WidgetColorProviders = GlanceTheme.colors,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalColorProviders provides colors) {
        content()
    }
}

data class WidgetColorProviders(
    val primary: ColorProvider,
    val onPrimary: ColorProvider,
    val primaryContainer: ColorProvider,
    val onPrimaryContainer: ColorProvider,
    val secondary: ColorProvider,
    val onSecondary: ColorProvider,
    val secondaryContainer: ColorProvider,
    val onSecondaryContainer: ColorProvider,
    val background: ColorProvider,
    val onBackground: ColorProvider,
    val surface: ColorProvider,
    val outline: ColorProvider
)

var lightColors: WidgetColorProviders = WidgetColorProviders(
    primary = ColorProvider(md_theme_light_primary),
    onPrimary = ColorProvider(md_theme_light_onPrimary),
    primaryContainer = ColorProvider(md_theme_light_primaryContainer),
    onPrimaryContainer = ColorProvider(md_theme_light_onPrimaryContainer),
    secondary = ColorProvider(md_theme_light_secondary),
    onSecondary = ColorProvider(md_theme_light_onSecondary),
    secondaryContainer = ColorProvider(md_theme_light_secondaryContainer),
    onSecondaryContainer = ColorProvider(md_theme_light_onSecondaryContainer),
    background = ColorProvider(md_theme_light_background),
    onBackground = ColorProvider(md_theme_light_onBackground),
    surface = ColorProvider(md_theme_light_surface),
    outline = ColorProvider(md_theme_light_outline),
)

var darkColors: WidgetColorProviders = WidgetColorProviders(
    primary = ColorProvider(md_theme_dark_primary),
    onPrimary = ColorProvider(md_theme_dark_onPrimary),
    primaryContainer = ColorProvider(md_theme_dark_primaryContainer),
    onPrimaryContainer = ColorProvider(md_theme_dark_onPrimaryContainer),
    secondary = ColorProvider(md_theme_dark_secondary),
    onSecondary = ColorProvider(md_theme_dark_onSecondary),
    secondaryContainer = ColorProvider(md_theme_dark_secondaryContainer),
    onSecondaryContainer = ColorProvider(md_theme_dark_onSecondaryContainer),
    background = ColorProvider(md_theme_dark_background),
    onBackground = ColorProvider(md_theme_dark_onBackground),
    surface = ColorProvider(md_theme_dark_surface),
    outline = ColorProvider(md_theme_dark_outline),
)




