package com.app.hubert.guide.newBieguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.hubert.guide.R;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.listener.OnPageChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.RelativeGuide;

public class NewBieGuideActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, NewBieGuideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.setTranslucent(this, 0);
        setContentView(R.layout.activity_newbieguide);
        TextView textView = (TextView) findViewById(R.id.tv);
        TextView tvBottom = (TextView) findViewById(R.id.tv_bottom);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridViewActivity.start(NewBieGuideActivity.this);
            }
        });
        final Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFragmentActivity.start(NewBieGuideActivity.this);
            }
        });

        View anchor = findViewById(R.id.ll_anchor);

        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);

        //???????????????????????????????????????????????????????????????
        NewbieGuide.with(this)
                .setLabel("page")//??????????????????????????????????????????????????????????????????
//                .anchor(anchor)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e(TAG, "NewbieGuide onShowed: ");
                        //???????????????
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e(TAG, "NewbieGuide  onRemoved: ");
                        //?????????????????????????????????????????????
                    }
                })
                .setOnPageChangedListener(new OnPageChangedListener() {

                    @Override
                    public void onPageChanged(int page) {
                        //??????????????????page????????????????????????0??????
                        Toast.makeText(NewBieGuideActivity.this, "??????????????????" + page, Toast.LENGTH_SHORT).show();
                    }
                })
                .alwaysShow(true)//???????????????????????????????????????false??????????????????
                .addGuidePage(//?????????????????????
                        GuidePage.newInstance()//??????????????????
                                .addHighLight(button)//???????????????view
                                .addHighLight(tvBottom,
                                        new RelativeGuide(R.layout.view_relative_guide, Gravity.TOP, 100) {
                                            @Override
                                            protected void offsetMargin(MarginInfo marginInfo, ViewGroup viewGroup, View view) {
                                                marginInfo.leftMargin += 100;
                                            }
                                        })
                                .setLayoutRes(R.layout.view_guide)//?????????????????????
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {

                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        //????????????????????????????????????????????????
                                        TextView tv = view.findViewById(R.id.textView2);
                                        tv.setText("???????????????????????????");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//????????????
                                .setExitAnimation(exitAnimation)//????????????
                )
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(tvBottom, HighLight.Shape.RECTANGLE, 20)
                        .setLayoutRes(R.layout.view_guide_custom, R.id.iv)//?????????????????????????????????????????????????????????????????????id
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, final Controller controller) {
                                view.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controller.showPreviewPage();
                                    }
                                });
                            }
                        })
                        .setEverywhereCancelable(false)//?????????????????????????????????????????????????????????????????????true
                        .setBackgroundColor(getResources().getColor(R.color.testColor))//???????????????????????????????????????????????????
                        .setEnterAnimation(enterAnimation)//????????????
                        .setExitAnimation(exitAnimation)//????????????
                )
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(tvBottom)
                        .setLayoutRes(R.layout.view_guide_dialog)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, final Controller controller) {
                                view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controller.showPage(0);
                                    }
                                });
                            }
                        })
                )
                .show();//???????????????(???????????????????????????????????????)
    }
}
