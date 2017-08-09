package br.com.preco.perdeu.perdeupreco.wizard.model;

import java.util.ArrayList;

/**
 * Created by Ronan Lima on 16/02/2016.
 */
public class PageList extends ArrayList<Page> implements PageTreeNode{

    public PageList(){

    }

    public PageList(Page... pages){
        for(Page page : pages){
            add(page);
        }
    }
    @Override
    public Page findByKey(String key) {
        for (Page childPage : this) {
            Page found = childPage.findByKey(key);
            if (found != null){
                return found;
            }
        }
        return null;
    }

    @Override
    public void flattenCurrentPageSequence(ArrayList<Page> dest) {
        for (Page childPage : this) {
            childPage.flattenCurrentPageSequence(dest);
        }
    }
}
