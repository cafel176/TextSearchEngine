package com.ttds.cw3.Tools;

import com.ttds.cw3.Transaction.Symbols.SymbolManager;
import com.ttds.cw3.Transaction.Symbols.StructSymbolType;

import java.util.ArrayList;
import java.util.Stack;

public abstract class QueryParser
{
    public static ArrayList<String> parse(String query,SymbolManager symbols) throws Exception
    {
        ArrayList<String> re = analysisQuery(query,symbols);
        return TransferQuery(re,symbols);
    }

    private static ArrayList<String> analysisQuery(String query,SymbolManager symbols)
    {
        ArrayList<String> results = new ArrayList<>();
        String cache = "";
        String token = " ";

        String[] params = query.split(token);
        for(int i=0;i<params.length;i++)
        {
            String t = params[i];
            StructSymbolType type = symbols.TxtToSymbolParser(t);
            if(type== StructSymbolType.other)
            {
                cache += token + t;
            }
            else
            {
                if(!cache.isEmpty())
                    results.add(cache.trim());
                results.add(t.trim());
                cache = "";
            }
        }
        if(!cache.isEmpty())
            results.add(cache.trim());

        return results;
    }

    private static ArrayList<String> TransferQuery(ArrayList<String> query,SymbolManager symbols) throws Exception
    {
        Stack<String> stack = new Stack<>();
        ArrayList<String> postfixList = new ArrayList<>();

        for(String str:query)
        {
            // 这个分支是当前项是操作符号的情况
            StructSymbolType type = symbols.TxtToSymbolParser(str);
            if(type!= StructSymbolType.other)
            {
                String opThis = str;

                if(stack.size()==0)
                {
                    // 如果栈为空，直接把操作符推入栈
                    stack.push(opThis);
                }
                else if(type== StructSymbolType.leftParen)
                {
                    // 如果操作符是左括号，直接推入栈
                    stack.push(opThis);
                }
                else if(type== StructSymbolType.rightParen)
                {
                    // 如果操作符是右括号，则往前找左括号，将左括号之后的操作符放到后续表达式列表中
                    while(symbols.TxtToSymbolParser(stack.peek())== StructSymbolType.leftParen)
                    { // stack.peek()是取栈顶元素而不弹出
                        postfixList.add(stack.pop());
                    }

                    stack.pop();// 左括号丢弃，由此完成了去括号的过程
                }
                else
                    {
                    // 看栈顶元素，如果它优先级大于等于当前操作符的优先级，则弹出放到后续表达式列表中
                    while( stack.size()>0 && (symbols.getLevel(stack.peek())>=symbols.getLevel(opThis)) )
                    {
                        postfixList.add(stack.pop());
                    }

                    stack.push(opThis);// 当前操作符入栈
                }
            }
            else
            {
                // 这个分支是当前项是操作数的情况
                postfixList.add(str);// 操作数直接入栈
            }
        }

        // 将栈中余下的操作符弹出放到后续表达式列表中
        while(stack.size()>0)
        {
            String opTop=stack.pop();
            postfixList.add(opTop);
        }

        for(int i=0;i<postfixList.size();i++)
        {
            StructSymbolType type = symbols.TxtToSymbolParser(postfixList.get(i));
            if(type == StructSymbolType.leftParen || type == StructSymbolType.rightParen)
                throw new Exception("括号不匹配异常！请检查输入语句中的所有圆括号");
        }

        return postfixList;
    }
}
