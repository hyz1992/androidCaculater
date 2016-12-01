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

    static private Map<String,Integer> priorityMap=new HashMap<String,Integer>();//用于存储操作符优先级的Map
    static private Map<String,String> allowMap=new HashMap<String,String>();//记录允许连续存在的操作符
    //初始化优先级约定(可根据计算的复杂程度扩展)
    public Manger()
    {
        priorityMap.put("+",0);//"＋"
        priorityMap.put("-",0);//－
        priorityMap.put("×", 1);//×
        priorityMap.put("÷", 1);//÷
        priorityMap.put("sin", 2);//sin
        priorityMap.put("cos", 2);//cos
        priorityMap.put("tan", 2);//tan
        priorityMap.put("log", 3);//log
        priorityMap.put("ln", 3);//ln
        priorityMap.put("√", 4);//√
        priorityMap.put("^", 4);//^
        priorityMap.put("!", 4);//!
        priorityMap.put("(", -1);//!
        priorityMap.put(")", 5);//!

        allowMap.put("+","(");
        allowMap.put("-","(");
        allowMap.put("×","(");
        allowMap.put("÷","(");
        allowMap.put("sin","(");
        allowMap.put("cos","(");
        allowMap.put("tan","(");
        allowMap.put("log","(");
        allowMap.put("ln","(");
        allowMap.put("√","(");

        allowMap.put(")","!");
        allowMap.put(")","^");
    }

    private static boolean IsOperator(String str){
        boolean ret = priorityMap.containsKey(str);
        return ret;
    }
    public static boolean isNum(String str){
        return !IsOperator(str);
    }

    public static boolean allowTwoOperator(String frist,String secend){
        return allowMap.get(frist).equals(secend);
    }

    //判断操作符能否出现在数字前
    public static boolean canFrontNumber(String str){
        //只能出现在数字后的操作符
        ArrayList<String> cannotList = new ArrayList<String>();
        cannotList.add(")");
        cannotList.add("^");
        cannotList.add("!");
        return !cannotList.contains(str);
    }

    public static boolean canBehindNumber(String str){
        //只能出现在数字前的操作符
        ArrayList<String> cannotList = new ArrayList<String>();
        cannotList.add("sin");
        cannotList.add("cos");
        cannotList.add("tan");
        cannotList.add("log");
        cannotList.add("ln");
        cannotList.add("√");
        cannotList.add("(");
        return !cannotList.contains(str);
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
                String tmpStr = _stack.lastElement();
                while(!tmpStr.equals("(")){
                    ret.add(tmpStr);
                    _stack.pop();
                    tmpStr = _stack.lastElement();
                }
                _stack.pop();
            }else{
                if(!IsOperator(str)){
                    ret.add(str);
                }else{
                    while(_stack.size()!=0&&getPriority(_stack.lastElement())>=getPriority(str)){
                        ret.add(_stack.lastElement());
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
        String ret = _stack.lastElement();
        if(ret.contains(".0")){
            ret = ret.substring(0,ret.length()-2);
        }
        return ret;
    }
}
