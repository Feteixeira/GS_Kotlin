package br.com.fiap.consumoconsciente.ui.screens.detalhe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.consumoconsciente.data.model.AcaoEconomia
import br.com.fiap.consumoconsciente.data.model.economiaMensal
import br.com.fiap.consumoconsciente.ui.components.EstadoVazio
import br.com.fiap.consumoconsciente.ui.components.IconeRecurso
import br.com.fiap.consumoconsciente.ui.components.formatarData
import br.com.fiap.consumoconsciente.ui.components.formatarNumero
import br.com.fiap.consumoconsciente.ui.viewmodel.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheScreen(
    snackbarHostState: SnackbarHostState,
    mostrarMensagem: (String) -> Unit,
    aoVoltar: () -> Unit,
    aoEditar: (Long) -> Unit,
    viewModel: DetalheViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var confirmarExclusao by remember { mutableStateOf(false) }
    val acao = uiState.acao

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da ação") },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (acao != null) {
                        IconButton(onClick = { aoEditar(acao.id) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        when {
            uiState.carregando -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            acao == null -> EstadoVazio(
                icone = Icons.Filled.SearchOff,
                titulo = "Ação não encontrada",
                mensagem = "Ela pode ter sido excluída.",
                modifier = Modifier.padding(innerPadding)
            )

            else -> ConteudoDetalhe(
                acao = acao,
                modifier = Modifier.padding(innerPadding),
                aoAlternarConclusao = {
                    viewModel.alternarConclusao()
                    mostrarMensagem(if (acao.concluida) "Ação reaberta" else "Boa! Ação concluída 🎉")
                },
                aoEditar = { aoEditar(acao.id) },
                aoExcluir = { confirmarExclusao = true }
            )
        }
    }

    if (confirmarExclusao && acao != null) {
        AlertDialog(
            onDismissRequest = { confirmarExclusao = false },
            icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
            title = { Text("Excluir ação?") },
            text = { Text("\"${acao.titulo}\" será removida permanentemente.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarExclusao = false
                    viewModel.excluir {
                        mostrarMensagem("Ação excluída")
                        aoVoltar()
                    }
                }) { Text("Excluir", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarExclusao = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun ConteudoDetalhe(
    acao: AcaoEconomia,
    aoAlternarConclusao: () -> Unit,
    aoEditar: () -> Unit,
    aoExcluir: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconeRecurso(acao.recurso)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(acao.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            if (acao.concluida) "Concluída ✓" else "Pendente",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (acao.concluida) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                if (acao.descricao.isNotBlank()) {
                    Text(acao.descricao, style = MaterialTheme.typography.bodyLarge)
                }
                HorizontalDivider()
                LinhaInfo("Recurso", acao.recurso.rotulo)
                LinhaInfo("Economia por vez", "${formatarNumero(acao.economiaPorVez)} ${acao.recurso.unidade}")
                LinhaInfo("Frequência", acao.frequencia.rotulo)
                LinhaInfo("Economia estimada/mês", "${formatarNumero(acao.economiaMensal())} ${acao.recurso.unidade}")
                LinhaInfo("Cadastrada em", formatarData(acao.criadaEm))
            }
        }

        if (acao.concluida) {
            OutlinedButton(onClick = aoAlternarConclusao, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Replay, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Reabrir ação")
            }
        } else {
            Button(onClick = aoAlternarConclusao, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Marcar como concluída")
            }
        }
        OutlinedButton(onClick = aoEditar, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Filled.Edit, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Editar")
        }
        OutlinedButton(
            onClick = aoExcluir,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Filled.Delete, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Excluir")
        }
    }
}

@Composable
private fun LinhaInfo(rotulo: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(rotulo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
