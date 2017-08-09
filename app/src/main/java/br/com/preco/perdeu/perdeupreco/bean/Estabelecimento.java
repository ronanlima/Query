package br.com.preco.perdeu.perdeupreco.bean;

/**
 * Created by Ronan.lima on 26/02/16.
 */
public class Estabelecimento {
    private Integer id;
    private Long latitude;
    private Long longitude;

    public Estabelecimento() {
    }

    public Estabelecimento(Integer id, Long latitude, Long longitude){
        setId(id);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }
}
