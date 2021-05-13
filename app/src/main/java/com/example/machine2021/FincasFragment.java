package com.example.machine2021;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FincasFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

    private LinearLayout btn_registro, modulo_registro;
    private RelativeLayout modulo_fincas;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    List<DatosFincas> ListFincas;
    RecyclerView recyclerView;

    String email, var_validate;

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
        View view = inflater.inflate(R.layout.fragment_fincas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewMisFincas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btn_registro = view.findViewById(R.id.btn_registro_f);
        btn_registro.setOnClickListener(this);

        modulo_registro = view.findViewById(R.id.modulRegistro);
        modulo_fincas = view.findViewById(R.id.modulMisFinicas);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email=user.getEmail();
        }

        ListFincas = new ArrayList<>();

        request = Volley.newRequestQueue(getContext());

        CargarDatos();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                //Toast.makeText(getContext(), "vamos para atras", Toast.LENGTH_SHORT).show();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                Fragment nuevoFragmento = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_fragment, nuevoFragmento);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(email);
                ((MainActivity)getActivity()).info_fragment=0;
                transaction.commit();
                break;

            case R.id.btn_mas:
                //Toast.makeText(getContext(), "agregando una empresa más", Toast.LENGTH_SHORT).show();
                CrearFinca();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void CrearFinca() {
        //Toast.makeText(getContext(), "Vas a crear una nueva finca", Toast.LENGTH_SHORT).show();
        Fragment nuevoFragmento = new FormFincaFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_fragment, nuevoFragmento);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.create_farms);
        transaction.commit();
    }

    private void CargarDatos() {
        String URL_CONSULTA = "https://emprendegrm.com/MachineLearning/ConsultMisFincas.php?datoUsuario="+email;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL_CONSULTA,null,this,this);
        request.add(jsonObjectRequest);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_registro_f:
                //Toast.makeText(getContext(), "Registrar empresa", Toast.LENGTH_SHORT).show();
                CrearFinca();
                break;
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d("JSONR", String.valueOf(response));
        DatosFincas datosFincas = null;
        //recibimos el dato que retorna del Json
        JSONArray json=response.optJSONArray("misFincas");

        try {
            for (int i=0;i<json.length();i++){
                datosFincas = new DatosFincas();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                datosFincas.setId_finca(jsonObject.optString("idFinca").trim());
                datosFincas.setName_finca(jsonObject.optString("nombreFincas").trim());

                ListFincas.add(datosFincas);

            }

            var_validate = datosFincas.getVar_dato();

            if (var_validate=="1") {
                modulo_fincas.setVisibility(View.VISIBLE);
                modulo_registro.setVisibility(View.GONE);
                AdapterMisFincas adapterMisFincas = new AdapterMisFincas(ListFincas);
                recyclerView.setAdapter(adapterMisFincas);
            }else{
                //Toast.makeText(getContext(), "Faltan datos", Toast.LENGTH_SHORT).show();
                modulo_fincas.setVisibility(View.GONE);
                modulo_registro.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


}