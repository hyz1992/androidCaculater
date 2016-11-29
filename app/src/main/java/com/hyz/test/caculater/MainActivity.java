package com.hyz.test.caculater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    final String TAG = "HYZ";
    ArrayList<String> m_express = new ArrayList<String>();
    String m_tmpStr="";
    TextView text_result;
    TextView text_express;
    Manger m_manger=new Manger();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        text_result = (TextView)this.findViewById(R.id.text_result);
        text_express = (TextView)this.findViewById(R.id.text_express);
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
                break;
            case R.id.btn_delete:
                break;
            case R.id.btn_turn:
                Toast.makeText(MainActivity.this, R.string.notOpen, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        if(str.equals("=")){
            if(m_express.size()==0)
                return;
            String result = m_manger.caculate(m_express);
            text_result.setText(result);
            m_express.clear();
            text_express.setText("0");
            m_tmpStr = str;
        }else if(!str.equals("")){
            if(str.equals(".")){
                if(m_tmpStr.matches("[0-9]+")&&m_tmpStr.indexOf(".")==-1){
                    m_tmpStr+=str;
                    m_express.set(m_express.size()-1,m_tmpStr);
                }
            }else {
                if(m_tmpStr.matches("[0-9]+")||m_tmpStr.indexOf(".")!=-1){//上次输入的是数字
                    if(str.matches("[0-9]+")){//这次也是数字
                        m_tmpStr+=str;
                        m_express.set(m_express.size()-1,m_tmpStr);
                    }else{//这次是操作符
                        m_express.add(str);
                        m_tmpStr = str;
                    }
                }else{//上次输入的是操作符
                    if(str.matches("[0-9]+")) {//这次也是数字
                        m_express.add(str);
                        m_tmpStr = str;
                    }else{
                        m_express.set(m_express.size()-1,str);
                        m_tmpStr = str;
                    }
                }
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
