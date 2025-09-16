package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.*;
import br.com.fiap.hackaton.entity.*;
import br.com.fiap.hackaton.enums.MensagemErroEnum;
import br.com.fiap.hackaton.enums.StatusAgendamento;
import br.com.fiap.hackaton.enums.TipoAgendamento;
import br.com.fiap.hackaton.model.Agendamento;
import br.com.fiap.hackaton.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServicoAgendamento {
    private final PacienteRepository repositorioPaciente;
    private final UnidadeRepository repositorioUnidade;
    private final AgendamentoRepository repositorioAgendamento;
    private final HorarioDisponivelRepository repositorioHorario;
    private final TriagemRepository repositorioTriagem;

    private static final int HORAS_MINIMAS_PARA_CANCELAMENTO = 24;

    public ServicoAgendamento(PacienteRepository repositorioPaciente,
                              UnidadeRepository repositorioUnidade,
                              AgendamentoRepository repositorioAgendamento,
                              HorarioDisponivelRepository repositorioHorario,
                              TriagemRepository repositorioTriagem) {
        this.repositorioPaciente = repositorioPaciente;
        this.repositorioUnidade = repositorioUnidade;
        this.repositorioAgendamento = repositorioAgendamento;
        this.repositorioHorario = repositorioHorario;
        this.repositorioTriagem = repositorioTriagem;
    }

    private PacienteEntity buscarPacienteAtivo(String pacienteId) {
        PacienteEntity paciente = repositorioPaciente.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException(MensagemErroEnum.PACIENTE_NAO_ENCONTRADO.getMensagem()));
        if (!paciente.isAtivo()) {
            throw new IllegalArgumentException(MensagemErroEnum.PACIENTE_INATIVO.getMensagem());
        }
        return paciente;
    }

    private List<UnidadeEntity> buscarUnidadesDisponiveis(TipoAgendamento tipo, String localizacao) {
        List<UnidadeEntity> unidades = repositorioUnidade.buscarPorTipoELocalizacao(tipo, localizacao);
        if (unidades.isEmpty()) {
            unidades = repositorioUnidade.buscarPorTipoELocalizacao(tipo, null);
        }
        if (unidades.isEmpty()) {
            throw new IllegalStateException(MensagemErroEnum.UNIDADE_NAO_DISPONIVEL.getMensagem());
        }
        return unidades;
    }

    private void verificarDisponibilidadeHorario(String pacienteId, LocalDateTime dataHora,
                                                 String unidadeId, String idProfissional) {
        if (repositorioAgendamento.existePorPacienteIdEDataHoraEStatusDiferenteDe(
                pacienteId, dataHora, StatusAgendamento.CANCELADA)) {
            throw new IllegalStateException(MensagemErroEnum.CONFLITO_HORARIO.getMensagem());
        }

        int deletados = repositorioHorario.deletarPorUnidadeDataHoraEProfissional(
                unidadeId, dataHora, idProfissional
        );
        if (deletados == 0) {
            throw new IllegalStateException(MensagemErroEnum.HORARIO_INDISPONIVEL.getMensagem());
        }
    }

    public SugestaoResposta buscarSugestao(AgendamentoRequisicao requisicao) {
        buscarPacienteAtivo(requisicao.getPacienteId());
        TipoAgendamento tipo = requisicao.getTipo();
        String localizacao = requisicao.getLocalizacao();
        int urgencia = normalizarUrgencia(requisicao.getUrgencia());

        List<UnidadeEntity> unidades = buscarUnidadesDisponiveis(tipo, localizacao);

        return buscarMelhorHorario(unidades, localizacao, urgencia)
                .orElseThrow(() -> new IllegalStateException(MensagemErroEnum.NENHUM_HORARIO_DISPONIVEL.getMensagem()));
    }

    @Transactional
    public Agendamento confirmarAgendamento(String pacienteId, String unidadeId, LocalDateTime dataHora,
                                            String idProfissional, TipoAgendamento tipo) {
        buscarPacienteAtivo(pacienteId);
        verificarDisponibilidadeHorario(pacienteId, dataHora, unidadeId, idProfissional);

        AgendamentoEntity entidade = new AgendamentoEntity(
                UUID.randomUUID().toString(),
                pacienteId,
                unidadeId,
                idProfissional,
                tipo,
                dataHora,
                StatusAgendamento.AGENDADA
        );

        AgendamentoEntity salvo = repositorioAgendamento.save(entidade);
        return converterParaAgendamento(salvo);
    }

    public SugestaoResposta buscarSugestaoComTriagem(AgendamentoRequisicao requisicao, TriagemDTO triagemDTO) {
        buscarPacienteAtivo(requisicao.getPacienteId());
        TriagemEntity triagem = salvarTriagem(triagemDTO);

        List<UnidadeEntity> unidades = buscarUnidadesDisponiveis(
                requisicao.getTipo(),
                requisicao.getLocalizacao()
        );

        return buscarMelhorHorario(unidades, requisicao.getLocalizacao(), triagem.getUrgencia())
                .orElseThrow(() -> new IllegalStateException(MensagemErroEnum.NENHUM_HORARIO_DISPONIVEL.getMensagem()));
    }

    @Transactional
    public Agendamento confirmarAgendamentoComTriagem(String pacienteId, String unidadeId,
                                                      LocalDateTime dataHora, String idProfissional,
                                                      TipoAgendamento tipo, String triagemId) {
        buscarPacienteAtivo(pacienteId);
        verificarDisponibilidadeHorario(pacienteId, dataHora, unidadeId, idProfissional);

        TriagemEntity triagem = repositorioTriagem.findById(triagemId)
                .orElseThrow(() -> new IllegalArgumentException("Triagem não encontrada"));

        AgendamentoEntity entidade = new AgendamentoEntity(
                UUID.randomUUID().toString(),
                pacienteId,
                unidadeId,
                idProfissional,
                tipo,
                dataHora,
                StatusAgendamento.AGENDADA
        );
        entidade.setTriagem(triagem);

        AgendamentoEntity salvo = repositorioAgendamento.save(entidade);
        return converterParaAgendamento(salvo);
    }

    @Transactional
    public Agendamento reagendarAgendamento(String agendamentoId, String novaUnidadeId,
                                            LocalDateTime novaDataHora, String novoProfissionalId) {
        AgendamentoEntity entidade = repositorioAgendamento.findById(agendamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        if (entidade.getStatus() == StatusAgendamento.CANCELADA) {
            throw new IllegalStateException("Agendamento cancelado");
        }

        entidade.setUnidadeId(novaUnidadeId);
        entidade.setDataHora(novaDataHora);
        entidade.setProfissionalId(novoProfissionalId);
        entidade.setStatus(StatusAgendamento.REAGENDADA);

        AgendamentoEntity salvo = repositorioAgendamento.save(entidade);
        return converterParaAgendamento(salvo);
    }

    @Transactional
    public Agendamento confirmar(String pacienteId, String unidadeId, LocalDateTime dataHora,
                                  String idProfissional, TipoAgendamento tipo) {
        buscarPacienteAtivo(pacienteId);
        verificarDisponibilidadeHorario(pacienteId, dataHora, unidadeId, idProfissional);

        AgendamentoEntity entidade = new AgendamentoEntity(
                UUID.randomUUID().toString(),
                pacienteId,
                unidadeId,
                idProfissional,
                tipo,
                dataHora,
                StatusAgendamento.AGENDADA
        );

        AgendamentoEntity salvo = repositorioAgendamento.save(entidade);
        return converterParaAgendamento(salvo);
    }

    @Transactional
    public Agendamento confirmarComTriagem(String pacienteId, String unidadeId, LocalDateTime dataHora,
                                           String idProfissional, TipoAgendamento tipo, String triagemId) {
        buscarPacienteAtivo(pacienteId);
        verificarDisponibilidadeHorario(pacienteId, dataHora, unidadeId, idProfissional);

        TriagemEntity triagem = repositorioTriagem.findById(triagemId)
                .orElseThrow(() -> new IllegalArgumentException("Triagem não encontrada"));

        AgendamentoEntity entidade = new AgendamentoEntity(
                UUID.randomUUID().toString(),
                pacienteId,
                unidadeId,
                idProfissional,
                tipo,
                dataHora,
                StatusAgendamento.AGENDADA
        );
        entidade.setTriagem(triagem);

        AgendamentoEntity salvo = repositorioAgendamento.save(entidade);
        return converterParaAgendamento(salvo);
    }

    public SugestaoResposta sugerir(AgendamentoRequisicao requisicao) {
        buscarPacienteAtivo(requisicao.getPacienteId());
        TipoAgendamento tipo = requisicao.getTipo();
        String localizacao = requisicao.getLocalizacao();
        int urgencia = normalizarUrgencia(requisicao.getUrgencia());

        List<UnidadeEntity> unidades = buscarUnidadesDisponiveis(tipo, localizacao);

        return buscarMelhorHorario(unidades, localizacao, urgencia)
                .orElseThrow(() -> new IllegalStateException(MensagemErroEnum.NENHUM_HORARIO_DISPONIVEL.getMensagem()));
    }

    public SugestaoResposta sugerirComTriagem(AgendamentoRequisicao requisicao, TriagemDTO triagemDTO) {
        buscarPacienteAtivo(requisicao.getPacienteId());
        TriagemEntity triagem = salvarTriagem(triagemDTO);

        List<UnidadeEntity> unidades = buscarUnidadesDisponiveis(
                requisicao.getTipo(),
                requisicao.getLocalizacao()
        );

        return buscarMelhorHorario(unidades, requisicao.getLocalizacao(), triagem.getUrgencia())
                .orElseThrow(() -> new IllegalStateException(MensagemErroEnum.NENHUM_HORARIO_DISPONIVEL.getMensagem()));
    }

    @Transactional
    public Agendamento reagendar(String agendamentoId, String novaUnidadeId, LocalDateTime novaDataHora, String novoProfissionalId) {
        AgendamentoEntity entidade = repositorioAgendamento.findById(agendamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        if (entidade.getStatus() == StatusAgendamento.CANCELADA) {
            throw new IllegalStateException("Agendamento cancelado");
        }

        entidade.setUnidadeId(novaUnidadeId);
        entidade.setDataHora(novaDataHora);
        entidade.setProfissionalId(novoProfissionalId);
        entidade.setStatus(StatusAgendamento.REAGENDADA);

        AgendamentoEntity salvo = repositorioAgendamento.save(entidade);
        return converterParaAgendamento(salvo);
    }

    private TriagemEntity salvarTriagem(TriagemDTO triagemDTO) {
        TriagemEntity triagem = new TriagemEntity(
                triagemDTO.getPacienteId(),
                triagemDTO.getSintomas(),
                triagemDTO.getUrgencia(),
                triagemDTO.getMotivo()
        );
        return repositorioTriagem.save(triagem);
    }

    private Optional<SugestaoResposta> buscarMelhorHorario(List<UnidadeEntity> unidades,
                                                           String localizacao, int urgencia) {
        return unidades.stream()
                .flatMap(unidade -> {
                    List<HorarioDisponivelEntity> horarios = unidade.getHorariosDisponiveis();
                    Optional<HorarioDisponivelEntity> melhorHorario = horarios.stream()
                            .min(Comparator.comparing(HorarioDisponivelEntity::getDataHora));

                    return melhorHorario.map(horario -> new UnidadeComHorario(unidade, horario)).stream();
                })
                .min(Comparator.comparingDouble(uh -> calcularPontuacao(
                        uh.horario.getDataHora(),
                        localizacao,
                        uh.unidade.getLocalizacao(),
                        urgencia
                )))
                .map(uh -> new SugestaoResposta(
                        uh.unidade.getId(),
                        uh.unidade.getNome(),
                        uh.horario.getDataHora(),
                        uh.horario.getIdProfissional()
                ));
    }

    private int normalizarUrgencia(int urgencia) {
        return Math.max(1, Math.min(5, urgencia));
    }

    private Agendamento converterParaAgendamento(AgendamentoEntity entidade) {
        return new Agendamento(
                entidade.getId(),
                entidade.getPacienteId(),
                entidade.getUnidadeId(),
                entidade.getProfissionalId(), // Corrigido para usar o método correto
                entidade.getTipo(),
                entidade.getDataHora(),
                entidade.getStatus()
        );
    }

    private static double calcularPontuacao(LocalDateTime dataHorario, String localizacaoSolicitada,
                                            String localizacaoUnidade, int urgencia) {
        double pontos = Duration.between(LocalDateTime.now(), dataHorario).toDays();

        if (localizacaoSolicitada != null && !localizacaoSolicitada.isBlank() &&
                localizacaoSolicitada.equalsIgnoreCase(localizacaoUnidade)) {
            pontos -= 2.0;
        }

        pontos -= (urgencia - 1) * 0.5;
        return pontos;
    }


    private static class UnidadeComHorario {
        final UnidadeEntity unidade;
        final HorarioDisponivelEntity horario;

        UnidadeComHorario(UnidadeEntity unidade, HorarioDisponivelEntity horario) {
            this.unidade = unidade;
            this.horario = horario;
        }
    }

    public RecusaResposta registrarRecusa(RecusaRequisicao requisicao) {
        // valida paciente
        buscarPacienteAtivo(requisicao.getPacienteId());

        TipoAgendamento tipo = requisicao.getTipo();
        String localizacao = requisicao.getLocalizacao();
        int urgencia = normalizarUrgencia(requisicao.getUrgencia());

        List<UnidadeEntity> unidades = buscarUnidadesDisponiveis(tipo, localizacao);

        Optional<SugestaoResposta> proxima = unidades.stream()
                .flatMap(unidade -> {
                    List<HorarioDisponivelEntity> horariosFiltrados = unidade.getHorariosDisponiveis().stream()
                            .filter(h -> !(
                                    unidade.getId().equals(requisicao.getUnidadeId())
                                    && h.getDataHora().equals(requisicao.getDataHora())
                                    && ((requisicao.getProfissionalId() == null && h.getIdProfissional() == null)
                                        || (requisicao.getProfissionalId() != null && requisicao.getProfissionalId().equals(h.getIdProfissional())))
                            ))
                            .collect(Collectors.toList());

                    Optional<HorarioDisponivelEntity> melhorHorario = horariosFiltrados.stream()
                            .min(Comparator.comparing(HorarioDisponivelEntity::getDataHora));

                    return melhorHorario.map(horario -> new UnidadeComHorario(unidade, horario)).stream();
                })
                .min(Comparator.comparingDouble(uh -> calcularPontuacao(
                        uh.horario.getDataHora(),
                        localizacao,
                        uh.unidade.getLocalizacao(),
                        urgencia
                )))
                .map(uh -> new SugestaoResposta(
                        uh.unidade.getId(),
                        uh.unidade.getNome(),
                        uh.horario.getDataHora(),
                        uh.horario.getIdProfissional()
                ));

        if (proxima.isPresent()) {
            return new RecusaResposta("Sugestão recusada. Próxima sugestão gerada.", proxima.get());
        } else {
            return new RecusaResposta("Sugestão recusada. Nenhuma alternativa disponível no momento.", null);
        }
    }

    @Transactional
    public CancelamentoResposta cancelarAgendamento(String agendamentoId, String pacienteId) {
        AgendamentoEntity agendamento = repositorioAgendamento.findById(agendamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        if (!agendamento.getPacienteId().equals(pacienteId)) {
            throw new IllegalArgumentException("Este agendamento não pertence ao paciente informado");
        }

        if (agendamento.getStatus() == StatusAgendamento.CANCELADA) {
            throw new IllegalStateException("Agendamento já está cancelado");
        }

        LocalDateTime agora = LocalDateTime.now();
        if (agora.plus(Duration.ofHours(HORAS_MINIMAS_PARA_CANCELAMENTO)).isAfter(agendamento.getDataHora())) {
            throw new IllegalStateException("Não é possível cancelar agendamentos com menos de " +
                                          HORAS_MINIMAS_PARA_CANCELAMENTO + " horas de antecedência");
        }

        // Alterar status para CANCELADA
        agendamento.setStatus(StatusAgendamento.CANCELADA);
        repositorioAgendamento.save(agendamento);

        // Restaurar o horário na agenda do profissional
        HorarioDisponivelEntity horarioDisponivel = new HorarioDisponivelEntity();
        horarioDisponivel.setDataHora(agendamento.getDataHora());
        horarioDisponivel.setIdProfissional(agendamento.getProfissionalId());

        UnidadeEntity unidade = repositorioUnidade.findById(agendamento.getUnidadeId())
                .orElseThrow(() -> new IllegalStateException("Unidade não encontrada"));
        horarioDisponivel.setUnidade(unidade);

        repositorioHorario.save(horarioDisponivel);

        return new CancelamentoResposta(
                agendamento.getId(),
                agendamento.getPacienteId(),
                agendamento.getDataHora(),
                agendamento.getStatus(),
                "Agendamento cancelado com sucesso"
        );
    }
}
