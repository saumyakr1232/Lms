package com.labstechnology.project1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.models.Assignment;
import com.labstechnology.project1.models.AssignmentResponse;
import com.labstechnology.project1.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import ru.katso.livebutton.LiveButton;
import xyz.hasnat.sweettoast.SweetToast;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class DialogUploadAssignment extends DialogFragment {
    private static final String TAG = "DialogUploadAssignment";
    private static final int FILE_SELECT_CODE = 0;
    private TextView textFileSize, textFileName, textDesc, textTitle;
    private LiveButton btnUpload;
    private CardView imageAddFile;
    private ProgressBar progressBar;
    private ImageView imgDocument;
    private String FileName, FileSize;
    private long sizeOfFile;

    private Uri documentUri;

    private StorageReference assignmentDocumentsRef;
    private DatabaseReference rttAssDatabaseRef, rttUserDatabaseRef;
    private String id;

    private Utils utils;

    private Assignment assignment;
    private User user;
    private boolean isSubmitted;

    public DialogUploadAssignment(Assignment assignment, boolean isSubmitted) {
        this.assignment = assignment;
        this.isSubmitted = isSubmitted;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_upload_assignment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);

        initViews(view);


        utils = new Utils(getContext());

        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("assignments");
        id = myRef.push().getKey();

        rttAssDatabaseRef = FirebaseDatabaseReference.DATABASE.getReference()
                .child(FirebaseConstants.ASSIGNMENTS).child(assignment.getId());

        assignmentDocumentsRef = FirebaseStorage.getInstance().getReference().child("assignmentDocumentsUsers")
                .child(Utils.getCurrentUid()).child(id + "$" + Utils.getCurrentUid());

        rttUserDatabaseRef = FirebaseDatabaseReference.DATABASE.getReference().child(FirebaseConstants.USERS).child(Utils.getCurrentUid());

        textTitle.setText(assignment.getTitle());

        imageAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressBar.getVisibility() == GONE) {
                    showFileChooser();
                } else {
                    SweetToast.error(getContext(), "Wait, Document is uploading");
                }

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentUri != null && !isSubmitted) {
                    uploadDocumentAndLinkToAssignment();
                } else if (isSubmitted) {
                    updateDocumentAndLinkToAssignment();
                } else {
                    SweetToast.error(getContext(), "No document selected");
                }

            }
        });

        return builder.create();
    }

    private void updateDocumentAndLinkToAssignment() {
        Log.d(TAG, "updateDocumentAndLinkToAssignment: called");

    }

    private void uploadDocumentAndLinkToAssignment() {
        Log.d(TAG, "uploadDocumentAndLinkToAssignment: called");
        textDesc.setText("Your Assignment is uploading");
        Log.d(TAG, "uploadDocumentAndLinkToAssignment: called");
        progressBar.setVisibility(View.VISIBLE);
        final StorageReference filePath = assignmentDocumentsRef.child(id + "upload");
        filePath.putFile(documentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    SweetToast.success(getContext(), "success");
                    Log.d(TAG, "onComplete: document update successful");
                    filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Log.d(TAG, "onComplete: HERE" + task.getResult());
                            Uri url = task.getResult();
                            assert url != null;
                            final String downloadUrl = url.toString();
                            Log.d(TAG, "onComplete: download url Document" + downloadUrl);
                            utils.getCurrentUser(new FireBaseCallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    User user = (User) object;
                                    HashMap<String, Object> uploadMap = new HashMap<>();


                                    Date date = new Date();
                                    long time = date.getTime();
//                                    Timestamp timestamp = new Timestamp(time);

                                    ArrayList<User> attemptedBy = assignment.getAttemptedBy();

                                    if (attemptedBy == null) {
                                        attemptedBy = new ArrayList<>();
                                        attemptedBy.add(user);
                                        uploadMap.put("attemptedBy", attemptedBy);
                                        Log.d(TAG, "onSuccess: we got first attempt on this assignment " + user.toString());
                                    } else {
                                        if (utils.isUserAttemptThisAssignment(assignment)) {
                                            attemptedBy.add(user);
                                            uploadMap.put("attemptedBy", attemptedBy);
                                            Log.d(TAG, "onSuccess: not first attempt on this assignment " + assignment.getAttemptedBy().size());
                                        }

                                    }

                                    AssignmentResponse assignmentResponse = new AssignmentResponse();
                                    assignmentResponse.setId(id + "$" + Utils.getCurrentUid());
                                    assignmentResponse.setuId(Utils.getCurrentUid());
                                    assignmentResponse.setAssignmentId(assignment.getId());
                                    assignmentResponse.setDocumentUri(downloadUrl);
                                    assignmentResponse.setTimestamp(time);

                                    //Gson gson = new Gson();
                                    ArrayList<AssignmentResponse> assignmentResponses = assignment.getResponses();
                                    if (assignmentResponses == null) {
                                        assignmentResponses = new ArrayList<>();
                                        assignmentResponses.add(assignmentResponse);

                                        Log.d(TAG, "onSuccess: first assignment response" + assignmentResponses.toString());
                                        uploadMap.put("responses", assignmentResponses);


                                    } else {
                                        assignmentResponses.add(assignmentResponse);
                                        uploadMap.put("responses", assignmentResponses);
                                        Log.d(TAG, "onSuccess: not first response " + assignmentResponse.toString());
                                    }

                                    rttAssDatabaseRef.updateChildren(uploadMap).addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        HashMap<String, String> attemptedAssignments = new HashMap<>();
                                                        attemptedAssignments.put(assignment.getId(), id);

                                                        HashMap<String, Object> updateUserMap = new HashMap<>();
                                                        updateUserMap.put("attemptedAssignments", attemptedAssignments);

                                                        rttUserDatabaseRef.updateChildren(updateUserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                textDesc.setText("File Uploaded");
                                                                textFileSize.setVisibility(GONE);
                                                                textFileName.setVisibility(View.VISIBLE);
                                                                progressBar.setVisibility(GONE);

                                                                dismiss();
                                                                Log.d(TAG, "onComplete: Document is linked to Assignment successful");
                                                                SweetToast.info(getContext(), "Document is linked to your Assignment successfully");
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                SweetToast.error(getContext(), "Unable to update your profile");
                                                            }
                                                        });

                                                    } else {
                                                        progressBar.setVisibility(GONE);
                                                        Log.d(TAG, "onComplete: Document link to Assignment is unsuccessful 😎");
                                                        SweetToast.error(getContext(), Objects.requireNonNull(task.getException()).getMessage());
                                                    }
                                                }
                                            }
                                    ).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            SweetToast.error(getContext(), "Unable to link Document to assignment, try again");
                                            progressBar.setVisibility(GONE);
                                        }
                                    });


                                }

                                @Override
                                public void onError(Object object) {
                                    try {
                                        DatabaseError error = (DatabaseError) object;
                                        Log.d(TAG, "onError: error" + error.getMessage());
                                    } catch (ClassCastException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }
                    });
                } else {
                    Log.d(TAG, "onComplete: error " + task.getException().getLocalizedMessage());
                    SweetToast.error(getContext(), "image upload unsuccessful 😞");

                }
            }
        });
    }

    private void showFileChooser() {
        Log.d(TAG, "showFileChooser: called");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");

        // Only pick openable and local files. Theoretically we could pull files from google drive
        // or other applications that have networked files, but that's unnecessary for this example.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            SweetToast.warning(getActivity(), "Please install a File Manager.", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: got the uri of document");
                    // Get the Uri of the selected file
                    documentUri = data.getData();
                    Log.d(TAG, "File Uri: " + documentUri.toString());
                    String documentPath = data.getType();
                    String mimeType = Objects.requireNonNull(getActivity()).getContentResolver().getType(documentUri);
                    Log.d(TAG, "onActivityResult: mimeType" + mimeType);

                    Cursor returnCursor =
                            getActivity().getContentResolver().query(documentUri, null, null, null, null);
                    /*
                     * Get the column indexes of the data in the Cursor,
                     * move to the first row in the Cursor, get the data,
                     * and display it.
                     */
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    FileName = returnCursor.getString(nameIndex);
                    FileSize = Long.toString(returnCursor.getLong(sizeIndex));
                    sizeOfFile = returnCursor.getLong(sizeIndex);
                    Log.d(TAG, "onActivityResult: Name " + returnCursor.getString(nameIndex));
                    Log.d(TAG, "onActivityResult: Size" + Long.toString(returnCursor.getLong(sizeIndex)));

                    setDocumentCard();

                }
                break;
            default:
                SweetToast.warning(getActivity(), "you did'nt select any file");
                break;
        }
    }

    private void setDocumentCard() {
        Log.d(TAG, "setDocumentCard: called");
        if (sizeOfFile < 999999) {
            FileSize = String.valueOf(sizeOfFile / 1000) + " KB";
        } else if (sizeOfFile > 999999) {
            FileSize = String.valueOf(sizeOfFile / 1000000) + " MB";
        }
        textFileSize.setText(FileSize);
        textFileName.setText(FileName);
        //Load thumbnail of a specific media item.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            try {
                Bitmap thumbnail =
                        Objects.requireNonNull(getActivity()).getApplicationContext().getContentResolver().loadThumbnail(
                                documentUri, new Size(640, 480), null);

                try {
                    textDesc.setText("You are all set, Click Upload");
                    imgDocument.setImageBitmap(thumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: called");
        textFileName = (TextView) view.findViewById(R.id.textFileName);
        textFileSize = (TextView) view.findViewById(R.id.textFileSize);
        textDesc = (TextView) view.findViewById(R.id.txtAddHere);
        btnUpload = (LiveButton) view.findViewById(R.id.btnUpload);
        imageAddFile = (CardView) view.findViewById(R.id.imageAddFile);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarFile);
        imgDocument = (ImageView) view.findViewById(R.id.imgDocument);
        textTitle = (TextView) view.findViewById(R.id.textTitle);
    }
}
