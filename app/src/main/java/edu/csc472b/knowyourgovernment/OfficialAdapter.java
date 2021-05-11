package edu.csc472b.knowyourgovernment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

    private List<Official> officialList;
    private MainActivity mainActivity;

    OfficialAdapter(List<Official> oList, MainActivity ma) {
        officialList = oList;
        mainActivity = ma;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_row, parent, false);

        itemView.setOnClickListener(mainActivity);
        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {

        Official official = officialList.get(position);

        holder.officialParty.setText(String.format("(%s)", official.getOfficialParty()));
        holder.officialOffice.setText(official.getOfficialOffice());
        holder.officialName.setText(official.getOfficialName());

    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
