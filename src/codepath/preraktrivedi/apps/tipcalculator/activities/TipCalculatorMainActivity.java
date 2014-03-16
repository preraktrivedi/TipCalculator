package codepath.preraktrivedi.apps.tipcalculator.activities;

import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.validateAmount;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import codepath.preraktrivedi.apps.tipcalculator.R;
import codepath.preraktrivedi.apps.tipcalculator.adapters.AmountAdapter;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipAmount;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData;


/** 
 * 
 * Main activity to accept user input and perform actions associated with it
 * @author Prerak Trivedi (prerak.d.trivedi@gmail.com)
 * 
 *
 **/

public class TipCalculatorMainActivity extends Activity {

	private Context mContext;
	private EditText mEtAmt;
	private ListView mTipOptionsListDisplay;
	private AmountAdapter mAmountAdapter;
	private ImageButton mIbAmtDone;
	private Button mBtSplit, mBtDone;
	private RelativeLayout mRlButtonContainer;
	private TipCalculatorAppData mAppData;
	private int currentItemSelected = -1, previousItemSelected = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator_main);
		mContext = this;
		initializeUi();
	}

	private void initializeUi() {
		mAppData = TipCalculatorAppData.getInstance();
		mEtAmt = (EditText) findViewById(R.id.et_amount);
		mIbAmtDone = (ImageButton) findViewById(R.id.ib_action_done);
		mTipOptionsListDisplay = (ListView) findViewById(R.id.lv_items);
		mBtSplit = (Button) findViewById(R.id.bt_split_amount);
		mBtDone = (Button) findViewById(R.id.bt_done);
		mRlButtonContainer = (RelativeLayout) findViewById(R.id.rl_button_container);
		mEtAmt.requestFocus();
		showSoftKeyboard(mEtAmt);
		setupListeners();
		showListView(false);
	}

	private void setupListeners() {

		mBtDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateForm()) {

				}
			}
		});

		mIbAmtDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = mEtAmt.getText().toString().trim();
				if (validateAmount(text)) {
					showListView(true);
				} else {
					showListView(false);
					showToast(getResources().getString(R.string.error_invalid_amount));
				}
			}
		});

		mEtAmt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String input = s.toString();

				if (!TextUtils.isEmpty(input) && input.trim().length() > 0) {
					if (validateAmount(input)) {
						showListView(true);
					} else {
						showListView(false);
						showToast(getResources().getString(R.string.error_invalid_amount));
					}
				} else {
					showListView(false);
					showToast(getResources().getString(R.string.error_invalid_amount));
				}
			}
		});

		mTipOptionsListDisplay.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
				view.setSelected(true);
				currentItemSelected = position;

				if (previousItemSelected != currentItemSelected) {
					previousItemSelected = currentItemSelected;
				}
			}
		});
	}

	private boolean validateForm() {
		boolean isFormValid = false;
		String errorMsg = "Please select a valid item before we can continue.";
		if (mAmountAdapter != null && validateCurrentItem()) {
			TipAmount item = mAmountAdapter.getItem(currentItemSelected);
			Toast.makeText(getApplicationContext(),
					"current item amount " + item.getTipAmount(), Toast.LENGTH_SHORT)
					.show();
		} 

		if (!isFormValid) {
			showToast(errorMsg);
		}
		
		return isFormValid;
	}

	private void showListView(boolean show) {
		if (show) {
			// Create the adapter to convert the array to views
			mAmountAdapter = null;
			mAmountAdapter = new AmountAdapter(this, TipAmount.getTipAmounts(mAppData.getCurrentBillAmount()));
			// Attach the adapter to a ListView
			mTipOptionsListDisplay.setAdapter(mAmountAdapter);
			if(validateCurrentItem()) {
				mTipOptionsListDisplay.setSelection(currentItemSelected);
			}
			mTipOptionsListDisplay.setVisibility(View.VISIBLE);
			mRlButtonContainer.setVisibility(View.VISIBLE);
		} else {
			mTipOptionsListDisplay.setVisibility(View.GONE);
			mRlButtonContainer.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tip_calculator_main, menu);
		return true;
	}

	private void showSoftKeyboard(View view) {
		if(view.isFocused()) {
			InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
		}
	}

	private void showToast(String msg) {
		Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	private boolean validateCurrentItem() {
		return (currentItemSelected >= 0 && currentItemSelected < mAmountAdapter.getCount());
	}
}
