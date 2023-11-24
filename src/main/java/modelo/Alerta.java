package modelo;

public class Alerta {
    private Integer idAlerta;
    private Double minimo;
    private Double maximo;
    private Integer fkTipoAlerta;
    private Integer fkTipoMonitoramentoComponente;
    private Integer fkEmpresa;

    public Alerta() {
    }

    public Alerta(Double minimo, Integer fkTipoAlerta, Integer fkTipoMonitoramentoComponente, Integer fkEmpresa) {
        this.minimo = minimo;
        this.fkTipoAlerta = fkTipoAlerta;
        this.fkTipoMonitoramentoComponente = fkTipoMonitoramentoComponente;
        this.fkEmpresa = fkEmpresa;
    }

    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Double getMinimo() {
        return minimo;
    }

    public Double getMaximo() {
        return maximo;
    }

    public void setMaximo(Double maximo) {
        this.maximo = maximo;
    }

    public void setMinimo(Double minimo) {
        this.minimo = minimo;
    }

    public Integer getFkTipoAlerta() {
        return fkTipoAlerta;
    }

    public void setFkTipoAlerta(Integer fkTipoAlerta) {
        this.fkTipoAlerta = fkTipoAlerta;
    }

    public Integer getFkTipoMonitoramentoComponente() {
        return fkTipoMonitoramentoComponente;
    }

    public void setFkTipoMonitoramentoComponente(Integer fkTipoMonitoramentoComponente) {
        this.fkTipoMonitoramentoComponente = fkTipoMonitoramentoComponente;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                Mínimo: %.2f
                Máximo: %.2f
                Tipo de Alerta (FK): %d
                Tipo de Monitoramento de Componente (FK): %d
                Empresa (FK): %d""".formatted(idAlerta, minimo, maximo, fkTipoAlerta, fkTipoMonitoramentoComponente, fkEmpresa);
    }
}
