package cz.uruba.ets2mpcompanion.model;

import java.io.Serializable;

import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.model.MeetupInfo;

public class MeetupInfoTRUCKERSEVENTS extends MeetupInfo {

    public MeetupInfoTRUCKERSEVENTS(String server, String when, String location, String organiser, String language, String participants, String relativeURL) {
        super(server, when, location, organiser, language, participants, relativeURL, URL.TRUCKERS_EVENTS);
    }
}
