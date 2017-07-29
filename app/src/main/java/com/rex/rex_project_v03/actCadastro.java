package com.rex.rex_project_v03;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class actCadastro extends AppCompatActivity {

    private static final String TAG = "actCadastro";
    private EditText edt_nome, edt_email, edt_password, edt_numSerie;
    private Button btn_cadastro;
    private TextView lnk_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cadastro);
        edt_nome     = (EditText)findViewById(R.id.edt_nome);
        edt_email    = (EditText)findViewById(R.id.edt_email);
        edt_password = (EditText)findViewById(R.id.edt_password);
        edt_numSerie = (EditText)findViewById(R.id.edt_numSerie);
        btn_cadastro = (Button)findViewById(R.id.btn_cadastrar);
        lnk_login    = (TextView)findViewById(R.id.lnk_login);

        btn_cadastro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cadastrar(); //Chama a função para fazer o cadastro
            }
        });

        lnk_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish(); /*Fecha a tela de cadastro e retorna para a página de login*/
            }
        });
    }

    public void cadastrar() {
        Log.d(TAG, "Cadastrar");

        if (!validate()) {
            onCadastroFailed();
            return;
        }

        if (isCadastroValido() == true) {
            btn_cadastro.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(actCadastro.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Criando conta! Aguarde...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onCadastroSucess();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        }
    }

    public boolean isCadastroValido(){
        boolean res = true;

        /*LOGICA PARA VERIFICAR E SALVAR OS DADOS JUNTO AO BANCO DE DADOS AQUI*/

        return res;
    }

    public void onCadastroSucess(){
        btn_cadastro.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onCadastroFailed(){
        Toast.makeText(getBaseContext(), "Cadastro incorreto!", Toast.LENGTH_LONG).show();
        btn_cadastro.setEnabled(true);
    }

    public boolean validate(){
        boolean valid = true;

        String name = edt_nome.getText().toString();
        String email = edt_email.getText().toString();
        String senha = edt_password.getText().toString();
        String numSerie = edt_numSerie.getText().toString();

        if (name.isEmpty() || name.length() < 3){                                       /* VALIDAÇÃO DO CAMPO NOME */
            edt_nome.setError("Deve conter pelo menos 3 caracteres!");
            valid = false;
        } else {
            edt_nome.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){       /* VALIDAÇÃO DO CAMPO EMAIL */
            edt_email.setError("Email inválido!");
            valid = false;
        } else {
            edt_email.setError(null);
        }

        if (senha.isEmpty() || senha.length() < 4){                                     /* VALIDAÇÃO DO CAMPO DE SENHA */
            edt_password.setError("Deve conter pelo menos 4 caracteres!");
            valid = false;
        } else {
            edt_password.setError(null);
        }

        if (numSerie.isEmpty() || numSerie.length() < 4){                               /* VALIDAÇÃO DO CAMPO DO NÚMERO DE SÉRIE */
            edt_numSerie.setError("Número de série inválido!");                         /* Precisa ser adicionado uma verificação junto ao banco de dados para tal numero de série*/
            valid = false;
        } else {
            edt_numSerie.setError(null);
        }

        return valid;
    }
}



