package com.noideastudios.hrms;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.noideastudios.hrms.Functions.BitmapToUri;
import static com.noideastudios.hrms.Functions.getResizedBitmap;

public class NewCandidateActivity extends AppCompatActivity {

    DBhandler dBhandler;
    TextView resume;
    TextInputLayout nameTIL, phoneTIL, posTIL;
    TextInputEditText cName, cPhone, cPos;
    Spinner cStatus;
    Button proceed, clear;
    CircleImageView userImage;
    Uri photoUri, resumeUri;
    String path, uriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_candidate);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Add New Candidate");
        }

        cStatus = findViewById(R.id.status);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item,
                getResources().getStringArray(R.array.statusSpinnner));
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        cStatus.setAdapter(arrayAdapter);
        setUpDB();
    }

    private void setUpDB() {
        dBhandler = new DBhandler(this, null, null, 1);

        cName = findViewById(R.id.name);
        cPhone = findViewById(R.id.phone);
        cPos = findViewById(R.id.position);
        nameTIL = findViewById(R.id.nameTIL);
        phoneTIL = findViewById(R.id.phoneTIL);
        posTIL = findViewById(R.id.posTIL);
        resume = findViewById(R.id.uploadResume);

        userImage = findViewById(R.id.photoInput);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoUri = uploadPic();
            }
        });

        uriString = "";
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadResume();
            }
        });

        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFields();
            }
        });

        proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameTIL.setErrorEnabled(false);
                phoneTIL.setErrorEnabled(false);
                posTIL.setErrorEnabled(false);

                cName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        nameTIL.setErrorEnabled(false);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });

                cPhone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        phoneTIL.setErrorEnabled(false);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });

                cPos.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        posTIL.setErrorEnabled(false);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });

                if (TextUtils.isEmpty(cName.getText())
                        || TextUtils.isEmpty(cPhone.getText())
                        || TextUtils.isEmpty(cPos.getText())) {
                    if (TextUtils.isEmpty(cName.getText())) {
                        nameTIL.setErrorEnabled(true);
                        nameTIL.setError("Name cannot be empty!");
                    }
                    if (TextUtils.isEmpty(cPhone.getText())) {
                        phoneTIL.setErrorEnabled(true);
                        phoneTIL.setError("Phone number cannot be empty!");
                    }
                    if (TextUtils.isEmpty(cPos.getText())) {
                        posTIL.setErrorEnabled(true);
                        posTIL.setError("Position cannot be empty!");
                    }
                } else {
                    Candidate candidate = new Candidate(
                            cName.getText().toString(),
                            cPhone.getText().toString(),
                            cPos.getText().toString(),
                            cStatus.getSelectedItem().toString(),
                            String.valueOf(photoUri),
                            uriString);
                    dBhandler.addCandidate(candidate);
                    Toast.makeText(NewCandidateActivity.this,
                            cName.getText().toString() + " added!", Toast.LENGTH_LONG).show();
                    resetFields();
                }
            }
        });
    }

    private void resetFields() {
        nameTIL.setErrorEnabled(false);
        phoneTIL.setErrorEnabled(false);
        posTIL.setErrorEnabled(false);
        nameTIL.setError(null);
        phoneTIL.setError(null);
        posTIL.setError(null);
        cName.setText("");
        cPhone.setText("");
        cPos.setText("");
        resume.setText(R.string.upload_resume);
        uriString = "";
        Picasso.with(this)
                .load(R.drawable.employee_tie)
                .into(userImage);
    }

    private Uri uploadPic() {
        PickSetup setup = new PickSetup()
                .setTitle("Pick your choice!")
                .setSystemDialog(true);

        PickImageDialog.build(setup)
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        Bitmap compressed = getResizedBitmap(r.getBitmap(), 1000);
                        photoUri = BitmapToUri(NewCandidateActivity.this, compressed);
                        Picasso.with(NewCandidateActivity.this)
                                .load(photoUri)
                                .error(R.drawable.employee_tie)
                                .into(userImage);
                    }
                })
                .setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {
                        Toast.makeText(getApplicationContext(),
                                "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).show(NewCandidateActivity.this);
        return photoUri;
    }

    private void uploadResume() {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            resumeUri = data.getData();
            if (resumeUri != null) {
                uriString = resumeUri.toString();
            }
            File file = new File(uriString);
            path = file.getAbsolutePath();
            String displayName = "";

            if (uriString.startsWith("content://")) {
                try (Cursor cursor = NewCandidateActivity.this.getContentResolver()
                        .query(resumeUri,
                                null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst())
                        displayName = "Selected pdf file is: " + cursor
                                .getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } else if (uriString.startsWith("file://")) {
                displayName = "Selected pdf file is: " + file.getName();
            }
            resume.setText(displayName);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
