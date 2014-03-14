package codepath.preraktrivedi.apps.tipcalculator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class TipCalculatorMainActivity extends Activity {

	private Context mContext;
	private EditText mEtAmt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator_main);
		
		mEtAmt = (EditText) findViewById(R.id.et_amount);
		mEtAmt.requestFocus();
		showSoftKeyboard(mEtAmt);
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
