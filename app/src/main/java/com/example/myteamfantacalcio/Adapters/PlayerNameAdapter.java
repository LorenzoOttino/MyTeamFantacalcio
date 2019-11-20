package com.example.myteamfantacalcio.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myteamfantacalcio.R;

import java.util.ArrayList;

public class PlayerNameAdapter extends RecyclerView.Adapter<PlayerNameAdapter.ViewHolder> {
    private ArrayList<String> playerNames;
    final private OnListItemClickListener mOnListItemClickListener;

    public PlayerNameAdapter(ArrayList<String> playerNames, OnListItemClickListener listener){
        mOnListItemClickListener = listener;
        this.playerNames = playerNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.player_name_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(playerNames.get(position));

    }

    @Override
    public int getItemCount() {
        return playerNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        Button button;

        ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.player_name);
            button = view.findViewById(R.id.player_name_edit);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition(), textView.getText().toString());
        }
    }

    public interface OnListItemClickListener{
        void onListItemClick(int clickedItemIndex, String message);
    }
}
