package modelo;

import java.util.ArrayList;
import java.util.List;

public class Computador {
    private Integer idComputador;
    private String tipo;
    private String nome;
    private String sistemaOperacional;
    private String enderecoIP;
    private String numeroSerial;
    private String codigo;
    private String status;
    private Integer fkEmpresa;
    private List<Componente> componentes;

    public Computador() {
        this.componentes = new ArrayList<>();
    }
    public Computador(String nome, String sistemaOperacional, String numeroSerial, String codigo, String status, Integer fkEmpresa) {
        this.nome = nome;
        this.sistemaOperacional = sistemaOperacional;
        // this.enderecoIP = enderecoIP;
        this.numeroSerial = numeroSerial;
        this.codigo = codigo;
        this.status = status;
        this.fkEmpresa = fkEmpresa;
        this.componentes = new ArrayList<>();
    }

    public void adicionarComponente(Componente componente) {
        this.componentes.add(componente);
    }

    public Integer getIdComputador() {
        return idComputador;
    }

    public void setIdComputador(Integer idComputador) {
        this.idComputador = idComputador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public void setSistemaOperacional(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public String getEnderecoIP() {
        return enderecoIP;
    }

    public void setEnderecoIP(String enderecoIP) {
        this.enderecoIP = enderecoIP;
    }

    public String getNumeroSerial() {
        return numeroSerial;
    }

    public void setNumeroSerial(String numeroSerial) {
        this.numeroSerial = numeroSerial;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    public List<Componente> getComponentes() {
        return componentes;
    }

    public void setComponentes(List<Componente> componentes) {
        this.componentes = componentes;
    }

    @Override
    public String toString() {
        return "Computador{" +
                "nome='" + nome + '\'' +
                ", sistemaOperacional='" + sistemaOperacional + '\'' +
                ", enderecoIP='" + enderecoIP + '\'' +
                ", numeroSerial='" + numeroSerial + '\'' +
                ", codigo='" + codigo + '\'' +
                ", status=" + status +
                ", fkEmpresa=" + fkEmpresa +
                ", componentes=" + componentes +
                '}';
    }
}
