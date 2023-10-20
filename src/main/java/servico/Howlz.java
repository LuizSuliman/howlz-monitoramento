package servico;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import dao.ComponenteDao;
import dao.ComputadorDao;
import dao.MonitoramentoDao;
import dao.UsuarioDao;
import modelo.Componente;
import modelo.Computador;
import modelo.Monitoramento;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;

import java.time.LocalDateTime;
import java.util.List;

public class Howlz {
    UsuarioDao usuarioDao = new UsuarioDao();
    ComputadorDao computadorDao = new ComputadorDao();
    ComponenteDao componenteDao = new ComponenteDao();
    MonitoramentoDao monitoramentoDao= new MonitoramentoDao();
    Looca looca = new Looca();
    SystemInfo si = new SystemInfo();
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
        Computador computador = new Computador(
                nome, looca.getSistema().getSistemaOperacional(), si.getHardware().getComputerSystem().getSerialNumber(), codigo, Computador.Status.LIGADO, 1
        );
        computadorDao.salvar(computador);
    }

    public void cadastrarNovosComponentes(String numeroSerial) {
        SystemInfo si = new SystemInfo();
        Computador computador = computadorDao.buscarPeloSerial(numeroSerial);
        Componente cpu = new Componente(Componente.Tipo.CPU, looca.getProcessador().getNome(), computador.getIdComputador());
        computador.adicionarComponente(cpu);
        componenteDao.salvar(cpu);
        Componente ram = new Componente(Componente.Tipo.RAM, "Não especificado", computador.getIdComputador());
        computador.adicionarComponente(ram);
        componenteDao.salvar(ram);
        DiscoGrupo discoGrupo = looca.getGrupoDeDiscos();
        List<Disco> discos = discoGrupo.getDiscos();
        for (Disco discoAtual : discos) {
            Componente disco = new Componente(Componente.Tipo.DISCO, discoAtual.getModelo(), computador.getIdComputador());
            computador.adicionarComponente(disco);
            componenteDao.salvar(disco);
        }
        List<GraphicsCard> placasDeVideo = si.getHardware().getGraphicsCards();
        for (GraphicsCard gpuDaVez : placasDeVideo) {
            Componente gpu = new Componente(Componente.Tipo.GPU, gpuDaVez.getName(), computador.getIdComputador());
            computador.adicionarComponente(gpu);
            componenteDao.salvar(gpu);
        }
        System.out.println(computador.getComponentes());
    }

    public void monitorar(Componente componente) {
        // CPU
        switch (componente.getTipo()) {
            case "CPU":
                Monitoramento usoCpu = new Monitoramento();
                usoCpu.setTipo("PORCENTAGEMUSO");
                usoCpu.setDataHora(LocalDateTime.now());
                usoCpu.setFkComponente(componente.getIdComponente());
                usoCpu.setValor(looca.getProcessador().getUso());
                monitoramentoDao.salvar(usoCpu);
                System.out.println(usoCpu);
                break;
            case "RAM":
                Monitoramento usoRam = new Monitoramento();
                usoRam.setTipo("GBUSO");
                usoRam.setDataHora(LocalDateTime.now());
                usoRam.setFkComponente(componente.getIdComponente());
                usoRam.setValor(looca.getMemoria().getEmUso().doubleValue());
                monitoramentoDao.salvar(usoRam);
                System.out.println(usoRam);
                break;
            case "DISCO":
                Monitoramento usoDisco = new Monitoramento();
                usoDisco.setTipo("GBDISPONIVEL");
                usoDisco.setDataHora(LocalDateTime.now());
                usoDisco.setFkComponente(componente.getIdComponente());
                List<Disco> discos = looca.getGrupoDeDiscos().getDiscos();
                Disco discoCerto = discos.get(0);
                for (Disco discoAtual : discos) {
                    if (discoAtual.getSerial().equals(componenteDao.buscarSerialDiscoPeloId(componente.getIdComponente()))) {
                        discoCerto = discoAtual;
                    }
                }
                usoDisco.setValor(discoCerto.getTamanho().doubleValue());
                monitoramentoDao.salvar(usoDisco);
                System.out.println(usoDisco);
                break;
            default:
                System.out.println("Tipo inválido");
        }

    }
    public void monitorarRam(Integer fkComponente) {
        // CPU
        Monitoramento monitoramento = new Monitoramento(looca.getProcessador().getUso(), fkComponente);
        monitoramentoDao.salvar(monitoramento);
    }
    public void monitorarDisco(Integer fkComponente) {
        // CPU
        Monitoramento monitoramento = new Monitoramento(looca.getProcessador().getUso(), fkComponente);
        monitoramentoDao.salvar(monitoramento);
    }

}
