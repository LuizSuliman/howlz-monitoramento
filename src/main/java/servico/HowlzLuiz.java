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
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HowlzLuiz extends Howlz {
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
    URL url = HowlzLuiz.class.getResource("/owlHowlz.png");
    Image imagem = Toolkit.getDefaultToolkit().getImage(url);

    TrayIcon trayIcon;
    public void exibirNaBandeja() throws AWTException {
        trayIcon = new TrayIcon(imagem, "Howlz");
        trayIcon.setImageAutoSize(true);
        systemTray.add(trayIcon);
        // Adicionando aplicação à barra de tarefas:
        trayIcon.displayMessage("Howlz Monitoramento", "Monitoramento Iniciado!", TrayIcon.MessageType.NONE);
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
