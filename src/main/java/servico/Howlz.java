package servico;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.processos.Processo;
import dao.*;
import modelo.*;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.github.britooo.looca.api.util.Conversor;

public class Howlz {
    UsuarioDao usuarioDao = new UsuarioDao();
    EmpresaDao empresaDao = new EmpresaDao();
    ComputadorDao computadorDao = new ComputadorDao();
    ComponenteDao componenteDao = new ComponenteDao();
    AssociacaoComputadorUsuarioDao associacaoComputadorUsuarioDao = new AssociacaoComputadorUsuarioDao();
    MonitoramentoComponenteDao monitoramentoComponenteDao = new MonitoramentoComponenteDao();
    MonitoramentoProcessoDao monitoramentoProcessoDao = new MonitoramentoProcessoDao();
    ProcessoDao processoDao = new ProcessoDao();
    JanelaDao janelaDao = new JanelaDao();
    Looca looca = new Looca();

    // Login
    public Boolean validarLogin(String email, String senha) {
        Integer usuarioTemLogin = usuarioDao.contarPeloEmailESenha(email, senha);
        return usuarioTemLogin == 1;
    }

    public Boolean computadorNaoCadastrado(String numeroSerial) {
        Integer computadorJaExiste = computadorDao.contarPeloSerial(numeroSerial);
        return computadorJaExiste == 0;
    }

    // Atualizar dados locais
    public void atualizarEmpresaDoUsuarioLocal(Integer fkUsuarioEmpresa) {
        Empresa empresaAtualizada = empresaDao.buscarPeloIdNuvem(fkUsuarioEmpresa);
        empresaDao.atualizarLocal(empresaAtualizada);
    }

    public void atualizarUsuarioLocal(Usuario usuarioAtualizado) {
        usuarioDao.atualizarLocal(usuarioAtualizado);
    }

    public void cadastrarNovoComputador(String codigoPatrimonio, Integer idEmpresa) {
        Computador computador = new Computador(
                codigoPatrimonio,
                looca.getSistema().getSistemaOperacional(),
                looca.getProcessador().getId(),
                idEmpresa
        );
        computadorDao.salvar(computador);
    }

    public void associarComputadorAoUsuario(Usuario usuario, Computador computador) {
        AssociacaoComputadorUsuario associacaoComputadorUsuario = new AssociacaoComputadorUsuario(
                usuario.getIdUsuario(),
                computador.getIdComputador(),
                LocalDateTime.now(),
                null
        );
        associacaoComputadorUsuarioDao.salvar(associacaoComputadorUsuario);
    }

    public void cadastrarNovosComponentes(Computador computador) {
        SystemInfo si = new SystemInfo();
        computador.setComponentes(new ArrayList<>());
        Componente cpu = new Componente(looca.getProcessador().getNome(), looca.getProcessador().getId(), computador.getIdComputador(), 1);
        computador.adicionarComponente(cpu);
        componenteDao.salvar(cpu);
        Componente ram = new Componente(String.format("Memória RAM %s", Conversor.formatarBytes(looca.getMemoria().getTotal())), "N/A", computador.getIdComputador(), 2);
        computador.adicionarComponente(ram);
        componenteDao.salvar(ram);
        DiscoGrupo discoGrupo = looca.getGrupoDeDiscos();
        List<Disco> discos = discoGrupo.getDiscos();
        for (Disco discoAtual : discos) {
            Componente disco = new Componente(discoAtual.getModelo(), discoAtual.getSerial(), computador.getIdComputador(), 3);
            computador.adicionarComponente(disco);
            componenteDao.salvar(disco);
        }
        List<GraphicsCard> placasDeVideo = si.getHardware().getGraphicsCards();
        for (GraphicsCard gpuDaVez : placasDeVideo) {
            Componente gpu = new Componente(gpuDaVez.getName(), gpuDaVez.getDeviceId(), computador.getIdComputador(), 4);
            computador.adicionarComponente(gpu);
            componenteDao.salvar(gpu);
        }
    }

    public String monitorarComponentes(Componente componente, Computador computador) throws IOException {
        // CPU
        Double valor = 0.;
        switch (componente.getFkTipoComponente()) {
            case 1: // CPU
                // Uso de CPU
                valor = looca.getProcessador().getUso();
                MonitoramentoComponente usoCpu = new MonitoramentoComponente(
                        valor,
                        1,
                        componente.getIdComponente()
                );
                monitoramentoComponenteDao.salvar(usoCpu);
                if (valor > 90.) {
                    return String.format("Crítico: Uso de CPU acima de 90%% no computador de código %s", computador.getCodigoPatrimonio());
                } else if (valor > 80.) {
                    return String.format("Alerta: Uso de CPU acima de 80%% no computador de código %s", computador.getCodigoPatrimonio());
                } else {
                    return "Bom";
                }
            case 2: // RAM
                // Uso de RAM
                valor = (looca.getMemoria().getEmUso().doubleValue() / (looca.getMemoria().getTotal().doubleValue())) * 100.;
                MonitoramentoComponente usoRam = new MonitoramentoComponente(
                        valor,
                        2,
                        componente.getIdComponente()
                );
                monitoramentoComponenteDao.salvar(usoRam);
                if (valor > 95.) {
                    return String.format("Crítico: Uso de RAM acima de 95%% no computador de código %s", computador.getCodigoPatrimonio());
                } else if (valor > 90.) {
                    return String.format("Alerta: Uso de RAM acima de 90%% no computador de código %s", computador.getCodigoPatrimonio());
                } else {
                    return "Bom";
                }
            case 3: // Disco
                // Uso de Disco
                List<Disco> discos = looca.getGrupoDeDiscos().getDiscos();
                Disco discoCerto = discos.get(0);
                for (Disco discoAtual : discos) {
                    if (discoAtual.getSerial().equals(componenteDao.buscarPeloIdentificador(componente.getIdentificador()).getIdentificador())) {
                        discoCerto = discoAtual;
                    }
                }
                valor = ((discoCerto.getBytesDeEscritas().doubleValue() + discoCerto.getBytesDeLeitura().doubleValue()) / discoCerto.getTamanho().doubleValue()) * 100.;
                MonitoramentoComponente usoDisco = new MonitoramentoComponente(
                        valor,
                        3,
                        componente.getIdComponente()
                );
                monitoramentoComponenteDao.salvar(usoDisco);
                if (valor > 90.) {
                    return String.format("Crítico: Uso de Disco acima de 90%% no computador de código %s", computador.getCodigoPatrimonio());
                } else if (valor > 85.) {
                    return String.format("Alerta: Uso de Disco acima de 85%% no computador de código %s", computador.getCodigoPatrimonio());
                } else {
                    return "Bom";
                }
            case 4: // GPU (INOVAÇÃO!)
                // Uso de GPU
                Process comando;
                if (computador.getSistemaOperacional().toLowerCase().contains("linux")) {
                    if (componente.getModelo().toLowerCase().contains("amd")) {
                        comando = Runtime.getRuntime().exec("/opt/amdgpu-pro/bin/amdgpu-pro-top -n 1 -b | grep -oP \"GPU[[:space:]]+\\K[0-9]+\"\n");
                    } else if (componente.getModelo().toLowerCase().contains("amd")) {
                        comando = Runtime.getRuntime().exec("nvidia-smi --query-gpu=utilization.gpu --format=csv,noheader,nounits");
                    } else if (componente.getModelo().toLowerCase().contains("intel")) {
                        // comando = Runtime.getRuntime().exec("echo 30.00");
                        return "GPU Inválida";
                    } else {
                        System.out.println("Placa de vídeo não compatível com o monitoramento");
                        break;
                    }
                } else if (computador.getSistemaOperacional().toLowerCase().contains("windows")) {
                    if (componente.getModelo().toLowerCase().contains("amd")) {
                        comando = Runtime.getRuntime().exec("ADL.exe -i 0 -s 0 -- -a | find \"Usage\"");
                    } else if (componente.getModelo().toLowerCase().contains("nvidia")) {
                        comando = Runtime.getRuntime().exec("nvidia-smi --query-gpu=utilization.gpu --format=csv,noheader,nounits");
                    } else if (componente.getModelo().toLowerCase().contains("intel")) {
                        // comando = Runtime.getRuntime().exec("echo 30.00");
                        return "GPU Inválida";
                    } else {
                        System.out.println("Placa de vídeo não conpátivel com o monitoramento");
                        return "GPU Inválida";
                    }
                } else {
                    System.out.println("Sistema operacional não compatível com monitoramento de GPU.");
                    return "Sistema não suportado";
                }

                // Output final
                BufferedReader output = new BufferedReader(new InputStreamReader(comando.getInputStream()));
                String retorno = output.readLine();

                if (retorno != null) {
                    valor = Double.valueOf(retorno);
                } else {
                    valor = 0.;
                }

                output.close();
                MonitoramentoComponente usoGPU = new MonitoramentoComponente(
                        valor,
                        4,
                        componente.getIdComponente()
                );
                monitoramentoComponenteDao.salvar(usoGPU);
                if (valor > 90.) {
                    return String.format("Crítico: Uso de GPU acima de 90%% no computador de código %s", computador.getCodigoPatrimonio());
                } else if (valor > 80.) {
                    return String.format("Alerta: Uso de GPU acima de 80%% no computador de código %s", computador.getCodigoPatrimonio());
                } else {
                    return "Bom";
                }
            default:
                System.out.println("Tipo inválido");
        }
        return "Bom";
    }

    public modelo.Processo monitorarProcessos(Processo processoLooca, Integer idComputador) {
        modelo.Processo processo = new modelo.Processo(processoLooca.getPid(), processoLooca.getNome(), idComputador);
        return processo;
    }

    public void salvarProcessos(List<modelo.Processo> processos) {
        processoDao.salvarMultiplos(processos);
    }

    public MonitoramentoProcesso monitorarUsoProcessos(Integer idProcesso, Integer idTipoComponente, Double uso, Integer idUnidadeMedida) {
        return new MonitoramentoProcesso(uso, idProcesso, idUnidadeMedida, idTipoComponente);
    }

    public void salvarMonitoramentoProcessos(List<MonitoramentoProcesso> monitoramentoProcessos) {
        monitoramentoProcessoDao.salvarMultiplos(monitoramentoProcessos);
    }

    public modelo.Janela monitorarJanelas(Janela janelaLooca, Integer idComputador) {
        Integer visibilidade = (janelaLooca.isVisivel()) ? 1 : 0;
        modelo.Janela janela = new modelo.Janela(janelaLooca.getPid().intValue(), janelaLooca.getComando(), janelaLooca.getTitulo(), visibilidade, idComputador);
        return janela;
    }

    public void salvarJanelas(List<modelo.Janela> janelas) {
        janelaDao.salvarMultiplos(janelas);
    }

    public List<String> alertasPassados = new ArrayList<>();

    public void enviarAlertas(List<String> alertas) throws IOException {
        for (String alerta : alertas) {
            Boolean enviadoRecentemente = false;
            for (String alertaPassado : alertasPassados) {
                if (alerta.equalsIgnoreCase(alertaPassado)) {
                    enviadoRecentemente = true;
                }
            }
            if (!enviadoRecentemente) {
                Slack.enviarMensagem(alerta);
            }
        }
        alertasPassados = alertas;
    }
}