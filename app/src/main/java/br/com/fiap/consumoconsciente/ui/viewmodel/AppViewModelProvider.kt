package br.com.fiap.consumoconsciente.ui.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.fiap.consumoconsciente.ConsumoConscienteApplication
import br.com.fiap.consumoconsciente.ui.screens.detalhe.DetalheViewModel
import br.com.fiap.consumoconsciente.ui.screens.formulario.FormularioViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AcoesViewModel(app().repository)
        }
        initializer {
            DetalheViewModel(createSavedStateHandle(), app().repository)
        }
        initializer {
            FormularioViewModel(createSavedStateHandle(), app().repository)
        }
    }
}

private fun CreationExtras.app(): ConsumoConscienteApplication =
    this[AndroidViewModelFactory.APPLICATION_KEY] as ConsumoConscienteApplication
