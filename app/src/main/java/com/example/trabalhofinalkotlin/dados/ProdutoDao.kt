package com.example.trabalhofinalkotlin.dados

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Query("SELECT * FROM produtos ORDER BY nome ASC")
    fun obterTodosProdutos(): Flow<List<Produto>>

    @Query("SELECT * FROM produtos WHERE id = :idProduto")
    suspend fun obterProdutoPorId(idProduto: Int): Produto?

    @Query("SELECT * FROM produtos WHERE categoria = :categoria")
    fun obterProdutosPorCategoria(categoria: String): Flow<List<Produto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(produto: Produto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirVarios(produtos: List<Produto>)

    @Delete
    suspend fun deletar(produto: Produto)

    @Query("DELETE FROM produtos")
    suspend fun deletarTodos()
}
