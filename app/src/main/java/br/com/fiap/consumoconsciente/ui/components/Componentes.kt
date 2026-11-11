package br.com.fiap.consumoconsciente.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.fiap.consumoconsciente.data.model.AcaoEconomia
import br.com.fiap.consumoconsciente.data.model.TipoRecurso
import br.com.fiap.consumoconsciente.data.model.economiaMensal
import br.com.fiap.consumoconsciente.ui.theme.CorAgua
import br.com.fiap.consumoconsciente.ui.theme.CorEnergia

fun TipoRecurso.icone(): ImageVector = when (this) {
    TipoRecurso.AGUA -> Icons.Filled.WaterDrop
    TipoRecurso.ENERGIA -> Icons.Filled.Bolt
}

fun TipoRecurso.cor(): Color = when (this) {
    TipoRecurso.AGUA -> CorAgua
    TipoRecurso.ENERGIA -> CorEnergia
}

@Composable
fun IconeRecurso(recurso: TipoRecurso, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(recurso.cor().copy(alpha = 0.15f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = recurso.icone(),
            contentDescription = recurso.rotulo,
            tint = recurso.cor()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcaoCard(
    acao: AcaoEconomia,
    aoClicar: () -> Unit,
    aoAlternarConclusao: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = aoClicar,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconeRecurso(acao.recurso)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .alpha(if (acao.concluida) 0.7f else 1f)
            ) {
                Text(
                    text = acao.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (acao.concluida) TextDecoration.LineThrough else null
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${acao.frequencia.rotulo} • ~${formatarNumero(acao.economiaMensal())} ${acao.recurso.unidade}/mês",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (acao.concluida) "Concluída ✓" else "Pendente",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (acao.concluida) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                )
            }
            Checkbox(
                checked = acao.concluida,
                onCheckedChange = { aoAlternarConclusao() }
            )
        }
    }
}

@Composable
fun EstadoVazio(
    icone: ImageVector,
    titulo: String,
    mensagem: String,
    modifier: Modifier = Modifier,
    acao: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icone,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(16.dp))
        Text(titulo, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(
            mensagem,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (acao != null) {
            Spacer(Modifier.height(20.dp))
            acao()
        }
    }
}
