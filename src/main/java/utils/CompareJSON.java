package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class CompareJSON {

	public boolean compare(String res1, String res2) {
		JsonParser parser = new JsonParser();
		try {
			JsonElement j1 = parser.parse(res1);
			JsonElement j2 = parser.parse(res2);
			return j1.equals(j2);
		} catch (JsonParseException jpe) {

			try {
				if (res1.equals(res2))        // for handling when response is just a plain text
					return true;
			} catch (Exception e) {

				return false;
			}
		} catch (NullPointerException ne) {

			if (res1 == null && res2 == null) // for handling when both response are empty
				return true;
			return false;
		} catch (Exception e) {

			return false;
		}
		return false;
	}
}

