package codepath.preraktrivedi.apps.tipcalculator.activities;

import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import codepath.preraktrivedi.apps.tipcalculator.R;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData;

public class BillSummaryActivity extends Activity {

	private TipCalculatorAppData mAppData;
	private TextView mTvBillTotal, mTvTipAmt, mTvTipAmtLabel, mTvGrandTotal, mTvNumPeople, mTvAmtPerPerson;
	private RelativeLayout mRlNumPeopleContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAppData = TipCalculatorAppData.getInstance();
		setContentView(R.layout.activity_bill_summary);
		initializeUi();
	}

	private void initializeUi() {
		mTvBillTotal = (TextView) findViewById(R.id.tv_bill_subtotal_amt);
		mTvTipAmt = (TextView) findViewById(R.id.tv_tip_amount_amt);
		mTvTipAmtLabel = (TextView) findViewById(R.id.tv_tip_amount);
		mTvGrandTotal = (TextView) findViewById(R.id.tv_grand_total_amt);
		mTvNumPeople = (TextView) findViewById(R.id.tv_num_people_amt);
		mTvAmtPerPerson = (TextView) findViewById(R.id.tv_per_person_amt);
		mRlNumPeopleContainer = (RelativeLayout) findViewById(R.id.rl_num_people_container);
		populateFields();
	}

	private void populateFields() {
		int numPeople = mAppData.getNumberOfPeople();
		double currentBillAmount = mAppData.getCurrentBillAmount(),
				currentTipAmount = mAppData.getCurrentTipAmount(),
				grandTotal = currentBillAmount + currentTipAmount,
				tipPercent = refineTipAmount((currentTipAmount / currentBillAmount)*100);
		mTvBillTotal.setText(addDollarValue(formatToCurrency(currentBillAmount)));
		mTvTipAmtLabel.setText("Tip Amount (" + formatToCurrency(tipPercent) + " %)");
		mTvTipAmt.setText(addDollarValue(formatToCurrency(currentTipAmount)));
		mTvGrandTotal.setText(addDollarValue(formatToCurrency(grandTotal)));

		if(numPeople > 1) {
			mRlNumPeopleContainer.setVisibility(View.VISIBLE);
			mTvNumPeople.setText("" + numPeople);
			mTvAmtPerPerson.setText(addDollarValue(formatToCurrency(grandTotal/numPeople)));
		} else {
			mRlNumPeopleContainer.setVisibility(View.GONE);
		}
	}
	
	private String addDollarValue(String input) {
		return "$ " + input;
	}
}
