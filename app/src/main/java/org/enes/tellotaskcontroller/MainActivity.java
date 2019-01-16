package org.enes.tellotaskcontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import org.enes.tellotaskcontroller.adapters.NewTaskAdapter;
import org.enes.tellotaskcontroller.adapters.StatusAdapter;
import org.enes.tellotaskcontroller.adapters.TaskAdapter;
import org.enes.tellotaskcontroller.command.Command;
import org.enes.tellotaskcontroller.objects.CreateTaskObject;
import org.enes.tellotaskcontroller.objects.Task;
import org.enes.tellotaskcontroller.threads.StatusReceiveThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewTaskAdapter.NewTaskPressedListener,
        DialogInterface.OnClickListener {

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBroadcastReceivers();
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Command.send_command_to_tello_without_response();
            }
        }).start();
        registerBroadcastReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterBroadcastReceivers();
    }

    private void initBroadcastReceivers() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(StatusReceiveThread.status_broadcast_action_name)) {
                    HashMap<String,String> status_data =
                            (HashMap<String, String>) intent.getSerializableExtra(
                                    StatusReceiveThread.status_broadcast_extra_name);
                    updateTelloStatusToUI(status_data);
                }else if(intent.getAction().equals(MyApplication.ADD_TASK_LIST)) {
                    addTaskToUI();
                }
            }
        };
    }

    private void registerBroadcastReceivers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StatusReceiveThread.status_broadcast_action_name);
        intentFilter.addAction(MyApplication.ADD_TASK_LIST);
        registerReceiver(broadcastReceiver,intentFilter);

    }

    private void unregisterBroadcastReceivers() {
        unregisterReceiver(broadcastReceiver);
    }

    private RecyclerView recyclerViewStatus;

    private HashMap<String,String> statusHashMap;

    private StatusAdapter statusAdapter;

    private LinearLayout ll_no_connection;

    private RecyclerView recyclerViewTasks;

    private List<Task> taskList;

    private TaskAdapter taskAdapter;

    private RecyclerView recyclerViewNewTasks;

    private NewTaskAdapter newTaskAdapter;

    private void initView() {
        // set first adapter
        recyclerViewStatus = findViewById(R.id.recyclerViewStatus);
        recyclerViewStatus.setLayoutManager(new GridLayoutManager(recyclerViewStatus.getContext(),4));
        statusHashMap = new HashMap<>();
        statusAdapter = new StatusAdapter(this,statusHashMap);
        recyclerViewStatus.setAdapter(statusAdapter);
        // set second adapter
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(recyclerViewTasks.getContext()));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this,taskList);
        recyclerViewTasks.setAdapter(taskAdapter);
        // set
        recyclerViewNewTasks = findViewById(R.id.recyclerViewNewTasks);
        recyclerViewNewTasks.setLayoutManager(new LinearLayoutManager(recyclerViewNewTasks.getContext()));
        newTaskAdapter = new NewTaskAdapter(this,MyApplication.getInstance().getCommandList());
        recyclerViewNewTasks.setAdapter(newTaskAdapter);
        newTaskAdapter.setNewTaskPressedListener(this);

        ll_no_connection = findViewById(R.id.ll_no_connection);
//        ll_no_connection.setOnClickListener(null);
    }

    /**
     *
     * @param data
     */
    private void updateTelloStatusToUI(HashMap<String,String> data) {
        statusHashMap.clear();
        statusHashMap.putAll(data);
        statusAdapter.notifyDataSetChanged();

        updateUICommon();
    }

    private void addTaskToUI() {
        taskAdapter.notifyDataSetChanged();
        updateUICommon();
    }

    private void updateUICommon() {
        if(ll_no_connection.getVisibility() == View.VISIBLE) {
            ll_no_connection.setVisibility(View.GONE);
        }
    }

    private CreateTaskObject now_pressed;

    @Override
    public void onPress(NewTaskAdapter who, int position) {
        CreateTaskObject createTaskObject = MyApplication.getInstance().getCommandList().get(position);
        now_pressed = createTaskObject;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(createTaskObject.command_name);
        builder.setMessage(createTaskObject.desc);
        if(createTaskObject.ext) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_number_picker,null);
            NumberPicker numberPicker = view.findViewById(R.id.num);
            numberPicker.setMinValue(createTaskObject.min);
            numberPicker.setMaxValue(createTaskObject.max);
            builder.setView(view);
        }
        builder.setPositiveButton(getString(R.string.add),this);
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(dialog instanceof AlertDialog) {
            if(which == DialogInterface.BUTTON_POSITIVE && now_pressed != null) {
                Task task = new Task();
                task.command_name = now_pressed.command_name;
                task.create_time = System.currentTimeMillis();
                if(now_pressed.ext) {
                    NumberPicker numberPicker = ((AlertDialog) dialog).findViewById(R.id.num);
                    task.extra_value = String.valueOf(numberPicker.getValue());
                    task.need_extra_value = true;
                }
                now_pressed = null;
                MyApplication.addTask(task);
            }
        }
    }
}
