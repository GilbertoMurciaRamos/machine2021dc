package com.example.machine2021;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;

    public static final int SIGN_IN_CODE = 777;

    private ProgressDialog dialog;

    String nameuser, email;

    private static String URL_REGIST = "https://emprendegrm.com/MachineLearning/RegistUsuario.php";

    //Declaración del objeto firebase
    private FirebaseAuth mAuthf;
    private FirebaseAuth.AuthStateListener firebaseAutoListener;

    Toolbar toolbar;
    private BottomNavigationView bottomNavigation;

    private boolean viewIsAtHome;
    public double info_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))// dat para autenticacion con firebase
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        mAuthf = FirebaseAuth.getInstance();

        firebaseAutoListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuthf.getCurrentUser();
                if (user != null){
                    email=user.getEmail();
                    nameuser=user.getDisplayName();
                    InitListener();
                }else{
                    Inicio();
                }
            }
        };



    }

    private void Inicio() {
        getSupportActionBar().setTitle(R.string.app_name);
        addFragment(new LoginFragment());
        viewIsAtHome = true;
    }

    private void InitListener(){
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int idMenu = item.getItemId();
                switch (idMenu) {
                    case R.id.inicio:
                        getSupportActionBar().setTitle(R.string.app_name);
                        addFragment(new HomeFragment());
                        viewIsAtHome = true;
                        break;
                    case R.id.salir:
                        logOut();
                        break;
                }
                return true;
            }
        });

        bottomNavigation.setSelectedItemId(R.id.inicio);
    }

    private void addFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    public void pantallaCompleta(View view) {
        //toolbar.setVisibility(View.GONE);
        bottomNavigation.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (!viewIsAtHome) { //Si la vista actual no es el fragment Home
            if (info_fragment > 0){
                if (info_fragment==1) {
                    getSupportActionBar().setTitle(R.string.app_name);
                    addFragment(new LoginFragment());
                    viewIsAtHome = true;
                    info_fragment=0;
                }
                if (info_fragment==1.2) {
                    getSupportActionBar().setTitle(R.string.type_user);
                    addFragment(new TypeUserFragment());
                    viewIsAtHome = false;
                    info_fragment=1;
                }
                if (info_fragment==2){
                    moveTaskToBack(true);
                }

            }else {
                bottomNavigation.setSelectedItemId(R.id.inicio); //Selecciona el fragment Home
            }
        } else {
            moveTaskToBack(true);  //Si presionas Back cuando ya muestras el fragment Home, sale de la app
        }
    }

    public void SinRegistro() {
        getSupportActionBar().setTitle(R.string.type_user);
        addFragment(new TypeUserFragment());
        viewIsAtHome = false;
        info_fragment=1;
    }

    public void LoginGoogle(View v) {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, SIGN_IN_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthf.addAuthStateListener(firebaseAutoListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAutoListener != null){
            mAuthf.removeAuthStateListener(firebaseAutoListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(@NotNull GoogleSignInResult result) {
        if (result.isSuccess()){
            //Al inicar sesión vamos a la activity correspondiente
            firebaseAuthWithGogle(result.getSignInAccount());
        }else{
            Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGogle(@NotNull GoogleSignInAccount signInAccount) {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.show();
        dialog.setMessage("Cargando...");

        AuthCredential credencial = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        mAuthf.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                dialog.dismiss();
                if (!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "No se ha logrado la autenticación con Firebase", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(MainActivity.this, "Autenticación con exito", Toast.LENGTH_SHORT).show();
                    LoginExitoso();
                }
            }
        });
    }

    private void LoginExitoso() {
        final ProgressDialog loading = ProgressDialog.show(this,"","Cargando..",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JSONR", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        //Toast.makeText(MainActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        getSupportActionBar().setTitle(email);
                        addFragment(new HomeFragment());
                        viewIsAtHome = true;
                        info_fragment=2;
                        loading.dismiss();

                    }else{
                        Toast.makeText(MainActivity.this, "En el momento tiene problemas de conexión, por favor inténtelo más tarde...", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        loading.dismiss();
                        logOut();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error de registro" + e.toString(), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error de registro" + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre",nameuser);
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    //Toast.makeText(MainActivity.this, "sesion cerrada con exito", Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setTitle(R.string.app_name);
                    addFragment(new LoginFragment());
                    viewIsAtHome = true;
                } else {
                    Toast.makeText(MainActivity.this, "No se pudo cerrar sesión", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void menus() {
        getSupportActionBar().setTitle(email);
        bottomNavigation.setVisibility(View.VISIBLE);
    }
}