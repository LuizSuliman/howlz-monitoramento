package servico;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.processos.Processo;
import dao.*;
import modelo.*;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import com.github.britooo.looca.api.util.Conversor;
import oshi.hardware.HWPartition;

public class Howlz {
    UsuarioDao usuarioDao = new UsuarioDao();
    EmpresaDao empresaDao = new EmpresaDao();
    ComputadorDao computadorDao = new ComputadorDao();
    ComponenteDao componenteDao = new ComponenteDao();
    MonitoramentoDao monitoramentoDao= new MonitoramentoDao();
    ProcessoDao processoDao = new ProcessoDao();
    JanelaDao janelaDao = new JanelaDao();
    Looca looca = new Looca();
    SystemInfo si = new SystemInfo();

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

    public void associarComputadorAoUsuario(String codigoPatrimonio, Integer idEmpresa) {
        Computador computador = new Computador(
                codigoPatrimonio,
                looca.getSistema().getSistemaOperacional(),
                looca.getProcessador().getId(),
                idEmpresa
        );
        computadorDao.salvar(computador);
    }

    public void cadastrarNovosComponentes(String numeroSerial) {
        SystemInfo si = new SystemInfo();
        Computador computador = computadorDao.buscarPeloSerial(numeroSerial);
        Componente cpu = new Componente(looca.getProcessador().getNome(), looca.getProcessador().getId(), computador.getIdComputador(), 1);
        computador.adicionarComponente(cpu);
        componenteDao.salvar(cpu);
        Componente ram = new Componente("Memória RAM ".concat(Conversor.formatarBytes(looca.getMemoria().getDisponivel())), "N/A", computador.getIdComputador(), 2);
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
        System.out.println(computador.getComponentes());
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
                        101,
                        componente.getIdComponente()
                );
                monitoramentoDao.salvar(usoCpu);
                System.out.println(usoCpu);
                break;
            case 2: // RAM
                // Uso de RAM
                valor = looca.getMemoria().getEmUso().doubleValue() / (1024 * 1024 * 1024);
                MonitoramentoComponente usoRam = new MonitoramentoComponente(
                        valor,
                        201,
                        componente.getIdComponente()
                );
                monitoramentoDao.salvar(usoRam);
                System.out.println(usoRam);
                break;
            case 3: // Disco
                // Uso de Disco
                List<Disco> discos = looca.getGrupoDeDiscos().getDiscos();
                Disco discoCerto = discos.get(0);
                for (Disco discoAtual : discos) {
                    if (discoAtual.getSerial().equals(componenteDao.buscarSerialDiscoPeloId(componente.getIdComponente()).getIdentificador())) {
                        discoCerto = discoAtual;
                    }
                }
                valor = ((discoCerto.getBytesDeEscritas().doubleValue() + discoCerto.getBytesDeLeitura().doubleValue()) / discoCerto.getTamanho().doubleValue()) * 100.;
                MonitoramentoComponente usoDisco = new MonitoramentoComponente(
                        valor,
                        301,
                        componente.getIdComponente()
                );
                monitoramentoDao.salvar(usoDisco);
                System.out.println(usoDisco);
                break;
            case 4: // GPU (INOVAÇÃO!)
                // Uso de GPU
                Process comando;
                if (computador.getSistemaOperacional().toLowerCase().contains("linux")) {
                    if (componente.getModelo().toLowerCase().contains("amd")) {
                        comando = Runtime.getRuntime().exec("/opt/amdgpu-pro/bin/amdgpu-pro-top -n 1 -b | grep -oP \"GPU[[:space:]]+\\K[0-9]+\"\n");
                    } else if (componente.getModelo().toLowerCase().contains("amd")) {
                        comando = Runtime.getRuntime().exec("nvidia-smi --query-gpu=utilization.gpu --format=csv,noheader,nounits");
                    } else if (componente.getModelo().toLowerCase().contains("intel")) {
                        comando = Runtime.getRuntime().exec("echo 30.00");
                    } else {
                        System.out.println("Placa de vídeo não conpátivel com o monitoramento");
                        break;
                    }
                } else if (computador.getSistemaOperacional().toLowerCase().contains("windows")) {
                    if (componente.getModelo().toLowerCase().contains("amd")) {
                        comando = Runtime.getRuntime().exec("ADL.exe -i 0 -s 0 -- -a | find \"Usage\"");
                    } else if (componente.getModelo().toLowerCase().contains("nvidia")) {
                        comando = Runtime.getRuntime().exec("nvidia-smi --query-gpu=utilization.gpu --format=csv,noheader,nounits");
                    } else if (componente.getModelo().toLowerCase().contains("intel")) {
                        comando = Runtime.getRuntime().exec("echo 30.00");
                    } else {
                        System.out.println("Placa de vídeo não conpátivel com o monitoramento");
                        break;
                    }
                } else {
                    System.out.println("Sistema operacional não compatível com monitoramento de GPU.");
                    break;
                }

                // Output final
                BufferedReader output = new BufferedReader(new InputStreamReader(comando.getInputStream()));
                System.out.println(output.readLine());

                valor = Double.valueOf(output.readLine());
                MonitoramentoComponente usoGPU = new MonitoramentoComponente(
                        valor,
                        401,
                        componente.getIdComponente()
                );
                monitoramentoDao.salvar(usoGPU);
                break;
            default:
                System.out.println("Tipo inválido");
                break;
        }
        if (valor > 90.) {
            return "Alerta";
        } else if (valor > 80.) {
            return "Crítico";
        } else {
            return "Bom";
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

    public String verificarEstadoCritico() {
        if (looca.getProcessador().getUso() > 80.0 || looca.getMemoria().getEmUso() > (looca.getMemoria().getTotal() - (looca.getMemoria().getTotal() / 10)) || looca.getMemoria().getEmUso() > (looca.getMemoria().getTotal() - (looca.getMemoria().getTotal() / 10))) {
            return "Crítico";
        } else if () {
            return "Alerta";
        } else {
            return "Bom";
        }
    }
}
