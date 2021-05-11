package edu.csc472b.knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    TextView officialName;
    TextView officialOffice;
    TextView officialParty;


    public OfficialViewHolder(@NonNull View itemView) {
        super(itemView);

        officialName =itemView.findViewById(R.id.officialName);
        officialOffice = itemView.findViewById(R.id.officialOffice);
        officialParty = itemView.findViewById(R.id.officialParty);

    }
}
