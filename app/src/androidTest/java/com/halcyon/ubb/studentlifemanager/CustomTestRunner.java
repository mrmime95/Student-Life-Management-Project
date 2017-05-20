package com.halcyon.ubb.studentlifemanager;

import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.test.runner.AndroidJUnitRunner;

public class CustomTestRunner extends AndroidJUnitRunner
{
    @Override
    public void onCreate(Bundle arguments)
    {
        MultiDex.install(getTargetContext());
        super.onCreate(arguments);
    }
}