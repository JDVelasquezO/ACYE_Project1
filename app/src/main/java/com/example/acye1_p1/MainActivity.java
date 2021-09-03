package com.example.acye1_p1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    // Constantes para la conexion BT
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;
    ReceiveData receiveData;

    private ImageButton up, down, left, right, stop, speed, lessSpedd, play;
    private Switch on_off;
    private BluetoothSocket btSocket = null;
    private TextView txtMeta;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializamos botones
        up = findViewById(R.id.btnUp);
        down = findViewById(R.id.btnDown);
        left = findViewById(R.id.btnLeft);
        right = findViewById(R.id.btnRight);
        stop = findViewById(R.id.btnStop);
        play = findViewById(R.id.btnPlay);
        speed = findViewById(R.id.btnSpeed);
        lessSpedd = findViewById(R.id.btnLessSpeed);
        txtMeta = findViewById(R.id.txtMeta);

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

        // Creamos adaptador puente entre mobil y BT-PC
        BluetoothDevice hc05 = btAdapter.getRemoteDevice("E4:02:9B:84:EA:3F");
        System.out.println("Nombre: " + hc05.getName());

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


        BluetoothSocket finalBtSocket1 = btSocket;
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendChar(finalBtSocket1, 'U');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendChar(finalBtSocket1, 'D');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        BluetoothSocket finalBtSocket2 = btSocket;
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendChar(finalBtSocket2, 'L');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendChar(finalBtSocket2, 'R');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendChar(finalBtSocket1, '0');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendChar(finalBtSocket1, '1');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendChar(finalBtSocket1, 'S');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        lessSpedd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendChar(finalBtSocket1, 'B');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            receiveData = new ReceiveData(btSocket);
            receiveData.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public void sendChar (BluetoothSocket btSocket, char character) throws IOException {
        txtMeta.setVisibility(View.INVISIBLE);
        OutputStream outputStream = btSocket.getOutputStream();
        System.out.println(character);
        outputStream.write(character);
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

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what)
            {
                case STATE_LISTENING:
                    //status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    //status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    //status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    //status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) message.obj;
                    String tempMsg=new String(readBuff,0,message.arg1);
                    txtMeta.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
    });

    class ReceiveData extends Thread {
        private final BluetoothSocket socket;
        private final InputStream is;

        ReceiveData(BluetoothSocket socket) throws IOException {
            this.socket = socket;
            InputStream tempIn = null;
            tempIn = socket.getInputStream();

            is = tempIn;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = is.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}