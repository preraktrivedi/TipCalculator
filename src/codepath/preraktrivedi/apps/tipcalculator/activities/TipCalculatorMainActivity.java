package codepath.preraktrivedi.apps.tipcalculator.activities;

import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.calculateCustomTipPercent;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.convertStringToDouble;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.validateAmount;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import codepath.preraktrivedi.apps.tipcalculator.R;
import codepath.preraktrivedi.apps.tipcalculator.adapters.AmountAdapter;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipAmount;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData.TipType;


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
	private double finalTipAmount = 0;
	private int currentItemSelected = -1, previousItemSelected = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator_main);
		mContext = this;
		mAppData = TipCalculatorAppData.getInstance();
		initializeUi();
	}

	private void initializeUi() {
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

		mBtSplit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateForm()) {
					showAlertDialog("Enter Number of People: ");
				}
			}
		});

		mBtDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateForm()) {
					mAppData.setNumberOfPeople(1);
					startSummaryActivity();
				}
			}
		});

		mIbAmtDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkIfInputIsValid();
			}
		});

		mEtAmt.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					checkIfInputIsValid();
				}    
				return false;
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

	private void checkIfInputIsValid() {
		String text = mEtAmt.getText().toString().trim();
		if (validateAmount(text)) {
			showListView(true);
		} else {
			showListView(false);
			showToast(getResources().getString(R.string.error_invalid_amount));
		}
	}

	private boolean validateForm() {
		boolean isFormValid = false;
		finalTipAmount  = mAppData.getCurrentTipAmount();
		String errorMsg = "Please select a valid item before we can continue.";
		if (mAmountAdapter != null && validateCurrentItem()) {
			TipAmount currentItem = mAmountAdapter.getItem(currentItemSelected);
			TipType itemTipType = currentItem.getTipType();

			if(TipType.CUSTOM_PERCENT.equals(itemTipType)) {
				if(mAppData.isCustomTipPercentSet()) {
					finalTipAmount = convertStringToDouble(calculateCustomTipPercent());
					if(finalTipAmount > 0) {
						isFormValid = true;
					}
				} else {
					errorMsg = "Please select a valid tip percent.";
				}
			} else if (TipType.CUSTOM_TIP_AMOUNT.equals(itemTipType)) {
				if (mAppData.isCustomTipAmountSet()) {
					finalTipAmount = mAppData.getCustomTipAmount();
					isFormValid = true;
				} else {
					errorMsg = "Please select a valid tip amount.";
				}
			} else if (TipType.CUSTOM_TOTAL_AMOUNT.equals(itemTipType)) {
				if (mAppData.isCustomTotalAmountSet()) {
					finalTipAmount = mAppData.getCustomTotalAmount() - mAppData.getCurrentBillAmount();
					isFormValid = true;
				} else {
					errorMsg = "Please select a valid total amount.";
				}
			} else if (TipType.TEN_PERCENT.equals(itemTipType) || TipType.FIFTEEN_PERCENT.equals(itemTipType) || TipType.TWENTY_PERCENT.equals(itemTipType)) {
				finalTipAmount = convertStringToDouble(currentItem.getTipAmount());
				isFormValid = true;
			}
		} 

		if (!(isFormValid && finalTipAmount > 0)) {
			isFormValid = false;
			showToast(errorMsg);
			finalTipAmount = -1;
		}

		mAppData.setCurrentTipAmount(finalTipAmount);

		return isFormValid;
	}

	private void showListView(boolean show) {
		if (show) {
			mAmountAdapter = null;
			mAmountAdapter = new AmountAdapter(this, TipAmount.getTipAmounts(mAppData.getCurrentBillAmount()));
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

	private void showAlertDialog(String msg) {

		LayoutInflater li = LayoutInflater.from(mContext);
		View editActionView = li.inflate(R.layout.alert_prompt, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

		alertDialogBuilder.setView(editActionView);

		final EditText userInput = (EditText) editActionView.findViewById(R.id.et_prompt_input);
		final TextView tvMessage = (TextView) editActionView.findViewById(R.id.tv_prompt_msg);
		final TextView tvCurrentAmount = (TextView) editActionView.findViewById(R.id.tv_prompt_current_amt);

		userInput.setInputType(InputType.TYPE_CLASS_NUMBER);
		tvMessage.setText(msg);
		tvCurrentAmount.setVisibility(View.GONE);

		alertDialogBuilder.setCancelable(false)
		.setPositiveButton("OK", null)
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});

		final AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button positiveButon = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				positiveButon.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String text = userInput.getText().toString();
						int numberOfPeople;
						try {
							numberOfPeople = Integer.parseInt(text);
						} catch (Exception e) {
							numberOfPeople = -1;
						}
						mAppData.setNumberOfPeople(numberOfPeople);
						if (numberOfPeople > 0 && numberOfPeople < 1000) {
							alertDialog.dismiss();
							startSummaryActivity();
						} else {
							showToast("Number of people should be in range 0 to 1000.");
						}
					}
				});
			}
		});
		alertDialog.show();
	}

	private void startSummaryActivity() {
		Intent intent = new Intent(this, BillSummaryActivity.class);
		startActivity(intent);
	}

	private boolean validateCurrentItem() {
		return (currentItemSelected >= 0 && currentItemSelected < mAmountAdapter.getCount());
	}
}
