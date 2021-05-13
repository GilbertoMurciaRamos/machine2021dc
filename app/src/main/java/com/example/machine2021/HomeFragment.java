package com.example.machine2021;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.PrimitiveIterator;

public class HomeFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>, View.OnClickListener {

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    String email, idDato;
    String var = "";

    private LinearLayout btn_insigniaP, btn_insigniaT, btn_insigniaE, btn_like1, btn_like2, btn_like3, btn_continue, liner_typeYser, linearMenu;

    private LinearLayout btn_perfil, btn_fincas, btn_externos, btn_historial, btn_newproceso;

    private static String URL_REGIST = "https://emprendegrm.com/MachineLearning/RegistTypeUser.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).menus();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        request = Volley.newRequestQueue(getContext());

        liner_typeYser = view.findViewById(R.id.type_user_home);
        linearMenu = view.findViewById(R.id.linear_menu);

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

        btn_perfil = view.findViewById(R.id.btn_perfiles);
        btn_perfil.setOnClickListener(this);
        btn_fincas = view.findViewById(R.id.btn_fincas);
        btn_fincas.setOnClickListener(this);
        btn_externos = view.findViewById(R.id.btn_externos);
        btn_externos.setOnClickListener(this);
        btn_historial = view.findViewById(R.id.btn_historial);
        btn_historial.setOnClickListener(this);
        btn_newproceso = view.findViewById(R.id.btn_procesos);
        btn_newproceso.setOnClickListener(this);

        CargarDatos();
        return view;
    }

    private void CargarDatos() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
        }
        String URL_CONSULTA = "https://emprendegrm.com/MachineLearning/CosulTypeUser.php?email="+email;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL_CONSULTA,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d("JSONR", String.valueOf(response));
        final DatosUsuario datosUsuario = new DatosUsuario();
        JSONArray json=response.optJSONArray("typeUser");
        JSONObject jsonObject=null;

        try {
            jsonObject=json.getJSONObject(0);
            datosUsuario.setIndicativo(jsonObject.optString("success").trim());

        } catch (Exception e) {
            e.printStackTrace();
        }

        idDato = datosUsuario.getIndicativo();

        if (!idDato.equals("s")){
            liner_typeYser.setVisibility(View.VISIBLE);
            linearMenu.setVisibility(View.GONE);
        }else{
            liner_typeYser.setVisibility(View.GONE);
            linearMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_p:
                //Toast.makeText(getContext(), "Selecionaste productor", Toast.LENGTH_SHORT).show();
                var = "Pr";
                btn_like1.setVisibility(View.VISIBLE);
                btn_like2.setVisibility(View.GONE);
                btn_like3.setVisibility(View.GONE);
                break;
            case R.id.btn_t:
                //Toast.makeText(getContext(), "Selecionaste técnico", Toast.LENGTH_SHORT).show();
                var = "Tc";
                btn_like1.setVisibility(View.GONE);
                btn_like2.setVisibility(View.VISIBLE);
                btn_like3.setVisibility(View.GONE);
                break;
            case R.id.btn_E:
                //Toast.makeText(getContext(), "Selecionaste empresa", Toast.LENGTH_SHORT).show();
                var = "Em";
                btn_like1.setVisibility(View.GONE);
                btn_like2.setVisibility(View.GONE);
                btn_like3.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_continuar:
                ValidarSeleccion();
                break;
            case R.id.btn_perfiles:
                Toast.makeText(getContext(), "mi perfil", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_fincas:
                Fragment fragmentE = new FincasFragment();
                FragmentTransaction transactionE = getFragmentManager().beginTransaction();
                transactionE.replace(R.id.content_fragment, fragmentE);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.my_farms);
                ((MainActivity)getActivity()).info_fragment=2;
                transactionE.commit();
                break;
            case R.id.btn_externos:
                Toast.makeText(getContext(), "personal externo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_historial:
                Toast.makeText(getContext(), "Historial", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_procesos:
                Toast.makeText(getContext(), "nuevo proceso", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void ValidarSeleccion() {
        if (!var.equals("")){
            //Toast.makeText(getContext(), "Puedes continuar ocn el registro", Toast.LENGTH_SHORT).show();
            final ProgressDialog loading = ProgressDialog.show(getContext(),"","Cargando..",false,false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("JSONR", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            Toast.makeText(getContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            CargarDatos();
                            //getSupportActionBar().setTitle(email);
                            //addFragment(new HomeFragment());
                            //viewIsAtHome = true;
                            //info_fragment=2;
                            loading.dismiss();

                        }else{
                            Toast.makeText(getContext(), "En el momento tiene problemas de conexión, por favor inténtelo más tarde...", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error de registro" + e.toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Error de registro" + error.toString(), Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("dato",var.trim());
                    params.put("id",idDato.trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(getContext(), "Debe seleccionar un tipo de usuario", Toast.LENGTH_SHORT).show();
        }
    }
}