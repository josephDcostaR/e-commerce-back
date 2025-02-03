package com.br.mesttra;

import java.util.ArrayList;
import java.util.List;

import com.br.mesttra.Models.Produto;
import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Main {
    private static List<Produto> lista_produtos = new ArrayList<>();
    public static void main(String[] args) {
        Spark.port(8081);

        //Lidando com o CORS
        Spark.options("/*", new Route() {
            @Override
            public Object handle(Request requisicaoHttp, Response respostaHttp) throws Exception {

                String accessControlRequestHeaders = requisicaoHttp.headers("Access-Control-Request-Headers");
                
                if (accessControlRequestHeaders != null) 
                    respostaHttp.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                
                String accessControlRequestMethod = requisicaoHttp.headers("Access-Control-Request-Method");

                if (accessControlRequestMethod != null) 
                    respostaHttp.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                
                return "OK";
            }
        });

        //Informando o Browser que é aceito os metodos HTTP OPTIONS, GET, POST, PUT, DELETE para qualquer endereço
        Spark.before(new spark.Filter() {
            @Override
            public void handle(Request requisicaoHttp, Response respostaHttp) throws Exception {
                respostaHttp.header("Access-Control-Allow-Origin", "*"); // Permite todas as origens
                respostaHttp.header("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
            }
        });

        Spark.get("/listar", listar_produtos());
        Spark.post("/cadastrar", cadastrar_produto());
        Spark.put("/atualizar/:codigo", atualizar_produto());
        Spark.delete("/deletar/:codigo", deletar_produto());
    }

    private static Route listar_produtos() {
        return (Request Request, Response response) -> {
            response.type("application/json");
            response.status(200);
            return new Gson().toJson(lista_produtos);
        };
    }

    private static Route cadastrar_produto() {
        return (Request request, Response response) -> {
            Produto novo_produto = new Gson().fromJson(request.body(), Produto.class);
            novo_produto.setId(lista_produtos.size() + 1);

            if (novo_produto.getNome_produto() == null || novo_produto.getNome_produto().isEmpty()) {
                response.status(400);
                return new Gson().toJson("Nome do produto é obrigatório");
            }

            if (novo_produto.getCategoria_produto() == null || novo_produto.getCategoria_produto().isEmpty()) {
                response.status(400);
                return new Gson().toJson("Nome da categoria é obrigatório");

            }

            if (novo_produto.getPreco_produto() == null) {
                response.status(400);
                return new Gson().toJson("Valor do produto é obrigatório");
            }

            if (novo_produto.getPreco_produto() <= 0 ) {
                response.status(400);
                return new Gson().toJson("O preço deve ser maior que zero");
            }

            lista_produtos.add(novo_produto);

            response.type("application/json");
            response.status(201);
            return new Gson().toJson(novo_produto);
        };
    }

    private static Route atualizar_produto() {
        return (Request request, Response response) -> {
            int codigo;
            
            try {
                codigo = Integer.parseInt(request.params(":codigo"));
            } catch (NumberFormatException e) {
                // TODO: handle exception
                response.status(400);
                return new Gson().toJson("ID inválido");
            }        

            if (lista_produtos.isEmpty()) {
                response.status(404); 
                return new Gson().toJson("Não existem produtos na base.");

            } else {
                for(Produto produto : lista_produtos){
                    if (produto.getId()  == codigo) {

                        // Converte o JSON recebido no corpo da requisição para um objeto Contato
                        Produto produto_atualizado = new Gson().fromJson(request.body(), Produto.class);

                        // Atualiza os dados do contato

                        produto.setNome_produto(produto_atualizado.getNome_produto());
                        produto.setCategoria_produto(produto_atualizado.getCategoria_produto());
                        produto.setPreco_produto(produto_atualizado.getPreco_produto());
 
                        response.type("application/json");

                        response.status(200);

                        return new Gson().toJson("Produto com ID " + codigo + " foi atualizado com sucesso!");
                    }
                   
                }

                response.status(404); // 404 Not Found
                return new Gson().toJson("Produto com o ID " + codigo + " especificado não encontrado.");
            }
        };
    }

    private static Route deletar_produto() {
        return (Request request, Response response) -> {
            int codigo;
            
            try {
                codigo = Integer.parseInt(request.params(":codigo"));
            } catch (NumberFormatException e) {
                // TODO: handle exception
                response.status(400);
                return new Gson().toJson("ID inválido");
            }
            
            
            // Lógica de exclusão
            if (lista_produtos.isEmpty()) {
                response.status(404); // 404 Not Found
                return new Gson().toJson("Não existem produtos na base");
            } 

            boolean produto_removido = lista_produtos.removeIf(produto ->
            produto.getId() == codigo);

            if (produto_removido) {
                response.type("application/json"); // Define o tipo de conteúdo como JSON

                response.status(200);
                return new Gson().toJson("Produto com ID " + codigo + " deletado.");
            } else {
                response.status(404);
                return new Gson().toJson("Produto com ID " + codigo + " não encontrado.");
            }
        };
    }
    
}