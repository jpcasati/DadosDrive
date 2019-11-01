package br.com.ml.dadosdrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nome;
    EditText codigo;
    Button salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.edtNome);
        codigo = findViewById(R.id.edtCodigo);

        salvar = findViewById(R.id.btnSalvar);
        salvar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Toast.makeText(this, "Salvando dados...", Toast.LENGTH_SHORT).show();
        salvarDadosNaPlanilha();

    }

    private void salvarDadosNaPlanilha() {

        final String nomeD = nome.getText().toString().trim();
        final String codigoD = codigo.getText().toString().trim();


        // Toda vez que alterar o script do google, tem que gerar uma nova versão
        StringRequest stringRequest =
                new StringRequest(Request.Method.POST,
                "https://script.google.com/macros/s/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("sucesso")) {
                            Toast.makeText(MainActivity.this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Erro ao salvar dados!", Toast.LENGTH_SHORT).show();

                            //Log.e("REPOSTA", response);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("REPOSTA", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("action", "adicionar");
                params.put("nome", nomeD);
                params.put("codigo", codigoD);

                return params;
            }
        };

        int socketTimeOut = 50000;

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);

    }
}
