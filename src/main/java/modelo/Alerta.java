package modelo;

public class Alerta {
    private Integer idAlerta;
    private Double limite;
    private Integer fkTipoAlerta;
    private Integer fkTipoMonitoramentoComponente;

    public Alerta() {
    }

    public Alerta(Double limite, Integer fkTipoAlerta, Integer fkTipoMonitoramentoComponente) {
        this.limite = limite;
        this.fkTipoAlerta = fkTipoAlerta;
        this.fkTipoMonitoramentoComponente = fkTipoMonitoramentoComponente;
    }

    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
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

    @Override
    public String toString() {
        return """
                ID: %d
                Limite: %.2f
                Tipo de Alerta (FK): %d
                Tipo de Monitoramento de Componente (FK): %d""".formatted(idAlerta, limite, fkTipoAlerta, fkTipoMonitoramentoComponente);
    }
}
