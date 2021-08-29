package com.example.acye1_p1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    // Constantes para la conexion BT
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;

    private ImageButton up, down, left, right, stop, speed;
    private Switch on_off;
    private BluetoothSocket btSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializamos botones
        up = findViewById(R.id.btnUp);
        down = findViewById(R.id.btnDown);
        left = findViewById(R.id.btnLeft);
        right = findViewById(R.id.btnRight);
        on_off = findViewById(R.id.switch1);
        on_off.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch1:
                if (b)
                    connect();
                else
                    disconnecct();
        }
    }

//    private void showMsg(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//    }

    private void connect() {

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            System.out.println("El dispositivo no soporta Bluetooth");
        }

        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Creamos adaptador puente entre mobil y hc05
        BluetoothDevice hc05 = btAdapter.getRemoteDevice("E4:02:9B:84:EA:3F");
        System.out.println(hc05.getName());

        // Conexion entre telefono y HC05
        int counter = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                btSocket.connect();
                System.out.println("La conexion es: " + btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        } while (!btSocket.isConnected() && counter < 3);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ARRIBA");
            }
        });
    }

    public void disconnecct() {

        BluetoothSocket finalBtSocket = btSocket;
        closeConn(finalBtSocket);
    }

    public void closeConn (BluetoothSocket btSocket) {
        // Conexion cerrada
        try {
            btSocket.close();
            System.out.println("La conexion es: " + btSocket.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}