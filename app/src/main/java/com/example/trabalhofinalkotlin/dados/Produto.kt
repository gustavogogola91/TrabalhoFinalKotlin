package com.example.trabalhofinalkotlin.dados

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val icone: String,
    val categoria: String
)