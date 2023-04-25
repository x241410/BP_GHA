package com.telus.bped.utils;

import com.intuit.karate.core.FeatureResult;
import com.telus.api.test.utils.APIJava;
import com.test.reporting.Reporting;
import com.test.utils.EncryptDecrypt;
import com.test.utils.EncryptionUtils;
import com.test.utils.Status;
import com.test.utils.SystemProperties;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GoogleSheetsUtils {

	static String accessToken = "";
	private static String e_d_key = SystemProperties.getStringValue("selenium.key");

	static JSONArray jsonArray = new JSONArray();

	static int updatedRow = 0;

	/**
	 * Method Description: This method is used to generate the access token
	 *
	 * @return accessToken
	 */
	public static String getAccessToken() {
		String cid = "";
		String csec = "";
		String cRT = "";

		GoogleSheetsUtils googleSheetsUtils = new GoogleSheetsUtils();
		Map<String, Object> apiOperation = new HashMap<>();

		try {
			/*
			 * cid = googleSheetsUtils.getKeyValue("GH_CD_RT"); csec =
			 * googleSheetsUtils.getKeyValue("GH_CS_RT"); cRT =
			 * googleSheetsUtils.getKeyValue("GH_RT");
			 */

			cid = System.getenv("GH_CD_RT");
			csec = System.getenv("GH_CS_RT");
			cRT = System.getenv("GH_RT");

			Reporting.logReporter(Status.INFO, "SUBSTRING:  " + cid.substring(0, 10));

			System.setProperty("karate.cid", cid);
			System.setProperty("karate.csec", csec);
			System.setProperty("karate.cRT", cRT);

			FeatureResult result = APIJava.runKarateFeatureReturnResultSet("NON-PROD",
					"classpath:services/getAuthToken.feature");
			apiOperation = result.getResultAsPrimitiveMap();
			accessToken = apiOperation.get("access_token").toString();
			Reporting.logReporter(Status.INFO, "Access Token Generated Successfully !");
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to generate Access Token. Exception Occured: " + e);
			return "";
		}

		return accessToken;
	}

	public JSONObject getJSONObjectFromGit(String value) {
		String a = value;
		a = a.replace("\\r\\n\\t\\", "");
		a = a.replace("\\r", "");
		a = a.replace("\\n", "");
		a = a.replace("\\t", "");
		a = a.replace("t\\", "");
		a = a.replace("\\", "");

		return new JSONObject(a);

	}

	/**
	 * @param appName
	 * @param status  // * @param authCode
	 */
	public void updateGoogleSheetViaAPI(String accessToken, String appName, String status) {
		try {
			Map<String, Object> apiOperationRead = new HashMap<>();

			System.setProperty("karate.bearerToken", accessToken);
			System.setProperty("karate.sheetId", GenericUtils.getSheetId());
			System.setProperty("karate.sheetName", GenericUtils.getSheetName());
			String executedAt = GenericUtils.getDateInMMDDYYYYHHMMSSInPST() + " (PST)";

			/**
			 * Call Read Googlesheet API
			 */

			FeatureResult result1 = APIJava.runKarateFeatureReturnResultSet("PT148",
					"classpath:services/readGoogleSheets.feature");
			apiOperationRead = result1.getResultAsPrimitiveMap();
			JSONArray jObj = new JSONArray(apiOperationRead.get("responseValues").toString());

			int app_index = GenericUtils.getIndexFromJsonObject(jObj, appName);
			JSONArray dataObject = (JSONArray) jObj.get(app_index - 1);

			System.setProperty("karate.applicationName" + java.lang.Thread.currentThread().getId(),
					dataObject.getString(0));
			System.setProperty("karate.cmdbId" + java.lang.Thread.currentThread().getId(), dataObject.getString(1));
			System.setProperty("karate.priority" + java.lang.Thread.currentThread().getId(), dataObject.getString(2));
			System.setProperty("karate.platform" + java.lang.Thread.currentThread().getId(), dataObject.getString(3));
			System.setProperty("karate.primeName" + java.lang.Thread.currentThread().getId(), dataObject.getString(4));
			System.setProperty("karate.managerName" + java.lang.Thread.currentThread().getId(),
					dataObject.getString(5));
			System.setProperty("karate.directorName" + java.lang.Thread.currentThread().getId(),
					dataObject.getString(6));

			/**
			 * Call Read Googlesheet API
			 */

			System.setProperty("karate.sheetName", GenericUtils.getSheetUpdateName());
			FeatureResult result = APIJava.runKarateFeatureReturnResultSet("PT148",
					"classpath:services/readGoogleSheets.feature");
			Map<String, Object> apiOperationReadMainSheet = new HashMap<>();

			apiOperationReadMainSheet = result.getResultAsPrimitiveMap();
			JSONArray jObjMainSheet = new JSONArray(apiOperationReadMainSheet.get("responseValues").toString());
			int rowIndex = jObjMainSheet.length() + 1;

			String sheetRange = GenericUtils.getSheetUpdateName() + "!" + GenericUtils.getStartColumn() + rowIndex + ":"
					+ GenericUtils.getEndColumn() + rowIndex;

			System.setProperty("karate.rowNum" + java.lang.Thread.currentThread().getId(),
					String.valueOf(rowIndex + 1));
			System.setProperty("karate.sheetRange" + java.lang.Thread.currentThread().getId(), sheetRange);
			System.setProperty("karate.appStatus" + java.lang.Thread.currentThread().getId(), status);
			System.setProperty("karate.executedAt" + java.lang.Thread.currentThread().getId(), executedAt);

			/**
			 * Call Update Googlesheet API
			 */

			FeatureResult result2 = APIJava.runKarateFeatureReturnResultSet("PT148",
					"classpath:services/updateGoogleSheets.feature");
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "GoogleSheet update failed ! Exception Occured: " + e);
		}

		Reporting.logReporter(Status.INFO, "GoogleSheet updated successfully !");

	}

	/**
	 * @param key
	 * @return keyValue
	 * @throws Exception
	 */
	public String getKeyValue(String key, boolean... finalDecryptedNotRequired) throws Exception {

		boolean flag = finalDecryptedNotRequired.length > 0 && finalDecryptedNotRequired[0];

		String keValue = "";
		Map<String, Object> apiOperation = new HashMap<>();
		System.setProperty("karate.keyName", key);

		String ekey = EncryptionUtils.decode(e_d_key);

		String egpt = SystemProperties.getStringValue("selenium.ghpt");
		String ghpt = EncryptDecrypt.generateDecryptData(egpt, ekey).toString();
		System.setProperty("karate.ghpt", ghpt);
		try {
			FeatureResult result = APIJava.runKarateFeatureReturnResultSet("NON-PROD",
					"classpath:services/getEnvGHVariables.feature");
			apiOperation = result.getResultAsPrimitiveMap();
			keValue = apiOperation.get("keyVal").toString();
			if (!flag) {
				keValue = EncryptDecrypt.generateDecryptData(keValue, ekey).toString();
			}
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to access required value");
			return "";
		}

		return keValue;

	}

	public JSONArray updateBulKDataIntoGSheets(JSONArray p1_apps, JSONArray p2_apps, JSONArray p3_apps) {

		GoogleSheetsUtils googleSheetsUtils = new GoogleSheetsUtils();

		JSONArray writeDataArray = new JSONArray();

		String token = GoogleSheetsUtils.getAccessToken();
		try {
			JSONArray baseDataAppDetailsArray = googleSheetsUtils.readBaseGoogleSheet(token);
			int rowIndex = readConsolidatedGoogleSheet(token);
			int lastUsedRow = rowIndex;
			updatedRow = rowIndex;

			writeDataArray.put(generateGoogleSheetArray(p1_apps, baseDataAppDetailsArray));
			writeDataArray.put(generateGoogleSheetArray(p2_apps, baseDataAppDetailsArray));
			writeDataArray.put(generateGoogleSheetArray(p3_apps, baseDataAppDetailsArray));

			JSONObject payload = generatePayload(jsonArray, lastUsedRow, updatedRow);

			System.setProperty("karate.bearerToken", token);
			System.setProperty("karate.sheetId", GenericUtils.getSheetId());
			System.setProperty("karate.sheetName", GenericUtils.getSheetUpdateName());
			System.setProperty("karate.payload", payload.toString());
			System.setProperty("karate.sheetRange", payload.getString("range"));

			FeatureResult result2 = APIJava.runKarateFeatureReturnResultSet("PT148",
					"classpath:services/updateGoogleSheets.feature");

//            System.out.println("writeDataArray:  " + writeDataArray);

//            System.out.println("writeDataArray:  " + payload);
			return writeDataArray;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public JSONArray readBaseGoogleSheet(String token) throws Exception {

		Map<String, Object> apiOperationRead = new HashMap<>();

		System.setProperty("karate.bearerToken", token);
		System.setProperty("karate.sheetId", GenericUtils.getSheetId());
		System.setProperty("karate.sheetName", GenericUtils.getSheetName());

		/**
		 * Call Read Googlesheet API
		 */

		FeatureResult result1 = APIJava.runKarateFeatureReturnResultSet("PT148",
				"classpath:services/readGoogleSheets.feature");
		apiOperationRead = result1.getResultAsPrimitiveMap();
		JSONArray jObj = new JSONArray(apiOperationRead.get("responseValues").toString());
		return jObj;

	}

	public JSONObject generatePayload(JSONArray writeDataArray, int lastUsedRow, int updatedRow) {

		String sheet = GenericUtils.getSheetUpdateName();

		JSONObject payload = new JSONObject();

		payload.put("range", sheet + "!A" + ++lastUsedRow + ":K" + updatedRow);
		payload.put("majorDimension", "ROWS");
		payload.put("values", writeDataArray);
		return payload;
	}

	public int readConsolidatedGoogleSheet(String token) throws Exception {

		Map<String, Object> apiOperationRead = new HashMap<>();

		System.setProperty("karate.bearerToken", token);
		System.setProperty("karate.sheetId", GenericUtils.getSheetId());
		System.setProperty("karate.sheetName", GenericUtils.getSheetUpdateName());

		/**
		 * Call Read Googlesheet API
		 */

		FeatureResult result1 = APIJava.runKarateFeatureReturnResultSet("PT148",
				"classpath:services/readGoogleSheets.feature");
		Map<String, Object> apiOperationReadMainSheet = new HashMap<>();

		apiOperationReadMainSheet = result1.getResultAsPrimitiveMap();
		JSONArray jObjMainSheet = new JSONArray(apiOperationReadMainSheet.get("responseValues").toString());
		return jObjMainSheet.length();

	}

	public JSONArray generateGoogleSheetArray(JSONArray p_apps, JSONArray baseDataAppDetailsArray) {

		for (int i = 0; i < p_apps.length(); i++) {
			JSONObject jObj = (JSONObject) p_apps.get(i);
			String appName = jObj.getString("appName");
			String appStatus = jObj.getString("appStatus");
			String executedAt = jObj.getString("executedAt");
			String date = jObj.getString("executedAt").split(" ")[0];

//                System.out.println(appName);
//                System.out.println("=======================: " + i + " :=====================");

			for (int j = 0; j < baseDataAppDetailsArray.length(); j++) {
				int app_index = GenericUtils.getIndexFromJsonObject(baseDataAppDetailsArray, appName);
				JSONArray dataObject1 = new JSONArray();

				dataObject1.put(updatedRow + "");
				updatedRow++;

				JSONArray dataObject = (JSONArray) baseDataAppDetailsArray.get(app_index - 1);

				for (Object a : dataObject) {
					dataObject1.put(a);
				}

				if (dataObject1.toString().contains(appName)) {
//                        dataObject.put(0, rowIndex);
					dataObject1.put(appStatus);
					dataObject1.put(executedAt);
					dataObject1.put(date);
					jsonArray.put(dataObject1);

					// System.out.println("INTERNAL Lop:==============> "+ dataObject);
					break;
				}
			}
		}

		return jsonArray;
	}

}
