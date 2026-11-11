package br.com.fiap.consumoconsciente.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.consumoconsciente.data.model.AcaoEconomia


@Database(entities = [AcaoEconomia::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun acaoDao(): AcaoDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "consumo_consciente.db"
                ).build().also { instancia = it }
            }
    }
}
