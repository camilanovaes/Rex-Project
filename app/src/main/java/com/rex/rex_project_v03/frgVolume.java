package com.rex.rex_project_v03;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class frgVolume extends Fragment implements MqttCallback {

    /*VARIAVEIS PARA O VOLUME*/
    private TextView edt_volume;
    private String text_volume;
    private double double_volume = 0.0;
    private double double_altura = 0.0;
    private String topic_volume;


    /*VARIAVEIS PARA A ATURA*/
    private TextView edt_altura;
    private String text_altura;
    private String topic_altura;

    /*VARIAVEIS PARA A ALTURA MAX*/
    private String topic_altura_max;
    private String text_altura_max;
    private double double_altura_max;

    //VARIAVEIS PARA CALCULO
    private double dAtura;
    private double dLargura_Raio;
    private double dComprimento;
    private double dVolume_manual;

    public frgVolume() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg_volume, container, false);

        edt_volume = (TextView) view.findViewById(R.id.edt_volume);                 //Text View para o Volume
        edt_altura = (TextView) view.findViewById(R.id.edt_altura);                 //Text View para o Altura


        /*========> MQTT <========*/
        try{
            MqttClient client = new MqttClient("tcp://test.mosca.io:1883", "APP", new MemoryPersistence());
            client.setCallback(this);

            client.connect(new MqttConnectOptions());
            topic_volume = "/rexproject/esp/01/volume";
            client.subscribe(topic_volume, 2);

            topic_altura = "/rexproject/esp/01/altura";
            client.subscribe(topic_altura, 2);

            topic_altura_max = "/rexproject/esp/01/alturaMax";
            client.subscribe(topic_altura_max, 2);

            if(client.isConnected()){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Conectado ao servidor", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Não foi possivel conectar ao servidor!", Toast.LENGTH_LONG).show();
            }

        }catch (MqttException e){
            e.printStackTrace();
        }

        //update current time view after every 1 seconds
        final Handler handler =new Handler();

        final Runnable updateTask=new Runnable() {
            @Override
            public void run() {
                //if (checkModoOp()){
                    calcVolume();

                //} else {
                    edt_volume.setText(text_volume);
                    edt_altura.setText(text_altura);
                //}
                handler.postDelayed(this,1000);
            }
        };

        handler.postDelayed(updateTask,1000);

        return view;

    }
    public void calcVolume(){
        switch (checkRecipiente()){
            case "0":
                setMedidas();
                dVolume_manual = (dAtura - double_altura)* dLargura_Raio * dComprimento;
                dVolume_manual = dVolume_manual /1000;
                String volumePara = String.valueOf(dVolume_manual) + " L";
                edt_volume.setText(volumePara);
                edt_altura.setText(text_altura);

                break;

            case "1":
                setMedidas();
                Double volumeCili = (dAtura - double_altura)*3.14*(dLargura_Raio * dLargura_Raio);
                volumeCili = volumeCili/1000;

                String volume_cili = String.valueOf(volumeCili) + " L";
                edt_volume.setText(volume_cili);
                edt_altura.setText(text_altura);

                break;

            case "2":
                setMedidas();
                dVolume_manual = (dAtura - double_altura)* dLargura_Raio * dComprimento;
                dVolume_manual = dVolume_manual /1000;
                String volume_manual = String.valueOf(dVolume_manual) + " L";
                edt_volume.setText(volume_manual);
                edt_altura.setText(text_altura);

                break;
        }
    }
    public void setMedidas(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String sAltura          = sharedPref.getString("key_mAltura", "");
        dAtura = Float.parseFloat(sAltura);

        String sLargura_raio    = sharedPref.getString("key_mLartura_Raio", "");
        dLargura_Raio = Float.parseFloat(sLargura_raio);

        String sComprimento     = sharedPref.getString("key_mComprimento", "");
        dComprimento = Float.parseFloat(sComprimento);


    }



    //Checa a informação do usuário
    public String checkRecipiente(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String recipienteOption = sharedPref.getString("key_recipiente", "");

        return recipienteOption;
    }

    //Checa a informação do usuário
    public boolean checkModoOp(){
        boolean valid = false;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String check_modoOp   = sharedPref.getString("key_modoOp", "");
        if (Objects.equals(check_modoOp, "M")){
            valid = true;
        }

        return valid;
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (Objects.equals(topic, topic_volume)) {
            text_volume = new String(message.getPayload());
            double_volume = Float.parseFloat(text_volume);
            double_volume = double_volume /1000;
            text_volume = String.valueOf(double_volume) + " L";
        }

        if (Objects.equals(topic, topic_altura)) {
            text_altura = new String(message.getPayload());
            double_altura = Float.parseFloat(text_altura);
            text_altura = text_altura + " cm";
        }

        if (Objects.equals(topic, topic_altura_max)){
            text_altura_max = new String(message.getPayload());
            double_altura_max = Float.parseFloat(text_altura_max);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
