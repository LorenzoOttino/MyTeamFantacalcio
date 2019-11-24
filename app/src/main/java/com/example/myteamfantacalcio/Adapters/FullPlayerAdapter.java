package com.example.myteamfantacalcio.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myteamfantacalcio.Network.Player;
import com.example.myteamfantacalcio.R;

import java.util.ArrayList;
import java.util.List;

public class FullPlayerAdapter extends RecyclerView.Adapter<FullPlayerAdapter.ViewHolder> {
    private List<Player> players;
    final private OnListItemClickListener itemClickListener;

    public FullPlayerAdapter(OnListItemClickListener onListItemClickListener){
        this.itemClickListener = onListItemClickListener;
        players = new ArrayList<>();
    }

    public void setPlayers(List<Player> players){
        this.players = players;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.full_player_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(players.get(position) != null)
            holder.playerName.setText(players.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView playerName;
        TextView playerMark;
        Switch isStarter;

        ViewHolder(View itemView){
            super(itemView);
            playerName = itemView.findViewById(R.id.playerNameField);
            playerMark = itemView.findViewById(R.id.playerMarkField);
            isStarter = itemView.findViewById(R.id.isStarterSwitch);
            isStarter.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onListItemClick(getAdapterPosition());
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
