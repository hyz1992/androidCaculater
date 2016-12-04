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
        String str_input = "";
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
                str_input = btn.getText().toString();
                str_input +="";
                break;
            case R.id.btn_e://自然底数
                str_input = "e";
                break;
            case R.id.btn_pi://圆周率，π
                str_input = "π";
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
                text_express.setText(resultStr);

                if(resultStr.equals(Manger.NOSUPPORT)){
                    Toast.makeText(this, R.string.supportSimpleOnly, Toast.LENGTH_SHORT).show();
                    resultStr="0";
                }else if(resultStr.equals(Manger.ERROR)){
                    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                    resultStr="0";
                }

                m_express.add(resultStr);
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
                m_isNoInput = true;
                break;
            case R.id.btn_delete:
            case R.id.btn_delete_2:
                if(m_express.size()==1){
                    m_express.clear();
                    m_tmpStr="0";
                    m_express.add(m_tmpStr);
                    text_express.setText(m_tmpStr);
                    text_result.setText("");
                    m_isNoInput = true;
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

        if(!str_input.equals("")){
            View _left = findViewById(R.id.btn_bracket_left);
            int _type = m_manger.checkInput(m_tmpStr,str_input);

            if(")".equals(str_input)&&_type==Manger.CheckRet.RET_1){//当输入的时“)”，还得判断前面有匹配的“(”

                int left_num  = 0;
                int right_num = 0;
                for(int i=0;i<m_express.size();i++){
                    if("(".equals(m_express.get(i)))
                        left_num++;
                    else if(")".equals(m_express.get(i)))
                        right_num++;
                }
                if(!(left_num>right_num))
                    _type = Manger.CheckRet.RET_4;
            }else{
                if(m_isNoInput){
                    if(Manger.isNum(str_input) && _type==Manger.CheckRet.RET_2)
                        _type = Manger.CheckRet.RET_3;
                    else if(_type==Manger.CheckRet.RET_4&&Manger.checkIs_ORDER_1(str_input)){
                        _type = Manger.CheckRet.RET_3;
                        if(Manger.mustHasBracketBehind(str_input)){
                            _type = Manger.CheckRet.RET_6;
                        }
                    }
                }
            }
            switch (_type){
                case Manger.CheckRet.RET_1://可新建添加
                    m_express.add(str_input);
                    m_tmpStr = str_input;
                    break;
                case Manger.CheckRet.RET_2://添加到str_1末尾
                    m_tmpStr = m_express.get(m_express.size()-1);
                    m_tmpStr+=str_input;
                    m_express.set(m_express.size()-1,m_tmpStr);
                    break;
                case Manger.CheckRet.RET_3://替换m_tmpStr
                    m_tmpStr = m_express.get(m_express.size()-1);
                    m_tmpStr=str_input;
                    m_express.set(m_express.size()-1,m_tmpStr);
                    break;
                case Manger.CheckRet.RET_4://不可输入
                    Toast.makeText(this, R.string.wrongful, Toast.LENGTH_SHORT).show();
                    break;
                case Manger.CheckRet.RET_5:
                    m_express.add(str_input);
                    m_tmpStr = str_input;
                    this.onClick(_left);
                    break;
                case Manger.CheckRet.RET_6:
                    m_tmpStr = m_express.get(m_express.size()-1);
                    m_tmpStr=str_input;
                    m_express.set(m_express.size()-1,m_tmpStr);
                    this.onClick(_left);
                default:break;
            }
            String _text="";
            for(int i=0;i<m_express.size();i++){
                _text+=(m_express.get(i));
            }
            text_express.setText(_text);
            m_isNoInput = false;
        }
        Log.d(TAG,str_input);
    }

    private void checkInput(String str){

    }

}
