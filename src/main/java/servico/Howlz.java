package servico;

import com.github.britooo.looca.api.core.Looca;
import dao.ComputadorDao;
import dao.UsuarioDao;
import modelo.Computador;
import oshi.SystemInfo;

public class Howlz {
    UsuarioDao usuarioDao = new UsuarioDao();
    ComputadorDao computadorDao = new ComputadorDao();
    public Boolean validarLogin(String email, String senha) {
        Integer usuarioTemLogin = usuarioDao.contarPeloEmailESenha(email, senha);
        if (usuarioTemLogin == 1) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean computadorNaoCadastrado(String numeroSerial) {
        Integer computadorJaExiste = computadorDao.contarPeloSerial(numeroSerial);
        if (computadorJaExiste == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void cadastrarNovoComputador(String nome, String codigo) {
        Looca looca = new Looca();
        SystemInfo si = new SystemInfo();
        Computador computador = new Computador(
                nome, looca.getSistema().getSistemaOperacional(), si.getHardware().getComputerSystem().getSerialNumber(), codigo, Computador.Status.LIGADO, 1
        );
        computadorDao.salvar(computador);
    }

}
