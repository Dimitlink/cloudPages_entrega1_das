package com.example.diario.listaDiarios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diario.ListaDiarios;
import com.example.diario.R;

import java.util.ArrayList;

public class DiariosAdapter  extends  RecyclerView.Adapter<DiariosAdapter.ViewHolder>{
    ArrayList<DiarioData> listaDiarios;
    Context context;
    public DiariosAdapter(ArrayList<DiarioData> listaDiarios, ListaDiarios activity) {
        this.listaDiarios = listaDiarios;
        this.context = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_diario,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DiarioData diarioData = listaDiarios.get(position);
        holder.titulo.setText(diarioData.getTitulo());
        holder.fecha.setText(diarioData.getFecha());
        holder.cuerpo.setText(diarioData.getCuerpo());
        Bitmap bmp = BitmapFactory.decodeByteArray(diarioData.getImagen(), 0, diarioData.getImagen().length);
        holder.fotoDiario.setImageBitmap(Bitmap.createScaledBitmap(bmp, 250, 250, false));
    }

    @Override
    public int getItemCount() {
        return listaDiarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView fotoDiario;
        TextView titulo,fecha,cuerpo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoDiario = (ImageView) itemView.findViewById(R.id.i_diario_lista);
            titulo = (TextView) itemView.findViewById(R.id.t_titulo_lista);
            fecha = (TextView) itemView.findViewById(R.id.t_fecha_lista);
            cuerpo = (TextView) itemView.findViewById(R.id.t_cuerpo_lista);
        }
    }
}
