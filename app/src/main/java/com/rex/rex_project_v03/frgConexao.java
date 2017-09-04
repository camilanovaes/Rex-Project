package com.rex.rex_project_v03;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class frgConexao extends Fragment implements MqttCallback {

    String espResponse = "null";
    String check_modoOp, check_recipiente;
    Boolean b_response = false;
    String TOPIC_RESPONSE = "/rexproject/esp/01/response";
    String TOPIC_RECIPIENTE = "/rexproject/esp/01/recipiente";
    String TOPIC_MODOOP = "/rexproject/esp/01/operacao";

    public frgConexao() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg_conexao, container, false);

        Toast.makeText(getActivity().getApplicationContext(),
                "Aguarde um momento!", Toast.LENGTH_LONG).show();

        for (int i=0; i<2; i++) {
            checkModoOp();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            chechRecipiente();
        }
            //Chama a próxima tela
            Intent intent = new Intent(getActivity(), actMain.class);
            startActivity(intent);
            getActivity().finish();
            getActivity().getFragmentManager().popBackStack();

        return view;
    }
    public void checkModoOp(){
        mqttClientSub(TOPIC_RESPONSE);
        checkInfo();

        if(Objects.equals(check_modoOp, "M")) {
            if (Objects.equals(espResponse, "OK")) {
                b_response = true;
            } else {
                mqttClientPub(TOPIC_MODOOP, check_modoOp);
            }
        } if(Objects.equals(check_modoOp, "A")){
                b_response = true;
        }

    }

    public void chechRecipiente(){
        mqttClientSub(TOPIC_RESPONSE);
        checkInfo();

        if (Objects.equals(espResponse, "RO")) {
            b_response = true;

        } else {
            switch (check_recipiente) {
                case "0":
                    mqttClientPub(TOPIC_RECIPIENTE, "0");
                    break;
                case "1":
                    mqttClientPub(TOPIC_RECIPIENTE, "1");
                    break;
                case "2":
                    mqttClientPub(TOPIC_RECIPIENTE, "2");
                    break;
            }
        }
    }

    //Checa a informação do usuário
    public void checkInfo(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        check_modoOp   = sharedPref.getString("key_modoOp", "");
        check_recipiente = sharedPref.getString("key_recipiente", "");

    }

    public void okResponse(){
        //Chama a próxima tela
        Intent intent = new Intent(getActivity(), actMain.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void mqttClientPub(String topic, String message){
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

    public void mqttClientSub(String topic){
        /*=========> MQTT <========*/
        try {
            MqttClient client = new MqttClient("tcp://test.mosca.io:1883", "APP", new MemoryPersistence());
            client.setCallback(this);

            client.connect(new MqttConnectOptions());
            client.subscribe(topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        espResponse = new String(message.getPayload());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
