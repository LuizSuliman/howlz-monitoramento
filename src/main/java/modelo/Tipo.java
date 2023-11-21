package modelo;

public class Tipo {
    protected String nome;

    public Tipo() {
    }

    public Tipo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return """
                Nome: %s""".formatted(nome);
    }
}
