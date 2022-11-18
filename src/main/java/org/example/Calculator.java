package org.example;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;
import static java.lang.Math.*;

/**
 * Класс-калькулятор вычисляющий значение выражения введеннго пользователем
 * @author romanchervakov
 */
public class Calculator {
    protected ExpressionParsing EP;
    protected double result;

    /**
     * Метод для считования переменных содержащихся в выражение
     */
    protected void VarInput() {
        for (int i = 0; i < EP.get_exp().length(); i++)
            if (EP.get_def(i) == 0) {
                char var = EP.get_exp().charAt(i);
                String var_val = null;
                Scanner input = new Scanner(System.in);
                boolean fl = true;
                while (fl) {
                    System.out.println("Введите значение переменной " + var + ": ");
                    var_val = input.nextLine();
                    if (!EP.VarInputCheck(var_val)) {
                        System.out.println("Переменная может принимать только числовые значения или наименования констант pi и e!");
                        System.out.println("Ввести значение переменной заново? (ответьте \"дa\" или \"нет\"): ");
                        String ans = input.nextLine();
                        ans = ans.toLowerCase();
                        if (!Objects.equals(ans, "да")) return;
                    }
                    else fl = false;
                }
                StringBuilder sb;
                for (int j = 0; j < EP.get_exp().length(); j++) {
                    sb = new StringBuilder(EP.get_exp());
                    if (sb.charAt(j) == var && EP.get_def(j) == 0) {
                        sb.replace(j, j + 1, var_val);
                        EP.set_exp(sb.toString());
                        EP.serv3();
                    }
                }
            }
        EP.serv4();
    }
    private void serv5(ArrayList<Character> oper, ArrayList<Double> val, int i) {
        oper.remove(i);
        val.remove(i);
    }

    /**
     * Метод вычисляющий значение выражения в скобках и раскрывающий их, заменяя на это значение в строке
     * @param l индекс открывающей скобки
     * @param r индекс закрывающей скобки
     * @return true, если вычисленное значение имеет смысл, false иначе
     */
    protected boolean CalcInPar(int l, int r) {
        ArrayList<Character> oper = new ArrayList<>();
        ArrayList<Double> val = new ArrayList<>();
        for (int i = l; i < r;) {
            if (EP.get_def(i) == 2) {
                oper.add(EP.get_exp().charAt(i));
                i++;
            }
            else oper.add('+');
            StringBuilder sb = new StringBuilder();
            for (; EP.get_def(i) != 2 && EP.get_def(i) != 10; i++)
                sb.append(EP.get_exp().charAt(i));
            switch (EP.get_def(i-1)) {
                case -8 -> val.add(PI);
                case -9 -> val.add(E);
                default -> val.add(Double.parseDouble(sb.toString()));
            }
        }
        for (int i = 0; i < oper.size(); i++)
            if (oper.get(i) == '^') {
                double res = pow(val.get(i-1), val.get(i));
                if (!Double.isNaN(res)) val.set(i-1, res);
                else return false;
                serv5(oper, val, i);
            }
        for (int i = 0; i < oper.size();) {
            if (oper.get(i) == '*')  {
                val.set(i - 1, val.get(i - 1) * val.get(i));
                serv5(oper, val, i);
                if (i < oper.size() && oper.get(i) != '*' && oper.get(i) != '/') i++;
            }
            else if (oper.get(i) == '/')  {
                if (val.get(i) == 0) return false;
                val.set(i - 1, val.get(i - 1) / val.get(i));
                serv5(oper, val, i);
                if (i < oper.size() && oper.get(i) != '*' && oper.get(i) != '/') i++;
            }
            else i++;
        }
        int k = oper.size();
        if (oper.get(0) == '-') val.set(0, -val.get(0));
        for (int i = 1; i < k; i++) {
            if (oper.get(1) == '+') {
                val.set(0, val.get(0) + val.get(1));
                serv5(oper, val, 1);
            }
            else if (oper.get(1) == '-') {
                val.set(0, val.get(0) - val.get(1));
                serv5(oper, val, 1);
            }
        }
        int fd = l-1;
        if (l-2 > 0 && EP.get_def(l-2) != 2 && abs(EP.get_def(l-2)) != 10)
            switch (EP.exp.charAt(l-2)) {
                case 's' -> {
                    if (abs(val.get(0)) == PI/2) val.set(0, 0.0);
                    else val.set(0, cos(val.get(0)));
                    fd = l-4;
                }
                case '0' -> {
                    if (val.get(0) < 0) return false;
                    val.set(0, log10(val.get(0)));
                    fd = l-6;
                }
                case 'n' -> {
                    if (EP.serv1(l-1,2).equals("ln")) {
                        if (val.get(0) < 0) return false;
                        val.set(0, log(val.get(0)));
                        fd = l-3;
                    }
                    if (EP.serv1(l-1,3).equals("sin")) {
                        if (abs(val.get(0)) == PI) val.set(0, 0.0);
                        val.set(0, sin(val.get(0)));
                        fd = l-4;
                    }
                    if (EP.serv1(l-1,3).equals("tan")) {
                        if (abs(val.get(0)) == PI/2) return false;
                        if (abs(val.get(0)) == PI) val.set(0, 0.0);
                        else val.set(0, tan(val.get(0)));
                        fd = l-4;
                    }
                }
                case 't' -> {
                    if (EP.serv1(l-1,3).equals("cot")) {
                        if (val.get(0) == 0 || val.get(0) == PI) return false;
                        if (abs(val.get(0)) == PI/2) val.set(0, 0.0);
                        else val.set(0, cos(val.get(0))/sin(val.get(0)));
                        fd = l-4;
                    }
                    if (EP.serv1(l-1,4).equals("sqrt")) {
                        if (val.get(0) < 0) return false;
                        val.set(0, sqrt(val.get(0)));
                        fd = l-5;
                    }
                }
            }
        StringBuilder sb = new StringBuilder(EP.get_exp());
        sb.replace(fd, r+1, String.valueOf(val.get(0)));
        EP.set_exp(sb.toString());
        EP.serv3();
        EP.serv4();
        return true;
    }

    /**
     * Метод вычисляющий значение выражения введенного пользователем путем постепенного раскрывания скобок
     * @return true, если вычисленное значение имеет смысл, false иначе
     */
    protected boolean Calculate() {
        VarInput();
        for (int i: EP.lpar) {
            int j = EP.lpar.get(0);
            while (EP.get_def(j) != -10) j++;
            EP.set_def(i-1, 10);
            EP.set_def(j,10);
            if (!CalcInPar(i, j)) return false;
        }
        StringBuilder sb = new StringBuilder(EP.get_exp());
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        result = Double.parseDouble(sb.toString());
        return true;
    }
    private void Description() {
        System.out.println("\nВыражение может содержать вещественные числа, любые арифметические операции,\nзнак возведения в степень (ситуация 9^9^9 не является однозначной поэтому не обрабатывается),\nнаименования переменных, а также функции cos, sin, tan, cot, извлечения квадратного корня sqrt,\nнатуральный логарифм ln и логарифм по основанию 10 log10.\n");
        System.out.println("Пример: e+(-5+cos(5+sin(a+b^y+pi)))\n");
    }

    /**
     * Метод возвращабщй результат посчитанного выражения
     * @return результат
     */
    public double Result() {return result;}

    /**
     * Конструктор для введения выражения из консоли
     */
    Calculator() {
        Description();
        Scanner input = new Scanner(System.in);
        System.out.println("Введите выражение: ");
        String str = input.nextLine();
        EP = new ExpressionParsing(str);
        if (!EP.Analyze()) return;
        if (!Calculate()) System.out.println("Выражение не имеет смысла!");
        else System.out.println("Результат: " + Result());
    }

    /**
     * Конструктор для прямой подачи выражения
     * @param str выражение
     */
    Calculator(String str) {
        EP = new ExpressionParsing(str);
        if (!EP.Analyze()) return;
        if (!Calculate()) System.out.println("Выражение не имеет смысла!");
        else System.out.println("Результат: " + Result());
    }


}


