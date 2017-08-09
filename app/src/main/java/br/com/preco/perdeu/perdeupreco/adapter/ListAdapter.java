package br.com.preco.perdeu.perdeupreco.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.preco.perdeu.perdeupreco.R;
import br.com.preco.perdeu.perdeupreco.model.BarCode;

/**
 * Created by brunolemgruber on 12/09/15.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>  {

    protected static final String TAG = "perdeupreco";
    private final List<BarCode> barCodes;
    private final Context context;

    public ListAdapter(Context context, List<BarCode> barCodes) {
        this.context = context;
        this.barCodes = barCodes;
    }

    @Override
    public int getItemCount() {
        return this.barCodes != null ? this.barCodes.size() : 0;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);

        // Cria o ViewHolder
        ListViewHolder holder = new ListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        // Atualiza a view
        BarCode b = barCodes.get(position);

        holder.produto.setText(b.getNomeProduto());
        holder.preco.setText(b.getPreco());

    }

    // ViewHolder com as views
    public static class ListViewHolder extends RecyclerView.ViewHolder {
        public TextView produto,preco;

        public ListViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            produto = (TextView) view.findViewById(R.id.produto);
            preco = (TextView) view.findViewById(R.id.preco);

        }
    }
}
