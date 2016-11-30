package com.hyz.test.caculater;

/**
 * Created by PeteHuang on 2016/11/28.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.Stack;
public class Manger {
    private Stack<Double> operandStack=new Stack<Double>();//操作数堆栈

    private Map<String,Integer> priorityMap=new HashMap<String,Integer>();//用于存储操作符优先级的Map
    //初始化优先级约定(可根据计算的复杂程度扩展)
    public Manger()
    {
        priorityMap.put("+",0);
        priorityMap.put("-",0);
        priorityMap.put("×", 1);
        priorityMap.put("÷", 1);
    }

    private static boolean IsOperator(String str){
        return !isNum(str);
    }
    public static boolean isNum(String str){
        return str.matches("-?[0-9]+.*[0-9]*");
    }

    private int getPriority(String op)//得到一个操作符的优先级
    {
        return priorityMap.get(op);
    }
    //由中缀转为后缀
    private ArrayList<String> getPostfix(ArrayList<String> input){
        ArrayList<String> ret = new ArrayList<String>();
        Stack<String> _stack=new Stack<String>();//操作符堆栈
        String str="";
        for(int i=0;i<input.size();i++){
            str = input.get(i);
            if(str.equals("(")){
                _stack.push(str);
            }else if(str.equals(")")){
                String tmpStr;
                while(!(tmpStr=_stack.pop()).equals("(")){
                    ret.add(tmpStr);
                }
            }else{
                if(!IsOperator(str)){
                    ret.add(str);
                }else{
                    while(_stack.size()!=0&&getPriority(_stack.firstElement())>=getPriority(str)){
                        ret.add(_stack.firstElement());
                        _stack.pop();
                    }
                    _stack.push(str);
                }
            }
        }
        while (_stack.size()!=0){
            ret.add(_stack.pop());
        }
        return ret;
    }

    public String caculate(ArrayList<String> input) {
        ArrayList<String> _express = getPostfix(input);
        Stack<String> _stack=new Stack<String>();
        String str="";
        for(int i=0;i<_express.size();i++){
            str=_express.get(i);
            if(!IsOperator(str)){
                _stack.push(str);
            }else{
                Double op_1 = Double.valueOf(_stack.pop());
                Double op_2 = Double.valueOf(_stack.pop());
                Double value=0.0;
                switch (str){
                    case "+":value=op_2+op_1; break;
                    case "-":value=op_2-op_1;break;
                    case "×":value=op_2*op_1;break;
                    case "÷":value=op_2/op_1;break;
                    default:break;
                }
                _stack.push(value.toString());
            }
        }
        String ret = _stack.firstElement();
        if(ret.contains(".0")){
            ret = ret.substring(0,ret.length()-2);
        }
        return ret;
    }
}
