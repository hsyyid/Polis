package io.github.hsyyid.polis.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class UUIDFetcher
{
	public static Optional<String> getName(UUID uuid)
	{
		String name = null;
		
		try
		{
			String link = ("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString()).replaceAll("-", "");
			URL url = new URL(link);
			URLConnection connection = url.openConnection();
			Scanner jsonScanner = new Scanner(connection.getInputStream(), "UTF-8");
			String json = jsonScanner.next();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(json);
			name = (String) ((JSONObject) obj).get("name");
			jsonScanner.close();
			return Optional.of(name);
		}
		catch (Exception ex)
		{
			;
		}

		return Optional.empty();
	}
}
