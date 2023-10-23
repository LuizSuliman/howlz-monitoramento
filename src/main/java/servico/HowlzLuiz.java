package servico;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.util.Conversor;
import dao.*;
import modelo.Componente;
import modelo.Computador;
import modelo.Monitoramento;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HowlzLuiz {
    UsuarioDao usuarioDao = new UsuarioDao();
    ComputadorDao computadorDao = new ComputadorDao();
    ComponenteDao componenteDao = new ComponenteDao();
    MonitoramentoDao monitoramentoDao= new MonitoramentoDao();
    ProcessoDao processoDao = new ProcessoDao();
    JanelaDao janelaDao = new JanelaDao();
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
                nome, looca.getSistema().getSistemaOperacional(), si.getHardware().getComputerSystem().getSerialNumber(), codigo, "Ligado", 1
        );
        computadorDao.salvar(computador);
    }

    public void cadastrarNovosComponentes(String numeroSerial) {
        SystemInfo si = new SystemInfo();
        Computador computador = computadorDao.buscarPeloSerial(numeroSerial);
        Componente cpu = new Componente("CPU", looca.getProcessador().getNome(), computador.getIdComputador());
        computador.adicionarComponente(cpu);
        componenteDao.salvar(cpu);
        Componente ram = new Componente("RAM", "Não especificado", computador.getIdComputador());
        computador.adicionarComponente(ram);
        componenteDao.salvar(ram);
        DiscoGrupo discoGrupo = looca.getGrupoDeDiscos();
        List<Disco> discos = discoGrupo.getDiscos();
        for (Disco discoAtual : discos) {
            Componente disco = new Componente("Disco", discoAtual.getModelo(), computador.getIdComputador(), discoAtual.getSerial());
            computador.adicionarComponente(disco);
            componenteDao.salvar(disco);
        }
        List<GraphicsCard> placasDeVideo = si.getHardware().getGraphicsCards();
        for (GraphicsCard gpuDaVez : placasDeVideo) {
            Componente gpu = new Componente("GPU", gpuDaVez.getName(), computador.getIdComputador());
            computador.adicionarComponente(gpu);
            componenteDao.salvar(gpu);
        }
        System.out.println(computador.getComponentes());
    }

    public void monitorarComponentes(Componente componente) {
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
                usoRam.setValor(looca.getMemoria().getEmUso().doubleValue() / (1024 * 1024 * 1024));
                monitoramentoDao.salvar(usoRam);
                System.out.println(usoRam);
                break;
            case "Disco":
                Monitoramento usoDisco = new Monitoramento();
                usoDisco.setTipo("GBTOTAL");
                usoDisco.setDataHora(LocalDateTime.now());
                usoDisco.setFkComponente(componente.getIdComponente());
                List<Disco> discos = looca.getGrupoDeDiscos().getDiscos();
                Disco discoCerto = discos.get(0);
                for (Disco discoAtual : discos) {
                    if (discoAtual.getSerial().equals(componenteDao.buscarSerialDiscoPeloId(componente.getIdComponente()))) {
                        discoCerto = discoAtual;
                    }
                }
                usoDisco.setValor(discoCerto.getTamanho().doubleValue() / (1024 * 1024 * 1024));
                monitoramentoDao.salvar(usoDisco);
                System.out.println(usoDisco);
                break;
            default:
                System.out.println("Tipo inválido");
                break;
        }

    }

    public void monitorarProcessos(Processo processoLooca, Integer idComputador) {
        modelo.Processo processo = new modelo.Processo(processoLooca.getPid(), processoLooca.getNome(), processoLooca.getUsoCpu(), processoLooca.getUsoMemoria(), Conversor.formatarBytes(processoLooca.getBytesUtilizados()), Conversor.formatarBytes(processoLooca.getMemoriaVirtualUtilizada()), idComputador, LocalDateTime.now());
        processoDao.salvar(processo);
    }

    public void monitorarJanelas(Janela janelaLooca, Integer idComputador, Boolean estadoCritico) {
        Integer visibilidade = (janelaLooca.isVisivel()) ?  1 : 0;
        modelo.Janela janela = new modelo.Janela(janelaLooca.getPid().intValue(), janelaLooca.getJanelaId().intValue(), janelaLooca.getComando(), janelaLooca.getTitulo(), janelaLooca.getLocalizacaoETamanho().toString(), visibilidade, idComputador, LocalDateTime.now());
        if (estadoCritico) {
            modelo.Processo processoRelacionado = processoDao.buscarPeloPid(janelaLooca.getPid().intValue());
            janela.setFkProcesso(processoRelacionado.getIdProcesso());
        }
        janelaDao.salvar(janela);
    }

    public List<String> estadoCritico() {
        List<String> razoesEstadoCritico = new ArrayList<>();
        // if (looca.getProcessador().getUso() > 90.0) {
        if (looca.getProcessador().getUso() > 0.) {
            razoesEstadoCritico.add("Uso da CPU acima de 90%");
        }
        // if (looca.getMemoria().getEmUso() > (looca.getMemoria().getTotal() - (looca.getMemoria().getTotal() / 10))) {
        if (looca.getMemoria().getEmUso() > (looca.getMemoria().getTotal() - (looca.getMemoria().getTotal()))) {
            razoesEstadoCritico.add("Uso da Memória RAM acima de 90%");
        }
        return razoesEstadoCritico;
    }

    // Projeto Individual do Luíz: Notificações de Alerta no Windows

    // Instânciando SystemTray do Computador, para adicionar a aplicação à Barra de Tarefas:
    SystemTray systemTray = SystemTray.getSystemTray();
    Image imagem = Toolkit.getDefaultToolkit().createImage("owlHowlz.png");

    TrayIcon trayIcon;
    public void exibirNaBandeja() throws AWTException {
        trayIcon = new TrayIcon(imagem, "Howlz");
        trayIcon.setImageAutoSize(true);
        systemTray.add(trayIcon);
        // Adicionando aplicação à barra de tarefas:
        trayIcon.displayMessage("Howlz Monitoramento", "Monitoramento Iniciado!", TrayIcon.MessageType.INFO);
    }

    public void emitirNotificacao(List<String> alertas) throws AWTException {
        // Gerando mensagem de alerta dinamicamente, de acordo com a quantidade de alertas:
        String mensagemAlerta = String.format("%d alerta(s):\n", alertas.size());
        for (String alertaAtual : alertas) {
            mensagemAlerta = String.format("%s%s\n", mensagemAlerta, alertaAtual);
        }

        // Exibindo notificação no Windows:
        trayIcon.displayMessage("Howlz Monitoramento", mensagemAlerta, TrayIcon.MessageType.WARNING);
    }
}
