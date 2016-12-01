package com.hyz.test.caculater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    final String TAG = "HYZ";
    ArrayList<String> m_express = new ArrayList<String>();
    String m_tmpStr="0";
    TextView text_result;
    TextView text_express;
    boolean m_isNoInput = true;//是不是还没有开始输入（初始化时、点了“=”时）
    Manger m_manger=new Manger();
    boolean _isNormalType = true;
    boolean m_isTurning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        text_result = (TextView)this.findViewById(R.id.text_result);
        text_express = (TextView)this.findViewById(R.id.text_express);

        text_express.setText("0");
        text_result.setText("");

        m_express.add("0");

        _isNormalType = true;

        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        View result_panel = findViewById(R.id.result_panel);
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) result_panel.getLayoutParams();
        linearParams.height = dm2.heightPixels*3/8;
        result_panel.setLayoutParams(linearParams);

    }
    private Animation getScaleAni(float formX,float toX,float formY,float toY,long duration,float archX,float archY){
        Animation scaleAnimation=new ScaleAnimation(formX,toX,formY,toY,
                Animation.RELATIVE_TO_SELF,archX,Animation.RELATIVE_TO_SELF,archY);

        scaleAnimation.setDuration(duration);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    private Animation getTranslateAniY(float formY,float toY,long duration){
        Animation translateAnimation=new TranslateAnimation(0, 0, formY, toY);

        translateAnimation.setDuration(duration);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    private Animation getAlphaAni(float from,float to,long duration){
        Animation alphaAnimation=new AlphaAnimation(from, to);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    private void runResultAction(){
        int fromY = text_express.getTop();
        int toY = text_result.getTop();
        
        AnimationSet ani_1=new AnimationSet(true);
        ani_1.addAnimation(getTranslateAniY(300,0.0f,200));
        ani_1.addAnimation(getAlphaAni(0.4f,1.0f,200));
        ani_1.setFillAfter(true);
        text_express.startAnimation(ani_1);

        AnimationSet ani_2=new AnimationSet(true);
        ani_2.addAnimation(getTranslateAniY(fromY - toY,0.0f,200));
        ani_2.addAnimation(getScaleAni(1.0f,0.6f,1.0f,0.6f,200,1.0f,0.5f));
        ani_2.setFillAfter(true);
        text_result.startAnimation(ani_2);
    }

    private void runTurnAction(){
        if(m_isTurning)
            return;
        _isNormalType = !_isNormalType;
        m_isTurning = true;
        final View frist_panel = findViewById((R.id.frist_panel));
        final View secend_panel = findViewById((R.id.secend_panel));
        secend_panel.setVisibility(View.VISIBLE);
        Animation scaleAnimation;
        final long duration = 500;
        if(_isNormalType)
            scaleAnimation = getScaleAni(1.0f,1.25f,1.0f,1.2f,duration,1.0f,1.0f);//从科学型放大到普通型
        else
            scaleAnimation = getScaleAni(1.25f,1.0f,1.2f,1.0f,duration,1.0f,1.0f);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e(TAG, "---start!");
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e(TAG, "---end!");
                m_isTurning = false;
                secend_panel.clearAnimation();
                secend_panel.invalidate();
                if(_isNormalType){
                    secend_panel.setVisibility(View.GONE);
                    frist_panel.setVisibility(View.VISIBLE);
                }else{
                    secend_panel.setVisibility(View.VISIBLE);
                    frist_panel.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.e(TAG, "---repeat!");
            }
        });

        secend_panel.startAnimation(scaleAnimation);
    }
    public void onClick(View v)
    {
        Button btn = (Button)v;
        int id = btn.getId();
        String str = "";
        switch(id)
        {
            case R.id.btn_0:
            case R.id.btn_0_2:
            case R.id.btn_1:
            case R.id.btn_1_2:
            case R.id.btn_2:
            case R.id.btn_2_2:
            case R.id.btn_3:
            case R.id.btn_3_2:
            case R.id.btn_4:
            case R.id.btn_4_2:
            case R.id.btn_5:
            case R.id.btn_5_2:
            case R.id.btn_6:
            case R.id.btn_6_2:
            case R.id.btn_7:
            case R.id.btn_7_2:
            case R.id.btn_8:
            case R.id.btn_8_2:
            case R.id.btn_9:
            case R.id.btn_9_2:
            case R.id.btn_point:        // .
            case R.id.btn_point_2:      // .
            case R.id.btn_add:          //＋
            case R.id.btn_add_2:        //＋
            case R.id.btn_sub:          //－
            case R.id.btn_sub_2:        //－
            case R.id.btn_multiply:     //×
            case R.id.btn_multiply_2:   //×
            case R.id.btn_devide:       //÷
            case R.id.btn_devide_2:     //÷
            case R.id.btn_sin:
            case R.id.btn_cos:
            case R.id.btn_tan:
            case R.id.btn_In:
            case R.id.btn_log:
            case R.id.btn_bracket_left://(
            case R.id.btn_bracket_right://)
            case R.id.btn_factorial://!
            case R.id.btn_power://开方
            case R.id.btn_sqrt://开根号
                str = btn.getText().toString();
                str +="";
                break;
            case R.id.btn_e://自然底数
                break;
            case R.id.btn_pi://圆周率，π
                break;
            case R.id.btn_equal:
            case R.id.btn_equal_2:
                m_isNoInput = true;
                if(m_express.size()<=1)
                    return;
                String resultStr = m_manger.caculate(m_express);
                String expressStr = text_express.getText().toString();
                text_result.setText(expressStr);
                m_express.clear();
                m_express.add(resultStr);
                text_express.setText(resultStr);
                m_tmpStr = resultStr;
                runResultAction();
                break;
            case R.id.btn_clear:
            case R.id.btn_clear_2:
                m_express.clear();
                m_tmpStr="0";
                m_express.add(m_tmpStr);
                text_express.setText(m_tmpStr);
                text_result.setText("");
                break;
            case R.id.btn_delete:
            case R.id.btn_delete_2:
                if(m_express.size()==1){
                    m_express.clear();
                    m_tmpStr="0";
                    m_express.add(m_tmpStr);
                    text_express.setText(m_tmpStr);
                    text_result.setText("");
                    return;
                }
                String lastStr = m_express.get(m_express.size()-1);
                if(lastStr.length()==1){
                    m_express.remove(m_express.size()-1);
                    m_tmpStr = m_express.get(m_express.size()-1);
                }else{
                    int i=lastStr.length();
                    lastStr = lastStr.substring(0,lastStr.length()-1);
                    m_express.set(m_express.size()-1,lastStr);
                    m_tmpStr = lastStr;
                }
                String _text="";
                for(int i=0;i<m_express.size();i++){
                    _text+=(m_express.get(i));
                }
                text_express.setText(_text);
                break;
            case R.id.btn_turn:
                runTurnAction();
                break;
            default:
                break;
        }

        if(!str.equals("")){
            if(str.equals(".")){
                if(Manger.isNum(m_tmpStr)&&m_tmpStr.indexOf(".")==-1){
                    m_tmpStr+=str;
                    m_express.set(m_express.size()-1,m_tmpStr);
                }
                m_isNoInput = false;
            }else {
                if(Manger.isNum(m_tmpStr)||m_tmpStr.indexOf(".")!=-1){//上次输入的是数字
                    if(Manger.isNum(str)){//这次也是数字
                        if(m_isNoInput){//还没有输入
                            m_tmpStr=str;
                        }else{
                            m_tmpStr+=str;
                        }

                        m_express.set(m_express.size()-1,m_tmpStr);
                    }else if(!m_manger.canBehindNumber(str)){//这次是操作符，但不合法，如“9(”、“9sin”
                        Toast.makeText(this, R.string.donotAllow, Toast.LENGTH_SHORT).show();
                    }
                    else{//这次是操作符，但合法
                        m_express.add(str);
                        m_tmpStr = str;
                    }
                }else{//上次输入的是操作符
                    if(Manger.isNum(str)) {//这次是数字
                        if(!m_manger.canFrontNumber(m_tmpStr)){//但是不合法，如")9"、"!9"
                            Toast.makeText(this, R.string.donotAllow, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            m_express.add(str);
                            m_tmpStr = str;
                        }

                    }else if(m_manger.allowTwoOperator(m_tmpStr,str)){//两次输入字符，合法
                        m_express.add(str);
                        m_tmpStr = str;
                    }
                    else{//两次输入字符，但不合法
                        m_express.set(m_express.size()-1,str);
                        m_tmpStr = str;
                    }
                }
                m_isNoInput = false;
            }

            String _text="";
            for(int i=0;i<m_express.size();i++){
                _text+=(m_express.get(i));
            }
            text_express.setText(_text);
        }
        Log.d(TAG,str);
    }

}
