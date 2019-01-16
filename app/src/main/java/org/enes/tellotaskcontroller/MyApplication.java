package org.enes.tellotaskcontroller;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.enes.tellotaskcontroller.command.Command;
import org.enes.tellotaskcontroller.objects.CreateTaskObject;
import org.enes.tellotaskcontroller.objects.Task;
import org.enes.tellotaskcontroller.threads.CommandReceiveThread;
import org.enes.tellotaskcontroller.threads.StatusReceiveThread;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    public static final String ADD_TASK_LIST = "ADD_TASK";

    private List<CreateTaskObject> command_list;

    public List<CreateTaskObject> getCommandList() {
        return command_list;
    }

    private static List<Task> taskList;

    synchronized static public List<Task> taskList() {
        if(taskList == null)
            taskList = new ArrayList<>();
        return taskList;
    }

    synchronized static public void addTask(Task task) {
        if(task != null) {
            taskList().add(task);
            Intent intent = new Intent();
            intent.setAction(ADD_TASK_LIST);
            getInstance().sendBroadcast(intent);
        }
    }

    synchronized static public void removeTask(int pos) {
        try {
            taskList().remove(pos);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MyApplication myApplication;

    private CommandReceiveThread commandReceiveThread;

    public CommandReceiveThread getCommandReceiveThread() {
        return commandReceiveThread;
    }

    private StatusReceiveThread statusReceiveThread;

    public StatusReceiveThread getStatusReceiveThread() {
        return statusReceiveThread;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerWifiStatusChangeListener();
        createList();
        myApplication = this;
        (commandReceiveThread = new CommandReceiveThread()).start();
        (statusReceiveThread = new StatusReceiveThread()).start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void registerWifiStatusChangeListener() {
        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                    WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    if(wifiInfo != null) {
                        if(wifiInfo.getSSID() != null) {
                            // send a command message
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Command.send_command_to_tello_without_response();
                                }
                            }).start();
                        }
                    }
                }
            }
        };
        registerReceiver(broadcastReceiver,intentFilter);
    }

    private void createList() {
        command_list = new ArrayList<>();
        String json_str = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream inputStream = getAssets().open("commands.json");
            byte [] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer,0,len);
            }
            json_str = byteArrayOutputStream.toString();
            byteArrayOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(json_str != null) {
            try {
                JSONArray jsonArray = new JSONArray(json_str);
                for(int i = 0 ; i < jsonArray.length() ; i ++ ) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    CreateTaskObject createTaskObject = new CreateTaskObject();
                    String cmd = jsonObject.getString("cmd");
                    String desc = jsonObject.getString("desc");
                    createTaskObject.desc =desc;
                    createTaskObject.command_name = cmd;
                    if(jsonObject.has("ext")) {
                        boolean ext = jsonObject.getBoolean("ext");
                        int min = jsonObject.getInt("min");
                        int max = jsonObject.getInt("max");
                        createTaskObject.max = max;
                        createTaskObject.min = min;
                        createTaskObject.ext = ext;
                    }
                    command_list.add(createTaskObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
