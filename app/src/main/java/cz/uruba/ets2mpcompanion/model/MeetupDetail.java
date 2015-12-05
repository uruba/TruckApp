package cz.uruba.ets2mpcompanion.model;

import java.util.Date;

public class MeetupDetail {
    private String organiser;
    private String server;
    private String location;
    private String destination;
    private boolean trailerRequired;
    private Date meetupDate;

    public MeetupDetail(String organiser, String server, String location, String destination, boolean trailerRequired, Date meetupDate) {
        this.organiser = organiser;
        this.server = server;
        this.location = location;
        this.destination = destination;
        this.trailerRequired = trailerRequired;
        this.meetupDate = meetupDate;
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
}
