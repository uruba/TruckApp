package cz.uruba.ets2mpcompanion.model;

import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.model.MeetupInfo;

public class MeetupInfoETS2C extends MeetupInfo {

    public MeetupInfoETS2C(String server, String when, String location, String organiser, String language, String participants, String relativeURL) {
        super(server, when, location, organiser, language, participants, relativeURL, URL.ETS2MP_CONVOYS);
    }
}
