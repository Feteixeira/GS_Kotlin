package br.com.fiap.consumoconsciente.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.consumoconsciente.ui.screens.detalhe.DetalheScreen
import br.com.fiap.consumoconsciente.ui.screens.formulario.FormularioScreen
import br.com.fiap.consumoconsciente.ui.screens.home.HomeScreen
import br.com.fiap.consumoconsciente.ui.screens.lista.ListaScreen
import kotlinx.coroutines.launch

object Rotas {
    const val ARG_ACAO_ID = "acaoId"
    const val SEM_ID = -1L

    const val HOME = "home"
    const val LISTA = "lista"
    const val DETALHE = "detalhe/{$ARG_ACAO_ID}"
    const val FORMULARIO = "formulario?$ARG_ACAO_ID={$ARG_ACAO_ID}"

    fun detalhe(id: Long) = "detalhe/$id"

    fun formulario(id: Long? = null) =
        if (id == null) "formulario" else "formulario?$ARG_ACAO_ID=$id"
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val mostrarMensagem: (String) -> Unit = { mensagem ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(mensagem)
        }
    }

    NavHost(navController = navController, startDestination = Rotas.HOME) {

        composable(Rotas.HOME) {
            HomeScreen(
                snackbarHostState = snackbarHostState,
                aoVerAcoes = { navController.navigate(Rotas.LISTA) },
                aoNovaAcao = { navController.navigate(Rotas.formulario()) }
            )
        }

        composable(Rotas.LISTA) {
            ListaScreen(
                snackbarHostState = snackbarHostState,
                mostrarMensagem = mostrarMensagem,
                aoVoltar = { navController.popBackStack() },
                aoAbrirAcao = { id -> navController.navigate(Rotas.detalhe(id)) },
                aoNovaAcao = { navController.navigate(Rotas.formulario()) }
            )
        }

        composable(
            route = Rotas.DETALHE,
            arguments = listOf(navArgument(Rotas.ARG_ACAO_ID) { type = NavType.LongType })
        ) {
            DetalheScreen(
                snackbarHostState = snackbarHostState,
                mostrarMensagem = mostrarMensagem,
                aoVoltar = { navController.popBackStack() },
                aoEditar = { id -> navController.navigate(Rotas.formulario(id)) }
            )
        }

        composable(
            route = Rotas.FORMULARIO,
            arguments = listOf(navArgument(Rotas.ARG_ACAO_ID) {
                type = NavType.LongType
                defaultValue = Rotas.SEM_ID
            })
        ) {
            FormularioScreen(
                snackbarHostState = snackbarHostState,
                aoVoltar = { navController.popBackStack() },
                aoSalvar = { mensagem ->
                    mostrarMensagem(mensagem)
                    navController.popBackStack()
                }
            )
        }
    }
}
