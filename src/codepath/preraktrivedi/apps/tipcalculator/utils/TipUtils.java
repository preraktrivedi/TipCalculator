package codepath.preraktrivedi.apps.tipcalculator.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.util.Log;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData;

/** Utils for common methods to calculate tip. 
 * @author Prerak Trivedi 
 *
 */

public class TipUtils {

	public static boolean validateAmount(String text) {
		boolean isAmountValid = false;
		double tipAmount = 0;
		try {
			tipAmount = Double.parseDouble(text);
			if (tipAmount > 0 && tipAmount < 9999999) {
				isAmountValid = true;
			}
		} catch (Exception e) {
			isAmountValid = false;
		}

		if (isAmountValid) {
			TipCalculatorAppData.getInstance().setCurrentBillAmount(refineTipAmount(tipAmount));
		} else {
			TipCalculatorAppData.getInstance().setCurrentBillAmount(-1);
		}

		return isAmountValid;
	}
	
	public static String formatToCurrency(double tipAmount) {
		BigDecimal bd = new BigDecimal(tipAmount);
		BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(rounded.doubleValue());
	}

	private static double refineTipAmount(double tipAmount) {
		return Double.parseDouble(formatToCurrency(tipAmount));
	}
	
	public static String calculateCustomTipPercent() {
		TipCalculatorAppData data = TipCalculatorAppData.getInstance();
		double customTipPercent = data.getCustomTipPercent();
		double currentBillAmount = data.getCurrentBillAmount();
		return calculateTipFromPercent(customTipPercent, currentBillAmount);
	}

	public static String calculateTipFromPercent(double tipPercent, double billAmount) {
		String tipText = "";
		double tipFraction = (tipPercent / 100);

		try {
			tipText = "" + formatToCurrency(tipFraction * billAmount);
		} catch (Exception e) {
			tipText = "";
		}
		return tipText;
	}
	
	public static String getCustomTipPercentString() {
		String text = "Custom Tip Percent";
		TipCalculatorAppData appData = TipCalculatorAppData.getInstance();
		
		if (appData.isCustomTipPercentSet()) {
			text = "Custom (" + formatToCurrency(appData.getCustomTipPercent()) + " %)";
		}
		
		return text;
	}
}
