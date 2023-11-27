package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.Computador;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class ComputadorDao {
    public void salvar(Computador novoComputador) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO Computador (codigoPatrimonio, sistemaOperacional, numeroSerial, fkEmpresa) VALUES (?, ?, ?, ?)";

        try {
            // Salvando no banco local
            con.update(sql, novoComputador.getCodigoPatrimonio(), novoComputador.getSistemaOperacional(), novoComputador.getNumeroSerial(), novoComputador.getFkEmpresa());

            // Salvando no banco do servidor
            conServer.update(sql, novoComputador.getCodigoPatrimonio(), novoComputador.getSistemaOperacional(), novoComputador.getNumeroSerial(), novoComputador.getFkEmpresa());

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();

        } finally {
            // Certifique-se de fechar as conexões, mesmo se ocorrer uma exceção
            if (con != null) {
                try {
                    con.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão local
                }
            }

            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }

    public List<Computador> buscarTodosPeloIdEmpresa(Integer idEmpresa) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Computador WHERE fkEmpresa = ?";

        try {
            // Buscando no banco local
            List<Computador> computadoresLocal = con.query(sql, new BeanPropertyRowMapper<>(Computador.class), idEmpresa);

            // Buscando no banco do servidor
            List<Computador> computadoresServer = conServer.query(sql, new BeanPropertyRowMapper<>(Computador.class), idEmpresa);

            return (computadoresLocal != null) ? computadoresLocal : computadoresServer;

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();
            return null;

        } finally {
            // Certifique-se de fechar as conexões, mesmo se ocorrer uma exceção
            if (con != null) {
                try {
                    con.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão local
                }
            }

            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }

    public List<Computador> buscarPeloId(Integer idComputador) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Computador WHERE idComputador = ?";

        try {
            // Buscando no banco local
            List<Computador> computadoresLocal = con.query(sql, new BeanPropertyRowMapper<>(Computador.class), idComputador);

            // Buscando no banco do servidor
            List<Computador> computadoresServer = conServer.query(sql, new BeanPropertyRowMapper<>(Computador.class), idComputador);

            return (computadoresLocal != null) ? computadoresLocal : computadoresServer;

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();
            return null;

        } finally {
            // Certifique-se de fechar as conexões, mesmo se ocorrer uma exceção
            if (con != null) {
                try {
                    con.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão local
                }
            }

            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }

    public Computador buscarPeloSerial(String numeroSerial) {
        // ConexaoMySQL conexao = new ConexaoMySQL();
        // JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Computador WHERE numeroSerial LIKE ? LIMIT 1";
        String sqlServer = "SELECT TOP 1 * FROM Computador WHERE numeroSerial LIKE ?";

        try {
            // Buscando no banco local
            // Computador computadorLocal = con.queryForObject(sql, new BeanPropertyRowMapper<>(Computador.class), "%" + numeroSerial + "%");

            // Buscando no banco do servidor
            Computador computadorServer = conServer.queryForObject(sqlServer, new BeanPropertyRowMapper<>(Computador.class), "%" + numeroSerial + "%");

            // Escolha qual computador retornar (pode ser lógica de negócios específica)
            // return (computadorLocal != null) ? computadorLocal : computadorServer;
            return computadorServer;

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();
            return null;

        } finally {
            // Certifique-se de fechar as conexões, mesmo se ocorrer uma exceção
            /*if (con != null) {
                try {
                    con.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão local
                }
            }*/

            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }

    public Integer contarPeloSerial(String numeroSerial) {
        // ConexaoMySQL conexao = new ConexaoMySQL();
        // JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT COUNT(*) FROM Computador WHERE numeroSerial LIKE ?";

        try {
            // Contando no banco local
            // Integer countLocal = con.queryForObject(sql, Integer.class, numeroSerial);

            // Contando no banco do servidor
            Integer countServer = conServer.queryForObject(sql, Integer.class, "%" + numeroSerial + "%");

            return countServer;

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();
            return 0;

        } finally {
            // Certifique-se de fechar as conexões, mesmo se ocorrer uma exceção
            /*
            if (con != null) {
                try {
                    con.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão local
                }
            }
            */

            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }
}
