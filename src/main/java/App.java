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
import servico.Slack;

import java.io.IOException;
import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Howlz howlz = new Howlz();
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
        howlz.atualizarUsuarioLocal(usuarioLogado);

        System.out.println(looca.getRede().getGrupoDeInterfaces().getInterfaces().get(1).getEnderecoMac());
        // Cadastro de novos computadores:
        if (howlz.computadorNaoCadastrado(looca.getRede().getGrupoDeInterfaces().getInterfaces().get(1).getEnderecoMac())) {
            System.out.println("Este computador ainda não está na nossa base de dados.\nQual o código de patrimônio que sua empresa usa para identificá-lo?");
            String codigo = in.next();
            System.out.println("Cadastrando computador...");
            howlz.cadastrarNovoComputador(codigo, usuarioLogado.getFkEmpresa());
            Computador computador = computadorDao.buscarPeloSerial(looca.getRede().getGrupoDeInterfaces().getInterfaces().get(1).getEnderecoMac());
            howlz.cadastrarNovosComponentes(computador);
            howlz.associarComputadorAoUsuario(usuarioLogado, computador);
        }

        Computador computador = computadorDao.buscarPeloSerial(looca.getRede().getGrupoDeInterfaces().getInterfaces().get(1).getEnderecoMac());
        List<Componente> componentes = componenteDao.buscarTodosPeloIdComputador(computador.getIdComputador());

        // Agendamendo tarefa de monitoramento:
        Timer timer = new Timer();
        int delay = 0; // Atraso inicial
        int intervalo = 30000; // Intervalo de tempo em milissegundos

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Monitorando...");
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                List<Janela> janelas = looca.getGrupoDeJanelas().getJanelasVisiveis();
                List<modelo.Janela> janelasModelo = new ArrayList<>();
                for (Janela janela : janelas) {
                    janelasModelo.add(howlz.monitorarJanelas(janela, computador.getIdComputador()));
                }
                howlz.salvarJanelas(janelasModelo);
            }
        }, delay, intervalo);
    }
}
