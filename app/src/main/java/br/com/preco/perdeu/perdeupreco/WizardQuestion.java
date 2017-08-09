package br.com.preco.perdeu.perdeupreco;

import android.content.Context;

import br.com.preco.perdeu.perdeupreco.wizard.model.AbstractWizardModel;
import br.com.preco.perdeu.perdeupreco.wizard.model.InstructionPage;
import br.com.preco.perdeu.perdeupreco.wizard.model.PageList;
import br.com.preco.perdeu.perdeupreco.wizard.model.SingleFixedChoicePage;

/**
 * Created by Ronan Lima on 24/02/2016.
 */
public class WizardQuestion extends AbstractWizardModel {
    public WizardQuestion(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(

                new InstructionPage(this, "Info"),

                new SingleFixedChoicePage(this, "Sexo")
                        .setChoices("Masculino", "Feminino", "Não importa")
                        .setRequired(true),

                new SingleFixedChoicePage(this, "Idade")
                        .setChoices("Menos de 16", "16 - 20", "21 - 30", "31 - 40", "41 - 50", "Mais de 50")
                        .setRequired(true)

        );
    }
}
