package codepath.preraktrivedi.apps.tipcalculator.adapters;

import java.util.ArrayList;

import codepath.preraktrivedi.apps.tipcalculator.R;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipAmount;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipCalculatorAppData.TipType;
import codepath.preraktrivedi.apps.tipcalculator.utils.TipUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AmountAdapter extends ArrayAdapter<TipAmount> {
	// View lookup cache
	private Context mContext;
	private TipCalculatorAppData mAppData;

	private static class ViewHolder {
		TextView type;
		TextView amount;
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
			viewHolder.container = (RelativeLayout) convertView.findViewById(R.id.rl_list_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		configureView(tipItem, viewHolder);

		return convertView;
	}

	private void configureView(TipAmount tipItem, ViewHolder viewHolder) {
		viewHolder.type.setText(tipItem.getTipType().toString());
		viewHolder.amount.setText("$ " +tipItem.getTipAmount());

		if (tipItem.getTipType().equals(TipType.CUSTOM_PERCENT)) {
			viewHolder.type.setText(TipUtils.getCustomTipPercentString());
		}
	}
}