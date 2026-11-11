package br.com.fiap.consumoconsciente.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.consumoconsciente.data.model.TipoRecurso
import br.com.fiap.consumoconsciente.ui.components.cor
import br.com.fiap.consumoconsciente.ui.components.formatarNumero
import br.com.fiap.consumoconsciente.ui.components.icone
import br.com.fiap.consumoconsciente.ui.viewmodel.AcoesViewModel
import br.com.fiap.consumoconsciente.ui.viewmodel.AppViewModelProvider

private val dicas = listOf(
    "Um vazamento pequeno na descarga pode desperdiçar dezenas de litros por dia. Confira a sua!",
    "Lâmpadas LED gastam bem menos energia que as incandescentes para iluminar o mesmo ambiente.",
    "Reaproveite a água da máquina de lavar para limpar o quintal ou a calçada.",
    "Geladeira longe do fogão e do sol trabalha menos e consome menos energia.",
    "Varrer a calçada em vez de usar a mangueira economiza muita água.",
    "Aproveite a luz natural: abra cortinas antes de acender lâmpadas durante o dia."
)

@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    aoVerAcoes: () -> Unit,
    aoNovaAcao: () -> Unit,
    viewModel: AcoesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val resumo by viewModel.resumo.collectAsStateWithLifecycle()
    val dica = remember { dicas.random() }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Eco, contentDescription = null, modifier = Modifier.size(36.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "Consumo Consciente",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Pequenas ações em casa, grande economia de água e energia.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Text("Por que este app existe?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                "O desperdício de água e de energia pesa na conta no fim do mês e pressiona " +
                    "os recursos da cidade e o clima. O Consumo Consciente ajuda famílias e " +
                    "moradores a cadastrar ações de economia, acompanhar o que já colocaram em " +
                    "prática e ver quanto deixam de gastar.",
                style = MaterialTheme.typography.bodyMedium
            )

            Text("Seu impacto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CardIndicador(
                    titulo = "Ações concluídas",
                    valor = "${resumo.concluidas}/${resumo.totalAcoes}",
                    icone = Icons.Filled.Eco,
                    cor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                CardIndicador(
                    titulo = "Água/mês",
                    valor = "${formatarNumero(resumo.litrosPorMes)} L",
                    icone = TipoRecurso.AGUA.icone(),
                    cor = TipoRecurso.AGUA.cor(),
                    modifier = Modifier.weight(1f)
                )
                CardIndicador(
                    titulo = "Energia/mês",
                    valor = "${formatarNumero(resumo.kwhPorMes)} kWh",
                    icone = TipoRecurso.ENERGIA.icone(),
                    cor = TipoRecurso.ENERGIA.cor(),
                    modifier = Modifier.weight(1f)
                )
            }
            if (resumo.totalAcoes == 0) {
                Text(
                    "Você ainda não cadastrou nenhuma ação. Que tal começar agora?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(onClick = aoVerAcoes, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Minhas ações")
            }
            OutlinedButton(onClick = aoNovaAcao, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cadastrar nova ação")
            }

            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Dica do dia", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Text(dica, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            Text("Conectado aos ODS da ONU", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Column {
                SuggestionChip(onClick = {}, label = { Text("ODS 11 · Cidades e comunidades sustentáveis") })
                SuggestionChip(onClick = {}, label = { Text("ODS 13 · Ação contra a mudança global do clima") })
            }
        }
    }
}

@Composable
private fun CardIndicador(
    titulo: String,
    valor: String,
    icone: ImageVector,
    cor: Color,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(Modifier.padding(12.dp)) {
            Icon(icone, contentDescription = null, tint = cor)
            Spacer(Modifier.height(6.dp))
            Text(valor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(titulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
