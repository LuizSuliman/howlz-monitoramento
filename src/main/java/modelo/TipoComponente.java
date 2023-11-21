package modelo;

public class TipoComponente extends Tipo {
    private Integer idTipoComponente;

    public TipoComponente() {
    }

    public TipoComponente(String nome) {
        super(nome);
    }

    public Integer getIdTipoComponente() {
        return idTipoComponente;
    }

    public void setIdTipoComponente(Integer idTipoComponente) {
        this.idTipoComponente = idTipoComponente;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                %s""".formatted(idTipoComponente, super.toString());
    }
}
