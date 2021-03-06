package br.com.preco.perdeu.perdeupreco.wizard.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Ronan Lima on 16/02/2016.
 */
public abstract class AbstractWizardModel implements ModelCallbacks {
    protected Context mContext;

    private List<ModelCallbacks> mListeners = new ArrayList<ModelCallbacks>();
    private PageList mRootPageList;

    public AbstractWizardModel(Context context) {
        mContext = context;
        mRootPageList = onNewRootPageList();
    }

    /**
     * Override this to define a new wizard model.
     */
    protected abstract PageList onNewRootPageList();

    @Override
    public void onPageDataChanged(Page page) {
        // can't use for each because of concurrent modification (review fragment
        // can get added or removed and will register itself as a listener)
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onPageDataChanged(page);
        }
    }

    @Override
    public void onPageTreeChanged() {
        // can't use for each because of concurrent modification (review fragment
        // can get added or removed and will register itself as a listener)
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onPageTreeChanged();
        }
    }

    public Page findByKey(String key) {
        return mRootPageList.findByKey(key);
    }

    public void load(Bundle savedValues) {
        for (String key : savedValues.keySet()) {
            mRootPageList.findByKey(key).resetData(savedValues.getBundle(key));
        }
    }

    public void registerListener(ModelCallbacks listener) {
        mListeners.add(listener);
    }

    public Bundle save() {
        Bundle bundle = new Bundle();
        for (Page page : getCurrentPageSequence()) {
            bundle.putBundle(page.getKey(), page.getData());
        }
        return bundle;
    }

    /**
     * Gets the current list of wizard steps, flattening nested (dependent) pages based on the
     * user's choices.
     */
    public List<Page> getCurrentPageSequence() {
        ArrayList<Page> flattened = new ArrayList<Page>();
        mRootPageList.flattenCurrentPageSequence(flattened);
        return flattened;
    }

    public void unregisterListener(ModelCallbacks listener) {
        mListeners.remove(listener);
    }


    public PageList getmRootPageList() {
        return mRootPageList;
    }

    public void setmRootPageList(PageList mRootPageList) {
        this.mRootPageList = mRootPageList;
    }

    public List<ModelCallbacks> getmListeners() {
        return mListeners;
    }

    public void setmListeners(List<ModelCallbacks> mListeners) {
        this.mListeners = mListeners;
    }
}