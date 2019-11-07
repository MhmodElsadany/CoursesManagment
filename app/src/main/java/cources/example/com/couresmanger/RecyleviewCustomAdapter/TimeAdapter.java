package cources.example.com.couresmanger.RecyleviewCustomAdapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cources.example.com.couresmanger.R;
import cources.example.com.couresmanger.Models.TimeItem;

/**
 * Created by Google       Company on 06/03/2018..
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.viewrowholder> {

    ArrayList<TimeItem> timeItems;
    Context context;

    public TimeAdapter(Context context, ArrayList<TimeItem> timeItems) {
        this.timeItems = timeItems;
        this.context = context;
    }

    @Override
    public viewrowholder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_center_row, null);
        //  CourseAdapter.AdverListRowHolder ALRH = new CourseAdapter.AdverListRowHolder(v);
        View view = LayoutInflater.from(context).inflate(R.layout.time_row, null);
        viewrowholder viewrowholder = new viewrowholder(view);
        return viewrowholder;
    }

    @Override
    public void onBindViewHolder(viewrowholder holder, int position) {
        TimeItem timeItem = timeItems.get(position);

        holder.day.setText(timeItem.getDay());
        holder.from.setText(timeItem.getFro());
        holder.too.setText(timeItem.getToo());

    }

    @Override
    public int getItemCount() {
        return timeItems.size();
    }

    public class viewrowholder extends RecyclerView.ViewHolder {
        TextView day;
        TextView from;
        TextView too;
        CardView cardView;

        public viewrowholder(View itemView) {
            super(itemView);

            day = (TextView) itemView.findViewById(R.id.day);
            from = (TextView) itemView.findViewById(R.id.from);
            too = (TextView) itemView.findViewById(R.id.too);
            cardView = (CardView) itemView.findViewById(R.id.card);

        }
    }

}
