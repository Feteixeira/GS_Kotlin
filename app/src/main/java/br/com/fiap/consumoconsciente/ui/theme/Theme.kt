package br.com.fiap.consumoconsciente.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Verde40,
    onPrimary = Color.White,
    primaryContainer = Verde90,
    onPrimaryContainer = Verde10,
    secondary = Azul40,
    onSecondary = Color.White,
    secondaryContainer = Azul90,
    tertiary = Ambar40,
    onTertiary = Color.White,
    tertiaryContainer = Ambar90,
    background = Fundo,
    surface = Fundo
)

private val EsquemaEscuro = darkColorScheme(
    primary = Verde80,
    onPrimary = Verde10,
    primaryContainer = Verde40,
    onPrimaryContainer = Verde90,
    secondary = Azul80,
    tertiary = Ambar80,
    background = FundoEscuro,
    surface = FundoEscuro
)

@Composable
fun ConsumoConscienteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaEscuro else EsquemaClaro,
        typography = Typography(),
        content = content
    )
}
