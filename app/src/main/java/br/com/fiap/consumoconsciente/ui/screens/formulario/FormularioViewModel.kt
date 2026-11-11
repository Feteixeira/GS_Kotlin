package br.com.fiap.consumoconsciente.ui.screens.formulario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.consumoconsciente.data.model.AcaoEconomia
import br.com.fiap.consumoconsciente.data.model.Frequencia
import br.com.fiap.consumoconsciente.data.model.TipoRecurso
import br.com.fiap.consumoconsciente.data.repository.AcaoRepository
import br.com.fiap.consumoconsciente.ui.navigation.Rotas
import kotlinx.coroutines.launch

data class FormularioUiState(
    val titulo: String = "",
    val descricao: String = "",
    val recurso: TipoRecurso = TipoRecurso.AGUA,
    val economiaTexto: String = "",
    val frequencia: Frequencia = Frequencia.DIARIA,
    val erroTitulo: String? = null,
    val erroEconomia: String? = null,
    val editando: Boolean = false,
    val salvando: Boolean = false
)

class FormularioViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AcaoRepository
) : ViewModel() {

    private val acaoId: Long = savedStateHandle.get<Long>(Rotas.ARG_ACAO_ID) ?: Rotas.SEM_ID
    private var acaoOriginal: AcaoEconomia? = null

    var uiState by mutableStateOf(FormularioUiState(editando = acaoId != Rotas.SEM_ID))
        private set

    init {
        if (acaoId != Rotas.SEM_ID) {
            viewModelScope.launch {
                repository.buscarPorId(acaoId)?.let { acao ->
                    acaoOriginal = acao
                    uiState = uiState.copy(
                        titulo = acao.titulo,
                        descricao = acao.descricao,
                        recurso = acao.recurso,
                        economiaTexto = formatarParaCampo(acao.economiaPorVez),
                        frequencia = acao.frequencia
                    )
                }
            }
        }
    }

    fun alterarTitulo(valor: String) {
        uiState = uiState.copy(titulo = valor, erroTitulo = null)
    }

    fun alterarDescricao(valor: String) {
        uiState = uiState.copy(descricao = valor)
    }

    fun alterarRecurso(valor: TipoRecurso) {
        uiState = uiState.copy(recurso = valor)
    }

    fun alterarEconomia(valor: String) {
        uiState = uiState.copy(economiaTexto = valor, erroEconomia = null)
    }

    fun alterarFrequencia(valor: Frequencia) {
        uiState = uiState.copy(frequencia = valor)
    }

    fun salvar(aoSalvar: (mensagem: String) -> Unit) {
        val titulo = uiState.titulo.trim()
        val economia = uiState.economiaTexto.trim().replace(',', '.').toDoubleOrNull()

        val erroTitulo = when {
            titulo.isEmpty() -> "Informe um título para a ação"
            titulo.length < 3 -> "O título precisa ter pelo menos 3 caracteres"
            else -> null
        }
        val erroEconomia = when {
            economia == null -> "Informe um número válido (ex.: 12,5)"
            economia <= 0.0 -> "A economia precisa ser maior que zero"
            else -> null
        }

        if (erroTitulo != null || erroEconomia != null || economia == null) {
            uiState = uiState.copy(erroTitulo = erroTitulo, erroEconomia = erroEconomia)
            return
        }

        uiState = uiState.copy(salvando = true)
        viewModelScope.launch {
            val original = acaoOriginal
            if (original == null) {
                repository.inserir(
                    AcaoEconomia(
                        titulo = titulo,
                        descricao = uiState.descricao.trim(),
                        recurso = uiState.recurso,
                        economiaPorVez = economia,
                        frequencia = uiState.frequencia
                    )
                )
                aoSalvar("Ação cadastrada com sucesso!")
            } else {
                repository.atualizar(
                    original.copy(
                        titulo = titulo,
                        descricao = uiState.descricao.trim(),
                        recurso = uiState.recurso,
                        economiaPorVez = economia,
                        frequencia = uiState.frequencia
                    )
                )
                aoSalvar("Ação atualizada!")
            }
        }
    }

    private fun formatarParaCampo(valor: Double): String =
        if (valor % 1.0 == 0.0) valor.toLong().toString()
        else valor.toString().replace('.', ',')
}
