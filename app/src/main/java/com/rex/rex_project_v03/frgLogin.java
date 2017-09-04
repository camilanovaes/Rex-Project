package com.rex.rex_project_v03;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class frgLogin extends Fragment implements MqttCallback{

    public static final String PREFS_NAME = "MyPrefsFile";

    private String recipiente = "null";
    private String numSerie;
    private String modoOp = null;
    private RadioGroup radioGroup;
    private RadioGroup radioGroup2;
    private TextView edt_nSerie;


    public frgLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Obter a view do fragmento
        View view = inflater.inflate(R.layout.frg_login, container, false);

        //Linkar com o componente da view
        Button btn_entrar = (Button) view.findViewById(R.id.btn_entrar);
        radioGroup        = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup2       = (RadioGroup) view.findViewById(R.id.radioGroup2);
        edt_nSerie        = (TextView) view.findViewById(R.id.edt_nSerie);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                //checkedId é o radio button selecionado: paralelepipedo - cilindro - cubo
                switch (checkedId) {
                    case R.id.rb_paralelepipedo:
                        // O número 0 representa o paralelepipedo no broker
                            recipiente = "0";
                        break;
                    case R.id.rb_cilindro:
                        // O número 1 representa o cilindro no broker
                            recipiente = "1";
                        break;
                    case R.id.rb_cubo:
                        // O número 2 representa o cubo no broker
                            recipiente = "2";
                        break;
                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup2, int checkedId) {
                //checkedId é o radio button selecionado: paralelepipedo - cilindro - cubo
                switch (checkedId) {
                    case R.id.rb_automatico:
                        // O número 0 representa o modo automatico no broker
                        modoOp = "A";
                        break;
                    case R.id.rb_manual:
                        // O número 1 representa o modo manual no broker
                        modoOp = "M";
                        break;
                }
            }
        });

        btn_entrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                    EntrarFunc();

            }
        });

        return view;
    }

    //Sava a informação do usuário
    public void saveInfo(View view){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("key_numSerie", numSerie);
        editor.putString("key_recipiente", recipiente);
        editor.putString("key_modoOp", modoOp);
        editor.apply();
    }

    //Checa a informação do usuário
    public boolean checkInfo(View view){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String check_numSerie   = sharedPref.getString("key_numSerie", "");
        String check_recipiente = sharedPref.getString("key_recipiente", "");

        if (Objects.equals(check_numSerie, "R3X") && (Objects.equals(check_recipiente, "0") || Objects.equals(check_recipiente, "1") || Objects.equals(check_recipiente, "2"))){
            return true;
        } else {
            return false;
        }

    }


    public void EntrarFunc() {
        if (isValidate()) {

            //Sava a informação do usuário
            saveInfo(getView());

            if (Objects.equals(modoOp, "M")){
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragContainerTest, new frgLoginManual())
                        .commit();
                mqttClient("/rexproject/esp/01/operacao", modoOp);
                getActivity().getFragmentManager().popBackStack();
            } else {
                //Publica no tópico
                mqttClient("/rexproject/esp/01/recipiente", recipiente);

                //Chama a próxima tela
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragContainerTest, new frgConexao())
                        .commit();
                getActivity().getFragmentManager().popBackStack();
            }


        } else {
            return;
        }
    }

    public void mqttClient(String topic, String message){
        /*=========> MQTT <========*/
        try {
            MqttClient client = new MqttClient("tcp://test.mosca.io:1883", "APP", new MemoryPersistence());
            client.setCallback(this);


            client.connect(new MqttConnectOptions());
            String mess = message.toString();
            client.publish(topic, mess.getBytes(), 0, false);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


    public boolean isValidate() { //Validação do campo de login
        boolean valid = true;
        numSerie = edt_nSerie.getText().toString();

        if (Objects.equals(recipiente,"0") || Objects.equals(recipiente,"1") || Objects.equals(recipiente,"2" )){
        } else {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Selecione um formato para o recipiente!", Toast.LENGTH_LONG).show();
            valid = false;

        }

        if (Objects.equals(numSerie, "R3X")){
        } else {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Número de série inválido!", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (Objects.equals(modoOp, "A") || Objects.equals(modoOp, "M")) {
        } else {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Selecione um modo de operação!", Toast.LENGTH_SHORT).show();
            valid = false;

        }

        return valid;
    }



}

