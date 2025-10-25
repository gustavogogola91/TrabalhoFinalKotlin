package com.example.trabalhofinalkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalhofinalkotlin.dados.BancoDados
import com.example.trabalhofinalkotlin.dados.Produto
import com.example.trabalhofinalkotlin.dados.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProdutoViewModel(aplicacao: Application) : AndroidViewModel(aplicacao) {
    private val repositorio: ProdutoRepository
    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos.asStateFlow()

    private val _produtoSelecionado = MutableStateFlow<Produto?>(null)
    val produtoSelecionado: StateFlow<Produto?> = _produtoSelecionado.asStateFlow()

    init {
        val produtoDao = BancoDados.obterBancoDados(aplicacao, viewModelScope).produtoDao()
        repositorio = ProdutoRepository(produtoDao)

        viewModelScope.launch {
            repositorio.todosProdutos.collect { listaProdutos ->
                _produtos.value = listaProdutos
            }
        }
    }

    fun inserir(produto: Produto) = viewModelScope.launch {
        repositorio.inserir(produto)
    }

    fun carregarProdutoPorId(id: Int) = viewModelScope.launch {
        _produtoSelecionado.value = repositorio.obterProdutoPorId(id)
    }
}