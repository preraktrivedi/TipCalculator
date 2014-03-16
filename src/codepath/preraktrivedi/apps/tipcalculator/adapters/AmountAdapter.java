package codepath.preraktrivedi.apps.tipcalculator.adapters;

import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.calculateCustomTipPercent;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.convertStringToDouble;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.formatToCurrency;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.getCustomTipPercentString;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.isTotalInRange;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import codepath.preraktrivedi.apps.tipcalculator.R;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipAmount;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData.TipType;


/** 
 * 
 * Adapter to configure the view for a list item.
 * @author Prerak Trivedi (prerak.d.trivedi@gmail.com)
 * 
 *
 **/

public class AmountAdapter extends ArrayAdapter<TipAmount> {

	private Context mContext;
	private TipCalculatorAppData mAppData;

	private static class ViewHolder {
		TextView type;
		TextView amount;
		ImageView edit;
	}

	public AmountAdapter(Context context, ArrayList<TipAmount> tipAmount) {
		super(context, R.layout.item_lv_amount, tipAmount);
		mContext = context;
		mAppData = TipCalculatorAppData.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TipAmount tipItem = getItem(position);    
		ViewHolder viewHolder; 

		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.item_lv_amount, null);
			viewHolder.type = (TextView) convertView.findViewById(R.id.tv_type);
			viewHolder.amount = (TextView) convertView.findViewById(R.id.tv_value);
			viewHolder.edit = (ImageView) convertView.findViewById(R.id.iv_action_edit);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		configureView(tipItem, viewHolder);

		return convertView;
	}

	private void configureView(final TipAmount tipItem, ViewHolder viewHolder) {

		final double currentBillAmt =  mAppData.getCurrentBillAmount();

		viewHolder.type.setText(tipItem.getTipType().toString());
		viewHolder.amount.setText("" + tipItem.getTipAmount());
		viewHolder.amount.setVisibility(View.VISIBLE);
		viewHolder.edit.setVisibility(View.GONE);

		if (TipType.CUSTOM_PERCENT.equals(tipItem.getTipType())) {
			viewHolder.edit.setVisibility(View.VISIBLE);
			setEditAction(viewHolder, "Enter Tip Percent: ", currentBillAmt, tipItem.getTipType());
			viewHolder.type.setText(getCustomTipPercentString());
			if (mAppData.isCustomTipPercentSet()) {
				viewHolder.amount.setText(calculateCustomTipPercent());
			} else {
				viewHolder.amount.setVisibility(View.GONE);
			}
		}

		if (TipType.CUSTOM_TIP_AMOUNT.equals(tipItem.getTipType())) {
			viewHolder.edit.setVisibility(View.VISIBLE);
			setEditAction(viewHolder, "Enter Tip Amount: ", currentBillAmt, tipItem.getTipType());
			viewHolder.amount.setText(calculateCustomTipPercent());
			double tipAmount = mAppData.getCustomTipAmount();
			if (mAppData.isCustomTipAmountSet()) {
				viewHolder.amount.setText("$ " + formatToCurrency(tipAmount));
			} else {
				viewHolder.amount.setVisibility(View.GONE);
			}
		}

		if (TipType.CUSTOM_TOTAL_AMOUNT.equals(tipItem.getTipType())) {
			viewHolder.edit.setVisibility(View.VISIBLE);
			setEditAction(viewHolder, "Enter Total Amount: ", currentBillAmt, tipItem.getTipType());
			double totalAmt = mAppData.getCustomTotalAmount();
			if (mAppData.isCustomTotalAmountSet() && totalAmt > currentBillAmt) {
				viewHolder.amount.setText("$ " + formatToCurrency(totalAmt));
			} else {
				viewHolder.amount.setVisibility(View.GONE);
				mAppData.setCustomTotalAmountSet(false);
			}
		}
	}

	private void setEditAction(ViewHolder viewHolder, final String msg, final double currentBillAmt, final TipType tipType) {
		viewHolder.edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog(msg, currentBillAmt, tipType);
			}
		});
	}

	private void showAlertDialog(String msg, double currentBillAmt, final TipType tipType) {

		LayoutInflater li = LayoutInflater.from(mContext);
		View editActionView = li.inflate(R.layout.alert_prompt, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

		alertDialogBuilder.setView(editActionView);

		final EditText userInput = (EditText) editActionView.findViewById(R.id.et_prompt_input);
		final TextView tvMessage = (TextView) editActionView.findViewById(R.id.tv_prompt_msg);
		final TextView tvCurrentAmount = (TextView) editActionView.findViewById(R.id.tv_prompt_current_amt);

		tvMessage.setText(msg);
		tvCurrentAmount.setText("Bill Amount : $ " + formatToCurrency(currentBillAmt));

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
						if (validateInput(text, tipType)) {
							alertDialog.dismiss();
							notifyDataSetChanged();
						}
					}
				});
			}
		});
		alertDialog.show();
	}

	private boolean validateInput(String input, TipType tipType) {
		boolean isInputValid = false;
		double currentBillAmt = mAppData.getCurrentBillAmount(), 
				inputValue = convertStringToDouble(input);
		String errorMsg = "Please enter a valid value greater than 0.";

		Log.d("AmountAdapter", " Input value - " + inputValue);

		if (inputValue > 0) {
			if (TipType.CUSTOM_PERCENT.equals(tipType)) {
				if (inputValue < 999.99) {
					mAppData.setCustomTipPercent(inputValue);
					isInputValid = true;
				} else {
					errorMsg = "Tip Percentage too high.";
				}
			} else if (TipType.CUSTOM_TIP_AMOUNT.equals(tipType)) {
				if (!isTotalInRange(inputValue + currentBillAmt)) {
					errorMsg = "Tip amount too high. Your total amount should not exceed $9999999.99";
				} else {
					mAppData.setCustomTipAmount(inputValue);
					isInputValid = true;
				}
			} else if (TipType.CUSTOM_TOTAL_AMOUNT.equals(tipType)) {

				if (inputValue < currentBillAmt) {
					errorMsg = "Please enter the total amount greater than the bill amount.";
				} else if (!isTotalInRange(inputValue + currentBillAmt)) {
					errorMsg = "Your total amount should not exceed $9999999.99";
				} else {
					mAppData.setCustomTotalAmount(inputValue);
					isInputValid = true;
				}
			} else {

			}
		} 

		if (!isInputValid) {
			Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
		}
		return isInputValid;
	}
}