package br.com.fiap.consumoconsciente

import android.app.Application
import br.com.fiap.consumoconsciente.data.local.AppDatabase
import br.com.fiap.consumoconsciente.data.repository.AcaoRepository
class ConsumoConscienteApplication : Application() {
    val repository: AcaoRepository by lazy {
        AcaoRepository(AppDatabase.getInstance(this).acaoDao())
    }
}
