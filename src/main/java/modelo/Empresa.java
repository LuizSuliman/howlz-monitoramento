package modelo;

public class Empresa {
    private Integer idEmpresa;
    private String razaoSocial;
    private String nomeFantasia;
    private String apelido;
    private String cnpj;

    public Empresa() {
    }

    public Empresa(String razaoSocial, String nomeFantasia, String apelido, String cnpj) {
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.apelido = apelido;
        this.cnpj = cnpj;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                Razão Social: %s
                Nome Fantasia: %s
                Apelido: %s
                CNPJ: %s""".formatted(idEmpresa, razaoSocial, nomeFantasia, apelido, cnpj);
    }
}
