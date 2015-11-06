package cz.uruba.ets2mpcompanion.model;

public class MeetupInfo {
    private String when;
    private String location;
    private String organiser;
    private String language;
    private String participants;

    public MeetupInfo(String when, String location, String organiser, String language, String participants) {
        this.when = when;
        this.location = location;
        this.organiser = organiser;
        this.language = language;
        this.participants = participants;
    }

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
}
