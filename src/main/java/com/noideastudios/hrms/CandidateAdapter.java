package com.noideastudios.hrms;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateAdapter extends ArrayAdapter<Candidate> {

    CandidateAdapter(@NonNull Context context, int resource, ArrayList<Candidate> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = ((Activity) getContext()).
                    getLayoutInflater().inflate(R.layout.candidate_tile, parent, false);

        TextView name = convertView.findViewById(R.id.nameTile);
        TextView phone = convertView.findViewById(R.id.phoneTile);
        TextView status = convertView.findViewById(R.id.statusTile);
        TextView pos = convertView.findViewById(R.id.posTile);
        CircleImageView userImage = convertView.findViewById(R.id.photoTile);

        Candidate candidate = getItem(position);

        if (candidate != null) {
            name.setText(candidate.getName());
            phone.setText(candidate.getPhone());
            status.setText(candidate.getStatus());
            pos.setText(candidate.getPosition());
            if (candidate.getPhotoURI() == null)
                Picasso.with(getContext())
                        .load(R.drawable.employee_tie)
                        .into(userImage);
            else
                Picasso.with(getContext())
                        .load(candidate.getPhotoURI())
                        .error(R.drawable.employee_tie)
                        .into(userImage);
        }
        return convertView;
    }
}
