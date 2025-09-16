package br.com.fiap.jackaton.service;

import br.com.fiap.jackaton.entity.*;
import br.com.fiap.jackaton.enums.TipoAgendamento;
import br.com.fiap.jackaton.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ConfiguracaoDadosService {

    private final PacienteRepository pacienteRepository;
    private final UnidadeRepository unidadeRepository;
    private final HorarioDisponivelRepository horarioDisponivelRepository;
    private final AgendamentoRepository agendamentoRepository;

    public ConfiguracaoDadosService(PacienteRepository pacienteRepository,
                          UnidadeRepository unidadeRepository,
                          HorarioDisponivelRepository horarioDisponivelRepository,
                          AgendamentoRepository agendamentoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.unidadeRepository = unidadeRepository;
        this.horarioDisponivelRepository = horarioDisponivelRepository;
        this.agendamentoRepository = agendamentoRepository;
    }

    @Transactional
    public void reiniciarDados() {
        agendamentoRepository.deleteAll();
        horarioDisponivelRepository.deleteAll();

        if (!pacienteRepository.existsById("p1")) {
            pacienteRepository.save(new PacienteEntity("p1", "Maria Silva", true));
        } else {
            PacienteEntity p1 = pacienteRepository.findById("p1").orElseThrow();
            p1.setAtivo(true);
            p1.setNome("Maria Silva");
            pacienteRepository.save(p1);
        }
        if (!pacienteRepository.existsById("p2")) {
            pacienteRepository.save(new PacienteEntity("p2", "João Souza", true));
        } else {
            PacienteEntity p2 = pacienteRepository.findById("p2").orElseThrow();
            p2.setAtivo(true);
            p2.setNome("João Souza");
            pacienteRepository.save(p2);
        }
        if (!pacienteRepository.existsById("p3")) {
            pacienteRepository.save(new PacienteEntity("p3", "Inativo", false));
        } else {
            PacienteEntity p3 = pacienteRepository.findById("p3").orElseThrow();
            p3.setAtivo(false);
            p3.setNome("Inativo");
            pacienteRepository.save(p3);
        }

        // Recria unidades e horários
        UnidadeEntity u1 = unidadeRepository.findById("u1").orElse(new UnidadeEntity("u1", "UBS Centro", "Centro"));
        u1.setNome("UBS Centro");
        u1.setLocalizacao("Centro");
        u1.setTiposSuportados(new ArrayList<>(Arrays.asList(TipoAgendamento.CLINICO_GERAL, TipoAgendamento.CARDIOLOGIA)));
        u1.getHorariosDisponiveis().clear();
        u1.adicionarHorario(new HorarioDisponivelEntity(
            LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0),
            "prof1"
        ));
        u1.adicionarHorario(new HorarioDisponivelEntity(
            LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0),
            "prof1"
        ));
        unidadeRepository.save(u1);

        UnidadeEntity u2 = unidadeRepository.findById("u2").orElse(new UnidadeEntity("u2", "UBS Bairro", "Bairro"));
        u2.setNome("UBS Bairro");
        u2.setLocalizacao("Bairro");
        u2.setTiposSuportados(new ArrayList<>(Arrays.asList(TipoAgendamento.CLINICO_GERAL, TipoAgendamento.PEDIATRIA)));
        u2.getHorariosDisponiveis().clear();
        u2.adicionarHorario(new HorarioDisponivelEntity(
            LocalDateTime.now().plusDays(1).withHour(11).withMinute(0).withSecond(0).withNano(0),
            "prof2"
        ));
        u2.adicionarHorario(new HorarioDisponivelEntity(
            LocalDateTime.now().plusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0),
            "prof2"
        ));
        unidadeRepository.save(u2);
    }
}
