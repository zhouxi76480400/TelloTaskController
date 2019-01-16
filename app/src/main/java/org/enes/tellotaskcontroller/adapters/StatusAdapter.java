package org.enes.tellotaskcontroller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.enes.tellotaskcontroller.R;

import java.util.HashMap;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusAdapterViewHolder> {

    private Context context;

    private HashMap<String,String> dataSource;

    public StatusAdapter(Context context, HashMap<String, String> statusHashMap) {
        super();
        this.context = context;
        dataSource = statusHashMap;
    }

    @NonNull
    @Override
    public StatusAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.layout_status,viewGroup,false);
        return new StatusAdapterViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAdapterViewHolder statusAdapterViewHolder, int i) {
        String keyAndValue = dataSource.get(String.valueOf(i));
        statusAdapterViewHolder.text.setText(keyAndValue);
    }

    @Override
    public int getItemCount() {
        if(dataSource != null) {
            return dataSource.size();
        }
        return 0;
    }

    public class StatusAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView text;

        public StatusAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
