package codepath.preraktrivedi.apps.tipcalculator.datamodel;

/** 
 * 
 * Central data store for maintaining data integrity
 * @author Prerak Trivedi (prerak.d.trivedi@gmail.com)
 * 
 *
 **/

public class TipCalculatorAppData {

	private static TipCalculatorAppData sInstance;
	private double currentBillAmount, customTipAmount, customTipPercent, customTotalAmount;
	private boolean isCustomTipPercentSet, isCustomTipAmountSet, isCustomTotalAmountSet;

	public static synchronized TipCalculatorAppData getInstance() {
		if (null == sInstance) {
			sInstance = new TipCalculatorAppData();
		}
		return sInstance;
	}

	public double getCurrentBillAmount() {
		return currentBillAmount;
	}

	public void setCurrentBillAmount(double currentBillAmount) {
		this.currentBillAmount = currentBillAmount;
	}

	public double getCustomTipAmount() {
		return customTipAmount;
	}

	public void setCustomTipAmount(double customTipAmount) {
		this.customTipAmount = customTipAmount;
		
		if(!isCustomTipAmountSet()) {
			setCustomTipAmountSet(true);
		}
	}

	public double getCustomTipPercent() {
		return customTipPercent;
	}

	public void setCustomTipPercent(double customTipPercent) {
		this.customTipPercent = customTipPercent;
		
		if(!isCustomTipPercentSet()) {
			setCustomTipPercentSet(true);
		}
	}

	public double getCustomTotalAmount() {
		return customTotalAmount;
	}

	public void setCustomTotalAmount(double customTotalAmount) {
		this.customTotalAmount = customTotalAmount;
		
		if(!isCustomTotalAmountSet()) {
			setCustomTotalAmountSet(true);
		}
	}

	public boolean isCustomTipPercentSet() {
		return isCustomTipPercentSet;
	}

	public void setCustomTipPercentSet(boolean isCustomTipPercentSet) {
		this.isCustomTipPercentSet = isCustomTipPercentSet;
	}

	public boolean isCustomTipAmountSet() {
		return isCustomTipAmountSet;
	}

	public void setCustomTipAmountSet(boolean isCustomTipAmountSet) {
		this.isCustomTipAmountSet = isCustomTipAmountSet;
	}

	public boolean isCustomTotalAmountSet() {
		return isCustomTotalAmountSet;
	}

	public void setCustomTotalAmountSet(boolean isCustomTotalAmountSet) {
		this.isCustomTotalAmountSet = isCustomTotalAmountSet;
	}

	public enum TipType {
		TEN_PERCENT("10 %"),
		FIFTEEN_PERCENT("15 %"),
		TWENTY_PERCENT("20 %"),
		CUSTOM_PERCENT("Custom Tip Percent"),
		CUSTOM_TIP_AMOUNT("Custom Tip Amount"),
		CUSTOM_TOTAL_AMOUNT("Custom Total Amount")
		;

		private String label;

		private TipType(String tipLabel) {
			this.label = tipLabel;
		}

		public String toString() {
			return label;
		}
	}
	
}
