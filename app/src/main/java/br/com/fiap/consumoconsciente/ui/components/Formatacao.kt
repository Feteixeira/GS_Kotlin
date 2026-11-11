package br.com.fiap.consumoconsciente.ui.components

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val localeBR: Locale = Locale.forLanguageTag("pt-BR")

fun formatarNumero(valor: Double): String =
    NumberFormat.getNumberInstance(localeBR).apply { maximumFractionDigits = 1 }.format(valor)

fun formatarData(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", localeBR).format(Date(millis))
