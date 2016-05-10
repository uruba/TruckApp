package cz.uruba.ets2mpcompanion.interfaces.model;

import java.io.Serializable;

import cz.uruba.ets2mpcompanion.constants.URL;

public abstract class MeetupInfo implements Serializable {
    protected String server;
    protected String when;
    protected String location;
    protected String organiser;
    protected String language;
    protected String participants;
    protected String relativeURL;
    protected String baseURL;

    public MeetupInfo(String server, String when, String location, String organiser, String language, String participants, String relativeURL, String baseURL) {
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
