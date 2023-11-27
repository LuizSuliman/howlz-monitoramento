/*
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.processos.Processo;
import dao.ComponenteDao;
import dao.ComputadorDao;
import dao.UsuarioDao;
import modelo.Componente;
import modelo.Computador;
import modelo.Usuario;
import oshi.SystemInfo;
import servico.HowlzLuiz;

import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class AppLuiz {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        HowlzLuiz howlz = new HowlzLuiz();
        Looca looca = new Looca();
        SystemInfo si = new SystemInfo();
        UsuarioDao usuarioDao = new UsuarioDao();
        ComputadorDao computadorDao = new ComputadorDao();
        ComponenteDao componenteDao = new ComponenteDao();

        String continuarOuNao = "";
        Boolean cadastroValidado = false;
        Usuario usuarioLogado = null;

        do {
            System.out.println("Digite seu email:");
            String email = in.next();

            System.out.println("Digite sua senha:");
            String senha = in.next();

            cadastroValidado = howlz.validarLogin(email, senha);
            if (cadastroValidado) {
                usuarioLogado = usuarioDao.buscarPeloEmailESenha(email, senha);
                System.out.println("Bem-vindo %s!".formatted(usuarioLogado.getNome()));
            } else {
                System.out.println("Seu email ou senha estão incorretos!");
                System.out.println("Deseja tentar logar novamente? S/N");
                continuarOuNao = in.next();
                if (continuarOuNao.equalsIgnoreCase("N")) {
                    System.out.println("Até a próxima!");
                    System.exit(0);
                }
            }
        } while(!cadastroValidado);

        if (howlz.computadorNaoCadastrado(si.getHardware().getComputerSystem().getSerialNumber())) {
            System.out.println("Este computador ainda não está na nossa base de dados.\nQual o código que sua empresa usa para identificá-lo?");
            String codigo = in.next();
            howlz.cadastrarNovoComputador(usuarioLogado.getNome(), codigo);
            howlz.cadastrarNovosComponentes(si.getHardware().getComputerSystem().getSerialNumber());
        }

        try {
            howlz.exibirNaBandeja();
        } catch (AWTException e) {
            System.out.println("Monitoramento iniciado!");
        }

        Computador computador = computadorDao.buscarPeloSerial(si.getHardware().getComputerSystem().getSerialNumber());
        List<Componente> componentes = componenteDao.buscarTodosPeloIdComputador(computador.getIdComputador());

        // Agendamendo tarefa de monitoramento:
        Timer timer = new Timer();
        int delay = 0; // Atraso inicial
        int intervalo = 15000; // Intervalo de tempo em milissegundos

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Componente componente : componentes) {
                    howlz.monitorarComponentes(componente);
                }

                List<String> alertas = howlz.estadoCritico();
                if (!alertas.isEmpty()) {
                    List<Processo> processos = looca.getGrupoDeProcessos().getProcessos();
                    for (Processo processo : processos) {
                        howlz.monitorarProcessos(processo, computador.getIdComputador());
                    }

                    if (SystemTray.isSupported()) {
                        try {
                            howlz.emitirNotificacao(alertas);
                        } catch (AWTException e) {
                            System.out.println("Não foi possível criar a notificação: " + e);
                        }
                    } else {
                        System.out.println("Este dispositivo não tem suporte à tecnologia 'SystemTray'!");
                    }
                }

                List<Janela> janelas = looca.getGrupoDeJanelas().getJanelasVisiveis();
                for (Janela janela : janelas) {
                    howlz.monitorarJanelas(janela, computador.getIdComputador(), !alertas.isEmpty());
                }

                System.out.println("Fim do Timer");
            }
        }, delay, intervalo);
    }
}
*/
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.processos.Processo;
import dao.ComponenteDao;
import dao.ComputadorDao;
import dao.ProcessoDao;
import dao.UsuarioDao;
import modelo.Componente;
import modelo.Computador;
import modelo.MonitoramentoProcesso;
import modelo.Usuario;
import oshi.SystemInfo;
import servico.Howlz;
import servico.HowlzLuiz;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AppLuiz {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        HowlzLuiz howlz = new HowlzLuiz();
        Looca looca = new Looca();
        UsuarioDao usuarioDao = new UsuarioDao();
        ComputadorDao computadorDao = new ComputadorDao();
        ComponenteDao componenteDao = new ComponenteDao();
        ProcessoDao processoDao = new ProcessoDao();

        String continuarOuNao = "";
        Boolean cadastroValidado = false;
        Usuario usuarioLogado = null;

        // Login
        do {
            System.out.println("Digite seu email:");
            String email = in.next();

            System.out.println("Digite sua senha:");
            String senha = in.next();

            cadastroValidado = howlz.validarLogin(email, senha);
            if (cadastroValidado) {
                usuarioLogado = usuarioDao.buscarPeloEmailESenha(email, senha);
                System.out.println("Bem-vindo %s!".formatted(usuarioLogado.getNome()));
            } else {
                System.out.println("Seu email ou senha estão incorretos!");
                System.out.println("Deseja tentar logar novamente? S/N");
                continuarOuNao = in.next();
                if (continuarOuNao.equalsIgnoreCase("N")) {
                    System.out.println("Até a próxima!");
                    System.exit(0);
                }
            }
        } while (!cadastroValidado);

        // Obtendo/Atualizando informações para o banco local:
        howlz.atualizarEmpresaDoUsuarioLocal(usuarioLogado.getFkEmpresa());
        // howlz.atualizarAlertasDaEmpresaLocal(usuarioLogado.getFkEmpresa());
        howlz.atualizarUsuarioLocal(usuarioLogado);

        // Cadastro de novos computadores:
        if (howlz.computadorNaoCadastrado(looca.getProcessador().getId())) {
            System.out.println("Este computador ainda não está na nossa base de dados.\nQual o código de patrimônio que sua empresa usa para identificá-lo?");
            String codigo = in.next();
            howlz.cadastrarNovoComputador(codigo, usuarioLogado.getFkEmpresa());
            Computador computador = computadorDao.buscarPeloSerial(looca.getProcessador().getId());
            howlz.cadastrarNovosComponentes(computador);
            howlz.associarComputadorAoUsuario(usuarioLogado, computador);
        }

        try {
            howlz.exibirNaBandeja();
        } catch (AWTException e) {
            System.out.println("Monitoramento iniciado!");
        }

        Computador computador = computadorDao.buscarPeloSerial(looca.getProcessador().getId());
        List<Componente> componentes = componenteDao.buscarTodosPeloIdComputador(computador.getIdComputador());

        System.out.println(computador.getNumeroSerial());

        // Agendamendo tarefa de monitoramento:
        Timer timer = new Timer();
        int delay = 0; // Atraso inicial
        int intervalo = 30000; // Intervalo de tempo em milissegundos

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<String> alertas = new ArrayList<>();
                for (Componente componente : componentes) {
                    try {
                        String retornoStatus = howlz.monitorarComponentes(componente, computador);
                        if (retornoStatus.contains("Alerta") || retornoStatus.contains("Crítico")) {
                            alertas.add(retornoStatus);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                // String estadoCritico = howlz.verificarEstadoCritico();
                if (alertas.size() > 0) {
                    List<Processo> processos = looca.getGrupoDeProcessos().getProcessos();
                    List<modelo.Processo> processosModelo = new ArrayList<>();
                    List<MonitoramentoProcesso> monitoramentoProcessos = new ArrayList<>();

                    processos.sort(Comparator.comparing(Processo::getUsoMemoria));
                    Integer totalProcessos = processoDao.contarTodos();
                    for (int i = processos.size() - 1; i > processos.size() - 6; i --) {
                        processosModelo.add(howlz.monitorarProcessos(processos.get(i), computador.getIdComputador()));
                        totalProcessos ++;
                        monitoramentoProcessos.add(howlz.monitorarUsoProcessos(totalProcessos, 1, processos.get(i).getUsoCpu(), 1));
                        monitoramentoProcessos.add(howlz.monitorarUsoProcessos(totalProcessos, 2, processos.get(i).getUsoMemoria(), 2));
                    }

                    howlz.salvarProcessos(processosModelo);
                    howlz.salvarMonitoramentoProcessos(monitoramentoProcessos);

                    try {
                        howlz.enviarAlertas(alertas);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (SystemTray.isSupported()) {
                        try {
                            howlz.emitirNotificacao(alertas);
                        } catch (AWTException e) {
                            System.out.println("Não foi possível criar a notificação: " + e);
                        }
                    } else {
                        System.out.println("Este dispositivo não tem suporte à tecnologia 'SystemTray'!");
                    }
                }

                List<Janela> janelas = looca.getGrupoDeJanelas().getJanelasVisiveis();
                List<modelo.Janela> janelasModelo = new ArrayList<>();
                for (Janela janela : janelas) {
                    janelasModelo.add(howlz.monitorarJanelas(janela, computador.getIdComputador()));
                }
                howlz.salvarJanelas(janelasModelo);

                System.out.println("Fim do Timer");
            }
        }, delay, intervalo);
    }
}

