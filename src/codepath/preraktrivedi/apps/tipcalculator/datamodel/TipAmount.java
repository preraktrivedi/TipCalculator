package codepath.preraktrivedi.apps.tipcalculator.datamodel;

import java.util.ArrayList;

import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData.TipType;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.calculateTipFromPercent;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.calculateCustomTipPercent;

/** 
 * 
 * Class to hold the tip type and amount associated with it
 * @author Prerak Trivedi (prerak.d.trivedi@gmail.com)
 * 
 *
 **/

public class TipAmount {

	private TipType tipType;
	private String tipAmount;

	public TipAmount(TipType tipType, String tipAmount) {
		super();
		this.setTipType(tipType);
		this.setTipAmount(tipAmount);
	}

	public static  ArrayList<TipAmount> getSampleTipAmounts() {
		ArrayList<TipAmount> tipAmount = new ArrayList<TipAmount>();
		tipAmount.add(new TipAmount(TipType.TEN_PERCENT, "10"));
		tipAmount.add(new TipAmount(TipType.FIFTEEN_PERCENT, "15"));
		tipAmount.add(new TipAmount(TipType.TWENTY_PERCENT, "20"));
		tipAmount.add(new TipAmount(TipType.CUSTOM_PERCENT, "30"));
		tipAmount.add(new TipAmount(TipType.CUSTOM_TIP_AMOUNT, "15"));
		tipAmount.add(new TipAmount(TipType.CUSTOM_TOTAL_AMOUNT, "150"));
		return tipAmount;
	}

	public static ArrayList<TipAmount> getTipAmounts(double billAmount) {
		ArrayList<TipAmount> tipAmount = new ArrayList<TipAmount>();
		tipAmount.add(new TipAmount(TipType.TEN_PERCENT, calculateTipFromPercent(10, billAmount)));
		tipAmount.add(new TipAmount(TipType.FIFTEEN_PERCENT, calculateTipFromPercent(15, billAmount)));
		tipAmount.add(new TipAmount(TipType.TWENTY_PERCENT, calculateTipFromPercent(20, billAmount)));
		tipAmount.add(new TipAmount(TipType.CUSTOM_PERCENT, calculateCustomTipPercent()));
		tipAmount.add(new TipAmount(TipType.CUSTOM_TIP_AMOUNT, "0"));
		tipAmount.add(new TipAmount(TipType.CUSTOM_TOTAL_AMOUNT, "0"));
		return tipAmount;
	}

	public TipType getTipType() {
		return tipType;
	}

	public void setTipType(TipType tipType) {
		this.tipType = tipType;
	}

	public String getTipAmount() {
		return tipAmount;
	}

	public void setTipAmount(String tipAmount) {
		this.tipAmount = tipAmount;
	}

}