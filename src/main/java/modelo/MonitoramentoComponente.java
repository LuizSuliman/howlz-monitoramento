package modelo;

public class MonitoramentoComponente extends Monitoramento {
    private Integer idMonitoramentoComponente;
    private Integer fkTipoMonitoramentoComponente;
    private Integer fkComponente;

    public MonitoramentoComponente() {
    }

    public MonitoramentoComponente(Double valor, Integer fkTipoMonitoramentoComponente, Integer fkComponente) {
        super(valor);
        this.fkTipoMonitoramentoComponente = fkTipoMonitoramentoComponente;
        this.fkComponente = fkComponente;
    }

    public Integer getIdMonitoramentoComponente() {
        return idMonitoramentoComponente;
    }

    public void setIdMonitoramentoComponente(Integer idMonitoramentoComponente) {
        this.idMonitoramentoComponente = idMonitoramentoComponente;
    }

    public Integer getFkTipoMonitoramentoComponente() {
        return fkTipoMonitoramentoComponente;
    }

    public void setFkTipoMonitoramentoComponente(Integer fkTipoMonitoramentoComponente) {
        this.fkTipoMonitoramentoComponente = fkTipoMonitoramentoComponente;
    }

    public Integer getFkComponente() {
        return fkComponente;
    }

    public void setFkComponente(Integer fkComponente) {
        this.fkComponente = fkComponente;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                %s
                Tipo de Monitoramento de Componente (FK): %d
                Componente (FK): %d""".formatted(idMonitoramentoComponente, super.toString(), fkTipoMonitoramentoComponente, fkComponente);
    }
}
