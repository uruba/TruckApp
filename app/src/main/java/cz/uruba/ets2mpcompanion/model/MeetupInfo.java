package cz.uruba.ets2mpcompanion.model;

public class MeetupInfo {
    private String location;
    private String organiser;
    private String when;
    private String language;
    private int participants;

    public MeetupInfo(String location, String organiser, String when, String language, int participants) {
        this.location = location;
        this.organiser = organiser;
        this.when = when;
        this.language = language;
        this.participants = participants;
    }

    public String getLocation() {
        return location;
    }

    public String getOrganiser() {
        return organiser;
    }

    public String getWhen() {
        return when;
    }

    public String getLanguage() {
        return language;
    }

    public int getParticipants() {
        return participants;
    }
}
