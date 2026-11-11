package br.com.fiap.consumoconsciente.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.consumoconsciente.data.model.AcaoEconomia
import br.com.fiap.consumoconsciente.data.model.TipoRecurso
import br.com.fiap.consumoconsciente.data.model.economiaMensal
import br.com.fiap.consumoconsciente.data.repository.AcaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
data class ResumoConsumo(
    val totalAcoes: Int = 0,
    val concluidas: Int = 0,
    val litrosPorMes: Double = 0.0,
    val kwhPorMes: Double = 0.0
)

class AcoesViewModel(private val repository: AcaoRepository) : ViewModel() {

    val acoes: StateFlow<List<AcaoEconomia>> = repository.observarTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _filtro = MutableStateFlow<TipoRecurso?>(null)
    val filtro: StateFlow<TipoRecurso?> = _filtro.asStateFlow()

    val acoesFiltradas: StateFlow<List<AcaoEconomia>> = combine(acoes, _filtro) { lista, filtro ->
        if (filtro == null) lista else lista.filter { it.recurso == filtro }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val resumo: StateFlow<ResumoConsumo> = acoes.map { lista ->
        val concluidas = lista.filter { it.concluida }
        ResumoConsumo(
            totalAcoes = lista.size,
            concluidas = concluidas.size,
            litrosPorMes = concluidas.filter { it.recurso == TipoRecurso.AGUA }.sumOf { it.economiaMensal() },
            kwhPorMes = concluidas.filter { it.recurso == TipoRecurso.ENERGIA }.sumOf { it.economiaMensal() }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ResumoConsumo())

    fun selecionarFiltro(recurso: TipoRecurso?) {
        _filtro.value = recurso
    }

    fun alternarConclusao(acao: AcaoEconomia) {
        viewModelScope.launch {
            repository.alterarStatus(acao.id, !acao.concluida)
        }
    }

    fun carregarSugestoes() {
        viewModelScope.launch { repository.inserirSugestoes() }
    }
}
