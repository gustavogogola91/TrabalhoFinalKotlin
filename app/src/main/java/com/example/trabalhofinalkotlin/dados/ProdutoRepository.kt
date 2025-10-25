package com.example.trabalhofinalkotlin.dados

import kotlinx.coroutines.flow.Flow

class ProdutoRepository(private val produtoDao: ProdutoDao) {
    val todosProdutos: Flow<List<Produto>> = produtoDao.obterTodosProdutos()

    suspend fun inserir(produto: Produto) {
        produtoDao.inserir(produto)
    }

    suspend fun obterProdutoPorId(id: Int): Produto? {
        return produtoDao.obterProdutoPorId(id)
    }

    fun obterProdutosPorCategoria(categoria: String): Flow<List<Produto>> {
        return produtoDao.obterProdutosPorCategoria(categoria)
    }
}