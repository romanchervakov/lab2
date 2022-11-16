package org.example;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;
import static java.lang.Math.*;

public class Calculator {
             protected String exp;
             protected int length;
             protected LinkedList<Integer> lpar;
             protected int [] def;
             protected double result;
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
             protected enum AllowedNums {
                            ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9");
                            private final String c;
                            AllowedNums(String c) { this.c = c; }
                            public String toString() { return this.c; }
             }
             protected boolean AN(char c) {
                                  for (AllowedNums j: AllowedNums.values())
                                       if (Character.toString(c).equals(j.toString())) return true;
                                  return false;
             }
             protected enum AllowedOpers {
                            PLUS("+"), MINUS("-"), MUL("*"), DIV("/"), POW("^");
                            private final String c;
                            AllowedOpers(String c) { this.c = c; }
                            public String toString() { return this.c; }
             }
             protected enum AllowedPars {
                            LP("("), RP(")");
                            private final String c;
                            AllowedPars(String c) { this.c = c; }
                            public String toString() { return this.c; }
             }
             protected enum AllowedVarNames {
                            A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z
             }
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
             private String serv1(int i, int k) {
                                  StringBuilder s = new StringBuilder();
                                  for (int j=i-k; j<i; j++) s.append(exp.charAt(j));
                                  return s.toString();
             }
             private void serv2(int i, int k, int f) { for (int j=i-k; j<i; j++) def[j] = f; }
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
             private void serv3(){
                                ParBalCheck();
                                SymbCheck();
                                FuncConstCheck();
             }
             private void serv4() {
                                for (int i = 0; i < length-2; i++) {
                                     if (def[i] == 2 && def[i + 1] == 2) def[i+1] = def[i+2];
                                     if (def[i] == 4) def[i] = 1;
                                     if (abs(def[i]) == 10 && def[i+1] == 2 && def[i+2] == 1) def[i+1] = 1;
                                }
             }
             protected void VarInput() {
                                     for (int i = 0; i < exp.length(); i++)
                                          if (def[i] == 0) {
                                              char var = exp.charAt(i);
                                              String var_val = null;
                                              Scanner input = new Scanner(System.in);
                                              boolean fl = true;
                                              while (fl) {
                                                     System.out.println("Введите значение переменной " + var + ": ");
                                                     var_val = input.nextLine();
                                                     if (!VarInputCheck(var_val)) {
                                                         System.out.println("Переменная может принимать только числовые значения или наименования констант pi и e!");
                                                         System.out.println("Ввести значение переменной заново? (ответьте \"дa\" или \"нет\"): ");
                                                         String ans = input.nextLine();
                                                         ans = ans.toLowerCase();
                                                         if (!Objects.equals(ans, "да")) return;
                                                     }
                                                     else fl = false;
                                              }
                                              StringBuilder sb;
                                              for (int j = 0; j < exp.length(); j++) {
                                                   sb = new StringBuilder(exp);
                                                   if (sb.charAt(j) == var && def[j] == 0) {
                                                       sb.replace(j, j + 1, var_val);
                                                       exp = sb.toString();
                                                       serv3();
                                                  }
                                              }
                                          }
                                     serv4();
             }
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
             private void serv5(ArrayList<Character> oper, ArrayList<Double> val, int i) {
                                oper.remove(i);
                                val.remove(i);
             }
             protected boolean CalcInPar(int l, int r) {
                                         ArrayList<Character> oper = new ArrayList<>();
                                         ArrayList<Double> val = new ArrayList<>();
                                         for (int i = l; i < r;) {
                                              if (def[i] == 2) {
                                                  oper.add(exp.charAt(i));
                                                  i++;
                                              }
                                              else oper.add('+');
                                              StringBuilder sb = new StringBuilder();
                                              for (; def[i] != 2 && def[i] != 10; i++)
                                                   sb.append(exp.charAt(i));
                                              switch (def[i-1]) {
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
                                         if (l-2 > 0 && def[l-2] != 2 && abs(def[l-2]) != 10)
                                             switch (exp.charAt(l-2)) {
                                                     case 's' -> {
                                                           val.set(0, cos(val.get(0)));
                                                           fd = l-4;
                                                     }
                                                     case '0' -> {
                                                           if (val.get(0) < 0) return false;
                                                           val.set(0, log10(val.get(0)));
                                                           fd = l-6;
                                                     }
                                                     case 'n' -> {
                                                           if (serv1(l-1,2).equals("ln")) {
                                                               if (val.get(0) < 0) return false;
                                                               val.set(0, log(val.get(0)));
                                                               fd = l-3;
                                                           }
                                                           if (serv1(l-1,3).equals("sin")) {
                                                               if (abs(val.get(0)) == PI) val.set(0, 0.0);
                                                               val.set(0, sin(val.get(0)));
                                                               fd = l-4;
                                                           }
                                                           if (serv1(l-1,3).equals("tan")) {
                                                               if (abs(val.get(0)) == PI/2) return false;
                                                               if (abs(val.get(0)) == PI) val.set(0, 0.0);
                                                               else val.set(0, tan(val.get(0)));
                                                               fd = l-4;
                                                           }
                                                     }
                                                     case 't' -> {
                                                           if (serv1(l-1,3).equals("cot")) {
                                                               if (val.get(0) == 0 || val.get(0) == PI) return false;
                                                               val.set(0, cos(val.get(0))/sin(val.get(0)));
                                                               fd = l-4;
                                                           }
                                                           if (serv1(l-1,4).equals("sqrt")) {
                                                               if (val.get(0) < 0) return false;
                                                               val.set(0, sqrt(val.get(0)));
                                                               fd = l-5;
                                                           }
                                                     }
                                             }
                                         StringBuilder sb = new StringBuilder(exp);
                                         sb.replace(fd, r+1, String.valueOf(val.get(0)));
                                         exp = sb.toString();
                                         serv3();
                                         serv4();
                                         return true;
             }
             protected boolean Calculate() {
                                         VarInput();
                                         for (int i: lpar) {
                                              int j = lpar.get(0);
                                              while (def[j] != -10) j++;
                                              def[i-1] = def[j] = 10;
                                              if (!CalcInPar(i, j)) return false;
                                         }
                                         StringBuilder sb = new StringBuilder(exp);
                                         sb.deleteCharAt(0);
                                         sb.deleteCharAt(sb.length()-1);
                                         result = Double.parseDouble(sb.toString());
                                         return true;
             }
             private void Rules() {
                                System.out.println("\nПравила ввода: ");
                                System.out.println("\nВыражение может содержать выщественные числа, любые арифметические операции, знак взведения в степень,\nнаименования переменных, а также функции cos, sin, tan, cot, извлечения квадратного корня sqrt,\nнатуральный логарифм ln и логарифм по основанию 10 log10.\n");
                                System.out.println("Пример: e+(-5+cos(5+sin(a+b^y+pi))-71.589/B-1/a*sqrt(5*4+5+tan(pi))+8.898)");
                                System.out.println("Примеры неправильного ввода: 10.10.10 и 10^10^10\n");
             }
             public double Result() {
                                  return result;
             }
             Calculator() {
                        Rules();
                        Scanner input = new Scanner(System.in);
                        System.out.println("Введите выражение: ");
                        exp = input.nextLine();
                        if (!Analyze()) return;
                        if (!Calculate()) System.out.println("Выражение не имеет смысла!");
                        else System.out.println("Результат: " + Result());
             }
}


