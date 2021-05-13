package com.example.machine2021;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private LinearLayout btnGoogle, btnSinRegistro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ((MainActivity)getActivity()).pantallaCompleta(view);

        btnGoogle = view.findViewById(R.id.btn_googlef);
        btnGoogle.setOnClickListener(this);
        btnSinRegistro = view.findViewById(R.id.btn_sinR);
        btnSinRegistro.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_googlef:
                //Toast.makeText(getContext(), "con registro", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).LoginGoogle(v);
                break;
            case R.id.btn_sinR:
                //Toast.makeText(getContext(), "sin registro", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).SinRegistro();
                break;
        }
    }
}