package br.com.preco.perdeu.perdeupreco.activity;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Ronan Lima on 13/03/2016.
 */
@SuppressWarnings("ResourceType")
public class GpsTracker extends Service implements LocationListener {

    private final Context context;
    private Location location;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private boolean canGetLocation = false;

    public GpsTracker(Context context) {
        this.context = context;
        getLocation(context);
    }

    public boolean isCanGetLocation(){
        return this.canGetLocation;
    }

    private void getLocation(final Context context) {
        boolean isGpsEnabled = false;
        Toast.makeText(context, "Primeira vez", 3000);
        try {
            setLocationManager((LocationManager) context.getSystemService(context.LOCATION_SERVICE));
            isGpsEnabled = getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER);
            //TODO ronan.lima: permitir pegar localização através do network_provider ?
            if (!isGpsEnabled){ // solicitar habilitação do gps
                solicitarHabilitacaoGps(context);
            } else {
                this.canGetLocation = true;
                if (location == null){
                    getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 9000, 0, this);
                    location = getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null){
                        setLatitude(location.getLatitude());
                        setLongitude(location.getLongitude());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateNewLocation(Location location) {
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
    }

    private void solicitarHabilitacaoGps(Context context) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS necessário");
        alertDialog.setMessage("O GPS não está habilitado. Solicitamos que o ligue.");
        alertDialog.setPositiveButton("Configurações", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(context, "Estou ouvindo", 3000);
        calculateNewLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

}
