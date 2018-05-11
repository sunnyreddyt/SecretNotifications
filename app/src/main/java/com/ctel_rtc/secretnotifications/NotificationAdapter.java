package com.ctel_rtc.secretnotifications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ctel-cpu-84 on 3/30/2017.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    ArrayList<SavedModel> savedModelArrayList = new ArrayList<SavedModel>();

    public NotificationAdapter(Context context,
                               ArrayList<SavedModel> savedModelArrayList) {
        this.context = context;
        this.savedModelArrayList = savedModelArrayList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;
        TextView titleTextView, textTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            titleTextView = (TextView) mView.findViewById(R.id.titleTextView);
            textTextView = (TextView) mView.findViewById(R.id.textTextView);

        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.titleTextView.setText(savedModelArrayList.get(position).getTitle());
        holder.textTextView.setText(savedModelArrayList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return savedModelArrayList.size();
    }
}
