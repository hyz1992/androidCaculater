package com.hyz.test.caculater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
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
    }
    private Animation getScaleAni(float form,float to,long duration){
        Animation scaleAnimation=new ScaleAnimation(form,to,form,to,
                Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.5f);

        scaleAnimation.setDuration(duration);//设置动画持续时间为3秒
        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        scaleAnimation.setFillAfter(true);//设置动画结束后保持当前的位置（即不返回到动画开始前的位置）
        return scaleAnimation;
    }

    private Animation getTranslateAniY(float formY,float toY,long duration){
        Animation translateAnimation=new TranslateAnimation(0, 0, formY, toY);

        translateAnimation.setDuration(duration);//设置动画持续时间为3秒
        translateAnimation.setInterpolator(new DecelerateInterpolator());//设置动画插入器
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    private Animation getAlphaAni(float from,float to,long duration){
        Animation alphaAnimation=new AlphaAnimation(from, to);
        alphaAnimation.setDuration(duration);//设置动画持续时间为3秒
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    private void runResultAction(){
        int fromY = text_express.getTop();
        int toY = text_result.getTop();

        AnimationSet ani_1=new AnimationSet(true);
        ani_1.addAnimation(getTranslateAniY(100,0.0f,200));
        ani_1.addAnimation(getAlphaAni(0.1f,1.0f,200));
        ani_1.setFillAfter(true);
        text_express.startAnimation(ani_1);

        AnimationSet ani_2=new AnimationSet(true);
        ani_2.addAnimation(getTranslateAniY(fromY - toY,0.0f,200));
        ani_2.addAnimation(getScaleAni(1.0f,0.6f,200));
        ani_2.setFillAfter(true);
        text_result.startAnimation(ani_2);

//        text_result.startAnimation(getTranslateAniY(fromY - toY,0.0f,200));
//        text_result.setAnimation(getScaleAni(1.0f,0.6f,200));
    }
    public void onClick(View v)
    {
        int id = v.getId();
        String str = "";
        switch(id)
        {
            case R.id.btn_0:str="0";break;
            case R.id.btn_1:str="1";break;
            case R.id.btn_2:str="2";break;
            case R.id.btn_3:str="3";break;
            case R.id.btn_4:str="4";break;
            case R.id.btn_5:str="5";break;
            case R.id.btn_6:str="6";break;
            case R.id.btn_7:str="7";break;
            case R.id.btn_8:str="8";break;
            case R.id.btn_9:str="9";break;
            case R.id.btn_point:str=".";break;
            case R.id.btn_add:str="+";break;
            case R.id.btn_sub:str="-";break;
            case R.id.btn_multiply:str="×";break;
            case R.id.btn_devide:str="÷";break;
            case R.id.btn_equal:str="=";break;
            case R.id.btn_clear:
                m_express.clear();
                m_tmpStr="0";
                m_express.add(m_tmpStr);
                text_express.setText(m_tmpStr);
                text_result.setText("");
                break;
            case R.id.btn_delete:
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
                Toast.makeText(MainActivity.this, R.string.notOpen, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        if(str.equals("=")){
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
        }else if(!str.equals("")){
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
                    }else{//这次是操作符
                        m_express.add(str);
                        m_tmpStr = str;
                    }
                }else{//上次输入的是操作符
                    if(Manger.isNum(str)) {//这次也是数字
                        m_express.add(str);
                        m_tmpStr = str;
                    }else{
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
