package br.com.preco.perdeu.perdeupreco.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import br.com.preco.perdeu.perdeupreco.R;

/**
 * Created by Ronan Lima on 21/03/2016.
 */
public class WelcomeEstabelecimento extends Activity {
    public Toolbar toolbar;
    private TextView nomeEstabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_estabelecimento);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);//TODO ronan.lima: trocar por icone gps
        toolbar.setTitle("Estabelecimento");

        //Bundle extras = getIntent().getExtras();
        nomeEstabelecimento = (TextView) findViewById(R.id.nome_estabelecimento);
        //nomeEstabelecimento.setText(extras.getString("nome_estabelecimento"));
        nomeEstabelecimento.setText("Estabelecimento xpto");

    }
}
