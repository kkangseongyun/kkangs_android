package com.example.user.part10_27;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab27_3Activity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    EditText editText;
    ImageView sendBtn;

    ProgressDialog progressDialog;

    ArrayList<ChatMessage> list;
    MyAdapter adapter;

    Handler writeHandler;

    AlertDialog serverDialog;

    boolean isConnected;
    boolean isDialogOpened;

    boolean serverFlag = true;
    boolean isConnectMessage = false;
    boolean readFlag = true;

    ReadThread readThread;
    WriteThread writeThread;

    //add~~~~~~~~~~~~~
    BluetoothAdapter bluetoothAdapter;
    UUID MY_UUID;
    BluetoothDevice device;

    BluetoothSocket socket=null;
    BluetoothServerSocket serverSocket;


    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab27_3);

        listView = (ListView) findViewById(R.id.lab3_list);
        editText = (EditText) findViewById(R.id.lab3_send_text);
        sendBtn = (ImageView) findViewById(R.id.lab3_send_btn);

        sendBtn.setOnClickListener(this);

        list = new ArrayList<ChatMessage>();
        adapter = new MyAdapter(this, R.layout.chat_item, list);
        listView.setAdapter(adapter);

        sendBtn.setEnabled(false);
        editText.setEnabled(false);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //enable...
        bluetoothEnable();

        // bluetooth server 로 동작할때 명시할 uuid 값.. 아무 id 값이나 적용.. uuid 생성기를 이용할수 있다.
        MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        //server thread start
        ServerThread serverThread=new ServerThread();
        serverThread.start();

    }

    private void addMessage(String who, String msg) {
        Log.d("kkang", "addMessage...." + who + "..." + msg);
        ChatMessage vo = new ChatMessage();
        vo.who = who;
        vo.msg = msg;
        list.add(vo);
        adapter.notifyDataSetChanged();
        listView.setSelection(list.size() - 1);
    }

    Handler mainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==10){
                sendBtn.setEnabled(true);
                editText.setEnabled(true);
            }else if(msg.what==20){
                //connection fail~~~
                sendBtn.setEnabled(false);
                editText.setEnabled(false);
            }else if(msg.what==30){
                AlertDialog.Builder builder = new AlertDialog.Builder(Lab27_3Activity.this);
                builder.setMessage("연결 요청이 들어왔습니다. \n 채팅을 허용하시겠습니까?");
                builder.setPositiveButton("허용", dialogListener);
                builder.setNegativeButton("거부", dialogListener);
                serverDialog = builder.create();
                serverDialog.show();
            } else if(msg.what==40){
                isConnected=true;
                editText.setEnabled(true);
                sendBtn.setEnabled(true);
                progressDialog.dismiss();
                progressDialog.cancel();
            } else if(msg.what==100){
                //message read....
                addMessage("you", (String)msg.obj);
            }else if(msg.what==200){
                //message write...
                addMessage("me", (String)msg.obj);
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lab3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_lab3_bluetooth) {
            //add~~~~~~~~~~~~~~
            final Set<BluetoothDevice> devices=bluetoothAdapter.getBondedDevices();
            if(devices.size()>0){
                final String[] deviceArray=new String[devices.size()];
                Iterator<BluetoothDevice> iter=devices.iterator();
                int i=0;
                while (iter.hasNext()){
                    BluetoothDevice d=iter.next();
                    deviceArray[i]=d.getName();
                    i++;
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("device 선택");
                builder.setItems(deviceArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectDeivceStr=deviceArray[which];
                        for(BluetoothDevice device : devices){
                            if(device.getName().equals(selectDeivceStr)){
                                Lab27_3Activity.this.device=device;
                                showProgressDialgo(device.getName());
                                ClientThread clientThread=new ClientThread();
                                clientThread.start();
                            }
                        }
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialgo(String name){
        progressDialog=ProgressDialog.show(this, "Waiting...", name+" 에 연결을 시도합니다.");
    }

    @Override
    public void onClick(View v) {
        if(isConnected){
            if (!editText.getText().toString().trim().equals("")) {
                Message msg=new Message();
                msg.obj=editText.getText().toString();
                writeHandler.sendMessage(msg);
                editText.setText("");
            }
        }
    }

    DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(dialog==serverDialog && which==DialogInterface.BUTTON_POSITIVE){
                //허용이면 네트웍을 위한 thread 구동..
                if(readThread != null){
                    readFlag=false;
                }
                readFlag=true;
                readThread=new ReadThread(socket);
                readThread.start();
                if(writeThread != null){
                    writeHandler.getLooper().quit();
                }
                writeThread=new WriteThread(socket);
                writeThread.start();

                isConnected=true;
                isDialogOpened=false;

                //최초에 연결 되었다는 신호 보낸다.
                isConnectMessage=true;

                //버튼등 활성화...
                Message msg1=new Message();
                msg1.what=10;
                mainHandler.sendMessage(msg1);

            }else if(dialog==serverDialog && which==DialogInterface.BUTTON_NEGATIVE){
                try{
                    //거부 버튼을 누르면 serversocket을 close 시켜서 다시 accept 대기하게 설정
                    serverSocket.close();
                    isConnected=false;
                    isDialogOpened=false;
                }catch (Exception e){}

            }
        }
    };

    private void bluetoothEnable() {
        //add~~~~~~~~~~~
        if(!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
        }

    }

    class ServerThread extends Thread {

        @Override
        public void run() {
            while (serverFlag) {
                if(!isConnected && !isDialogOpened) {
                    if (bluetoothAdapter.isEnabled()) {
                        try {
                            //add~~~~~~~~~~

                            serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord("kkang_test", MY_UUID);
                            socket=serverSocket.accept();

                            Message message=new Message();
                            message.what=30;
                            mainHandler.sendMessage(message);

                            isDialogOpened = true;


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("kkang", e.getMessage());
                        }
                        if (socket != null) {
                            try {
                                serverSocket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }else {
                    SystemClock.sleep(1000);
                }

            }

        }
    }

    class ClientThread extends Thread {
        @Override
        public void run() {
            try {
                //add~~~~~~~~~~~~~
                bluetoothAdapter.cancelDiscovery();
                socket=device.createRfcommSocketToServiceRecord(MY_UUID);
                socket.connect();

                if(readThread != null){
                    readFlag=false;
                }
                readThread=new ReadThread(socket);
                readThread.start();
                if(writeThread != null){
                    writeHandler.getLooper().quit();
                }
                writeThread=new WriteThread(socket);
                writeThread.start();

                isConnected=true;
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }



    class ReadThread extends Thread {
        BluetoothSocket socket;
        BufferedInputStream in = null;
        public ReadThread(BluetoothSocket socket){
            this.socket=socket;
            try{
                in = new BufferedInputStream(socket.getInputStream());
            }catch (Exception e){}
        }

        @Override
        public void run() {
            while (readFlag){
                try{
                    byte[] buffer = new byte[1024];
                    int bytes;
                    bytes = in.read(buffer);
                    String readStr = new String(buffer, 0, bytes);
                    if(readStr.equals("connect..")){
                        Message msg1=new Message();
                        msg1.what=40;
                        mainHandler.sendMessage(msg1);
                    }
                    if(readStr.equals("destory..")){
                        isConnected=false;
                        if(serverSocket != null){
                            serverSocket.close();
                        }
                        if(socket != null){
                            socket.close();
                        }
                        readFlag=false;
                        writeHandler.getLooper().quit();
                    }

                    Message message=new Message();
                    message.what=100;
                    message.obj=readStr;
                    mainHandler.sendMessage(message);
                }catch (Exception e){
                    readFlag = false;
                    isConnected = false;
                }
            }
            Message msg=new Message();
            msg.what=20;
            mainHandler.sendMessage(msg);
        }
    }

    class WriteThread extends Thread {
        BluetoothSocket socket=null;
        OutputStream out = null;
        public WriteThread(BluetoothSocket socket){
            this.socket=socket;
            try{
                out=socket.getOutputStream();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            if(isConnectMessage){
                try{
                    out.write(("connect..").getBytes());
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
                isConnectMessage=false;
            }
            Looper.prepare();
            writeHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    try {
                        out.write(((String) msg.obj).getBytes());
                        out.flush();

                        Message message=new Message();
                        message.what=200;
                        message.obj=msg.obj;
                        mainHandler.sendMessage(message);
                    }catch (Exception e){
                        e.printStackTrace();
                        isConnected = false;
                        writeHandler.getLooper().quit();
                        try {
                            readFlag = false;
                        } catch (Exception e1) {

                        }

                    }
                }
            };
            Looper.loop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Message message=new Message();
        message.obj="destory..";
        writeHandler.sendMessage(message);
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (Exception e){

        }
    }
}

class ChatMessage {
    String who;
    String msg;
}

class MyAdapter extends ArrayAdapter<ChatMessage> {
    ArrayList<ChatMessage> list;
    int resId;
    Context context;

    public MyAdapter(Context context, int resId, ArrayList<ChatMessage> list) {
        super(context, resId, list);
        this.context = context;
        this.resId = resId;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resId, null);


        TextView msgView = (TextView) convertView.findViewById(R.id.mission2_item_msg);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) msgView
                .getLayoutParams();

        ChatMessage msg = list.get(position);
        if (msg.who.equals("me")) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
                    RelativeLayout.TRUE);
            msgView.setTextColor(Color.WHITE);
            msgView.setBackgroundResource(R.drawable.bg_right);
        } else if (msg.who.equals("you")) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                    RelativeLayout.TRUE);
            msgView.setBackgroundResource(R.drawable.bg_left);
        }
        msgView.setText(msg.msg);

        return convertView;

    }
}
