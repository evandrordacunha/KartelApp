package com.example.kartelapp;

public class Distribuidor {

    private String cnpj;
    private String razaoSocial;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;
    private String bandeira;
    private String precoVendaGasComum;
    private String precoVendaGasAditivada;
    private String precoVendaEtanol;
    private String precoVendaDiesel;
    private String precoVendaGnv;
    private String latitude;
    private String longitude;
    private String ultimaAtualizacao;
    //RAZAO SOCIAL;ENDERECO;BAIRRO;CIDADE;ESTADO;PAIS;BANDEIRA;GASOLINA COMUM;GASOLINA ADITIVADA;ETANOL;DIESEL;GNV;LATITUDE;LONGITUDE;DATA COLETA;


    /**
     *
     * @param cnpj
     * @param razaoSocial
     * @param endereco
     * @param bairro
     * @param cidade
     * @param estado
     * @param pais
     * @param bandeira
     * @param precoVendaGasComum
     * @param precoVendaGasAditivada
     * @param precoVendaEtanol
     * @param precoVendaDiesel
     * @param precoVendaGnv
     * @param latitude
     * @param longitude
     * @param ultimaAtualizacao
     */
    public Distribuidor(String cnpj, String razaoSocial, String endereco, String bairro, String cidade, String estado, String pais, String bandeira, String precoVendaGasComum, String precoVendaGasAditivada, String precoVendaEtanol, String precoVendaDiesel, String precoVendaGnv, String latitude, String longitude, String ultimaAtualizacao) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.bandeira = bandeira;
        this.precoVendaGasComum = precoVendaGasComum;
        this.precoVendaGasAditivada = precoVendaGasAditivada;
        this.precoVendaEtanol = precoVendaEtanol;
        this.precoVendaDiesel = precoVendaDiesel;
        this.precoVendaGnv = precoVendaGnv;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    /**
     * CONSTRUTOR PARA DADOS INFORMADOS PELOS USUARIOS
     *
     * @param razaoSocial
     * @param endereco
     * @param bairro
     * @param cidade
     * @param estado
     * @param bandeira
     * @param precoVendaGasComum
     * @param precoVendaGasAditivada
     * @param precoVendaEtanol
     * @param precoVendaDiesel
     * @param precoVendaGnv
     * @param ultimaAtualizacao
     */
    public Distribuidor(String razaoSocial, String endereco, String bairro, String cidade, String estado, String bandeira, String precoVendaGasComum, String precoVendaGasAditivada, String precoVendaEtanol, String precoVendaDiesel, String precoVendaGnv, String ultimaAtualizacao) {
        this.razaoSocial = razaoSocial;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.bandeira = bandeira;
        this.precoVendaGasComum = precoVendaGasComum;
        this.precoVendaGasAditivada = precoVendaGasAditivada;
        this.precoVendaEtanol = precoVendaEtanol;
        this.precoVendaDiesel = precoVendaDiesel;
        this.precoVendaGnv = precoVendaGnv;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getPrecoVendaGasComum() {
        return precoVendaGasComum;
    }

    public void setPrecoVendaGasComum(String precoVendaGasComum) {
        this.precoVendaGasComum = precoVendaGasComum;
    }

    public String getPrecoVendaGasAditivada() {
        return precoVendaGasAditivada;
    }

    public void setPrecoVendaGasAditivada(String precoVendaGasAditivada) {
        this.precoVendaGasAditivada = precoVendaGasAditivada;
    }

    public String getPrecoVendaEtanol() {
        return precoVendaEtanol;
    }

    public void setPrecoVendaEtanol(String precoVendaEtanol) {
        this.precoVendaEtanol = precoVendaEtanol;
    }

    public String getPrecoVendaDiesel() {
        return precoVendaDiesel;
    }

    public void setPrecoVendaDiesel(String precoVendaDiesel) {
        this.precoVendaDiesel = precoVendaDiesel;
    }

    public String getPrecoVendaGnv() {
        return precoVendaGnv;
    }

    public void setPrecoVendaGnv(String precoVendaGnv) {
        this.precoVendaGnv = precoVendaGnv;
    }

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}