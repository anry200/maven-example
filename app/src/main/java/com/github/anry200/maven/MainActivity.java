package com.github.anry200.maven;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.github.anry200.local.LocalHelloWorld;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalHelloWorld.sayHello("Jon Dou");
    }
}
