package com.telus.bped.utils;

import com.test.files.interaction.ReadJSON;
import com.test.logging.Logging;
import com.test.utils.EncryptDecrypt;
import com.test.utils.EncryptionUtils;
import com.test.utils.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ***************************************************************************
 * DESCRIPTION: This class contains reusable Java methods AUTHOR: x241410
 * ***************************************************************************
 */

public class GenericUtils {

	private static String gsheetConfigJsonFilepath = "\\config\\" + "gSheetConfig.json";
	static JSONObject jsonFileObjectForDetails = GenericUtils.getJSONObjectFromJSONFile(gsheetConfigJsonFilepath);

	/**
	 * Description: The purpose of this method is to fetch the json key value for
	 * given parameters
	 *
	 * @param fileName - name of the json file
	 * @param node     - json node
	 * @param key      - json key
	 * @return String output value
	 */
	public static String getKeyValueFromJsonNode(String fileName, String node, String key) {
		JSONObject obj = null;
		String output = null;

		try {
			JSONObject jsonFile = new JSONObject(ReadJSON.parse(fileName));
			obj = jsonFile.getJSONObject(node);
			output = obj.getString(key);
		} catch (JSONException e) {
			Logging.logReporter(Status.DEBUG, "JSON_EXCEPTION " + e);
		}

		return output;
	}

	/**
	 * Description: The purpose of this method is to get the json object from a json
	 * file
	 *
	 * @param fileName
	 * @return jsonObj
	 */
	public static JSONObject getJSONObjectFromJSONFile(String fileName) {
		JSONObject jsonObj = null;

		try {
			jsonObj = new JSONObject(ReadJSON.parse(fileName));

		} catch (JSONException e) {
			Logging.logReporter(Status.DEBUG, "JSON_EXCEPTION " + e);
		}

		return jsonObj;
	}

	/**
	 * Description: The purpose of this method is to return PST Date time
	 *
	 * @return current system date in mm/dd/yyyy format
	 */
	public static String getDateInMMDDYYYYHHMMSSInPST() {
		Instant instant = Instant.now();
		ZonedDateTime zdtNewYork = instant.atZone(ZoneId.of("America/Los_Angeles"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		return zdtNewYork.format(formatter);
	}

	public static String getGSheetAppName(String appName) {

		switch (appName.toUpperCase()) {
		case "BLIF":
			appName = "BLIF";
			break;

		case "BLIF - BATCH JOBS":
			appName = "BLIF - Batch";
			break;

		case "LDORS":
			appName = "LDORS";
			break;

		case "LDORS - BATCH JOBS":
			appName = "LDORS - Batch";
			break;

		case "VPOP-INTERNAL":
			appName = "VPOP - INTERNAL";
			break;

		case "VPOP-EXTERNAL":
			appName = "VPOP - EXTERNAL";
			break;

		case "VPOP - BATCH JOBS":
			appName = "VPOP - Batch";
			break;

		case "RRW":
			appName = "Residential Resellers";
			break;

		case "REX":
			appName = "Rebiller";
			break;

		case "REX - BATCH JOBS":
			appName = "Rebiller - Batch";
			break;

		case "DAS - BATCH JOBS":
			appName = "Directory Assistance System";
			break;

		case "ART":
			appName = "ASR Reporting Tool";
			break;

		case "AAB - BATCH JOBS":
			appName = "Assurance Account Batch";
			break;

		case "IVS2-EXTERNAL":
			appName = "Evolve Voice Services - EXTERNAL";
			break;

		case "IVS2-INTERNAL":
			appName = "Evolve Voice Services - INTERNAL";
			break;
		case "LSR - BATCH JOBS":
			appName = "Local Service Request - Batch";
			break;
		case "OST - BATCH JOBS":
			appName = "Order Status Tool - Batch";
			break;
		case "ECB - BATCH JOBS":
			appName = "Enterprise Contract Builder - Batch";
			break;

		case "CONTRACT-SUITE":
			appName = "Contract Suite: Partner";
			break;

		case "LEGACY-IVS":
			appName = "Evolve Voice Services - Legacy";
			break;

		case "TLC":
			appName = "TLC: Termination Liability Charge";
			break;

		case "HUMBOLDT":
			appName = "Humboldt";
			break;

		case "OST":
			appName = "Order Status Tool";
			break;

		case "LSR":
			appName = "Local Service Request";
			break;

		}
		return appName;

	}

	/**
	 * @param jObj
	 * @param val
	 * @return
	 */
	public static int getIndexFromJsonObject(JSONArray jObj, String val) {

		int c = 0;
		for (int i = 0; i < jObj.length(); i++) { // this will iterate through key1 - key3
			String str = jObj.get(i).toString().split(",")[0].split(",")[0].replace("\"", "").replace("[", "");
			if (str.equalsIgnoreCase(val)) {
				c = i + 1;
				break;
			}
		}
		return c;
	}

	/**
	 * Method Description: To get the sheet Id
	 *
	 * @return
	 */
	public static String getSheetId() {
		return jsonFileObjectForDetails.get("gSheetId").toString();
	}

	/**
	 * Method Description: To get the sheet Name
	 *
	 * @return
	 */
	public static String getSheetName() {
		return jsonFileObjectForDetails.get("gSheetName").toString();

	}

	/**
	 * Method Description: To get the sheet Name
	 *
	 * @return
	 */
	public static String getSheetUpdateName() {
		return jsonFileObjectForDetails.get("gSheetUpdateName").toString();

	}
	
	/**
	 * Method Description: To get the sheet start column
	 *
	 * @return
	 */
	public static String getStartColumn() {
		return jsonFileObjectForDetails.get("start_column").toString();
	}

	/**
	 * Method Description: To get the sheet end column
	 *
	 * @return
	 */
	public static String getEndColumn() {
		return jsonFileObjectForDetails.get("end_column").toString();

	}
	
	public static void main(String[] args) throws Exception {
		/*
		 * String str = EncryptDecrypt.generateEncryptData(
		 * "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", "gh-key-string");
		 * System.out.println(str);
		 */
	       
	       System.out.println(EncryptionUtils.encode("x258025"));
	       System.out.println(EncryptionUtils.encode("telus127"));

	       System.out.println(EncryptionUtils.encode("as258025"));

	       System.out.println(EncryptionUtils.encode("tel"));

	}
}
