package org.enes.tellotaskcontroller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.enes.tellotaskcontroller.R;
import org.enes.tellotaskcontroller.objects.CreateTaskObject;

import java.util.List;

public class NewTaskAdapter extends RecyclerView.Adapter<NewTaskAdapter.NewTaskViewHolder> {

    public interface NewTaskPressedListener {

        void onPress(NewTaskAdapter who, int position);

    }

    private NewTaskPressedListener newTaskPressedListener;

    public NewTaskPressedListener getNewTaskPressedListener() {
        return newTaskPressedListener;
    }

    public void setNewTaskPressedListener(NewTaskPressedListener new_) {
        this.newTaskPressedListener = new_;
    }

    private List<CreateTaskObject> dataSource;

    private Context context;

    public NewTaskAdapter(Context context, List<CreateTaskObject> createTaskObjects) {
        super();
        this.context = context;
        dataSource = createTaskObjects;
    }

    @NonNull
    @Override
    public NewTaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.layout_new_task,viewGroup,false);
        return new NewTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewTaskViewHolder newTaskViewHolder, int i) {
        CreateTaskObject createTaskObject = dataSource.get(i);
        newTaskViewHolder.tv_command_name.setText(newTaskViewHolder.tv_command_name.getContext().
                getString(R.string.command) +":" + createTaskObject.command_name);
        newTaskViewHolder.tv_desc.setText(newTaskViewHolder.tv_command_name.getContext().
                getString(R.string.description) +":" + createTaskObject.desc);
    }

    @Override
    public int getItemCount() {
        if(dataSource != null) {
            return dataSource.size();
        }
        return 0;
    }

    public class NewTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_command_name;

        public TextView tv_desc;

        public NewTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_command_name = itemView.findViewById(R.id.tv_command_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(newTaskPressedListener != null) {
                newTaskPressedListener.onPress(NewTaskAdapter.this,getLayoutPosition());
            }
        }
    }

}
