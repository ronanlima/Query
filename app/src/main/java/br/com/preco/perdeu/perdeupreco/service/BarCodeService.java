package br.com.preco.perdeu.perdeupreco.service;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.preco.perdeu.perdeupreco.model.BarCode;

/**
 * Created by brunolemgruber on 07/01/16.
 */
public class BarCodeService {

    public static List<BarCode> getBarCode(Context context) {

        List<BarCode> barCodes = new ArrayList<BarCode>();

        BarCode barCode = new BarCode();
        barCode.setCodigoBarra("00123456789");
        barCode.setNomeProduto("Cerveja");
        barCode.setPreco("R$ 2,05");
        barCodes.add(barCode);

        BarCode barCode1 = new BarCode();
        barCode1.setCodigoBarra("10123456789");
        barCode1.setNomeProduto("Coca cola");
        barCode1.setPreco("R$ 4,05");
        barCodes.add(barCode1);

        BarCode barCode2 = new BarCode();
        barCode2.setCodigoBarra("20123456789");
        barCode2.setNomeProduto("Arroz");
        barCode2.setPreco("R$ 12,05");
        barCodes.add(barCode2);

        BarCode barCode3 = new BarCode();
        barCode3.setCodigoBarra("30123456789");
        barCode3.setNomeProduto("Feijão");
        barCode3.setPreco("R$ 5,15");
        barCodes.add(barCode3);

        BarCode barCode4 = new BarCode();
        barCode4.setCodigoBarra("40123456789");
        barCode4.setNomeProduto("Pão de queijo");
        barCode4.setPreco("R$ 4,00");
        barCodes.add(barCode4);

        BarCode barCode5 = new BarCode();
        barCode5.setCodigoBarra("50123456789");
        barCode5.setNomeProduto("Batata");
        barCode5.setPreco("R$ 0,50");
        barCodes.add(barCode5);

        BarCode barCode6 = new BarCode();
        barCode6.setCodigoBarra("60123456789");
        barCode6.setNomeProduto("Alho");
        barCode6.setPreco("R$ 1,55");
        barCodes.add(barCode6);

        BarCode barCode7 = new BarCode();
        barCode7.setCodigoBarra("70123456789");
        barCode7.setNomeProduto("Chocolate");
        barCode7.setPreco("R$ 2,05");
        barCodes.add(barCode7);

        BarCode barCode8 = new BarCode();
        barCode8.setCodigoBarra("80123456789");
        barCode8.setNomeProduto("Leite");
        barCode8.setPreco("R$ 2,05");
        barCodes.add(barCode8);

        return barCodes;

    }
}
