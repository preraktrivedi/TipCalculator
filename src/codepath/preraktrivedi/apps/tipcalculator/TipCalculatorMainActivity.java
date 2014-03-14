package codepath.preraktrivedi.apps.tipcalculator;

import java.util.ArrayList;

import codepath.preraktrivedi.apps.tipcalculator.adapters.AmountAdapter;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipAmount;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

public class TipCalculatorMainActivity extends Activity {

	private Context mContext;
	private EditText mEtAmt;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator_main);

		mEtAmt = (EditText) findViewById(R.id.et_amount);
		listview = (ListView) findViewById(R.id.lv_items);
		mEtAmt.requestFocus();
		showSoftKeyboard(mEtAmt);

		// Construct the data source
		ArrayList<TipAmount> tipArray = new ArrayList<TipAmount>();
		// Create the adapter to convert the array to views
		AmountAdapter adapter = new AmountAdapter(this, TipAmount.getSampleTipAmounts());
		// Attach the adapter to a ListView
		listview.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tip_calculator_main, menu);
		return true;
	}

	public void showSoftKeyboard(View view) {
		if(view.isFocused()) {
			InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
		}
	}
}
