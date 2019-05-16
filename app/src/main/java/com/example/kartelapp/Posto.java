package com.example.kartelapp;

public class Posto {

    private String identificador;
    private String razaoSocial;
    private String cnpj;
    private String distribuidora;
    private String endereco;
    private String complemento;
    private String bairro;
    private String municipio;
    private String latitude;
    private String longitude;
    private String uf;


    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDistribuidora() {
        return distribuidora;
    }

    public void setDistribuidora(String distribuidora) {
        this.distribuidora = distribuidora;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String toString(){
        String s = "ID Posto:  "+getIdentificador() +"\n"
                +"Razão Social:  "+getRazaoSocial() +"\n"
                +"CNPJ:  " +getCnpj() +"\n"
                +"Bandeira:  "+getDistribuidora() +"\n"
                +"Endereço:  "+getEndereco() +"\n"
                +"Complemento:  "+getComplemento()+"\n"
                +"Bairro  " +getBairro() +"\n"
                +"Município:  "+getMunicipio() +"\n"
                +"UF:  " +getUf() +"\n"
                +"Latitude:  "+getLatitude() +"\n"
                +"Longitude:  "+getLongitude();
        return s;
    }

    public Posto(String identificador, String razaoSocial, String cnpj, String distribuidora, String endereco, String complemento, String bairro, String municipio, String latitude, String longitude, String uf) {
        this.identificador = identificador;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.distribuidora = distribuidora;
        this.endereco = endereco;
        this.complemento = complemento;
        this.bairro = bairro;
        this.municipio = municipio;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uf = uf;
    }
    public  Posto(){

    }

}
