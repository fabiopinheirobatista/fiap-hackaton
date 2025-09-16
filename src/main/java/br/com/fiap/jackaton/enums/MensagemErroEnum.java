package br.com.fiap.jackaton.enums;

public enum MensagemErroEnum {
    PACIENTE_NAO_ENCONTRADO("Paciente não encontrado"),
    PACIENTE_INATIVO("Paciente inativo"),
    HORARIO_INDISPONIVEL("Horário não disponível"),
    CONFLITO_HORARIO("Conflito de horário para o paciente"),
    UNIDADE_NAO_DISPONIVEL("Nenhuma unidade disponível para o tipo solicitado na localização informada"),
    NENHUM_HORARIO_DISPONIVEL("Nenhum horário disponível");

    private final String mensagem;

    MensagemErroEnum(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
