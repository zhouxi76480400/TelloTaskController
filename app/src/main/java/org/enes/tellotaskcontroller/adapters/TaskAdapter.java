package org.enes.tellotaskcontroller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.enes.tellotaskcontroller.R;
import org.enes.tellotaskcontroller.objects.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskAdapterViewHolder> {

    private List<Task> dataSource;

    public TaskAdapter(Context context, List<Task> taskList) {
        super();
        dataSource = taskList;
    }

    @NonNull
    @Override
    public TaskAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.layout_task,viewGroup,false);
        return new TaskAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapterViewHolder taskAdapterViewHolder, int i) {
        Task task = dataSource.get(i);
        taskAdapterViewHolder.tv_command_name.setText(taskAdapterViewHolder.tv_command_name.getContext().
                getString(R.string.command) +":" + task.command_name);
        String ext_cmd = null;
        if(task.need_extra_value) {
            ext_cmd = task.extra_value;
        }else {
            ext_cmd = taskAdapterViewHolder.itemView.getContext().getString(R.string.empty);
        }
        taskAdapterViewHolder.tv_ext.setText(taskAdapterViewHolder.tv_ext.getContext().
                getString(R.string.extra_command) +":" + ext_cmd);
        String status = null;
        if(i == 0) {
            status = taskAdapterViewHolder.itemView.getContext().getString(R.string.perform);
        }else {
            status = taskAdapterViewHolder.itemView.getContext().getString(R.string.wait);
        }
        taskAdapterViewHolder.tv_status.setText(
                taskAdapterViewHolder.tv_status.getContext().getString(R.string.status) + ":" + status);




    }

    @Override
    public int getItemCount() {
        if(dataSource != null) {
            return dataSource.size();
        }
        return 0;
    }

    public class TaskAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_command_name;

        public TextView tv_status;

        public TextView tv_ext;

        public TaskAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_command_name = itemView.findViewById(R.id.tv_command_name);
            tv_ext = itemView.findViewById(R.id.tv_ext);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
