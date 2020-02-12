package com.example.randomaizer;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button currentTab;
    static int colorDefaultTab = Color.parseColor("#008577");
    static int colorCurrentTab = Color.parseColor("#00574B");

    static boolean RandType = true;

    //for swipe detector. min swipe distance
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

        RadioButton randTypeR1 = findViewById(R.id.randTypeR1);
        randTypeR1.setChecked(true);
    }

    //swipe catch
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x1 = 0;
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x2 - x1;

                if(deltaX > MIN_DISTANCE || -deltaX > MIN_DISTANCE) {
                    LinearLayout container = findViewById(R.id.tabContainer);
                    int tabCount = container.getChildCount();
                    int tagCurrentTab = Integer.parseInt(currentTab.getTag().toString());

                    if(x1 > x2) {
                        //right
//                        Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show ();
                        if(tagCurrentTab < tabCount) {
                            OpenTab(container.getChildAt(tagCurrentTab));//idk i think need tagCurrentTab +1 but no
                        }
                        break;
                    }

                    if (x1 < x2) {
                        //left
//                        Toast.makeText(this, "right2left | tab:" + tagCurrentTab, Toast.LENGTH_SHORT).show ();
                        if (tagCurrentTab > 1) {
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
                tabContainer.scrollTo(0, 0);
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

    public void CoinRandom(View view) {
        CheckBox enableAnim = findViewById(R.id.enableAnim);

        if(enableAnim.isChecked()) {
            //anim of rotate and scale
            Animation flipCoin = AnimationUtils.loadAnimation(this, R.anim.little_stretch);
            findViewById(R.id.result).startAnimation(flipCoin);

            //text change delay
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //change result value after 400ms
                    TextView res = findViewById(R.id.result);
                    res.setText(Randomayzer.GetCoinRandom(RandType));
                }
            }, 400);

        } else {
            //without animation
            TextView res = findViewById(R.id.result);
            res.setText(Randomayzer.GetCoinRandom(RandType));
        }
    }

    @SuppressLint("SetTextI18n")
    public void NumberRandom(View view) {
        EditText ETmin = findViewById(R.id.min);
        EditText ETmax = findViewById(R.id.max);

        int min, max;
        try {
            min = Integer.valueOf(ETmin.getText().toString());
            max = Integer.valueOf(ETmax.getText().toString());
        } catch (Exception ex) {
            ETmin.setText("0");
            ETmax.setText("100");
            Toast.makeText(this, "oops... Don't do that anymore!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(min >= max) {
            ETmin.setText("0");
            ETmax.setText("100");
            Toast.makeText(this, "be accurate...", Toast.LENGTH_SHORT).show();
            return;
        }


        TextView res = findViewById(R.id.result);

        CheckBox enableAnim = findViewById(R.id.enableAnim);
        if (enableAnim.isChecked()) {
            Animation flipCoin = AnimationUtils.loadAnimation(this, R.anim.little_stretch);
            res.startAnimation(flipCoin);
        }

        res.setText(String.valueOf(Randomayzer.GetRandom(min, max)));
    }

    public void WordGenerate(View v) {
        TextView[] res = {
                findViewById(R.id.result1),
                findViewById(R.id.result2),
                findViewById(R.id.result3)
        };
        EditText ETwordlen = findViewById(R.id.wordlen);
        EditText ETspread = findViewById(R.id.spread);

        int wordlen;
        int spread;
        try {
            wordlen = Integer.valueOf(ETwordlen.getText().toString());
            spread = Integer.valueOf(ETspread.getText().toString());
        } catch (Exception ex) {
            ETwordlen.setText("6");
            ETspread.setText("1");
            Toast.makeText(this, "bad guy...", Toast.LENGTH_SHORT).show();
            return;
        }

        //output
        for (TextView re : res) {
            re.setText(Randomayzer.GetWordRandom(wordlen, spread));
            re.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    public void SwitchRandTypeRes(View view) {
        RadioButton rb = (RadioButton) view;

        RandType = rb.getTag().toString().equals("true");
    }
}
