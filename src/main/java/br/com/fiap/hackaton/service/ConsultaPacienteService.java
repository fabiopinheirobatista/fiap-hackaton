package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.ConsultaPacienteResposta;
import br.com.fiap.hackaton.entity.AgendamentoEntity;
import br.com.fiap.hackaton.entity.UnidadeEntity;
import br.com.fiap.hackaton.enums.StatusAgendamento;
import br.com.fiap.hackaton.repository.AgendamentoRepository;
import br.com.fiap.hackaton.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConsultaPacienteService {

    private final AgendamentoRepository agendamentoRepository;
    private final UnidadeRepository unidadeRepository;

    private static final List<StatusAgendamento> STATUS_ATIVOS = Arrays.asList(
        StatusAgendamento.AGENDADA,
        StatusAgendamento.CONFIRMADA,
        StatusAgendamento.REAGENDADA
    );

    public ConsultaPacienteService(AgendamentoRepository agendamentoRepository,
                                  UnidadeRepository unidadeRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.unidadeRepository = unidadeRepository;
    }

    public List<ConsultaPacienteResposta> buscarConsultasAtivas(String pacienteId) {
        LocalDateTime agora = LocalDateTime.now();
        List<AgendamentoEntity> agendamentos = agendamentoRepository
            .findAgendamentosAtivosPorPaciente(pacienteId, agora, STATUS_ATIVOS);

        return converterParaResposta(agendamentos);
    }

    public List<ConsultaPacienteResposta> buscarHistoricoConsultas(String pacienteId) {
        LocalDateTime agora = LocalDateTime.now();
        List<AgendamentoEntity> agendamentos = agendamentoRepository
            .findHistoricoAtendimentosPorPaciente(pacienteId, agora);

        return converterParaResposta(agendamentos);
    }

    public List<ConsultaPacienteResposta> buscarTodasConsultas(String pacienteId) {
        List<AgendamentoEntity> agendamentos = agendamentoRepository
            .findAllByPacienteId(pacienteId);

        return converterParaResposta(agendamentos);
    }

    private List<ConsultaPacienteResposta> converterParaResposta(List<AgendamentoEntity> agendamentos) {
        if (agendamentos.isEmpty()) {
            return List.of();
        }

        List<String> unidadeIds = agendamentos.stream()
            .map(AgendamentoEntity::getUnidadeId)
            .distinct()
            .toList();

        Map<String, UnidadeEntity> unidadesMap = unidadeRepository.findAllById(unidadeIds)
            .stream()
            .collect(Collectors.toMap(UnidadeEntity::getId, Function.identity()));

        return agendamentos.stream()
            .map(agendamento -> {
                UnidadeEntity unidade = unidadesMap.get(agendamento.getUnidadeId());
                return new ConsultaPacienteResposta(
                    agendamento.getId(),
                    agendamento.getDataHora(),
                    unidade != null ? unidade.getNome() : "Unidade não encontrada",
                    unidade != null ? unidade.getLocalizacao() : null,
                    agendamento.getProfissionalId(),
                    agendamento.getTipo(),
                    agendamento.getStatus()
                );
            })
            .toList();
    }
}
