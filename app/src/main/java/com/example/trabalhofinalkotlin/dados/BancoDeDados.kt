package com.example.trabalhofinalkotlin.dados

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Produto::class], version = 1, exportSchema = false)
abstract class BancoDados : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    companion object {
        @Volatile
        private var INSTANCIA: BancoDados? = null

        fun obterBancoDados(contexto: Context, escopo: CoroutineScope): BancoDados {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    BancoDados::class.java,
                    "marketplace_banco_dados"
                )
                    .addCallback(CallbackBancoDados(escopo))
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }

        private class CallbackBancoDados(private val escopo: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCIA?.let { bancoDados ->
                    escopo.launch(Dispatchers.IO) {
                        popularBancoDados(bancoDados.produtoDao())
                    }
                }
            }
        }

        suspend fun popularBancoDados(produtoDao: ProdutoDao) {
            produtoDao.deletarTodos()

            val produtos = listOf(
                Produto(
                    nome = "Fone Bluetooth",
                    descricao = "Fone de ouvido bluetooth com cancelamento de ruído, bateria de 30 horas, áudio premium",
                    preco = 299.90,
                    icone = "🎧",
                    categoria = "Eletrônicos"
                ),
                Produto(
                    nome = "Teclado Mecânico",
                    descricao = "Teclado mecânico RGB com switches azuis, layout ABNT2, retroiluminação personalizável",
                    preco = 450.00,
                    icone = "⌨️",
                    categoria = "Eletrônicos"
                ),
                Produto(
                    nome = "Mouse Gamer",
                    descricao = "Mouse gamer 16000 DPI, RGB personalizável, 8 botões programáveis",
                    preco = 189.90,
                    icone = "🖱️",
                    categoria = "Eletrônicos"
                ),
                Produto(
                    nome = "Webcam HD",
                    descricao = "Webcam Full HD 1080p com microfone integrado, foco automático",
                    preco = 249.90,
                    icone = "📷",
                    categoria = "Eletrônicos"
                ),
                Produto(
                    nome = "Monitor 24\"",
                    descricao = "Monitor LED 24 polegadas Full HD 75Hz, painel IPS, HDMI e DisplayPort",
                    preco = 699.00,
                    icone = "🖥️",
                    categoria = "Eletrônicos"
                ),
                Produto(
                    nome = "Cadeira Gamer",
                    descricao = "Cadeira gamer ergonômica com ajuste de altura, reclinável até 180°",
                    preco = 899.90,
                    icone = "🪑",
                    categoria = "Móveis"
                )
            )

            produtoDao.inserirVarios(produtos)
        }
    }
}