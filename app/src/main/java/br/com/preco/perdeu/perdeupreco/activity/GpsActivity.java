package br.com.preco.perdeu.perdeupreco.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import br.com.preco.perdeu.perdeupreco.R;

/**
 * Created by Ronan Lima on 13/03/2016.
 */
@SuppressWarnings("ResourceType")
public class GpsActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    private static final String TAG = "Query";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    private boolean requestLocationUpdates = false;
    private LocationRequest locationRequest;

    private static int INTERVALO_ATUALIZACAO = 1000 * 7;
    private static int INTERVALOR_RAPIDO = 1000 * 2;
    private static int DISTANCIA = 30; // 30 metros
    private double latitude = 0;
    private double longitude = 0;

    GpsTracker gpsTracker;
    private TextView textLocal;

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teste_gps_location);

        if (checkPlayServices()){
            buildGoogleApiClient();
            createLocationRequest();
        }

        //togglePeriodicLocationUpdates();
        /*textLocal = (TextView) findViewById(R.id.local_atual);
        gpsTracker = new GpsTracker(this);
        if (gpsTracker.isCanGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            textLocal.setText("Sua localização é: \nLatitude= " + latitude + "\nLongitude= " + longitude);
            //gpsTracker.getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsTracker);
        }*/
    }

    private void togglePeriodicLocationUpdates(){
        if (!requestLocationUpdates){
            requestLocationUpdates = true;
            if(!googleApiClient.isConnected()){
                googleApiClient.connect();
            }
            startLocationUpdate();
            Log.i(TAG,"Verificação periódica em andamento...");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null){
            googleApiClient.connect();
            requestLocationUpdates = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (googleApiClient.isConnected() && requestLocationUpdates){
            startLocationUpdate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdate();
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void startLocationUpdate() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        Log.i(TAG, "Estou atualizando...");
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVALO_ATUALIZACAO);
        locationRequest.setFastestInterval(INTERVALOR_RAPIDO);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISTANCIA);
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultado = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultado != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultado)){
                GooglePlayServicesUtil.getErrorDialog(resultado, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(this, "Este aparelho não suporte os serviços do Google Play Services.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        getLocation();
        Log.i(TAG,"Conexão estabelecida.");
        if (requestLocationUpdates){
            startLocationUpdate();
        }
    }

    private void getLocation() {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null){
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            textLocal = (TextView) findViewById(R.id.local_atual);
            if (textLocal.getText() != null && !textLocal.getText().toString().isEmpty()){
                textLocal.setText(textLocal.getText()+"\nSua localização é: \nLatitude= " + latitude + "\nLongitude= " + longitude);
                Thread background = new Thread() {
                    public void run() {

                        try {

                            sleep(10 * 1000);

                            //Intent i=new Intent(getBaseContext(),IntroActivity.class);
                            Intent i = new Intent(getBaseContext(), WelcomeEstabelecimento.class);
                            startActivity(i);
                            finish();

                        } catch (Exception e) {
                            Log.e("estabelecimento usuário", e.getMessage());
                        }
                    }
                };

                background.start();
            } else {
                textLocal.setText("Sua localização é: \nLatitude= " + latitude + "\nLongitude= " + longitude);
            }
        } else {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS necessário");
            alertDialog.setMessage("O GPS não está habilitado. Solicitamos sua ativação.");
            alertDialog.setPositiveButton("Configurações", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.getErrorCode() == ConnectionResult.SERVICE_MISSING){
            Log.i(TAG, connectionResult.getErrorMessage());
        } else if (connectionResult.getErrorCode() == ConnectionResult.SERVICE_DISABLED){
            Log.i(TAG, "O GPS está desligado.");
        } else if (connectionResult.getErrorCode() == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            Log.i(TAG, "É necessário atualizar a biblioteca do Google Play Services!");
        }
        Log.i(TAG,"Conexão falhou: ConnectionResult.getErrorCode = "+connectionResult.getErrorCode());
    }
}
