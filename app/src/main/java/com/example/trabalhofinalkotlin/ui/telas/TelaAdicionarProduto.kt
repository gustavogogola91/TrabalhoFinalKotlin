package com.example.trabalhofinalkotlin.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.trabalhofinalkotlin.dados.Produto
import com.example.trabalhofinalkotlin.viewmodel.ProdutoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAdicionarProduto(
    viewModel: ProdutoViewModel,
    aoVoltar: () -> Unit,
    aoProdutoAdicionado: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var icone by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf("") }
    var mostrarMensagemSucesso by remember { mutableStateOf(false) }
    val escopo = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Produto") },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Preencha os dados do novo produto",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Produto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = mensagemErro.isNotEmpty() && nome.isEmpty()
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                isError = mensagemErro.isNotEmpty() && descricao.isEmpty()
            )

            OutlinedTextField(
                value = preco,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                        preco = it
                    }
                },
                label = { Text("Preço (R$)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = mensagemErro.isNotEmpty() && preco.isEmpty(),
                placeholder = { Text("299.90") }
            )

            OutlinedTextField(
                value = icone,
                onValueChange = { icone = it },
                label = { Text("Ícone (Emoji)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = mensagemErro.isNotEmpty() && icone.isEmpty(),
                placeholder = { Text("🎧") }
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = mensagemErro.isNotEmpty() && categoria.isEmpty(),
                placeholder = { Text("Eletrônicos") }
            )

            if (mensagemErro.isNotEmpty()) {
                Text(
                    text = mensagemErro,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    when {
                        nome.isEmpty() -> mensagemErro = "Por favor, preencha o nome do produto"
                        descricao.isEmpty() -> mensagemErro = "Por favor, preencha a descrição"
                        preco.isEmpty() -> mensagemErro = "Por favor, preencha o preço"
                        icone.isEmpty() -> mensagemErro = "Por favor, adicione um ícone (emoji)"
                        categoria.isEmpty() -> mensagemErro = "Por favor, preencha a categoria"
                        else -> {
                            val precoDouble = preco.toDoubleOrNull()
                            if (precoDouble == null || precoDouble <= 0) {
                                mensagemErro = "Por favor, insira um preço válido"
                            } else {
                                val novoProduto = Produto(
                                    nome = nome,
                                    descricao = descricao,
                                    preco = precoDouble,
                                    icone = icone,
                                    categoria = categoria
                                )
                                viewModel.inserir(novoProduto)
                                mostrarMensagemSucesso = true

                                escopo.launch {
                                    delay(1500)
                                    aoProdutoAdicionado()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !mostrarMensagemSucesso
            ) {
                Text(if (mostrarMensagemSucesso) "PRODUTO ADICIONADO!" else "ADICIONAR PRODUTO")
            }

            if (mostrarMensagemSucesso) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "✓ Produto adicionado com sucesso!",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}