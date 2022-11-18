package org.example;
import java.util.LinkedList;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * Класс распознающий выражение введенное пользователем и проверяющий готово ли оно к вычислению
 */
public class ExpressionParsing {
    protected String exp;
    protected int length;
    protected int [] def;
    protected LinkedList<Integer> lpar;
    ExpressionParsing(String exp) { this.exp = exp; }

    /**
     * Метод для проверки баланса скобок в выражение
     * @return true, если балан соблюден, false иначе
     */
    protected int ParBalCheck(){
        if (exp.charAt(0) != '(') exp = "(" + exp + ")";
        length = exp.length();
        int l = 0, r = 0;
        lpar = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            if (exp.charAt(i) == '(') {
                l++;
                lpar.addFirst(i+1);
            }
            if (exp.charAt(i) == ')') {
                r++;
                if (r > l) return 1;
                if (exp.charAt(i-1) == '(') return 2;
            }
        }
        if (r == l) return 0;
        else return 1;
    }
    public String get_exp() {
                          return exp;
    }
    public void set_exp(String exp) { this.exp = exp; }
    public void get_lpar(LinkedList<Integer> lpar) { lpar.addAll(this.lpar); }
    public int get_def(int i) { return def[i]; }
    public void set_def(int i, int val) { def[i] = val; }
    protected boolean AN(char c) {
        for (AllowedNums j: AllowedNums.values())
            if (Character.toString(c).equals(j.toString())) return true;
        return false;
    }

    /**
     * Метод для проверки символов в выражение
     * @return true, если выражение содержит только разрешенные символы, false иначе
     */
    protected boolean SymbCheck() {
        def = new int[length];
        int k = 0;
        for (int i = 0; i<length; i++) {
            char ati = exp.charAt(i);
            if (AN(ati)) {
                k++;
                def[i] = 1;
            }
            if (k != length)
                for (AllowedOpers j: AllowedOpers.values())
                    if (Character.toString(ati).equals(j.toString())) {
                        k++;
                        def[i] = 2;
                        break;
                    }
            if (k != length)
                for (AllowedPars j: AllowedPars.values())
                    if (Character.toString(ati).equals(j.toString())) {
                        k++;
                        if (ati == '(') def[i] = 10;
                        if (ati == ')') def[i] = -10;
                        break;
                    }
            if (k != length)
                for (AllowedVarNames j: AllowedVarNames.values())
                    if (Character.toString(ati).equals(j.toString())) {
                        k++;
                        def[i] = 0;
                        break;
                    }
            if (k != length && ati == '.') {
                k++;
                def[i] = 4;
            }
        }
        return k == length;
    }
    public String serv1(int i, int k) {
        StringBuilder s = new StringBuilder();
        for (int j=i-k; j<i; j++) s.append(exp.charAt(j));
        return s.toString();
    }
    private void serv2(int i, int k, int f) { for (int j=i-k; j<i; j++) def[j] = f; }

    /**
     * Метод для проверки правильности введения разрешенных функций
     * @return true, если функции введены правильно, false иначе
     */
    protected boolean FuncConstCheck() {
        for (int i = 0; i<length; i++) {
            char ati = exp.charAt(i), atim = 0, atip = 0;
            if (i > 0) atim = exp.charAt(i-1);
            if (i < length-1) atip = exp.charAt(i+1);
            if (i > 0 && ati == '(') {
                if (atim == 's')
                    if (i < 3) return false;
                    else if (!serv1(i,3).equals("cos")) return false;
                    else serv2(i, 3, -1);
                if (atim == '0')
                    if (i < 5) return false;
                    else if (!serv1(i,5).equals("log10")) return false;
                    else serv2(i, 5, -2);
                if (atim == 'n')
                    if (i < 2) return false;
                    else if (!serv1(i,2).equals("ln"))
                        if (i < 3) return false;
                        else if (!serv1(i,3).equals("sin"))
                            if (!serv1(i,3).equals("tan")) return false;
                            else serv2(i, 3, -3);
                        else serv2(i, 3, -4);
                    else serv2(i, 2, -5);
                if (atim == 't')
                    if (i < 3) return false;
                    else if (!serv1(i,3).equals("cot"))
                        if (i < 4) return false;
                        else if (!serv1(i,4).equals("sqrt")) return false;
                        else serv2(i, 4, -6);
                    else serv2(i, 3, -7);
            }
            if (ati == 'p' && atip == 'i' || ati == 'P' && atip == 'I' || ati == 'P' && atip == 'i' || ati == 'p' && atip == 'I') {
                if (i == 0 && i+1 < length-1 && def[i+2]==2) serv2(i+2, 2, -8);
                if (i > 0 && i+1 == length-1 && def[i-1]==2) serv2(i+2, 2, -8);
                if (i > 0 && i+1 < length-1 && ((def[i-1]==2 || abs(def[i-1])==10) && (def[i+2]==2 || abs(def[i+2])==10))) serv2(i+2, 2, -8);
            }
            if (ati == 'e' || ati == 'E') {
                if (i == 0 && i < length-1 && def[i+1]==2) serv2(i+1, 1, -9);
                if (i > 0 && i == length-1 && def[i-1]==2) serv2(i+1, 1, -9);
                if (i > 0 && i < length-1 && ((def[i-1]==2 || abs(def[i-1])==10) && (def[i+1]==2 || abs(def[i+1])==10))) serv2(i+1, 1, -9);
            }
        }
        return true;
    }

    /**
     * Проверка правильности расстоновки разрешенных символов в выражение
     * @return true, если символы введены правильно, false иначе
     */
    protected boolean SymbAnalysis() {
        for (int i = 0; i < length; i++) {
            char ati = exp.charAt(i), atim = 0, atip = 0;
            if (i > 0) atim = exp.charAt(i-1);
            if (i < length-1) atip = exp.charAt(i+1);
            if (atim != 0 && ati == '(' && atim != 's' && atim != '0' && atim != 'n' && atim != 't' && def[i-1]!=2 && atim != '(') return false;
            if (atip !=0 && ati == ')' && def[i+1]!=2 && atip != ')') return false;
            if (i>0 && i < length-1) {
                if ((ati == '+' || ati == '-') && (def[i - 1] == 2 || def[i + 1] == 2 || atip == ')'))
                    return false;
                if ((ati == '*' || ati == '/' || ati == '^') && (def[i - 1] == 2 || def[i + 1] == 2 || atip == ')' || atim == '('))
                    return false;
            }
            if (def[length-1]==2) return false;
            if (ati == '.') {
                if (def[i-1]!=1 || def[i+1]!=1) return false;
                for (int j = i+1; j < length && def[j] != 2 && abs(def[j]) != 10; j++)
                    if (def[j] == 4) return false;
            }
            if (ati == '^') {
                int j = i+1;
                for (; j < length && def[j] != 2 && abs(def[j]) != 10; j++);
                if (j < length && exp.charAt(j) == '^') return false;
            }
            if (i == 0 && i < length-1 && def[i] == 0 && (def[i+1]==0 || def[i+1]==1)) return false;
            if (i > 0 && i < length-1 && def[i] == 0 && (def[i-1]==0 || def[i+1]==0 || def[i-1]==1 || def[i+1]==1)) return false;
            if (i > 0 && i == length-1 && def[i] == 0 && (def[i-1]==0 || def[i-1]==1)) return false;
        }
        return true;
    }

    /**
     * Проверка правильности введенных значений переменных содержащихся в выражение
     * @param var_val значение переменной
     * @return true, если значение переменной введено правильно, false иначе
     */
    protected boolean VarInputCheck(String var_val) {
        if (Objects.equals(var_val.toLowerCase(), "e") || Objects.equals(var_val.toLowerCase(), "-e") || Objects.equals(var_val.toLowerCase(), "+e")|| Objects.equals(var_val.toLowerCase(), "pi") || Objects.equals(var_val.toLowerCase(), "-pi") || Objects.equals(var_val.toLowerCase(), "+pi")) return true;
        if (Objects.equals(var_val, "+") || Objects.equals(var_val, "-")) return false;
        int len = var_val.length();
        char at0 =  var_val.charAt(0);
        if (at0 == '.') return false;
        if (var_val.charAt(len - 1) == '.') return false;
        int i = (at0 == '+' || at0 == '-') ? 1 : 0;
        for (; i < len; i++) {
            char ati = var_val.charAt(i);
            if (!AN(ati) && ati != '.') return false;
            if (ati == '.')
                for (int j = i+1; j < len; j++)
                    if (var_val.charAt(j) == '.') return false;
        }
        return true;
    }
    public void serv3(){
        ParBalCheck();
        SymbCheck();
        FuncConstCheck();
    }
    public void serv4() {
        for (int i = 0; i < length-2; i++) {
            if (def[i] == 2 && def[i + 1] == 2) def[i+1] = def[i+2];
            if (def[i] == 4) def[i] = 1;
            if (abs(def[i]) == 10 && def[i+1] == 2 && def[i+2] == 1) def[i+1] = 1;
        }
    }

    /**
     * Функция проводящая полный анализ введеного выражения и выводящая сообщения об ошибке при некорректном вводе
     * @return true, если введенное выражение введено корректно и готово к вычислению, false иначе
     */
    protected boolean Analyze() {
        switch (ParBalCheck()) {
            case 1 -> {
                System.out.println("Скобки не сбалансированы!");
                return false;
            }
            case 2 -> {
                System.out.println("Выражение содержит пустые скобки!");
                return false;
            }
        }
        if (!SymbCheck()) {
            System.out.println("Выражение содержит недопустимые символы!");
            return false;
        }
        String err_mes = "Некорректное задание выражения, еще раз обратите внимание на правила ввода.";
        if (!FuncConstCheck()) {
            System.out.println(err_mes);
            return false;
        }
        if (!SymbAnalysis()) {
            System.out.println(err_mes);
            return false;
        }
        return true;
    }
}
