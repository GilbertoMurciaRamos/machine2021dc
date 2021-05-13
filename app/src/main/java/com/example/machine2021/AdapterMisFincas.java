package com.example.machine2021;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterMisFincas extends RecyclerView.Adapter<AdapterMisFincas.ViewHolder>{

    List<DatosFincas> listFincas;
    private Context context;

    public AdapterMisFincas(List<DatosFincas> listEmpresas) {
        this.listFincas = listEmpresas;
    }

    @NonNull
    @Override
    public AdapterMisFincas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_mis_fincas, parent,false);
        return new AdapterMisFincas.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMisFincas.ViewHolder holder, int position) {
        DatosFincas datosFincas = listFincas.get(position);
        holder.nombre_finca.setText(datosFincas.getName_finca());

        holder.Id_finca = datosFincas.getId_finca();
        holder.Name_Finca = datosFincas.getName_finca();
    }

    @Override
    public int getItemCount() {
        return listFincas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nombre_finca;
        ImageButton img_vermas;

        String Id_finca, Name_Finca;

        public ViewHolder(@NonNull View view) {
            super(view);
            context = view.getContext();

            nombre_finca = view.findViewById(R.id.textNombre_f);
            img_vermas = view.findViewById(R.id.img_verFinca);
            img_vermas.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_verFinca:
                    //Toast.makeText(context, "vamos para a ver los datos de esta empresa", Toast.LENGTH_SHORT).show();
                    Fragment nuevoFragmento = new FormFincaFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("nombre_empresas", Id_finca);
                    nuevoFragmento.setArguments(bundle);
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_fragment, nuevoFragmento);
                    ((AppCompatActivity)context).getSupportActionBar().setTitle(Name_Finca);
                    transaction.commit();
                    break;
            }
        }
    }
}
