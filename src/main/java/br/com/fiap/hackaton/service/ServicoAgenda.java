package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.*;
import br.com.fiap.hackaton.entity.AgendamentoEntity;
import br.com.fiap.hackaton.entity.BloqueioEntity;
import br.com.fiap.hackaton.entity.HorarioDisponivelEntity;
import br.com.fiap.hackaton.entity.UnidadeEntity;
import br.com.fiap.hackaton.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServicoAgenda {

    private final UnidadeRepository repositorioUnidade;
    private final AgendamentoRepository repositorioAgendamento;
    private final BloqueioRepository repositorioBloqueio;
    private final HorarioDisponivelRepository repositorioHorario;
    private final ListaEsperaRepository repositorioListaEspera;

    public ServicoAgenda(UnidadeRepository repositorioUnidade,
                        AgendamentoRepository repositorioAgendamento,
                        BloqueioRepository repositorioBloqueio,
                        HorarioDisponivelRepository repositorioHorario,
                        ListaEsperaRepository repositorioListaEspera) {
        this.repositorioUnidade = repositorioUnidade;
        this.repositorioAgendamento = repositorioAgendamento;
        this.repositorioBloqueio = repositorioBloqueio;
        this.repositorioHorario = repositorioHorario;
        this.repositorioListaEspera = repositorioListaEspera;
    }

    public AgendaResponse visualizarAgenda(AgendaRequest requisicao) {
        UnidadeEntity unidade = repositorioUnidade.findById(requisicao.getUnidadeId())
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada"));

        if (!unidade.getTiposSuportados().contains(requisicao.getEspecialidade())) {
            throw new IllegalArgumentException("Unidade não suporta a especialidade informada");
        }

        LocalDateTime iniciodia = requisicao.getData().atStartOfDay();
        LocalDateTime fimDia = requisicao.getData().atTime(LocalTime.MAX);

        List<AgendamentoEntity> agendamentos = repositorioAgendamento
                .findByUnidadeIdAndDataHoraBetween(requisicao.getUnidadeId(), iniciodia, fimDia);

        List<BloqueioEntity> bloqueios = repositorioBloqueio
                .findByUnidadeIdAndDataHoraBetween(requisicao.getUnidadeId(), iniciodia, fimDia);

        List<HorarioDisponivelEntity> horariosDisponiveis = unidade.getHorariosDisponiveis().stream()
                .filter(h -> h.getDataHora().toLocalDate().equals(requisicao.getData()))
                .collect(Collectors.toList());

        int pacientesNaEspera = (int) repositorioListaEspera.countByUnidadeIdAndEspecialidade(
                requisicao.getUnidadeId(), requisicao.getEspecialidade().name());

        Map<String, List<HorarioDisponivelEntity>> horariosPorProfissional = horariosDisponiveis.stream()
                .filter(h -> h.getIdProfissional() != null)
                .collect(Collectors.groupingBy(HorarioDisponivelEntity::getIdProfissional));

        List<ProfissionalAgendaDTO> profissionais = horariosPorProfissional.entrySet().stream()
                .map(entry -> {
                    String profissionalId = entry.getKey();
                    List<HorarioDisponivelEntity> horarios = entry.getValue();

                    List<HorarioStatusDTO> horariosStatus = horarios.stream()
                            .map(horario -> {
                                String status = determinarStatusHorario(horario, agendamentos, bloqueios);
                                String observacao = obterObservacaoHorario(horario, agendamentos, bloqueios);
                                return new HorarioStatusDTO(horario.getDataHora(), status, observacao);
                            })
                            .collect(Collectors.toList());

                    return new ProfissionalAgendaDTO(profissionalId, "Dr. " + profissionalId, horariosStatus);
                })
                .collect(Collectors.toList());

        return new AgendaResponse(
                unidade.getId(),
                unidade.getNome(),
                requisicao.getData(),
                requisicao.getEspecialidade(),
                profissionais,
                pacientesNaEspera
        );
    }

    @Transactional
    public void criarBloqueio(BloqueioRequest requisicao) {
        UnidadeEntity unidade = repositorioUnidade.findById(requisicao.getUnidadeId())
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada"));

        if (requisicao.getProfissionalId() != null) {
            repositorioHorario.deletarPorUnidadeDataHoraEProfissional(
                    requisicao.getUnidadeId(),
                    requisicao.getDataHora(),
                    requisicao.getProfissionalId()
            );
        }

        BloqueioEntity bloqueio = new BloqueioEntity(
                UUID.randomUUID().toString(),
                unidade,
                requisicao.getDataHora(),
                requisicao.getProfissionalId(),
                requisicao.getMotivo()
        );

        repositorioBloqueio.save(bloqueio);
    }

    private String determinarStatusHorario(HorarioDisponivelEntity horario,
                                         List<AgendamentoEntity> agendamentos,
                                         List<BloqueioEntity> bloqueios) {

        boolean temBloqueio = bloqueios.stream()
                .anyMatch(b -> b.getDataHora().equals(horario.getDataHora()) &&
                        ((b.getIdProfissional() == null) ||
                         (b.getIdProfissional() != null && b.getIdProfissional().equals(horario.getIdProfissional()))));

        if (temBloqueio) {
            return "BLOQUEADO";
        }

        boolean temAgendamento = agendamentos.stream()
                .anyMatch(a -> a.getDataHora().equals(horario.getDataHora()) &&
                        a.getProfissionalId().equals(horario.getIdProfissional()));

        return temAgendamento ? "OCUPADO" : "VAGO";
    }

    private String obterObservacaoHorario(HorarioDisponivelEntity horario,
                                        List<AgendamentoEntity> agendamentos,
                                        List<BloqueioEntity> bloqueios) {

        return bloqueios.stream()
                .filter(b -> b.getDataHora().equals(horario.getDataHora()) &&
                        ((b.getIdProfissional() == null) ||
                         (b.getIdProfissional() != null && b.getIdProfissional().equals(horario.getIdProfissional()))))
                .findFirst()
                .map(BloqueioEntity::getMotivo)
                .orElse(null);
    }
}
