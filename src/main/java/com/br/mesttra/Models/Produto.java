package com.br.mesttra.Models;

public class Produto {
    private int id;
    public String nome_produto;
    public String categoria_produto;
    public Double preco_produto;

    public Produto(String nome_produto, String categoria_produto, Double preco_produto) {
        this.nome_produto = nome_produto;
        this.categoria_produto = categoria_produto;
        this.preco_produto = preco_produto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public String getCategoria_produto() {
        return categoria_produto;
    }

    public void setCategoria_produto(String categoria_produto) {
        this.categoria_produto = categoria_produto;
    }

    public Double getPreco_produto() {
        return preco_produto;
    }

    public void setPreco_produto(Double preco_produto) {
        this.preco_produto = preco_produto;
    }

    @Override
    public String toString() {
        return "id=" + id + 
        ",nome_produto=" + nome_produto + 
        ",categoria_produto=" + categoria_produto + 
        ",preco_produto=" + preco_produto ;
    }

    

    
}
