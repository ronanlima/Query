package br.com.preco.perdeu.perdeupreco.wizard.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.ArrayList;

import br.com.preco.perdeu.perdeupreco.wizard.ui.InstructionFragment;

/**
 * Created by Ronan Lima on 16/02/2016.
 */
public class InstructionPage extends Page {
    protected ArrayList<String> mChoices = new ArrayList<String>();

    public InstructionPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return InstructionFragment.create(getKey());
    }

    public String getOptionAt(int position) {
        return mChoices.get(position);
    }

    public int getOptionCount() {
        return mChoices.size();
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

        /*
         The line below is commented out to prevent another ReviewItem being added to the review
         at the end of the wizard. If you want to enable this the value displayed will be '(None)'
         but you can change this value by changing the value inside mData.getString(<value you want>)
          */


        //dest.add(new ReviewItem(getTitle(), mData.getString(SIMPLE_DATA_KEY), getKey()));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(SIMPLE_DATA_KEY));
    }

    public InstructionPage setValue(String value) {
        mData.putString(SIMPLE_DATA_KEY, value);
        return this;
    }


}