package com.example.machine2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class PresentacionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;

    private TextView[] mDots;

    private AdapterSlider adapterSlider;

    private Button back, next;

    private int mCurren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_presentacion);

        viewPager = findViewById(R.id.slideVipager);
        linearLayout = findViewById(R.id.layoutSlide);

        back = findViewById(R.id.btnBack);
        next = findViewById(R.id.btnNext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurren+1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurren-1);
            }
        });

        adapterSlider = new AdapterSlider(this);

        viewPager.setAdapter(adapterSlider);

        if (restorePrefData()){
            Intent intent = new Intent(PresentacionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        addDotsIndicador(0);

        viewPager.addOnPageChangeListener(viewListener);
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;
    }

    private void addDotsIndicador(int position) {
        mDots = new TextView[3];
        linearLayout.removeAllViews();

        for (int i = 0; i< mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparente));

            linearLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.purple_200));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicador(i);
            mCurren = i;
            if (i==0){
                next.setEnabled(true);
                back.setEnabled(false);
                back.setVisibility(View.INVISIBLE);

                next.setText("Siguiente");
                back.setText("");
            }else if (i == mDots.length -1){
                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);

                next.setText("Finalizar");

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PresentacionActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(PresentacionActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        saveDato();
                        finish();
                    }
                });

                back.setText("Atrás");
            }else{
                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);

                next.setText("Siguiente");
                back.setText("Atrás");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void saveDato() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();
    }
}