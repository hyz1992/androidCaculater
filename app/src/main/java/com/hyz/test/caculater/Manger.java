package com.hyz.test.caculater;

/**
 * Created by PeteHuang on 2016/11/28.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.Stack;

/**
 *
 */
public class Manger {
    private static Map<String,Integer> priorityMap=new HashMap<String,Integer>();//用于存储操作符优先级的Map
    private static Map<String,Integer> orderMap=new HashMap<String,Integer>();

    private static final int ORDER_1 = 1;
    private static final int ORDER_2 = 2;
    private static final int ORDER_3 = 3;

    public static class CheckRet{
        //可新建添加
        public static final int RET_1 = 1;
        //添加到str_1末尾
        public static final int RET_2 = 2;
        //替换str_1
        public static final int RET_3 = 3;
        //不可输入
        public static final int RET_4 = 4;
        //可新建添加，并追加一个“（”
        public static final int RET_5 = 5;
        //替换str_1，并追加一个“(”
        public static final int RET_6 = 6;
    }

    public static final String NOSUPPORT = "不支持";
    public static final String ERROR = "出错";
    //初始化优先级约定(可根据计算的复杂程度扩展)
    public Manger()
    {
        priorityMap.put("+",0);//"＋"
        priorityMap.put("-",0);//－
        priorityMap.put("×", 1);//×
        priorityMap.put("÷", 1);//÷

        priorityMap.put("sin", 2);//sin 必须后跟“(”
        priorityMap.put("cos", 2);//cos 必须后跟“(”
        priorityMap.put("tan", 2);//tan 必须后跟“(”
        priorityMap.put("log", 2);//log,以10为底 必须后跟“(”
        priorityMap.put("ln", 2); //ln ,以e为底 必须后跟“(”

        priorityMap.put("^", 3);//^ 开若干次方 双目

        priorityMap.put("√", 4);//√ 开平方根 单目
        priorityMap.put("!", 4);//! 阶乘 单目

        priorityMap.put("(", -1);//!
        priorityMap.put(")", 5);//!


        //只能出现在数字或“(”前
        orderMap.put("sin", ORDER_1);//sin
        orderMap.put("cos", ORDER_1);//cos
        orderMap.put("tan", ORDER_1);//tan
        orderMap.put("log", ORDER_1);//log
        orderMap.put("ln", ORDER_1);//ln
        orderMap.put("√", ORDER_1);//√

        //既能出现在数字、“ORDER_1”、“(” 前，又能出现在数字、“ORDER_3”、“)”后
        orderMap.put("+",ORDER_2);//"＋"
        orderMap.put("-",ORDER_2);//－
        orderMap.put("×", ORDER_2);//×
        orderMap.put("÷", ORDER_2);//÷
        orderMap.put("^", ORDER_2);//^

        //只能出现在数字、“、”后面

        orderMap.put("!", ORDER_3);//!

        //只能出现在数字、“ORDER_1”前
        orderMap.put("(", ORDER_1);//!
        //只能出现在数字、“ORDER_3”后
        orderMap.put(")", ORDER_3);//!
    }

    public static final boolean checkIs_ORDER_1(String operator){
        return orderMap.get(operator)==ORDER_1;
    }

    private static boolean IsOperator(String str){
        boolean ret = priorityMap.containsKey(str);
        return ret;
    }
    public static boolean isNum(String str){
        return "e".equals(str)||"π".equals(str)||!IsOperator(str);
    }

    //判断连续出现的两个操作符是否被允许
    private static boolean allowTwoOperator(String frist,String secend){
        if(frist.equals(secend))
            return false;
        final int order_frist = orderMap.get(frist);
        final int order_secend = orderMap.get(secend);
        if(order_frist-order_secend==1)
            return true;
        else if(order_secend-order_frist==0){
            if(order_frist==ORDER_1 && "(".equals(secend))
                return true;
            else if(order_secend==ORDER_3 && ")".equals(frist))
                return true;
            else
                return false;
        }else{
            return false;
        }
    }

    /**
     * @param str_1      最新一次输入的值
     * @param str_input 本次试图输入的值
     * @return checkRet
     */
    public static int checkInput(String str_1,String str_input){
        if(str_input.equals(("")))
            return  CheckRet.RET_4;

        if(str_input.equals(".")){
            if(Manger.isNum(str_1)||str_1.contains("."))
                return CheckRet.RET_2;
            else
                return CheckRet.RET_4;
        }
        else if(Manger.isNum(str_input)){ //输入数字
            if("0".equals(str_1)){
                return CheckRet.RET_3;
            }
            else if(Manger.isNum(str_1)||str_1.equals("."))//上次也是数字或者小数点
                return CheckRet.RET_2;
            final int order_1 = orderMap.get(str_1);
            if(order_1==ORDER_3)
                return CheckRet.RET_4;
            else if(order_1==ORDER_1||order_1==ORDER_2)
                return CheckRet.RET_1;
        }else { //输入字符
            if(str_1.equals("."))
                return CheckRet.RET_4;
            final int order_input = orderMap.get(str_input);
            if(Manger.isNum(str_1)){ //上一次数字
                if(order_input==ORDER_1)
                    return CheckRet.RET_4;
                else
                    return Manger.mustHasBracketBehind(str_input) ? CheckRet.RET_5 : CheckRet.RET_1;
            }else{//两次都是字符
                if(Manger.allowTwoOperator(str_1,str_input))
                    return Manger.mustHasBracketBehind(str_input) ? CheckRet.RET_5 : CheckRet.RET_1;
                final int order_1 = orderMap.get(str_1);
                if(order_1>orderMap.get(str_input)){
                    return CheckRet.RET_4;
                }else{ //order相等的情况
                    if(str_1.equals("(")&&order_input==ORDER_1)
                        return Manger.mustHasBracketBehind(str_input) ? CheckRet.RET_5 : CheckRet.RET_1;
                    else if(str_input.equals(")")&&order_1==ORDER_3){
                            return Manger.mustHasBracketBehind(str_input) ? CheckRet.RET_5 : CheckRet.RET_1;
                    }
                    else if(order_1==orderMap.get(str_input))//排除“(”“)”
                        return Manger.mustHasBracketBehind(str_input) ? CheckRet.RET_6 : CheckRet.RET_3;
                }
            }
        }
        return CheckRet.RET_4;
    }

    private int getPriority(String op)//得到一个操作符的优先级
    {
        return priorityMap.get(op);
    }

    /**
     * 判断操作符后面是否必须紧跟左括号
     * @param operator
     * @return
     */
    public static boolean mustHasBracketBehind(String operator){
        return priorityMap.get(operator)==2;
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

    private static void check_E_PI(ArrayList<String> input){
        for(int i=input.size()-1;i>=0;i--){
            String str = input.get(i);
            if(str.contains("e")||str.contains("π")){
                Double value = 1.0;
                String[] result;
                if(str.contains("e")) {
                    result = str.split("e");
                    value = Math.E;
                }
                else{
                    result = str.split("π");
                    value = Math.PI;
                }
                for(int j=0;j<result.length;j++){
                    String tmp = result[j];
                    if("".equals(tmp)){
                        continue;
                    }
                    else if(".".equals(tmp.substring(tmp.length()-1,tmp.length()-1))){
                        tmp = tmp.substring(0,tmp.length()-2);
                    }else if(".".equals(tmp.substring(0,0))){
                        tmp = tmp.substring(1,tmp.length()-1);
                        tmp = ((Double)(Double.valueOf(tmp)*0.1)).toString();
                    }
                    value*=Double.valueOf(tmp);
                }
                input.set(i,value.toString());
            }
        }

    }

    public String caculate(ArrayList<String> input) {
        Manger.check_E_PI(input);
        ArrayList<String> _express = getPostfix(input);
        Stack<String> _stack=new Stack<String>();
        String str="";
        for(int i=0;i<_express.size();i++){
            str=_express.get(i);
            if(!IsOperator(str)){
                _stack.push(str);
            }else{
                Double value=0.0;
                if(orderMap.get(str)==ORDER_2){
                    String op_str_1 = _stack.pop();
                    String op_str_2 = _stack.pop();

                    Double op_1 = Double.valueOf(op_str_1);
                    Double op_2 = Double.valueOf(op_str_2);

                    switch (str){
                        case "+":value=op_2+op_1; break;
                        case "-":value=op_2-op_1;break;
                        case "×":value=op_2*op_1;break;
                        case "÷":value=op_2/op_1;break;
                        case "^":value = 1.0;
                            for(int j=1;j<=op_1;j++)
                                value*=op_2;
                            break;
                        default:
                            return NOSUPPORT;
                    }
                    _stack.push(value.toString());
                }else if(orderMap.get(str)==ORDER_3){
                    Double op_1 = Double.valueOf(_stack.pop());
                    value=1.0;
                    switch (str){
                        case "!":
                            if(op_1>1.0){
                                for(Double j=op_1;j>=1.0;j--){
                                    value*=j;
                                }
                            }else{
                                return ERROR;
                            }
                            break;
                        default:
                            return NOSUPPORT;
                    }
                    _stack.push(value.toString());
                }else if(orderMap.get(str)==ORDER_1){
                    Double op_1 = Double.valueOf(_stack.pop());
                    Double radian = 2*Math.PI*op_1/360;//角度转为弧度
                    value=1.0;
                    switch (str){
                        case "sin":
                            value = Math.sin(radian);
                            break;
                        case "cos":
                            value = Math.cos(radian);
                            break;
                        case "tan":
                            value = Math.tan(radian);
                            break;
                        case "log":
                            value = Math.log10(op_1);
                            break;
                        case "ln":
                            value = Math.log(op_1);
                            break;
                        case "√":
                            value = Math.sqrt(op_1);
                            break;
                        default:;return NOSUPPORT;
                    }
                    _stack.push(value.toString());
                }else{
                    return ERROR;
                }
            }
        }
        String ret = _stack.lastElement();
        if(ret.contains(".0")){
            ret = ret.substring(0,ret.length()-2);
        }
        return ret;
    }

}
