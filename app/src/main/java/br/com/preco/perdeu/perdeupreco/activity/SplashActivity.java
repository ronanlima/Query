package br.com.preco.perdeu.perdeupreco.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import br.com.preco.perdeu.perdeupreco.R;

/**
 * Created by Ronan Lima on 10/02/2016.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        Thread background = new Thread() {
            public void run() {

                try {

                    sleep(2 * 1000);

                    //Intent i=new Intent(getBaseContext(),IntroActivity.class);
                    Intent i = new Intent(getBaseContext(), WizardActivity.class);
                    startActivity(i);
                    finish();

                } catch (Exception e) {

                    Log.e("Mostrar wizard usuário", e.getMessage());


                }
            }
        };

        background.start();

    }
}

