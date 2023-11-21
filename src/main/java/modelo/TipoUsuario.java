package modelo;

public class TipoUsuario extends Tipo {
    private Integer idTipoUsuario;

    public TipoUsuario() {
    }

    public TipoUsuario(String nome) {
        super(nome);
    }

    public Integer getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(Integer idTipoUsuario) {
        idTipoUsuario = idTipoUsuario;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                %s""".formatted(idTipoUsuario, super.toString());
    }
}
