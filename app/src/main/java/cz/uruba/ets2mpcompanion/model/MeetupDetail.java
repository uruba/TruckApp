package cz.uruba.ets2mpcompanion.model;

import java.util.Date;

public class MeetupDetail {
    private String organiser;
    private String server;
    private String location;
    private String destination;
    private boolean trailerRequired;
    private Date meetupDate;
    private String language;

    public MeetupDetail(String organiser, String server, String location, String destination, boolean trailerRequired, Date meetupDate, String language) {
        this.organiser = organiser;
        this.server = server;
        this.location = location;
        this.destination = destination;
        this.trailerRequired = trailerRequired;
        this.meetupDate = meetupDate;
        this.language = language;
    }

    public String getOrganiser() {
        return organiser;
    }

    public String getServer() {
        return server;
    }

    public String getLocation() {
        return location;
    }

    public String getDestination() {
        return destination;
    }

    public boolean isTrailerRequired() {
        return trailerRequired;
    }

    public Date getMeetupDate() {
        return meetupDate;
    }

    public String getLanguage() {
        return language;
    }
}
