package br.com.fiap.consumoconsciente.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.consumoconsciente.data.model.AcaoEconomia
import kotlinx.coroutines.flow.Flow

@Dao
interface AcaoDao {

    @Query("SELECT * FROM acoes_economia ORDER BY concluida ASC, criadaEm DESC")
    fun observarTodas(): Flow<List<AcaoEconomia>>

    @Query("SELECT * FROM acoes_economia WHERE id = :id")
    fun observarPorId(id: Long): Flow<AcaoEconomia?>

    @Query("SELECT * FROM acoes_economia WHERE id = :id")
    suspend fun buscarPorId(id: Long): AcaoEconomia?

    @Insert
    suspend fun inserir(acao: AcaoEconomia): Long

    @Insert
    suspend fun inserirVarias(acoes: List<AcaoEconomia>)

    @Update
    suspend fun atualizar(acao: AcaoEconomia)

    @Delete
    suspend fun excluir(acao: AcaoEconomia)

    @Query("UPDATE acoes_economia SET concluida = :concluida WHERE id = :id")
    suspend fun atualizarStatus(id: Long, concluida: Boolean)
}
