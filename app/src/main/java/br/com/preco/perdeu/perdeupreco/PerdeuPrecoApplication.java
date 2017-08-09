package br.com.preco.perdeu.perdeupreco;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by brunolemgruber on 08/01/16.
 */
public class PerdeuPrecoApplication extends Application {

    private static PerdeuPrecoApplication instance = null;

    public PerdeuPrecoApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("perdeupreco")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
