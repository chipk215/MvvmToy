package com.keyeswest.mvvmtoy.utilities;

import static java.lang.Math.abs;

public class MathHelper {

    private static final Double epsilon = 1E-6;

    public static boolean areSame(Double first, Double second){
        if (abs(first - second) < epsilon){
            return true;
        }else{
            return false;
        }

    }
}
