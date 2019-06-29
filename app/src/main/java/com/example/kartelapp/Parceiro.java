package com.example.kartelapp;

public class Parceiro {

    private  String id;
    private String parceiro;
    private String atividade;
    private String endereco;
    private String bairro;
    private String cidade;
    private String fone;
    private String imagem;

    public Parceiro(String id, String parceiro, String atividade, String endereco, String bairro, String cidade, String fone, String imagem) {
        this.id = id;
        this.parceiro = parceiro;
        this.atividade = atividade;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.fone = fone;
        this.imagem = imagem;
    }

    public Parceiro() {
    }

    public String getId() {
        return id;
    }

    public String getParceiro() {
        return parceiro;
    }

    public String getAtividade() {
        return atividade;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getFone() {
        return fone;
    }

    public String getImagem() {
        return imagem;
    }
}
