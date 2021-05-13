package com.example.machine2021;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class AdapterSlider extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public AdapterSlider(Context context) {
        this.context = context;
    }

    //Arrays
    public int[] slide_images ={
            R.drawable.ic_compania,
            R.drawable.ic_productor,
            R.drawable.ic_tecnico
    };

    public String[] slide_headings = {
            "XXXX XXXX",
            "XXX XXX XXX XXX",
            "XXX XXX XX XX"
    };

    public String[] slide_desc = {
            " is simply dummy text of the printing and typesetting"+
                    " industry. Lorem Ipsum has been the industry's standard dummy" +
                    " text ever since the 1500s, when an unknown printer took a galley" +
                    "  text ever since the 1500s, when an unknown printer took a galley.",
            "Sis simply dummy text of the printing and typesetting," +
                    " is simply dummy text of the printing and typesetting.",
            "is simply dummy text of the printing and typesetting."+"" +
                    "text ever since the 1500s, when an unknown printer took a galley."+
        "is simply dummy text of the printing and typesetting."

    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.presentacion_layout, container, false);

        ImageView imgSlide = view.findViewById(R.id.imgSlide);

        TextView txtname = view.findViewById(R.id.txtSlide);
        TextView txtdes = view.findViewById(R.id.txtSlideD);

        imgSlide.setImageResource(slide_images[position]);
        txtname.setText(slide_headings[position]);
        txtdes.setText(slide_desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
