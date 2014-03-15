package codepath.preraktrivedi.apps.tipcalculator.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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
			TipCalculatorAppData.getInstance().setCurrentTipAmount(refineTipAmount(tipAmount));
		} else {
			TipCalculatorAppData.getInstance().setCurrentTipAmount(-1);
		}

		return isAmountValid;
	}

	private static double refineTipAmount(double tipAmount) {
		BigDecimal bd = new BigDecimal(tipAmount);
		BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		DecimalFormat df = new DecimalFormat("#0.00");
		return Double.parseDouble(df.format(rounded.doubleValue()));
	}

	public static String calculateTipFromPercent(int tipPercent, double billAmount) {
		String tipText = "";
		double tipFraction = ((double) tipPercent) / 100;

		try {
			tipText = "" + (tipFraction * billAmount);
		} catch (Exception e) {
			tipText = "";
		}
		return tipText;
	}
}
