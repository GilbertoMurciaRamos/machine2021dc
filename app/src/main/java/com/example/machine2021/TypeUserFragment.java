package com.example.machine2021;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TypeUserFragment extends Fragment implements View.OnClickListener {

    private LinearLayout btn_insigniaP, btn_insigniaT, btn_insigniaE, btn_like1, btn_like2, btn_like3, btn_continue;
    private int var;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_type_user, container, false);

        btn_like1 = view.findViewById(R.id.item_selectP);
        btn_like2 = view.findViewById(R.id.item_selectT);
        btn_like3 = view.findViewById(R.id.item_selectE);

        btn_insigniaP = view.findViewById(R.id.btn_p);
        btn_insigniaP.setOnClickListener(this);
        btn_insigniaT = view.findViewById(R.id.btn_t);
        btn_insigniaT.setOnClickListener(this);
        btn_insigniaE = view.findViewById(R.id.btn_E);
        btn_insigniaE.setOnClickListener(this);

        btn_continue = view.findViewById(R.id.btn_continuar);
        btn_continue.setOnClickListener(this);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                Fragment nuevoFragmento = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_fragment, nuevoFragmento);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
                transaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_p:
                //Toast.makeText(getContext(), "Selecionaste productor", Toast.LENGTH_SHORT).show();
                var=1;
                btn_like1.setVisibility(View.VISIBLE);
                btn_like2.setVisibility(View.GONE);
                btn_like3.setVisibility(View.GONE);
                break;
            case R.id.btn_t:
                //Toast.makeText(getContext(), "Selecionaste técnico", Toast.LENGTH_SHORT).show();
                var=2;
                btn_like1.setVisibility(View.GONE);
                btn_like2.setVisibility(View.VISIBLE);
                btn_like3.setVisibility(View.GONE);
                break;
            case R.id.btn_E:
                //Toast.makeText(getContext(), "Selecionaste empresa", Toast.LENGTH_SHORT).show();
                var=3;
                btn_like1.setVisibility(View.GONE);
                btn_like2.setVisibility(View.GONE);
                btn_like3.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_continuar:
                ValidarSeleccion();
                break;
        }
    }

    private void ValidarSeleccion() {
        if (var > 0 ){
            //Toast.makeText(getContext(), "puede continuar", Toast.LENGTH_SHORT).show();
            Fragment nuevoFragmento = new SolicitudAnonimaFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", var);
            nuevoFragmento.setArguments(bundle);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_fragment, nuevoFragmento);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(null);
            ((MainActivity)getActivity()).info_fragment=1.2;
            transaction.commit();
        }else{
            Toast.makeText(getContext(), "Debe seleccionar un tipo de usuario", Toast.LENGTH_SHORT).show();
        }
    }
}