package br.com.fiap.consumoconsciente.ui.screens.formulario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.consumoconsciente.data.model.Frequencia
import br.com.fiap.consumoconsciente.data.model.TipoRecurso
import br.com.fiap.consumoconsciente.ui.components.icone
import br.com.fiap.consumoconsciente.ui.viewmodel.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    snackbarHostState: SnackbarHostState,
    aoVoltar: () -> Unit,
    aoSalvar: (mensagem: String) -> Unit,
    viewModel: FormularioViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val estado = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (estado.editando) "Editar ação" else "Nova ação") },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "Descreva um hábito que ajuda a economizar água ou energia na sua casa.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = estado.titulo,
                onValueChange = viewModel::alterarTitulo,
                label = { Text("Título da ação *") },
                placeholder = { Text("Ex.: Banho de 5 minutos") },
                isError = estado.erroTitulo != null,
                supportingText = { estado.erroTitulo?.let { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.descricao,
                onValueChange = viewModel::alterarDescricao,
                label = { Text("Descrição (opcional)") },
                placeholder = { Text("Como você vai colocar em prática?") },
                minLines = 2,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Recurso economizado", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TipoRecurso.entries.forEach { recurso ->
                    FilterChip(
                        selected = estado.recurso == recurso,
                        onClick = { viewModel.alterarRecurso(recurso) },
                        label = { Text(recurso.rotulo) },
                        leadingIcon = { Icon(recurso.icone(), contentDescription = null) }
                    )
                }
            }

            OutlinedTextField(
                value = estado.economiaTexto,
                onValueChange = viewModel::alterarEconomia,
                label = { Text("Economia por vez (${estado.recurso.unidade}) *") },
                placeholder = { Text(if (estado.recurso == TipoRecurso.AGUA) "Ex.: 45" else "Ex.: 0,5") },
                isError = estado.erroEconomia != null,
                supportingText = {
                    Text(estado.erroEconomia ?: "Uma estimativa já basta: quanto você deixa de gastar a cada vez.")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Frequência", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Frequencia.entries.forEach { frequencia ->
                    FilterChip(
                        selected = estado.frequencia == frequencia,
                        onClick = { viewModel.alterarFrequencia(frequencia) },
                        label = { Text(frequencia.rotulo) }
                    )
                }
            }

            Spacer(Modifier.size(4.dp))
            Button(
                onClick = { viewModel.salvar(aoSalvar) },
                enabled = !estado.salvando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estado.salvando) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Filled.Save, contentDescription = null)
                }
                Spacer(Modifier.width(8.dp))
                Text(if (estado.editando) "Salvar alterações" else "Cadastrar ação")
            }
            Text(
                "* Campos obrigatórios",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
