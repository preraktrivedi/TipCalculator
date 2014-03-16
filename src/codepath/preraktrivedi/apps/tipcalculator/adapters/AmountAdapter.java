package codepath.preraktrivedi.apps.tipcalculator.adapters;

import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.calculateCustomTipPercent;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.formatToCurrency;
import static codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils.getCustomTipPercentString;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import codepath.preraktrivedi.apps.tipcalculator.R;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipAmount;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData.TipType;

public class AmountAdapter extends ArrayAdapter<TipAmount> {
	// View lookup cache
	private Context mContext;
	private TipCalculatorAppData mAppData;

	private static class ViewHolder {
		TextView type;
		TextView amount;
		ImageView edit;
		RelativeLayout container;
	}

	public AmountAdapter(Context context, ArrayList<TipAmount> tipAmount) {
		super(context, R.layout.item_lv_amount, tipAmount);
		mContext = context;
		mAppData = TipCalculatorAppData.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TipAmount tipItem = getItem(position);    

		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.item_lv_amount, null);
			viewHolder.type = (TextView) convertView.findViewById(R.id.tv_type);
			viewHolder.amount = (TextView) convertView.findViewById(R.id.tv_value);
			viewHolder.edit = (ImageView) convertView.findViewById(R.id.iv_action_edit);
			viewHolder.container = (RelativeLayout) convertView.findViewById(R.id.rl_list_item);
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
			viewHolder.type.setText(getCustomTipPercentString());
			viewHolder.amount.setText(calculateCustomTipPercent());
		}

		if (TipType.CUSTOM_TIP_AMOUNT.equals(tipItem.getTipType())) {
			viewHolder.edit.setVisibility(View.VISIBLE);
			viewHolder.amount.setText(calculateCustomTipPercent());
		}

		if (TipType.CUSTOM_TOTAL_AMOUNT.equals(tipItem.getTipType())) {
			viewHolder.edit.setVisibility(View.VISIBLE);
			viewHolder.edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showAlertDialog("Enter Total Amount: ", currentBillAmt, tipItem.getTipType());
				}
			});
			double totalAmt = mAppData.getCustomTotalAmount();
			if (totalAmt > currentBillAmt) {
				viewHolder.amount.setText("$ " + totalAmt);
			} else {
				viewHolder.amount.setVisibility(View.GONE);
			}
		}
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
		tvCurrentAmount.setText("Current Amount : $ " + formatToCurrency(currentBillAmt));

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
							Toast.makeText(mContext, "total amt " + text, Toast.LENGTH_SHORT).show();
							mAppData.setCustomTotalAmount(2215.23);
							notifyDataSetChanged();
							alertDialog.dismiss();
						}
					}
				});
			}
		});
		alertDialog.show();
	}

	private boolean validateInput(String input, TipType tipType) {
		boolean isInputValid = false;
		Toast.makeText(mContext, "false dude", Toast.LENGTH_SHORT).show();
		
		return isInputValid;
	}
}