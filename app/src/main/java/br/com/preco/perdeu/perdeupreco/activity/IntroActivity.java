package br.com.preco.perdeu.perdeupreco.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import br.com.preco.perdeu.perdeupreco.R;
import br.com.preco.perdeu.perdeupreco.util.PreferencesUtils;

/**
 * Created by brunolemgruber on 22/09/15.
 */
public class IntroActivity extends AppIntro2 {

    @Override
    public void init(@Nullable Bundle bundle) {

        if (PreferencesUtils.getPreference(IntroActivity.this, "login").equalsIgnoreCase("0")){

            addSlide(AppIntroFragment.newInstance("Leitor", "Descrição sobre o leitor", R.drawable.ic_slide1, getResources().getColor(R.color.colorPrimaryDark)));
            addSlide(AppIntroFragment.newInstance("Código de barra", "Descrição sobre o código de barra", R.drawable.ic_slide2, getResources().getColor(R.color.colorPrimaryDark)));
            addSlide(AppIntroFragment.newInstance("Produto", "Descrição sobre o produto", R.drawable.ic_slide3, getResources().getColor(R.color.colorPrimaryDark)));
            addSlide(AppIntroFragment.newInstance("Valor final", "Descrição sobre o valor final ", R.drawable.ic_slide4, getResources().getColor(R.color.colorPrimaryDark)));

        } else{

            loadMainActivity();

        }
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }
}
