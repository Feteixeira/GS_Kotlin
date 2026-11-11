package br.com.fiap.consumoconsciente.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "acoes_economia")
data class AcaoEconomia(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titulo: String,
    val descricao: String,
    val recurso: TipoRecurso,
    /** Quanto se economiza cada vez que a ação é praticada (litros ou kWh). */
    val economiaPorVez: Double,
    val frequencia: Frequencia,
    val concluida: Boolean = false,
    val criadaEm: Long = System.currentTimeMillis()
)

fun AcaoEconomia.economiaMensal(): Double = economiaPorVez * frequencia.vezesPorMes
