package cources.example.com.couresmanger.RecyleviewCustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cources.example.com.couresmanger.ActivityDetail.ActivitDetailCourse;
import cources.example.com.couresmanger.Models.DetailItems;
import cources.example.com.couresmanger.Models.ItemPoster;
import cources.example.com.couresmanger.R;
import cources.example.com.couresmanger.Tabs.Main2Activity;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.AdverListRowHolder> {

    DetailItems[] detailItemses;
    ArrayList<ItemPoster> images;
    Context c;
    String resesve;
    Main2Activity main2Activity;

    public CourseAdapter(ArrayList<ItemPoster> images, Context c, DetailItems[] detailItemses, String resesve) {
        this.images = images;
        this.c = c;
        this.detailItemses = detailItemses;
        this.resesve = resesve;

    }


    @Override
    public int getItemCount() {
        return (null != images ? images.size() : 0);

    }


    @Override
    public AdverListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_center_row, null);


        AdverListRowHolder ALRH = new AdverListRowHolder(v);

        return ALRH;
    }

    @Override
    public void onBindViewHolder(AdverListRowHolder holder, int position) {

        ItemPoster items = images.get(position);
        Picasso.with(c).load("http://mahmoudllsadany.000webhostapp.com/center/images/" + items.getImages()).placeholder(R.drawable.loading).error(R.drawable.loading).into(holder.imageView);
        holder.textView.setText(images.get(position).getname());

    }


    public class AdverListRowHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        CardView cv;
        TextView textView;

        public AdverListRowHolder(View itemView) {
            super(itemView);
            //this.cv =(CardView)itemView.findViewById(R.id.cv);
            this.imageView = (ImageView) itemView.findViewById(R.id.postter);
            textView = (TextView) itemView.findViewById(R.id.course_namee);


            //go to all detail of course
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent detail = new Intent(c, ActivitDetailCourse.class);
                    detail.putExtra("id_course", detailItemses[getAdapterPosition()].getid());
                    detail.putExtra("name_course", detailItemses[getAdapterPosition()].getName_course());
                    detail.putExtra("insrator_course", detailItemses[getAdapterPosition()].getInstractor_course());
                    detail.putExtra("price_course", detailItemses[getAdapterPosition()].getPrice());
                    detail.putExtra("image_icon", detailItemses[getAdapterPosition()].getImage_icon());
                    detail.putExtra("iduser", detailItemses[getAdapterPosition()].getiduser());
                    detail.putExtra("reseve", resesve);
                    c.startActivity(detail);

                }
            });
        }
    }

}
