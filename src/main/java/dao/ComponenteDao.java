package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.Componente;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class ComponenteDao {
    public void salvar(Componente novoComponente) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO Componente (modelo, identificador, fkComputador, fkTipoComponente) VALUES (?, ?, ?, ?)";

        try {
            // Salvando no banco local
            con.update(sql, novoComponente.getModelo(), novoComponente.getIdentificador(), novoComponente.getFkComputador(), novoComponente.getFkTipoComponente());

            // Salvando no banco do servidor
            conServer.update(sql, novoComponente.getModelo(), novoComponente.getIdentificador(), novoComponente.getFkComputador(), novoComponente.getFkTipoComponente());

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

    public List<Componente> buscarTodosPeloIdComputador(Integer idComputador) {
        // ConexaoMySQL conexao = new ConexaoMySQL();
        // JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Componente WHERE fkComputador = ?";

        try {
            // Buscando no banco local
            // List<Componente> componentesLocal = con.query(sql, new BeanPropertyRowMapper<>(Componente.class), idComputador);

            // Buscando no banco do servidor
            List<Componente> componentesServer = conServer.query(sql, new BeanPropertyRowMapper<>(Componente.class), idComputador);

            // return (componentesLocal != null) ? componentesLocal : componentesServer;
            return componentesServer;

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
                }catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }

    public List<Componente> buscarPeloTipoEIdComputador(String nomeTipo, Integer idComputador) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Componente WHERE tipo = ? AND fkComputador = ?";

        try {
            // Buscando no banco local
            List<Componente> componentesLocal = con.query(sql, new BeanPropertyRowMapper<>(Componente.class), nomeTipo, idComputador);

            // Buscando no banco do servidor
            List<Componente> componentesServer = conServer.query(sql, new BeanPropertyRowMapper<>(Componente.class), nomeTipo, idComputador);

            return (componentesLocal != null) ? componentesLocal : componentesServer;

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

    public Componente buscarPeloIdentificador(String identificador, Integer idComputador) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Componente WHERE fkTipoComponente = 3 AND identificador = ? AND fkComputador = ?";

        try {
            // Buscando no banco local
            Componente componenteLocal = con.queryForObject(sql, new BeanPropertyRowMapper<>(Componente.class), identificador, idComputador);

            // Buscando no banco do servidor
            Componente componenteServer = conServer.queryForObject(sql, new BeanPropertyRowMapper<>(Componente.class), identificador, idComputador);

            // Escolha qual componente retornar (pode ser lógica de negócios específica)
            return (componenteLocal != null) ? componenteLocal : componenteServer;

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
}
