package org.dreamexposure.discal.server.api.endpoints.v2.event;

import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;

import org.dreamexposure.discal.core.database.DatabaseManager;
import org.dreamexposure.discal.core.logger.LogFeed;
import org.dreamexposure.discal.core.logger.object.LogObject;
import org.dreamexposure.discal.core.object.GuildSettings;
import org.dreamexposure.discal.core.object.calendar.CalendarData;
import org.dreamexposure.discal.core.object.web.AuthenticationState;
import org.dreamexposure.discal.core.utils.GlobalConst;
import org.dreamexposure.discal.core.utils.JsonUtil;
import org.dreamexposure.discal.core.utils.JsonUtils;
import org.dreamexposure.discal.core.wrapper.google.CalendarWrapper;
import org.dreamexposure.discal.core.wrapper.google.EventWrapper;
import org.dreamexposure.discal.server.utils.Authentication;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import discord4j.common.util.Snowflake;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v2/events")
public class GetEventEndpoint {
    @PostMapping(value = "/get", produces = "application/json")
    public String getEventsForMonth(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final String rBody) {
        //Authenticate...
        final AuthenticationState authState = Authentication.authenticate(request);
        if (!authState.getSuccess()) {
            response.setStatus(authState.getStatus());
            response.setContentType("application/json");
            return JsonUtil.INSTANCE.encodeToString(AuthenticationState.class, authState);
        }

        //Okay, now handle actual request.
        try {
            final JSONObject requestBody = new JSONObject(rBody);

            final String guildId = requestBody.getString("guild_id");
            final int calNumber = requestBody.getInt("calendar_number");
            final String eventId = requestBody.getString("event_id");

            final GuildSettings settings = DatabaseManager.getSettings(Snowflake.of(guildId)).block();
            Mono<CalendarData> calDataMono = DatabaseManager.getCalendar(settings.getGuildID(), calNumber)
                .cache();
            final Event event = calDataMono.flatMap(calData -> EventWrapper.getEvent(calData, eventId))
                .block();
            final ZoneId tz = calDataMono.flatMap(CalendarWrapper::getCalendar)
                .map(Calendar::getTimeZone)
                .map(ZoneId::of)
                .block();


            response.setContentType("application/json");
            if (event != null) {
                response.setStatus(GlobalConst.STATUS_SUCCESS);
                return JsonUtils.convertEventToJson(event, tz, settings).block().toString();
            } else {
                response.setStatus(GlobalConst.STATUS_NOT_FOUND);
                return JsonUtils.getJsonResponseMessage("Event not Found");
            }
        } catch (final JSONException e) {
            e.printStackTrace();

            response.setContentType("application/json");
            response.setStatus(GlobalConst.STATUS_BAD_REQUEST);
            return JsonUtils.getJsonResponseMessage("Bad Request");
        } catch (final Exception e) {
            LogFeed.log(LogObject
                .forException("[API-v2]", "get event by ID err", e, this.getClass()));

            response.setContentType("application/json");
            response.setStatus(GlobalConst.STATUS_INTERNAL_ERROR);
            return JsonUtils.getJsonResponseMessage("Internal Server Error");
        }
    }
}
