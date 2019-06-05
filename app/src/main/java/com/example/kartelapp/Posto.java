package com.example.kartelapp;

public class Posto {
    private String nome;
    private String cnpj;
    private String bandeira;
    private String endereco;
    private String bairro;
    private String cidade;
    private String latitude;
    private String longitude;
    private String estado;
    private String pais;
    private String precoGasolinaAditivada;
    private String precoGasolinaComum;
    private String precoEtanol;
    private String precoDiesel;
    private String precoGnv;
    private String ultimaAtualizacao;

    public Posto() {
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getPrecoGasolinaAditivada() {
        return precoGasolinaAditivada;
    }

    public void setPrecoGasolinaAditivada(String precoGasolinaAditivada) {
        this.precoGasolinaAditivada = precoGasolinaAditivada;
    }

    public String getPrecoGasolinaComum() {
        return precoGasolinaComum;
    }

    public void setPrecoGasolinaComum(String precoGasolinaComum) {
        this.precoGasolinaComum = precoGasolinaComum;
    }

    public String getPrecoEtanol() {
        return precoEtanol;
    }

    public void setPrecoEtanol(String precoEtanol) {
        this.precoEtanol = precoEtanol;
    }

    public String getPrecoDiesel() {
        return precoDiesel;
    }

    public void setPrecoDiesel(String precoDiesel) {
        this.precoDiesel = precoDiesel;
    }

    public String getPrecoGnv() {
        return precoGnv;
    }

    public void setPrecoGnv(String precoGnv) {
        this.precoGnv = precoGnv;
    }

    public String getNome() {
        return nome;
    }

    public void setRazaoSocial(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String toString() {
        String s =
                "Razão Social:  " + getNome() + "\n"
                        + "CNPJ:  " + getCnpj() + "\n"
                        + "Bandeira:  " + getBandeira() + "\n"
                        + "Endereço:  " + getEndereco() + "\n"
                        + "Bairro  " + getBairro() + "\n"
                        + "Município:  " + getCidade() + "\n"
                        + "UF:  " + getEstado() + "\n"
                        + "País: " + getPais() + "\n"
                        + "Latitude:  " + getLatitude() + "\n"
                        + "Longitude:  " + getLongitude() + "\n"
                        + "Preço Gasolina Comum: " + getPrecoGasolinaComum() + "\n"
                        + "Preço Gasolina Aditivada: " + getPrecoGasolinaAditivada() + "\n"
                        + "Preço Etanol: " + getPrecoEtanol() + "\n"
                        + "Preço Diesel: " + getPrecoDiesel() + "\n"
                        + "Preço GNV " + getPrecoGnv() + "\n"
                        + "Última atualização: " + getUltimaAtualizacao();
        return s;
    }

    public Posto(String razaoSocial, String cnpj, String distribuidora, String endereco, String bairro, String municipio, String uf, String pais, String latitude, String longitude, String precoGasolinaAditivada, String precoGasolinaComum, String precoEtanol, String precoDiesel, String precoGnv, String ultimaAtualizacao) {
        this.nome = razaoSocial;
        this.cnpj = cnpj;
        this.bandeira = distribuidora;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = municipio;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estado = uf;
        this.pais = pais;
        this.precoGasolinaAditivada = precoGasolinaAditivada;
        this.precoGasolinaComum = precoGasolinaComum;
        this.precoEtanol = precoEtanol;
        this.precoDiesel = precoDiesel;
        this.precoGnv = precoGnv;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }




    /**
     * CONSTRUTOR USADO PARA O RECYCLERVIEW LISTAR OS POSTOS DA BASE COM DADOS SELECIONADOS
     * @param nome
     * @param endereco
     * @param bairro
     * @param cidade
     * @param precoGasolinaComum
     * @param precoGasolinaAditivada
     * @param precoEtanol
     * @param precoDiesel
     * @param precoGnv
     */
    public Posto(String nome, String endereco, String bairro, String cidade, String precoGasolinaComum, String precoGasolinaAditivada, String precoEtanol, String precoDiesel, String precoGnv){
        this.nome = nome;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.precoGasolinaComum = precoGasolinaComum;
        this.precoGasolinaAditivada = precoGasolinaAditivada;
        this.precoDiesel = precoDiesel;
        this.precoEtanol = precoEtanol;
        this.precoGnv = precoGnv;

    }
}
