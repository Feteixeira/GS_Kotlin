package br.com.fiap.consumoconsciente.data.repository

import br.com.fiap.consumoconsciente.data.local.AcaoDao
import br.com.fiap.consumoconsciente.data.model.AcaoEconomia
import br.com.fiap.consumoconsciente.data.model.Frequencia
import br.com.fiap.consumoconsciente.data.model.TipoRecurso
import kotlinx.coroutines.flow.Flow

class AcaoRepository(private val dao: AcaoDao) {

    fun observarTodas(): Flow<List<AcaoEconomia>> = dao.observarTodas()

    fun observarPorId(id: Long): Flow<AcaoEconomia?> = dao.observarPorId(id)

    suspend fun buscarPorId(id: Long): AcaoEconomia? = dao.buscarPorId(id)

    suspend fun inserir(acao: AcaoEconomia): Long = dao.inserir(acao)

    suspend fun atualizar(acao: AcaoEconomia) = dao.atualizar(acao)

    suspend fun excluir(acao: AcaoEconomia) = dao.excluir(acao)

    suspend fun alterarStatus(id: Long, concluida: Boolean) = dao.atualizarStatus(id, concluida)

    suspend fun inserirSugestoes() = dao.inserirVarias(
        listOf(
            AcaoEconomia(
                titulo = "Banho de 5 minutos",
                descricao = "Desligar o chuveiro ao se ensaboar e cronometrar o banho.",
                recurso = TipoRecurso.AGUA,
                economiaPorVez = 45.0,
                frequencia = Frequencia.DIARIA
            ),
            AcaoEconomia(
                titulo = "Torneira fechada ao escovar os dentes",
                descricao = "Usar um copo d'água para enxaguar a boca.",
                recurso = TipoRecurso.AGUA,
                economiaPorVez = 10.0,
                frequencia = Frequencia.DIARIA
            ),
            AcaoEconomia(
                titulo = "Tirar aparelhos do modo stand-by",
                descricao = "Desligar TV, micro-ondas e carregadores da tomada à noite.",
                recurso = TipoRecurso.ENERGIA,
                economiaPorVez = 0.3,
                frequencia = Frequencia.DIARIA
            ),
            AcaoEconomia(
                titulo = "Lavar roupa com a máquina cheia",
                descricao = "Juntar as roupas para fazer menos lavagens na semana.",
                recurso = TipoRecurso.ENERGIA,
                economiaPorVez = 1.5,
                frequencia = Frequencia.SEMANAL
            )
        )
    )
}
