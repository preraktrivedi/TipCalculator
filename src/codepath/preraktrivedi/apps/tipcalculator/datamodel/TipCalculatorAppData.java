package codepath.preraktrivedi.apps.tipcalculator.datamodel;

public class TipCalculatorAppData {

	private static TipCalculatorAppData sInstance;
	private double currentTipAmount;

	public static synchronized TipCalculatorAppData getInstance() {
		if (null == sInstance) {
			sInstance = new TipCalculatorAppData();
		}
		return sInstance;
	}

	public double getCurrentTipAmount() {
		return currentTipAmount;
	}

	public void setCurrentTipAmount(double currentTipAmount) {
		this.currentTipAmount = currentTipAmount;
	}

	public enum TipType {
		TEN_PERCENT("10 %"),
		FIFTEEN_PERCENT("15 %"),
		TWENTY_PERCENT("20 %"),
		CUSTOM_PERCENT("Custom %"),
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
