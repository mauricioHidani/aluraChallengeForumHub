package br.com.alura.challenges.forum.hub.models.enums;

public enum TopicStatus {
    OPENED("aberto"),
    CLOSED("fechado")
    ;

    private String description;

    TopicStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
