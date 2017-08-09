package br.com.preco.perdeu.perdeupreco.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import br.com.preco.perdeu.perdeupreco.R;
import br.com.preco.perdeu.perdeupreco.adapter.ListAdapter;
import br.com.preco.perdeu.perdeupreco.model.BarCode;
import io.realm.Realm;

/**
 * Created by brunolemgruber on 08/01/16.
 */
public class ListActivity extends Activity {

    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<BarCode> barCodes;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list);

        realm = Realm.getInstance(this);
        barCodes = realm.where(BarCode.class).findAll();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new ListAdapter(this, barCodes));
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
