package org.dreamexposure.discal.core.object;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Nova Fox on 11/10/17.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCal-Discord-Bot
 */
public class GuildSettings {
	private long guildID;

	private boolean externalCalendar;
	private String privateKey;

	private String encryptedAccessToken;
	private String encryptedRefreshToken;

	private String controlRole;
	private String discalChannel;

	private boolean simpleAnnouncements;
	private String lang;
	private String prefix;

	private boolean patronGuild;
	private boolean devGuild;
	private int maxCalendars;

	private boolean twelveHour;
	private boolean branded;

	private final ArrayList<String> dmAnnouncements = new ArrayList<>();

	public GuildSettings(long _guildId) {
		guildID = _guildId;

		externalCalendar = false;
		privateKey = "N/a";

		encryptedAccessToken = "N/a";
		encryptedRefreshToken = "N/a";

		controlRole = "everyone";
		discalChannel = "all";

		simpleAnnouncements = false;
		lang = "ENGLISH";
		prefix = "!";

		patronGuild = false;
		devGuild = false;
		maxCalendars = 1;

		twelveHour = true;
	}

	//Getters
	public long getGuildID() {
		return guildID;
	}

	public boolean useExternalCalendar() {
		return externalCalendar;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getEncryptedAccessToken() {
		return encryptedAccessToken;
	}

	public String getEncryptedRefreshToken() {
		return encryptedRefreshToken;
	}

	public String getControlRole() {
		return controlRole;
	}

	public String getDiscalChannel() {
		return discalChannel;
	}

	public boolean usingSimpleAnnouncements() {
		return simpleAnnouncements;
	}

	public String getLang() {
		return lang;
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean isPatronGuild() {
		return patronGuild;
	}

	public boolean isDevGuild() {
		return devGuild;
	}

	public int getMaxCalendars() {
		return maxCalendars;
	}

	public boolean useTwelveHour() {
		return twelveHour;
	}

	public boolean isBranded() {
		return branded;
	}

	public ArrayList<String> getDmAnnouncements() {
		return dmAnnouncements;
	}

	public String getDmAnnouncementsString() {
		StringBuilder users = new StringBuilder();
		int i = 0;
		for (String sub: dmAnnouncements) {
			if (i == 0) {
				users = new StringBuilder(sub);
			} else {
				users.append(",").append(sub);
			}
			i++;
		}
		return users.toString();
	}

	//Dumb getters so that Tymeleaf plays nice...
	public boolean isExternalCalendar() {
		return externalCalendar;
	}

	public boolean isSimpleAnnouncements() {
		return simpleAnnouncements;
	}

	//Setters
	public void setUseExternalCalendar(boolean _useExternal) {
		externalCalendar = _useExternal;
	}

	public void setPrivateKey(String _privateKey) {
		privateKey = _privateKey;
	}

	public void setEncryptedAccessToken(String _access) {
		encryptedAccessToken = _access;
	}

	public void setEncryptedRefreshToken(String _refresh) {
		encryptedRefreshToken = _refresh;
	}

	public void setControlRole(String _controlRole) {
		controlRole = _controlRole;
	}

	public void setDiscalChannel(String _discalChannel) {
		discalChannel = _discalChannel;
	}

	public void setSimpleAnnouncements(boolean _simpleAnnouncements) {
		simpleAnnouncements = _simpleAnnouncements;
	}

	public void setLang(String _lang) {
		lang = _lang;
	}

	public void setPrefix(String _prefix) {
		prefix = _prefix;
	}

	public void setPatronGuild(boolean _patronGuild) {
		patronGuild = _patronGuild;
	}

	public void setDevGuild(boolean _devGuild) {
		devGuild = _devGuild;
	}

	public void setMaxCalendars(Integer _maxCalendars) {
		maxCalendars = _maxCalendars;
	}

	public void setTwelveHour(boolean _twelveHour) {
		twelveHour = _twelveHour;
	}

	public void setBranded(boolean _branded) {
		branded = _branded;
	}

	public void setDmAnnouncementsFromString(String userList) {
		String[] subs = userList.split(",");
		Collections.addAll(dmAnnouncements, subs);
	}

	public JSONObject toJson() {
		JSONObject data = new JSONObject();

		data.put("GuildId", guildID);
		data.put("ExternalCalendar", externalCalendar);
		data.put("PrivateKey", privateKey);
		data.put("AccessToken", encryptedAccessToken);
		data.put("RefreshToken", encryptedRefreshToken);
		data.put("ControlRole", controlRole);
		data.put("DisCalChannel", discalChannel);
		data.put("SimpleAnnouncements", simpleAnnouncements);
		data.put("Lang", lang);
		data.put("Prefix", prefix);
		data.put("PatronGuild", patronGuild);
		data.put("DevGuild", devGuild);
		data.put("MaxCalendars", maxCalendars);
		data.put("TwelveHour", twelveHour);
		data.put("Branded", branded);

		return data;
	}

	public GuildSettings fromJson(JSONObject data) {
		guildID = data.getLong("GuildId");
		externalCalendar = data.getBoolean("ExternalCalendar");
		privateKey = data.getString("PrivateKey");
		encryptedAccessToken = data.getString("AccessToken");
		encryptedRefreshToken = data.getString("RefreshToken");
		controlRole = data.getString("ControlRole");
		discalChannel = data.getString("DisCalChannel");
		simpleAnnouncements = data.getBoolean("SimpleAnnouncements");
		lang = data.getString("Lang");
		prefix = data.getString("Prefix");
		patronGuild = data.getBoolean("PatronGuild");
		devGuild = data.getBoolean("DevGuild");
		maxCalendars = data.getInt("MaxCalendars");
		twelveHour = data.getBoolean("TwelveHour");
		branded = data.getBoolean("Branded");

		return this;
	}
}