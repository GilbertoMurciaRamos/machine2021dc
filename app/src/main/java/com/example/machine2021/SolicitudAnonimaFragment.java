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
import android.widget.Toast;


public class SolicitudAnonimaFragment extends Fragment {

    private int id_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        id_type = arguments.getInt("type");

        Toast.makeText(getContext(), "dato entrante: "+id_type, Toast.LENGTH_SHORT).show();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solicitud_anonima, container, false);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                Fragment nuevoFragmento = new TypeUserFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_fragment, nuevoFragmento);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.type_user);
                transaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}