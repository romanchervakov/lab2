package org.example;

enum AllowedPars {
     LP("("), RP(")");
     private final String c;
     AllowedPars(String c) { this.c = c; }
     public String toString() { return this.c; }
}