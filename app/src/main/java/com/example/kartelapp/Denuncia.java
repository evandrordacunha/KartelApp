package com.example.kartelapp;

public class Denuncia {

    private String data;
    private String dataAtendimento;
    private String idUsuario;
    private String motivo;
    private String posto;
    private String comprovante;

    public Denuncia(String dataCadastro, String dataAtendimento, String comprovante, String emailUsuario, String motivo, String postoCnpj) {
        this.data = dataCadastro;
        this.dataAtendimento = dataAtendimento;
        this.idUsuario = emailUsuario;
        this.motivo = motivo;
        this.posto = postoCnpj;
        this.comprovante = comprovante;

    }


    public Denuncia() {
    }

    public String getData() {
        return data;
    }

    public String getDataAtendimento() {
        return dataAtendimento;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getPosto() {
        return posto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getComprovante() {
        return comprovante;
    }
}
