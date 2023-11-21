package modelo;

public class UnidadeMedida extends Tipo {
    private Integer idUnidadeMedida;
    private String simbolo;

    public UnidadeMedida() {
    }

    public UnidadeMedida(String nome, String simbolo) {
        super(nome);
        this.simbolo = simbolo;
    }

    public Integer getIdUnidadeMedida() {
        return idUnidadeMedida;
    }

    public void setIdUnidadeMedida(Integer idUnidadeMedida) {
        this.idUnidadeMedida = idUnidadeMedida;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                %s
                Símbolo: %s""".formatted(idUnidadeMedida, super.toString(), simbolo);
    }
}
