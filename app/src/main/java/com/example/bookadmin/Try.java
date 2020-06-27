package com.example.bookadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class Try extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234 ;
    ImageView bookimg;
    Uri filePath;
    EditText title;
    EditText author,rating,likes,hates,publisher,no_of_page,isbn;
    EditText price;
    Button upload;
    Button choose;
    String url;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);

        bookimg = findViewById(R.id.img);
        title = findViewById(R.id.title1);
        likes = findViewById(R.id.likes);
        hates = findViewById(R.id.hates);
        publisher = findViewById(R.id.publisher);
        rating = findViewById(R.id.rating);
        author = findViewById(R.id.author);
        price = findViewById(R.id.price);
        upload = findViewById(R.id.upload);
        choose = findViewById(R.id.choose);
        no_of_page = findViewById(R.id.no_of_pages);
        isbn = findViewById(R.id.isbn);

        //storageReference = FirebaseStorage.getInstance().getReference("Book_Images");
       // databaseReference = FirebaseDatabase.getInstance().getReference("Book");


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an Image"),PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                bookimg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile(){


            final ProgressDialog progressDialog = new ProgressDialog(this);
            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference().child("Book Image").child(filePath.getLastPathSegment());

            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri urlImage = uriTask.getResult();
                            url = urlImage.toString();
                            uploadBook();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Try.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded" + ((int) progress) + "%...");
                        }
                    });
        }


   public void uploadBook(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Books Uploading...");
        progressDialog.show();

        BookData bookData = new BookData(
                title.getText().toString(),
                author.getText().toString(),
                price.getText().toString(),
                publisher.getText().toString(),
                hates.getText().toString(),
                likes.getText().toString(),
                rating.getText().toString(),
                no_of_page.getText().toString(),
                isbn.getText().toString(),
                url
        );

        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());
        FirebaseDatabase.getInstance().getReference("Books")
                .child(myCurrentDateTime).setValue(bookData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(Try.this,"Book Uploaded",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Try.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
}
