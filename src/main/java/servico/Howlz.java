package servico;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import dao.*;
import modelo.*;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private static void LogLogin(String mensagemDoLog) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataHoraAtual = dateFormat.format(new Date());

        try (BufferedWriter arquivo = new BufferedWriter(new FileWriter("log-login.txt", true))) {
            arquivo.write(dataHoraAtual + " - " + mensagemDoLog + "\n");
            System.out.println("Log registrado com sucesso.");
        } catch (IOException mensagemErro) {
            System.err.println("Erro ao escrever no arquivo de log: " + mensagemErro.getMessage());
        }
    }


    public Boolean validarLogin(String email, String senha) {
        Integer usuarioTemLogin = usuarioDao.contarPeloEmailESenha(email, senha);
        if (usuarioTemLogin == 1) {
            LogLogin("Login efetuado pelo usuário do email " + email + " .\n");
            return true;
        } else {
            LogLogin("Falha no login do usuário com o email " + email + " .\n");
            return false;
        }
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
                gerarSerial(looca.getRede().getGrupoDeInterfaces().getInterfaces()),
                idEmpresa
        );
        computadorDao.salvar(computador);

        LogCadastroComputador(computador);
    }

    private void LogCadastroComputador(Computador computador) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataHoraAtual = dateFormat.format(new Date());

        try (BufferedWriter arquivo = new BufferedWriter(new FileWriter("log-cadastroComputador.txt", true))) {
            arquivo.write(dataHoraAtual + " - Novo computador cadastrado: Código Patrimônio " + computador.getCodigoPatrimonio() + " foi adicionado com sucesso.\n");
            System.out.println("Log de cadastro de computador registrado com sucesso.");
        } catch (IOException mensagemErro) {
            System.err.println("Erro ao escrever no arquivo de log: " + mensagemErro.getMessage());
        }
    }

    public String gerarSerial(List<RedeInterface> redeInterfaces) {
        String serial = "";
        for (RedeInterface redeInterface : redeInterfaces) {
            serial = String.format("%s %s", serial, redeInterface.getEnderecoMac());
        }
        return serial;
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


    private void LogMonitoramentoRAM(Monitoramento monitoramento, Computador computador) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataHoraAtual = dateFormat.format(new Date());

        try (BufferedWriter arquivo = new BufferedWriter(new FileWriter("log-monitoramentoComponenteRAM.txt", true))) {
            arquivo.write(dataHoraAtual + " - Monitoramento da RAM: A utilização atual da memória RAM no computador de ID " + computador.getIdComputador() + " é de " +  monitoramento.getValor()+"% \n");
            System.out.println("Log de monitoramento de componente registrado com sucesso.");
        } catch (IOException mensagemErro) {
            System.err.println("Erro ao escrever no arquivo de log: " + mensagemErro.getMessage());
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
                LogMonitoramentoRAM(usoRam, computador);
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
                    if (discoAtual.getSerial().equals(componenteDao.buscarPeloIdentificador(componente.getIdentificador(), computador.getIdComputador()).getIdentificador())) {
                        discoCerto = discoAtual;
                    }
                }
                valor = (discoCerto.getBytesDeEscritas().doubleValue() / discoCerto.getTamanho().doubleValue()) * 100.;
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
                if (computador.getSistemaOperacional().toLowerCase().contains("ubuntu")) {
                    if (componente.getModelo().toLowerCase().contains("amd")) {
                        comando = Runtime.getRuntime().exec("/opt/amdgpu-pro/bin/amdgpu-pro-top -n 1 -b | grep -oP \"GPU[[:space:]]+\\K[0-9]+\"\n");
                    } else if (componente.getModelo().toLowerCase().contains("amd")) {
                        comando = Runtime.getRuntime().exec("nvidia-smi --query-gpu=utilization.gpu --format=csv,noheader,nounits");
                    } else if (componente.getModelo().toLowerCase().contains("intel")) {
                        // comando = Runtime.getRuntime().exec("echo 30.00");
                        System.out.println("Placa de vídeo não compatível com o monitoramento");
                        return "Placa de vídeo não compatível com o monitoramento";
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
                        System.out.println("Placa de vídeo não conpatível com o monitoramento");
                        return "Placa de vídeo não compatível com o monitoramento";
                    } else {
                        System.out.println("Placa de vídeo não conpatível com o monitoramento");
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

    public void enviarAlertas(List<String> alertas) throws Exception {
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