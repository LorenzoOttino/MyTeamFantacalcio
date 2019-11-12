package com.example.myteamfantacalcio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CupsAdapter extends RecyclerView.Adapter<CupsAdapter.ViewHolder>{
    private List<Competition> allCompetitions;
    final private OnListItemClickListener mOnListItemClickListener;

    CupsAdapter(OnListItemClickListener mOnListItemClickListener){
        allCompetitions = new ArrayList<>();
        this.mOnListItemClickListener = mOnListItemClickListener;
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public void setCompetitions(List<Competition> competitions){
        this.allCompetitions = competitions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CupsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.competition_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.compName.setText(allCompetitions.get(position).getName());
        holder.compYear.setText(allCompetitions.get(position).getYear());
        holder.compPos.setText(allCompetitions.get(position).getPosition());
    }

    @Override
    public int getItemCount() {
        return allCompetitions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView compName;
        TextView compYear;
        TextView compPos;

        ViewHolder(View view){
            super(view);
            compName = view.findViewById(R.id.textCompetitionName);
            compYear = view.findViewById(R.id.textCompetitionYear);
            compPos = view.findViewById(R.id.textCompetitionPos);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}