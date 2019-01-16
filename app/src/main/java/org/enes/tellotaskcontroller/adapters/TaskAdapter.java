package org.enes.tellotaskcontroller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapterViewHolder taskAdapterViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        if(dataSource != null) {
            return dataSource.size();
        }
        return 0;
    }

    public class TaskAdapterViewHolder extends RecyclerView.ViewHolder {

        public TaskAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
