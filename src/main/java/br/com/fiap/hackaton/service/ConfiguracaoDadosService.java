package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.entity.HorarioDisponivelEntity;
import br.com.fiap.hackaton.entity.PacienteEntity;
import br.com.fiap.hackaton.entity.UnidadeEntity;
import br.com.fiap.hackaton.enums.TipoAgendamento;
import br.com.fiap.hackaton.repository.AgendamentoRepository;
import br.com.fiap.hackaton.repository.HorarioDisponivelRepository;
import br.com.fiap.hackaton.repository.PacienteRepository;
import br.com.fiap.hackaton.repository.UnidadeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ConfiguracaoDadosService {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguracaoDadosService.class);

    private final PacienteRepository pacienteRepository;
    private final UnidadeRepository unidadeRepository;
    private final HorarioDisponivelRepository horarioDisponivelRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final JdbcTemplate jdbcTemplate;

    public ConfiguracaoDadosService(PacienteRepository pacienteRepository,
                          UnidadeRepository unidadeRepository,
                          HorarioDisponivelRepository horarioDisponivelRepository,
                          AgendamentoRepository agendamentoRepository,
                          JdbcTemplate jdbcTemplate) {
        this.pacienteRepository = pacienteRepository;
        this.unidadeRepository = unidadeRepository;
        this.horarioDisponivelRepository = horarioDisponivelRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void reiniciarDados() {
        logger.info("Iniciando processo de reinicialização dos dados...");

        try {
            jdbcTemplate.execute("DELETE FROM agendamentos");
            logger.info("Tabela 'agendamentos' limpa com sucesso");
        } catch (DataAccessException ex) {
            logger.warn("Aviso: não foi possível limpar tabela 'agendamentos': {}", ex.getMessage());
        }

        try {
            agendamentoRepository.deleteAll();
            logger.debug("Repositório de agendamentos limpo via JPA");
        } catch (DataAccessException ex) {
            logger.warn("Aviso: limpeza do repositório de agendamentos falhou: {}", ex.getMessage());
        }

        try {
            horarioDisponivelRepository.deleteAll();
            logger.debug("Repositório de horários disponíveis limpo");
        } catch (DataAccessException ex) {
            logger.warn("Aviso: limpeza do repositório de horários disponíveis falhou: {}", ex.getMessage());
        }

        logger.info("Configurando pacientes de teste...");

        if (!pacienteRepository.existsById("p1")) {
            pacienteRepository.save(new PacienteEntity("p1", "Maria Silva", true));
            logger.info("Paciente 'Maria Silva' criado");
        } else {
            PacienteEntity p1 = pacienteRepository.findById("p1").orElseThrow();
            p1.setAtivo(true);
            p1.setNome("Maria Silva");
            pacienteRepository.save(p1);
            logger.info("Paciente 'Maria Silva' atualizado");
        }

        if (!pacienteRepository.existsById("p2")) {
            pacienteRepository.save(new PacienteEntity("p2", "João Souza", true));
            logger.info("Paciente 'João Souza' criado");
        } else {
            PacienteEntity p2 = pacienteRepository.findById("p2").orElseThrow();
            p2.setAtivo(true);
            p2.setNome("João Souza");
            pacienteRepository.save(p2);
            logger.info("Paciente 'João Souza' atualizado");
        }

        if (!pacienteRepository.existsById("p3")) {
            pacienteRepository.save(new PacienteEntity("p3", "Paciente Inativo", false));
            logger.info("Paciente inativo criado para testes");
        } else {
            PacienteEntity p3 = pacienteRepository.findById("p3").orElseThrow();
            p3.setAtivo(false);
            p3.setNome("Paciente Inativo");
            pacienteRepository.save(p3);
            logger.info("Paciente inativo atualizado");
        }

        logger.info("Configurando unidades de saúde...");

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
        logger.info("UBS Centro configurada com horários disponíveis");

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
        logger.info("UBS Bairro configurada com horários disponíveis");

        logger.info("Processo de reinicialização dos dados concluído com sucesso!");
    }
}
