package org.example;

enum AllowedNums {
     ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9");
     private final String c;
     AllowedNums(String c) { this.c = c; }
     public String toString() { return this.c; }
}