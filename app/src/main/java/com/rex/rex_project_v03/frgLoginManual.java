package com.rex.rex_project_v03;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class frgLoginManual extends Fragment implements MqttCallback {

    private Button btn_mEntrar;
    private TextView edt_mRecipiente, edt_mAltura, edt_mLargura_Raio, edt_mComprimento;
    public float mAtura, mLargura_Raio, mComprimento;
    public String check_recicpiente;
    public String txt_mRecipiente = "null";
    public String txt_mAltura = "null";
    public String txt_mLargura_Raio = "null";
    public String txt_mComprimento = "null";

    public frgLoginManual() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg_login_manual, container, false);

        btn_mEntrar         = (Button) view.findViewById(R.id.btn_mEntrar);
        edt_mRecipiente     = (TextView) view.findViewById(R.id.edt_mRecipiente);
        btn_mEntrar         = (Button) view.findViewById(R.id.btn_mEntrar);
        edt_mAltura         = (TextView) view.findViewById(R.id.edt_mAltura);
        edt_mLargura_Raio   = (TextView) view.findViewById(R.id.edt_mLagura_Raio);
        edt_mComprimento    = (TextView) view.findViewById(R.id.edt_mComprimento);

        checkInfo();


        btn_mEntrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EntrarFunc();
            }
        });

        return view;
    }

    //Sava a informação do usuário
    public void saveInfo(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        txt_mAltura         = Float.toString(mAtura);
        txt_mLargura_Raio   = Float.toString(mLargura_Raio);
        txt_mComprimento    = Float.toString(mComprimento);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("key_mAltura", txt_mAltura);
        editor.putString("key_mLartura_Raio", txt_mLargura_Raio);
        editor.putString("key_mComprimento", txt_mComprimento);
        editor.apply();

    }

    public void EntrarFunc(){
        if (isValid()){
            checkInfo();
            saveInfo();

            mqttClient("/rexproject/esp/01/recipiente", check_recicpiente); //É enviado o recipiente a ser utilizado

            //Chama a próxima tela
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragContainerTest, new frgConexao())
                    .commit();
            getActivity().getFragmentManager().popBackStack();
        } else {
            return;
        }
    }

    //Checa a informação do usuário
    public void checkInfo(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        check_recicpiente   = sharedPref.getString("key_recipiente", "");

        if (Objects.equals(check_recicpiente, "0")){
            edt_mRecipiente.setText("Recipiente escolhido: Paralelepipedo");

        } if (Objects.equals(check_recicpiente, "1")){
            edt_mRecipiente.setText("Recipiente escolhido: Cilindro");

        } else {
            edt_mRecipiente.setText("Recipiente escolhido: Cubo");

        }
    }

    public boolean isValid(){
        setText();

        boolean valid = true;
        if (Objects.equals(check_recicpiente, "1")) {
            if (Objects.equals(txt_mAltura, "")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Campo de Altura vazio!", Toast.LENGTH_SHORT).show();
                valid = false;

            }
            if (Objects.equals(txt_mLargura_Raio, "")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Campo de Largura / Raio vazio!", Toast.LENGTH_SHORT).show();
                valid = false;
            }

        } else {
            if (Objects.equals(txt_mAltura, "")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Campo de Altura vazio!", Toast.LENGTH_SHORT).show();
                valid = false;

            }
            if (Objects.equals(txt_mLargura_Raio, "")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Campo de Largura / Raio vazio!", Toast.LENGTH_SHORT).show();
                valid = false;

            }
            if (Objects.equals(txt_mComprimento, "")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Campo de Comprimento vazio!", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        return valid;
    }

    public void setText(){
        txt_mAltura         = edt_mAltura.getText().toString();
        txt_mLargura_Raio   = edt_mLargura_Raio.getText().toString();
        txt_mComprimento    = edt_mComprimento.getText().toString();
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
}
