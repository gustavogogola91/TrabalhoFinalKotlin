package com.example.trabalhofinalkotlin.ui.navegacao

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trabalhofinalkotlin.ui.telas.TelaDetalheProduto
import com.example.trabalhofinalkotlin.ui.telas.TelaLogin
import com.example.trabalhofinalkotlin.ui.telas.TelaPrincipal
import com.example.trabalhofinalkotlin.ui.telas.TelaAdicionarProduto
import com.example.trabalhofinalkotlin.viewmodel.ProdutoViewModel

sealed class Rotas(val rota: String) {
    object Login : Rotas("login")
    object Principal : Rotas("principal")
    object DetalheProduto : Rotas("detalhe/{produtoId}") {
        fun criarRota(produtoId: Int) = "detalhe/$produtoId"
    }
    object AdicionarProduto : Rotas("adicionar_produto")
}

@Composable
fun NavegacaoApp() {
    val controladorNavegacao = rememberNavController()
    val viewModel: ProdutoViewModel = viewModel()

    NavHost(
        navController = controladorNavegacao,
        startDestination = Rotas.Login.rota
    ) {
        composable(Rotas.Login.rota) {
            TelaLogin(
                aoFazerLogin = {
                    controladorNavegacao.navigate(Rotas.Principal.rota) {
                        popUpTo(Rotas.Login.rota) { inclusive = true }
                    }
                }
            )
        }

        composable(Rotas.Principal.rota) {
            TelaPrincipal(
                viewModel = viewModel,
                aoClicarProduto = { produto ->
                    controladorNavegacao.navigate(Rotas.DetalheProduto.criarRota(produto.id))
                },
                aoAdicionarProduto = {
                    controladorNavegacao.navigate(Rotas.AdicionarProduto.rota)
                }
            )
        }

        composable(
            route = Rotas.DetalheProduto.rota,
            arguments = listOf(navArgument("produtoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val produtoId = backStackEntry.arguments?.getInt("produtoId") ?: 0
            TelaDetalheProduto(
                viewModel = viewModel,
                produtoId = produtoId,
                aoVoltar = { controladorNavegacao.popBackStack() }
            )
        }

        composable(Rotas.AdicionarProduto.rota) {
            TelaAdicionarProduto(
                viewModel = viewModel,
                aoVoltar = { controladorNavegacao.popBackStack() },
                aoProdutoAdicionado = {
                    controladorNavegacao.popBackStack()
                }
            )
        }
    }
}