package com.noideastudios.hrms;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class SelectedCandidatesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    DBhandler dBhandler;
    Button sortBy, deleteS;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ListView selectedListView;
    ArrayList<Candidate> arrayList;
    CandidateAdapter candidateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_candidates);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Selected Candidates");
        }
        setUpList();
    }

    private void setUpList() {
        dBhandler = new DBhandler(this, null, null, 1);
        selectedListView = findViewById(R.id.selectedCandidateList);
        arrayList = dBhandler.returnCandidates(1);
        candidateAdapter = new CandidateAdapter(
                this, R.layout.candidate_tile, arrayList);
        selectedListView.setAdapter(candidateAdapter);
        selectedListView.setOnItemClickListener(this);

        sortBy = findViewById(R.id.sortbySelectedbutton);
        sortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpSortBy();
            }
        });

        deleteS = findViewById(R.id.deleteS);
        deleteS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dBhandler.deleteSelected();
                candidateAdapter.clear();
            }
        });
    }

    private void setUpSortBy() {
        final View sortDialog = getLayoutInflater().inflate(R.layout.sortby_selected, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(SelectedCandidatesActivity.this);
        builder.setView(sortDialog);

        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        radioGroup = sortDialog.findViewById(R.id.sortByRG);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = sortDialog.findViewById(checkedId);
                Toast.makeText(SelectedCandidatesActivity.this,
                        "Sorting by " + radioButton.getText().toString(),
                        Toast.LENGTH_LONG).show();

                selectedListView = findViewById(R.id.selectedCandidateList);
                switch (radioButton.getText().toString()) {
                    case "Alphabetically":
                        arrayList = dBhandler.returnCandidates(201);
                        break;
                    case "Position Applied For":
                        arrayList = dBhandler.returnCandidates(301);
                        break;
                    case "Time":
                    default:
                        arrayList = dBhandler.returnCandidates(1);
                        break;

                }
                candidateAdapter = new CandidateAdapter(
                        SelectedCandidatesActivity.this, R.layout.candidate_tile, arrayList);
                selectedListView.setAdapter(candidateAdapter);

                dialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(SelectedCandidatesActivity.this, CandidateDetailsActivity.class);
        intent.putExtra("employee_id", arrayList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpList();
    }
}
