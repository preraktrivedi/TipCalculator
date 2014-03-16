package codepath.preraktrivedi.apps.tipcalculator.activities;

import codepath.preraktrivedi.apps.tipcalculator.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BillSummaryActivity extends Activity {

	private Context mContext;
	private TextView mTvBillTotal, mTvTipAmt, mTvTipAmtLabel, mTvGrandTotal, mTvNumPeople, mTvAmtPerPerson;
	private RelativeLayout mRlNumPeopleContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
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
	}
}
