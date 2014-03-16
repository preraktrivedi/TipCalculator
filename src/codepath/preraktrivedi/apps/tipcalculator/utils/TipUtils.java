package codepath.preraktrivedi.apps.tipcalculator.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData;

/** 
 * 
 * Utils for common methods to calculate tip. 
 * @author Prerak Trivedi (prerak.d.trivedi@gmail.com)
 *
 **/

public class TipUtils {

	public static boolean isTotalInRange(double total) {
		return (total > 0 && total < 9999999.99);
	}

	public static double convertStringToDouble(String text) {
		double amount = -1;
		try {
			amount = Double.parseDouble(text);
		} catch (Exception e) {
			amount = -1;
		}
		return amount;
	}

	public static boolean validateAmount(String text) {
		double tipAmount = convertStringToDouble(text);
		boolean isAmountValid = isTotalInRange(tipAmount);
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

	public static double refineTipAmount(double tipAmount) {
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
			tipText = "$ " + formatToCurrency(tipFraction * billAmount);
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
