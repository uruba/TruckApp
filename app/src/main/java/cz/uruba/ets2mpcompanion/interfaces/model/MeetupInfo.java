package cz.uruba.ets2mpcompanion.interfaces.model;

import java.io.Serializable;

public abstract class MeetupInfo implements Serializable {
    private final String server;
    private final String when;
    private final String location;
    private final String organiser;
    private final String language;
    private final String participants;
    private final String relativeURL;
    private final String baseURL;

    protected MeetupInfo(String server, String when, String location, String organiser, String language, String participants, String relativeURL, String baseURL) {
        this.server = server;
        this.when = when;
        this.location = location;
        this.organiser = organiser;
        this.language = language;
        this.participants = participants;
        this.relativeURL = relativeURL;
        this.baseURL = baseURL;
    }

    public String getServer() { return server; }

    public String getWhen() {
        return when;
    }

    public String getLocation() {
        return location;
    }

    public String getOrganiser() {
        return organiser;
    }

    public String getLanguage() {
        return language;
    }

    public String getParticipants() {
        return participants;
    }

    public String getRelativeURL() {
        return relativeURL;
    }

    public String getAbsoluteURL() {
        return baseURL + getRelativeURL();
    }
}
