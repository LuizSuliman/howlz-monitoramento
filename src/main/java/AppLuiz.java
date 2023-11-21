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
