package cz.uruba.ets2mpcompanion.model;

import java.io.Serializable;

import cz.uruba.ets2mpcompanion.constants.URL;

public class MeetupInfo implements Serializable {
    private String server;
    private String when;
    private String location;
    private String organiser;
    private String language;
    private String participants;
    private String relativeURL;

    public MeetupInfo(String server, String when, String location, String organiser, String language, String participants, String relativeURL) {
        this.server = server;
        this.when = when;
        this.location = location;
        this.organiser = organiser;
        this.language = language;
        this.participants = participants;
        this.relativeURL = relativeURL;
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
        return URL.ETS2MP_CONVOYS + getRelativeURL();
    }
}
