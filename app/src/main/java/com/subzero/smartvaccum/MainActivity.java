package com.subzero.smartvaccum;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private final String DEVICE_ADDRESS = "00:19:10:09:33:BA"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Creates a socket to handle the outgoing connection PORT_UUID

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    boolean isPrinted = true;
    public static boolean a = false;

    Button forward_btn, forward_left_btn, forward_right_btn, reverse_btn, bluetooth_connect_btn,btn_test,btn_new;

    String command = "S"; //string variable that will store value to be transmitted to the bluetooth module

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declaration of button variables
        forward_btn = (Button) findViewById(R.id.forward_btn);
        forward_left_btn = (Button) findViewById(R.id.forward_left_btn);
        forward_right_btn = (Button) findViewById(R.id.forward_right_btn);
        reverse_btn = (Button) findViewById(R.id.reverse_btn);
        bluetooth_connect_btn = (Button) findViewById(R.id.bluetooth_connect_btn);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (isPrinted) {
                    try {
                        try
                        {
                            if(a){
                                outputStream.write(command.getBytes());
                                //transmits the value of command to the bluetooth module
                                System.out.println(command);
                            }

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        new Thread(runnable).start();

        //OnTouchListener code for the forward button (button long press)
        btn_new.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                new Thread(runnable).start();
                int eventaction = event.getAction();
//                new Thread(runnable).start();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        command = "N";
//                        isPrinted = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        command = "S";
                        isPrinted = true;
                        break;
                }
                return false;
            }
        });

        forward_btn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                new Thread(runnable).start();
                int eventaction = event.getAction();
//                new Thread(runnable).start();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        command = "F";
//                        isPrinted = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        command = "S";
                        isPrinted = true;
                        break;
                }
                return false;
            }
        });

        btn_test.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                new Thread(runnable).start();
                int eventaction = event.getAction();
//                new Thread(runnable).start();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        command = "T";
//                        isPrinted = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        command = "S";
                        isPrinted = true;
                        break;
                }
                return false;
            }
        });
        //OnTouchListener code for the reverse button (button long press)

        reverse_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
//                new Thread(runnable).start();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        command = "B";
                        isPrinted = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        command = "S";
                        isPrinted = true;
                        break;
                }
                return false;
            }
        });

        //OnTouchListener code for the forward left button (button long press)

        forward_left_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
//                new Thread(runnable).start();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        command = "L";
                        isPrinted = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        command = "S";
                        isPrinted = true;
                        break;
                }
                return false;
            }
        });

        //OnTouchListener code for the forward right button (button long press)

        forward_right_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
//                new Thread(runnable).start();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        command = "R";
                        isPrinted = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        command = "S";
                        isPrinted = true;
                        break;
                }
                return false;
            }
        });

        //Button that connects the device to the bluetooth module when pressed
        bluetooth_connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BTinit())//true
                {
                    BTconnect();
                }

            }
        });

    }

    //Initializes bluetooth module
    public boolean BTinit()
    {
        boolean found = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    public boolean BTconnect()
    {
        boolean connected = true;

        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();



            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            connected = false;
        }

        if(connected)//True
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
                MainActivity.a= true;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return connected;
    }


    @Override
    protected void onStart()
    {
        super.onStart();
    }

}