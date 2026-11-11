package br.com.fiap.consumoconsciente.ui.screens.detalhe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.consumoconsciente.data.model.AcaoEconomia
import br.com.fiap.consumoconsciente.data.repository.AcaoRepository
import br.com.fiap.consumoconsciente.ui.navigation.Rotas
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetalheUiState(
    val carregando: Boolean = true,
    val acao: AcaoEconomia? = null
)

class DetalheViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AcaoRepository
) : ViewModel() {

    private val acaoId: Long = checkNotNull(savedStateHandle.get<Long>(Rotas.ARG_ACAO_ID))

    val uiState: StateFlow<DetalheUiState> = repository.observarPorId(acaoId)
        .map { DetalheUiState(carregando = false, acao = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetalheUiState())

    fun alternarConclusao() {
        val acao = uiState.value.acao ?: return
        viewModelScope.launch { repository.alterarStatus(acao.id, !acao.concluida) }
    }

    fun excluir(aoConcluir: () -> Unit) {
        val acao = uiState.value.acao ?: return
        viewModelScope.launch {
            repository.excluir(acao)
            aoConcluir()
        }
    }
}
