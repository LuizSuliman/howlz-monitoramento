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
import servico.Howlz;

import java.io.IOException;
import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Howlz howlz = new Howlz();
        Looca looca = new Looca();
        SystemInfo si = new SystemInfo();
        UsuarioDao usuarioDao = new UsuarioDao();
        ComputadorDao computadorDao = new ComputadorDao();
        ComponenteDao componenteDao = new ComponenteDao();

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

        // Cadastro de novos computadores:
        if (howlz.computadorNaoCadastrado(looca.getProcessador().getId())) {
            System.out.println("Este computador ainda não está na nossa base de dados.\nQual o código de patrimônio que sua empresa usa para identificá-lo?");
            String codigo = in.next();
            howlz.cadastrarNovoComputador(codigo, usuarioLogado.getFkEmpresa());
            howlz.cadastrarNovosComponentes(looca.getProcessador().getId());
        }

        Computador computador = computadorDao.buscarPeloSerial(looca.getProcessador().getId());
        List<Componente> componentes = componenteDao.buscarTodosPeloIdComputador(computador.getIdComputador());

        // Agendamendo tarefa de monitoramento:
        Timer timer = new Timer();
        int delay = 0; // Atraso inicial
        int intervalo = 15000; // Intervalo de tempo em milissegundos

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<String> statusComponentes = new ArrayList<>();
                for (Componente componente : componentes) {
                    try {
                        statusComponentes.add(howlz.monitorarComponentes(componente, computador));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                String estadoCritico = howlz.verificarEstadoCritico();
                if (estadoCritico) {
                    List<Processo> processos = looca.getGrupoDeProcessos().getProcessos();
                    for (Processo processo : processos) {
                        howlz.monitorarProcessos(processo, computador.getIdComputador());
                    }
                } else if () {

                }

                List<Janela> janelas = looca.getGrupoDeJanelas().getJanelasVisiveis();
                for (Janela janela : janelas) {
                    howlz.monitorarJanelas(janela, computador.getIdComputador(), estadoCritico);
                }

                System.out.println("Fim do Timer");
            }
        }, delay, intervalo);
    }
}
