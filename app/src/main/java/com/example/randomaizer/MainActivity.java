package com.example.randomaizer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    static Button currentTab;
    static int colorDefaultTab = Color.parseColor("#008577");
    static int colorCurrentTab = Color.parseColor("#00574B");

    //for swipe detector
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout content = findViewById(R.id.content);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.tab_1, content);

        currentTab = findViewById(R.id.firstTabButton);
        currentTab.setBackgroundColor(colorCurrentTab);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if(deltaX > MIN_DISTANCE || -deltaX > MIN_DISTANCE) {
                    LinearLayout container = findViewById(R.id.tabContainer);
                    int tabCount = container.getChildCount();
                    int tagCurrentTab = Integer.parseInt(currentTab.getTag().toString());

                    if(x1 > x2) {
//                        Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show ();
                        if(tagCurrentTab < tabCount) {
                            OpenTab(container.getChildAt(tagCurrentTab));//idk i think need tagCurrentTab +1 but no
                        }
                        break;
                    }

                    if(x2 > x1) {
//                        Toast.makeText(this, "right2left | tab:" + tagCurrentTab, Toast.LENGTH_SHORT).show ();
                        if(tagCurrentTab > 0) {
                            OpenTab(container.getChildAt(tagCurrentTab - 2));
                        }
                        break;
                    }
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    public void nothing(View view) {
        Toast.makeText(this, "Nothing...", Toast.LENGTH_SHORT).show();
    }

    public void OpenTab(View v) {
        Button btn = (Button)v;
        LinearLayout content = findViewById(R.id.content);
        LayoutInflater inflater = getLayoutInflater();
        HorizontalScrollView tabContainer  = findViewById(R.id.tabScrollContainer);
        int tag = Integer.valueOf(btn.getTag().toString());
        int view;

        switch (tag) {
            case 1:
                view = R.layout.tab_1;
                tabContainer.scrollTo(00, 0);
                break;
            case 2:
                view = R.layout.tab_2;
                tabContainer.scrollTo(75, 0);
                break;
            case 3:
                view = R.layout.tab_3;
                tabContainer.scrollTo(200, 0);
                break;
            case 4:
                view = R.layout.tab_4;
                tabContainer.scrollTo(300, 0);
                break;
            default:
                return;
        }

        content.removeAllViews();
        inflater.inflate(view, content);

        //change button color
        currentTab.setBackgroundColor(colorDefaultTab);
        btn.setBackgroundColor(colorCurrentTab);
        currentTab = btn;
    }

    private static int getRandom(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void CoinRandom(View view) {
        TextView res = findViewById(R.id.result);
        if(getRandom(0, 1) == 1) {
            res.setText("Да");
        } else {
            res.setText("Нет");
        }
    }

    public void NumberRandom(View view) {
        TextView res = findViewById(R.id.result);

        EditText ETmin = findViewById(R.id.min);
        int min = Integer.valueOf(ETmin.getText().toString());

        EditText ETmax = findViewById(R.id.max);
        int max = Integer.valueOf(ETmax.getText().toString());

        res.setText(String.valueOf(getRandom(min, max)));
    }

    public void WordGenerate(View v) {
        TextView res1 = findViewById(R.id.result1);
        TextView res2 = findViewById(R.id.result2);
        TextView res3 = findViewById(R.id.result3);
        EditText ETwordlen = findViewById(R.id.wordlen);
        EditText ETspread = findViewById(R.id.spread);

//        char[] alphabetEN = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
//                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] ENglas = new char[] {'a', 'e', 'i', 'o', 'u', 'y'};
        char[] ENsogl = new char[] {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k',
                'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'};

        String[] words = new String[] { "", "", "" };

        for (int n = 0; n < 3; n++) {

            //чтобы иногда начиналось с гласных
            if(getRandom(0, 1) == 1) {
                char[] x = ENglas;
                ENglas = ENsogl;
                ENsogl = x;
            }

            int glen = ENglas.length;
            int slen = ENsogl.length;
            int wordlen = Integer.valueOf(ETwordlen.getText().toString());
            int spread = Integer.valueOf(ETspread.getText().toString());

            if(getRandom(0,1) == 1)
                wordlen += getRandom(0, spread);
            else
                wordlen -= getRandom(0, spread);

            for(int i = 0; i < wordlen; i++) {
                if(i%2 == 0)
                    words[n] += (ENsogl[getRandom(0, slen-1)]);
                else
                    words[n] += (ENglas[getRandom(0, glen-1)]);
            }
        }

        res1.setText(words[0]);
        res2.setText(words[1]);
        res3.setText(words[2]);
    }
}
