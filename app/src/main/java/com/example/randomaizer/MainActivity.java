package com.example.randomaizer;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    Button currentTab;
    static int colorDefaultTab = Color.parseColor("#008577");
    static int colorCurrentTab = Color.parseColor("#00574B");
    //for swipe detector. min swipe distance
    static final int MIN_DISTANCE = 150;

    //"coin" or "yes/no"
    static boolean CoinViewType = true;
    //list of dices picture
    LevelListDrawable dices = new LevelListDrawable();
    static boolean DiceInMove = false;

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

        //list inflate
        dices.addLevel(0, 6, ResourcesCompat.getDrawable(getResources(), R.drawable.dices, null));
    }

    //swipe catch
    float x1;//не трогай эту переменную

    //РАБОТАЕТ - НЕТРОГАЙ
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;

            case MotionEvent.ACTION_UP:
                float x2 = event.getX();

                float deltaX = x2 - x1;

                if(deltaX > MIN_DISTANCE || -deltaX > MIN_DISTANCE) {
                    LinearLayout container = findViewById(R.id.tabContainer);
                    int tabCount = container.getChildCount();
                    int tagCurrentTab = parseInt(currentTab.getTag().toString());

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
            default:
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
        int tag = parseInt(btn.getTag().toString());
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
            //anim
            Animation flipCoin = AnimationUtils.loadAnimation(this, R.anim.shoc);
            findViewById(R.id.result).startAnimation(flipCoin);

            //text change delay
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //change result value after 400ms
                    TextView res = findViewById(R.id.result);
                    res.setText(Randomayzer.GetCoinRandom(CoinViewType));
                }
            }, 400);

        } else {
            //without animation
            TextView res = findViewById(R.id.result);
            res.setText(Randomayzer.GetCoinRandom(CoinViewType));
        }
    }

    @SuppressLint("SetTextI18n")
    public void NumberRandom(View view) {
        EditText ETmin = findViewById(R.id.min);
        EditText ETmax = findViewById(R.id.max);

        int min, max;
        try {
            min = parseInt(ETmin.getText().toString());
            max = parseInt(ETmax.getText().toString());
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
            Animation flipCoin = AnimationUtils.loadAnimation(this, R.anim.little_stretch_and_mega_rotate);
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
            wordlen = parseInt(ETwordlen.getText().toString());
            spread = parseInt(ETspread.getText().toString());
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

        CoinViewType = rb.getTag().toString().equals("true");
    }

    public void DiceRandom(View view) {
        ImageButton but = (ImageButton) view;

        //BUG
        //если не юзануть 2 нижние строчки, то первый "бросок"
        // кубика будет без изменений (выкинет первый элемент из diceList)
        dices.setLevel(0);
        but.setImageDrawable(dices.getCurrent());

        Animation diceRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        but.startAnimation(diceRotate);
        but.setRotation(but.getRotation() - 90);

        dices.setLevel(Randomayzer.GetRandom(1, 6));
        but.setImageDrawable(dices.getCurrent());
    }

    public void Info(View view) {
        ImageButton but = findViewById(R.id.tipButton);
        TextView text = findViewById(R.id.wordTipTextContainer);

        if (but.getTag().toString().equals("1")) {
            but.setImageBitmap(((BitmapDrawable) Objects.requireNonNull(getDrawable(R.drawable.tip_default))).getBitmap());
            but.setTag(0);
            text.setVisibility(View.INVISIBLE);
        } else {
            but.setImageBitmap(((BitmapDrawable) Objects.requireNonNull(getDrawable(R.drawable.tip_accent))).getBitmap());
            but.setTag(1);
            text.setVisibility(View.VISIBLE);
        }
    }
}
