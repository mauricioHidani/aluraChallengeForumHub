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

    public static TopicStatus getByDescription(String description) {
        for (TopicStatus status : TopicStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        return TopicStatus.OPENED;
    }

}
