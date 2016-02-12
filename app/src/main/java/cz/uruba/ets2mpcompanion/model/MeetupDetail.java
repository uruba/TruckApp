package cz.uruba.ets2mpcompanion.model;

import java.util.Date;
import java.util.HashMap;

import cz.uruba.ets2mpcompanion.interfaces.AbstractMarkupProcessor;

public class MeetupDetail extends AbstractMarkupProcessor {
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

    @Override
    public String processMarkup(String inputString) {
        HashMap<String, String> matchMap = new HashMap<>();
        matchMap.put("organiser", getOrganiser());
        matchMap.put("server", getServer());
        matchMap.put("location", getLocation());
        matchMap.put("destination", getDestination());

        return getStringFromMarkup(inputString, matchMap);
    }
}
