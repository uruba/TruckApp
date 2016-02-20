package cz.uruba.ets2mpcompanion.tasks;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.interfaces.AbstractFetchJsoupDataTask;
import cz.uruba.ets2mpcompanion.model.MeetupInfo;

public class FetchMeetupListTask extends AbstractFetchJsoupDataTask<ArrayList<MeetupInfo>> {

    public FetchMeetupListTask(DataReceiver<ArrayList<MeetupInfo>> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    @Override
    protected ArrayList<MeetupInfo> processJsoupData(Document document) {
        if (document == null) {
            return null;
        }

        ArrayList<MeetupInfo> meetups = new ArrayList<>();

        Elements elem_table_list = document.select(".table_list .row");
        elem_table_list.remove(0);

        for (Element elem : elem_table_list) {
            Elements elem_data = elem.children();
            int iterCount = 0;
            String server, time, location, organiser, language, participants, relativeURL;

            server = time = location = organiser = language = participants = relativeURL = "";

            for (Element data_field : elem_data) {
                iterCount++;

                String elemContent = data_field.text();
                switch (iterCount) {
                    case 1:
                        server = elemContent;
                    case 2:
                        time = elemContent;
                        break;
                    case 3:
                        location = elemContent;
                        break;
                    case 4:
                        organiser = elemContent;
                        break;
                    case 5:
                        language = elemContent;
                        break;
                    case 6:
                        participants = elemContent;
                        break;
                    case 7:
                        relativeURL = data_field.select("a").first() != null ? data_field.select("a").first() .attr("href") : "";
                        break;
                }
            }

            MeetupInfo meetupInfo = new MeetupInfo(server, time, location, organiser, language, participants, relativeURL);
            meetups.add(meetupInfo);
        }

        return meetups;
    }
}
