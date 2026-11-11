package br.com.fiap.consumoconsciente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.fiap.consumoconsciente.ui.navigation.AppNavigation
import br.com.fiap.consumoconsciente.ui.theme.ConsumoConscienteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConsumoConscienteTheme {
                AppNavigation()
            }
        }
    }
}
