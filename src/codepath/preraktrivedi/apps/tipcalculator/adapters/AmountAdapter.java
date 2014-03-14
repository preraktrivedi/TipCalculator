package codepath.preraktrivedi.apps.tipcalculator.adapters;

import java.util.ArrayList;

import codepath.preraktrivedi.apps.tipcalculator.R;
import codepath.preraktrivedi.apps.tipcalculator.datamodel.TipAmount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AmountAdapter extends ArrayAdapter<TipAmount> {
    // View lookup cache
    private static class ViewHolder {
        TextView type;
        TextView amount;
    }

    public AmountAdapter(Context context, ArrayList<TipAmount> tipAmount) {
       super(context, R.layout.item_lv_amount, tipAmount);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       TipAmount user = getItem(position);    
       // Check if an existing view is being reused, otherwise inflate the view
       ViewHolder viewHolder; // view lookup cache stored in tag
       if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_lv_amount, null);
          viewHolder.type = (TextView) convertView.findViewById(R.id.tv_type);
          viewHolder.amount = (TextView) convertView.findViewById(R.id.tv_value);
          convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder) convertView.getTag();
       }
       // Populate the data into the template view using the data object
       viewHolder.type.setText(user.tipType);
       viewHolder.amount.setText(user.tipAmount);
       // Return the completed view to render on screen
       return convertView;
   }
}