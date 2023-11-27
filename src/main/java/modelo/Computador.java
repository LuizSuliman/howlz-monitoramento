package modelo;

import java.util.ArrayList;
import java.util.List;

public class Computador {
    private Integer idComputador;
    private String codigoPatrimonio;
    private String sistemaOperacional;
    private String numeroSerial;
    private Integer fkEmpresa;
    private List<Componente> componentes;

    public Computador() {
    }

    public Computador(String codigoPatrimonio, String sistemaOperacional, String numeroSerial, Integer fkEmpresa) {
        this.codigoPatrimonio = codigoPatrimonio;
        this.sistemaOperacional = sistemaOperacional;
        this.numeroSerial = numeroSerial;
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

    public String getCodigoPatrimonio() {
        return codigoPatrimonio;
    }

    public void setCodigoPatrimonio(String codigoPatrimonio) {
        this.codigoPatrimonio = codigoPatrimonio;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public void setSistemaOperacional(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public String getNumeroSerial() {
        return numeroSerial;
    }

    public void setNumeroSerial(String numeroSerial) {
        this.numeroSerial = numeroSerial;
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
        return """
                ID: %d
                Código do Patrimônio: %s
                Número Serial: %s
                Sistema Operacional: %s
                Empresa (FK): %d""".formatted(idComputador, codigoPatrimonio, numeroSerial, sistemaOperacional, fkEmpresa, componentes);
    }
}
