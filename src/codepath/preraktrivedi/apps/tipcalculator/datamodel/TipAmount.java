package codepath.preraktrivedi.apps.tipcalculator.datamodel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TipAmount {
	public String tipType;
	public String tipAmount;

	public TipAmount(String tipType, String tipAmount) {
		super();
		this.tipType = tipType;
		this.tipAmount = tipAmount;
	}

	public TipAmount(JSONObject object){
		try {
			this.tipType = object.getString("tipType");
			this.tipAmount = object.getString("tipAmount");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static  ArrayList<TipAmount> getSampleTipAmounts() {
		ArrayList<TipAmount> tipAmount = new ArrayList<TipAmount>();
		tipAmount.add(new TipAmount("Ten Percent", "10"));
		tipAmount.add(new TipAmount("Fifteen Percent", "15"));
		tipAmount.add(new TipAmount("Twenty Percent", "20"));
		tipAmount.add(new TipAmount("Custom Tip Percent", "30"));
		tipAmount.add(new TipAmount("Custom Tip Amount", "15"));
		tipAmount.add(new TipAmount("Custom Total Amount", "150"));
		return tipAmount;

	}

	public static ArrayList<TipAmount> fromJson(JSONArray jsonObjects) {
		ArrayList<TipAmount> users = new ArrayList<TipAmount>();
		for (int i = 0; i < jsonObjects.length(); i++) {
			try {
				users.add(new TipAmount(jsonObjects.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return users;
	}
}