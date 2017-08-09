package br.com.preco.perdeu.perdeupreco.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import br.com.preco.perdeu.perdeupreco.R;
import br.com.preco.perdeu.perdeupreco.model.BarCode;
import br.com.preco.perdeu.perdeupreco.service.BarCodeService;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Button button,btnLevar,btnNaoLevar,btnFinalizar;
    private TextView produto,preco;
    private List<BarCode> barCodes,produtosSelecionados;
    private BarCode barCodeSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        produtosSelecionados = new ArrayList<>();

        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new IntentIntegrator(MainActivity.this).setCaptureActivity(BarCodeActivity.class).initiateScan();

            }
        });

        btnFinalizar = (Button)findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                barCodes = BarCodeService.getBarCode(this);

                for (BarCode b : barCodes){

                    if(result.getContents().substring(0,1).equalsIgnoreCase(b.getCodigoBarra().substring(0,1))){
                        barCodeSelecionado = b;
                        break;
                    }

                }

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog);
                dialog.show();

                produto = (TextView) dialog.findViewById(R.id.produto);
                preco = (TextView) dialog.findViewById(R.id.preco);

                produto.setText(barCodeSelecionado.getNomeProduto());
                preco.setText(barCodeSelecionado.getPreco());

                btnLevar = (Button) dialog.findViewById(R.id.levar);
                btnLevar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        produtosSelecionados.add(barCodeSelecionado);

                        salvarProdutos(produtosSelecionados);

                        dialog.dismiss();
                    }
                });

                btnNaoLevar = (Button) dialog.findViewById(R.id.nao_levar);
                btnNaoLevar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void salvarProdutos(List<BarCode> produtosSelecionados){

        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();
        realm.where(BarCode.class).findAll().clear();
        realm.copyToRealm(produtosSelecionados);
        realm.commitTransaction();
        realm.close();

    }
}
