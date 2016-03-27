package cz.uruba.ets2mpcompanion.tasks;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.Map;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.interfaces.tasks.AbstractFetchJsoupDataTask;
import cz.uruba.ets2mpcompanion.model.MeetupDetail;

public class FetchMeetupDetailTask extends AbstractFetchJsoupDataTask<MeetupDetail> {

    public FetchMeetupDetailTask(DataReceiver<MeetupDetail> callbackObject, String requestURL, Map<String, String> cookies, boolean notifyUser) {
        super(callbackObject, requestURL, cookies, notifyUser);
    }

    @Override
    protected MeetupDetail processJsoupData(Document document) {
        // by the virtue of proper selectors, we get to the data that we want
        Element elem_form = document.select(".form").first();


        Elements elem_data = elem_form.children();
        int iterCount = 0;
        String organiser, server, location, destination;
        boolean trailerRequired;
        Date meetupDate;

        organiser = server = location = destination = "";
        trailerRequired = false;
        meetupDate = null;

        // we iterate on the "data" elements
        for (Element elem : elem_data) {
            iterCount++;

            String elemContent = elem.select(".desc").first().text().replaceAll("\u00A0", "").trim();
            switch (iterCount) {
                case 1:
                    organiser = elemContent;
                    break;
                case 2:
                    server = elemContent;
                    break;
                case 3:
                    location = elemContent;
                    break;
                case 4:
                    destination = elemContent;
                    break;
                case 5:
                    trailerRequired = elemContent.toLowerCase().equals("yes");
                    break;
                case 6:
                    meetupDate = new Date(Long.parseLong(elem.select(".desc").first().attr("data-stamp")) * 1000);
                    break;
            }
        }

        return new MeetupDetail(organiser, server, location, destination, trailerRequired, meetupDate);
    }
}
