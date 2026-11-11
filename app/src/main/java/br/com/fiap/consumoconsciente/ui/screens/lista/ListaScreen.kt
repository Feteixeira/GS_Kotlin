package br.com.fiap.consumoconsciente.ui.screens.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.consumoconsciente.data.model.TipoRecurso
import br.com.fiap.consumoconsciente.ui.components.AcaoCard
import br.com.fiap.consumoconsciente.ui.components.EstadoVazio
import br.com.fiap.consumoconsciente.ui.components.icone
import br.com.fiap.consumoconsciente.ui.viewmodel.AcoesViewModel
import br.com.fiap.consumoconsciente.ui.viewmodel.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    snackbarHostState: SnackbarHostState,
    mostrarMensagem: (String) -> Unit,
    aoVoltar: () -> Unit,
    aoAbrirAcao: (Long) -> Unit,
    aoNovaAcao: () -> Unit,
    viewModel: AcoesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val todas by viewModel.acoes.collectAsStateWithLifecycle()
    val acoes by viewModel.acoesFiltradas.collectAsStateWithLifecycle()
    val filtro by viewModel.filtro.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas ações") },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = aoNovaAcao,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Nova ação") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filtro == null,
                    onClick = { viewModel.selecionarFiltro(null) },
                    label = { Text("Todas") },
                    leadingIcon = { Icon(Icons.Filled.FilterList, contentDescription = null) }
                )
                TipoRecurso.entries.forEach { recurso ->
                    FilterChip(
                        selected = filtro == recurso,
                        onClick = { viewModel.selecionarFiltro(recurso) },
                        label = { Text(recurso.rotulo) },
                        leadingIcon = { Icon(recurso.icone(), contentDescription = null) }
                    )
                }
            }

            when {
                todas.isEmpty() -> EstadoVazio(
                    icone = Icons.Filled.Eco,
                    titulo = "Nenhuma ação cadastrada",
                    mensagem = "Toque em \"Nova ação\" para registrar seu primeiro hábito de economia " +
                        "ou carregue algumas sugestões para começar.",
                    acao = {
                        Button(onClick = {
                            viewModel.carregarSugestoes()
                            mostrarMensagem("Sugestões adicionadas!")
                        }) { Text("Carregar sugestões") }
                    }
                )

                acoes.isEmpty() -> EstadoVazio(
                    icone = filtro?.icone() ?: Icons.Filled.Eco,
                    titulo = "Nada por aqui",
                    mensagem = "Você ainda não tem ações de ${filtro?.rotulo?.lowercase() ?: "economia"}.",
                    acao = {
                        TextButton(onClick = { viewModel.selecionarFiltro(null) }) { Text("Ver todas") }
                    }
                )

                else -> LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(acoes, key = { it.id }) { acao ->
                        AcaoCard(
                            acao = acao,
                            aoClicar = { aoAbrirAcao(acao.id) },
                            aoAlternarConclusao = {
                                viewModel.alternarConclusao(acao)
                                mostrarMensagem(
                                    if (acao.concluida) "Ação reaberta" else "Boa! Ação concluída 🎉"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
