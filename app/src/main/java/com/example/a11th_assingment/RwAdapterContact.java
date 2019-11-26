package com.example.a11th_assingment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a11th_assingment.Room.EntityContact;

import java.util.List;

public class RwAdapterContact extends RecyclerView.Adapter<RwAdapterContact.itemHolder> {

    private final LayoutInflater mInflater;
    private List<EntityContact> mEvents;

    RwAdapterContact(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.rw_listitem, parent, false);
        return new itemHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull itemHolder holder, int position) {
        if (mEvents != null) {
            EntityContact current = mEvents.get(position);
            holder.nameView.setText(current.name);
        } else {
            holder.nameView.setText("No EntityContact");
        }
    }

    List<EntityContact> getItems() {
        return mEvents;
    }

    void setItems(List<EntityContact> events) {
        mEvents = events;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mEvents != null)
            return mEvents.size();
        else return 0;
    }

    class itemHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;


        private itemHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.contactName);
        }
    }
}