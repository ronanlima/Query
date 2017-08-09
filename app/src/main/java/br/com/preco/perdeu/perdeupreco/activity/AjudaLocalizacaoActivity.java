package br.com.preco.perdeu.perdeupreco.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import br.com.preco.perdeu.perdeupreco.R;

/**
 * Created by Ronan.lima on 30/03/16.
 */
public class AjudaLocalizacaoActivity extends Activity{
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajuda_localizacao);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_logo);
    }
}
