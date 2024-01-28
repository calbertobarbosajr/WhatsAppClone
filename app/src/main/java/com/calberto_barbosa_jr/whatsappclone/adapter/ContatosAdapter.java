package com.calberto_barbosa_jr.whatsappclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calberto_barbosa_jr.whatsappclone.R;
import com.calberto_barbosa_jr.whatsappclone.model.Usuario;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {

    private List<Usuario> contatos;
    private Context context;

    public ContatosAdapter(List<Usuario> listaContatos, Context c) {
        this.contatos = listaContatos;
        this.context = c;
    }

    public List<Usuario> getContatos(){
        return this.contatos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from( parent.getContext() ).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Usuario usuario = contatos.get( position );
        boolean cabecalho = usuario.getEmail().isEmpty();

        holder.nome.setText( usuario.getNome() );
        holder.email.setText( usuario.getEmail() );

        if( usuario.getFoto() != null ){
            Uri uri = Uri.parse( usuario.getFoto() );
            Glide.with( context ).load( uri ).into( holder.foto );
        }else {
            if( cabecalho ){
                holder.foto.setImageResource( R.drawable.icone_grupo );
                holder.email.setVisibility( View.GONE );
            }else {
                holder.foto.setImageResource( R.drawable.padrao );
            }

        }

    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome, email;

        public MyViewHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            email = itemView.findViewById(R.id.textEmailContato);

        }
    }

}
