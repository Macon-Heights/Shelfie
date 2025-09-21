package com.example.compose
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import me.alexandervortex.shelfie.ui.theme.Typography
import me.alexandervortex.shelfie.ui.theme.backgroundDark
import me.alexandervortex.shelfie.ui.theme.backgroundDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.backgroundDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.backgroundLight
import me.alexandervortex.shelfie.ui.theme.backgroundLightHighContrast
import me.alexandervortex.shelfie.ui.theme.backgroundLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.errorContainerDark
import me.alexandervortex.shelfie.ui.theme.errorContainerDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.errorContainerDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.errorContainerLight
import me.alexandervortex.shelfie.ui.theme.errorContainerLightHighContrast
import me.alexandervortex.shelfie.ui.theme.errorContainerLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.errorDark
import me.alexandervortex.shelfie.ui.theme.errorDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.errorDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.errorLight
import me.alexandervortex.shelfie.ui.theme.errorLightHighContrast
import me.alexandervortex.shelfie.ui.theme.errorLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.inverseOnSurfaceDark
import me.alexandervortex.shelfie.ui.theme.inverseOnSurfaceDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.inverseOnSurfaceDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.inverseOnSurfaceLight
import me.alexandervortex.shelfie.ui.theme.inverseOnSurfaceLightHighContrast
import me.alexandervortex.shelfie.ui.theme.inverseOnSurfaceLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.inversePrimaryDark
import me.alexandervortex.shelfie.ui.theme.inversePrimaryDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.inversePrimaryDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.inversePrimaryLight
import me.alexandervortex.shelfie.ui.theme.inversePrimaryLightHighContrast
import me.alexandervortex.shelfie.ui.theme.inversePrimaryLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.inverseSurfaceDark
import me.alexandervortex.shelfie.ui.theme.inverseSurfaceDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.inverseSurfaceDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.inverseSurfaceLight
import me.alexandervortex.shelfie.ui.theme.inverseSurfaceLightHighContrast
import me.alexandervortex.shelfie.ui.theme.inverseSurfaceLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onBackgroundDark
import me.alexandervortex.shelfie.ui.theme.onBackgroundDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onBackgroundDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onBackgroundLight
import me.alexandervortex.shelfie.ui.theme.onBackgroundLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onBackgroundLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onErrorContainerDark
import me.alexandervortex.shelfie.ui.theme.onErrorContainerDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onErrorContainerDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onErrorContainerLight
import me.alexandervortex.shelfie.ui.theme.onErrorContainerLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onErrorContainerLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onErrorDark
import me.alexandervortex.shelfie.ui.theme.onErrorDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onErrorDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onErrorLight
import me.alexandervortex.shelfie.ui.theme.onErrorLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onErrorLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onPrimaryContainerDark
import me.alexandervortex.shelfie.ui.theme.onPrimaryContainerDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onPrimaryContainerDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onPrimaryContainerLight
import me.alexandervortex.shelfie.ui.theme.onPrimaryContainerLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onPrimaryContainerLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onPrimaryDark
import me.alexandervortex.shelfie.ui.theme.onPrimaryDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onPrimaryDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onPrimaryLight
import me.alexandervortex.shelfie.ui.theme.onPrimaryLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onPrimaryLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onSecondaryContainerDark
import me.alexandervortex.shelfie.ui.theme.onSecondaryContainerDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onSecondaryContainerDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onSecondaryContainerLight
import me.alexandervortex.shelfie.ui.theme.onSecondaryContainerLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onSecondaryContainerLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onSecondaryDark
import me.alexandervortex.shelfie.ui.theme.onSecondaryDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onSecondaryDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onSecondaryLight
import me.alexandervortex.shelfie.ui.theme.onSecondaryLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onSecondaryLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onSurfaceDark
import me.alexandervortex.shelfie.ui.theme.onSurfaceDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onSurfaceDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onSurfaceLight
import me.alexandervortex.shelfie.ui.theme.onSurfaceLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onSurfaceLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onSurfaceVariantDark
import me.alexandervortex.shelfie.ui.theme.onSurfaceVariantDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onSurfaceVariantDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onSurfaceVariantLight
import me.alexandervortex.shelfie.ui.theme.onSurfaceVariantLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onSurfaceVariantLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onTertiaryContainerDark
import me.alexandervortex.shelfie.ui.theme.onTertiaryContainerDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onTertiaryContainerDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onTertiaryContainerLight
import me.alexandervortex.shelfie.ui.theme.onTertiaryContainerLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onTertiaryContainerLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.onTertiaryDark
import me.alexandervortex.shelfie.ui.theme.onTertiaryDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.onTertiaryDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.onTertiaryLight
import me.alexandervortex.shelfie.ui.theme.onTertiaryLightHighContrast
import me.alexandervortex.shelfie.ui.theme.onTertiaryLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.outlineDark
import me.alexandervortex.shelfie.ui.theme.outlineDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.outlineDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.outlineLight
import me.alexandervortex.shelfie.ui.theme.outlineLightHighContrast
import me.alexandervortex.shelfie.ui.theme.outlineLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.outlineVariantDark
import me.alexandervortex.shelfie.ui.theme.outlineVariantDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.outlineVariantDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.outlineVariantLight
import me.alexandervortex.shelfie.ui.theme.outlineVariantLightHighContrast
import me.alexandervortex.shelfie.ui.theme.outlineVariantLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.primaryContainerDark
import me.alexandervortex.shelfie.ui.theme.primaryContainerDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.primaryContainerDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.primaryContainerLight
import me.alexandervortex.shelfie.ui.theme.primaryContainerLightHighContrast
import me.alexandervortex.shelfie.ui.theme.primaryContainerLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.primaryDark
import me.alexandervortex.shelfie.ui.theme.primaryDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.primaryDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.primaryLight
import me.alexandervortex.shelfie.ui.theme.primaryLightHighContrast
import me.alexandervortex.shelfie.ui.theme.primaryLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.scrimDark
import me.alexandervortex.shelfie.ui.theme.scrimDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.scrimDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.scrimLight
import me.alexandervortex.shelfie.ui.theme.scrimLightHighContrast
import me.alexandervortex.shelfie.ui.theme.scrimLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.secondaryContainerDark
import me.alexandervortex.shelfie.ui.theme.secondaryContainerDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.secondaryContainerDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.secondaryContainerLight
import me.alexandervortex.shelfie.ui.theme.secondaryContainerLightHighContrast
import me.alexandervortex.shelfie.ui.theme.secondaryContainerLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.secondaryDark
import me.alexandervortex.shelfie.ui.theme.secondaryDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.secondaryDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.secondaryLight
import me.alexandervortex.shelfie.ui.theme.secondaryLightHighContrast
import me.alexandervortex.shelfie.ui.theme.secondaryLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceBrightDark
import me.alexandervortex.shelfie.ui.theme.surfaceBrightDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceBrightDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceBrightLight
import me.alexandervortex.shelfie.ui.theme.surfaceBrightLightHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceBrightLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerDark
import me.alexandervortex.shelfie.ui.theme.surfaceContainerDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighDark
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighLight
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighLightHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighestDark
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighestDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighestDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighestLight
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighestLightHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerHighestLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLight
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLightHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowDark
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowLight
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowLightHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowestDark
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowestDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowestDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowestLight
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowestLightHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceContainerLowestLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceDark
import me.alexandervortex.shelfie.ui.theme.surfaceDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceDimDark
import me.alexandervortex.shelfie.ui.theme.surfaceDimDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceDimDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceDimLight
import me.alexandervortex.shelfie.ui.theme.surfaceDimLightHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceDimLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceLight
import me.alexandervortex.shelfie.ui.theme.surfaceLightHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceVariantDark
import me.alexandervortex.shelfie.ui.theme.surfaceVariantDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceVariantDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.surfaceVariantLight
import me.alexandervortex.shelfie.ui.theme.surfaceVariantLightHighContrast
import me.alexandervortex.shelfie.ui.theme.surfaceVariantLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.tertiaryContainerDark
import me.alexandervortex.shelfie.ui.theme.tertiaryContainerDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.tertiaryContainerDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.tertiaryContainerLight
import me.alexandervortex.shelfie.ui.theme.tertiaryContainerLightHighContrast
import me.alexandervortex.shelfie.ui.theme.tertiaryContainerLightMediumContrast
import me.alexandervortex.shelfie.ui.theme.tertiaryDark
import me.alexandervortex.shelfie.ui.theme.tertiaryDarkHighContrast
import me.alexandervortex.shelfie.ui.theme.tertiaryDarkMediumContrast
import me.alexandervortex.shelfie.ui.theme.tertiaryLight
import me.alexandervortex.shelfie.ui.theme.tertiaryLightHighContrast
import me.alexandervortex.shelfie.ui.theme.tertiaryLightMediumContrast

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color,
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun ShelfieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
      typography = Typography,
        content = content
    )
}