package org.example;

enum AllowedOpers {
     PLUS("+"), MINUS("-"), MUL("*"), DIV("/"), POW("^");
     private final String c;
     AllowedOpers(String c) { this.c = c; }
     public String toString() { return this.c; }
}