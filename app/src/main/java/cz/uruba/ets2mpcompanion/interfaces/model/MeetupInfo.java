package cz.uruba.ets2mpcompanion.interfaces.model;

import java.io.Serializable;

public abstract class MeetupInfo implements Serializable {
    private String server;
    private String when;
    private String location;
    private String organiser;
    private String language;
    private String participants;
    private String relativeURL;
    private String baseURL;

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
