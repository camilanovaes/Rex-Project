package com.rex.rex_project_v03;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class actLogin extends AppCompatActivity {

    private static final String TAG = "atcLogin";
    private static final int REQUEST_SINGUP = 0;

    private EditText edt_email, edt_senha;
    private Button btn_login;
    private TextView lnk_cadastro; //TextView que vai servir de link para o act de cadastro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        edt_email    = (EditText)findViewById(R.id.edt_email);
        edt_senha = (EditText)findViewById(R.id.edt_senha);
        btn_login    = (Button)findViewById(R.id.btn_login);
        lnk_cadastro = (TextView)findViewById(R.id.lnk_cadastro);

        btn_login.setOnClickListener(new View.OnClickListener() { //Listener para o botão de login
            @Override
            public void onClick(View v){
                login(); //Chama a função para fazer login
            }
        });

        lnk_cadastro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Inicia a act de cadastro
                Intent it = new Intent(getApplicationContext(), actCadastro.class);
                startActivityForResult(it, REQUEST_SINGUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        if (isLoginValidate() == true) {
            btn_login.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(actLogin.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Acessando conta, aguarde...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onLoginSucess();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        } else {
            onLoginFailed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_SINGUP) {
            if (requestCode == RESULT_OK) {
                //Se o login for valido:
                this.finish();
            }
        }
    }

    public boolean isLoginValidate(){ /* IMPLEMENTAR TODA A LOGICA DE LOGIN AQUI */

        String email = edt_email.getText().toString();
        String senha = edt_senha.getText().toString();

        if (Objects.equals(email, "adm@rex.com") && (Objects.equals(senha, "admadm123"))){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        //Desabilita voltar para o act main
        moveTaskToBack(true);
    }

    public void onLoginSucess(){
        btn_login.setEnabled(true);
        finish();
    }

    public void onLoginFailed(){
        Toast.makeText(getBaseContext(), "Erro no login", Toast.LENGTH_LONG).show();
        btn_login.setEnabled(true);
    }

    public boolean validate(){ //Validação do campo de login
        boolean valid = true;

        String email = edt_email.getText().toString();
        String senha = edt_senha.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email.setError("Email inválido!");
            valid = false;
        } else {
            edt_email.setError(null);
        }

        if (senha.isEmpty() || senha.length() < 4){
            edt_senha.setError("Deve conter mais que 4 caracteres");
            valid = false;
        } else {
            edt_senha.setError(null);
        }

        return valid;
    }
}