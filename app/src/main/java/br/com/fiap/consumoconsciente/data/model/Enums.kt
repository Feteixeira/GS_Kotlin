package br.com.fiap.consumoconsciente.data.model

enum class TipoRecurso(val rotulo: String, val unidade: String) {
    AGUA("Água", "litros"),
    ENERGIA("Energia", "kWh")
}

enum class Frequencia(val rotulo: String, val vezesPorMes: Int) {
    DIARIA("Diária", 30),
    SEMANAL("Semanal", 4),
    MENSAL("Mensal", 1)
}
