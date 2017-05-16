package com.example.asus.finalproject;

/**
 * Created by ASUS on 25.04.2017.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ASUS on 25.04.2017.
 */

public class ProfileTab3Upload extends Fragment {

    private Button btnUpload;
    private ImageButton imgBtnGallery;
    private EditText etProductTitle;
    private EditText etProductDescription;
    private Spinner spinner;


    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private DatabaseReference mDatabasePosts;
    private DatabaseReference mDatabaseElectronicPosts;
    private DatabaseReference mDatabaseGearPosts;
    private DatabaseReference mDatabaseOtherPosts;
    private DatabaseReference mDatabaseUserPosts;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private Uri imageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private ProgressDialog mProgress;
    private String postCategory;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_tab3_upload, container, false);


        btnUpload = (Button) rootView.findViewById(R.id.profile_upload_btnUpload);
        imgBtnGallery = (ImageButton) rootView.findViewById(R.id.profile_upload_imgBtnGallery);
        etProductTitle = (EditText) rootView.findViewById(R.id.profile_upload_etTitle);
        etProductDescription = (EditText) rootView.findViewById(R.id.profile_upload_etDescription);
        mProgress = new ProgressDialog(getActivity());
        spinner = (Spinner) rootView.findViewById(R.id.spinner);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = mDatabase.child("User").child(mCurrentUser.getUid());

        mDatabasePosts = mDatabase.child("Posts");
        mDatabaseElectronicPosts = mDatabase.child("ElectronicPosts");
        mDatabaseGearPosts = mDatabase.child("GearPosts");
        mDatabaseOtherPosts = mDatabase.child("OtherPosts");
        mDatabaseUserPosts = mDatabase.child("UserPosts");


        imgBtnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleyIntent = new Intent(Intent.ACTION_GET_CONTENT); // galeri ekranı için yeni bir intent oluşturuyoruz
                galleyIntent.setType("image/+");                            // // TODO: 24.02.2017 ACTION_GET_CONTENT ve bunun gibi birçok intent yardımcı komutunu araştır!!!
                startActivityForResult(galleyIntent, GALLERY_REQUEST);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        postCategory = "electronics";
                        break;
                    case 1:
                        postCategory = "gear";
                        break;
                    case 2:
                        postCategory = "other";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;

    }

    private void startPosting() {
        mProgress.setMessage("Dosya Yükleniyor ..."); // dialog ile birlikte çıkan yazı
        mProgress.show(); // dialog dönen daire şeklinde görünmeye başladı işlemle beraber

        final String title_val = etProductTitle.getText().toString().trim(); // edittext ten değerler alınıyor.
        final String desc_val = etProductDescription.getText().toString().trim();// edittext ten değerler alınıyor.

        if(!TextUtils.isEmpty(title_val)&& !TextUtils.isEmpty(desc_val) && imageUri!=null){ // eğer başlık, açıklama ve foto url si boş değilse
            // yükleme yapar, yoksa yapmaz
            StorageReference filepath = mStorage.child("Posts_Images").child(imageUri.getLastPathSegment());// Firebase Storage içinde
            // Blog_Images klasörü oluşturulup içine galerinden alınan foto basılıyor

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() { //dosyanın veritabanına yüklenirkenki
                @Override           // durumunu dinleyen bir dinleyici
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl(); //firebase içinde tutulduğu yerin url sini alıyoruz

                    final DatabaseReference newUserPost = mDatabaseUserPosts.child(mAuth.getCurrentUser().getUid()).push();


                    //final String post_key = newUserPost.getRoot().getKey();
                    //final DatabaseReference newPostOthers = mDatabaseOtherPosts.child(String.valueOf(mDatabaseUserPosts.child(mAuth.getCurrentUser().getUid()).push()));

                    final DatabaseReference newPostOthers = mDatabaseOtherPosts.child(newUserPost.getRef().getKey());
                    //final DatabaseReference newPostOthers = mDatabaseOtherPosts.push();
                    final DatabaseReference newPostGear = mDatabaseGearPosts.child(newUserPost.getRef().getKey());
                    //final DatabaseReference newPostGear = mDatabaseGearPosts.push();
                    final DatabaseReference newPostElectronics = mDatabaseElectronicPosts.child(newUserPost.getRef().getKey());
                    //final DatabaseReference newPostElectronics = mDatabaseElectronicPosts.push();
                    final DatabaseReference newPostPosts = mDatabasePosts.child(newUserPost.getRef().getKey());
                    //final DatabaseReference newPostPosts = mDatabasePosts.push();//generates unique id yeni bir database ref oluşturduk ve buna otomatik

                    // olarak oluşturulan bir eşsiz anahtar ürettik
                    // otomatik anahtar atadığığımız düğümün altına sırayısla değerleri gönderiyoruz

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String nameSurname = dataSnapshot.child("firstName").getValue() +" "+ dataSnapshot.child("surName").getValue();
                        //--------------------Kategoriye Göre Gönderi Girilmesi
                            switch (postCategory){
                                case "electronics":
                                    newPostElectronics.child("category").setValue(postCategory);
                                    newPostElectronics.child("title").setValue(title_val); // title düğümü değerini title_val olarak aldık
                                    newPostElectronics.child("description").setValue(desc_val);//  benzer işlemler
                                    newPostElectronics.child("image").setValue(downloadUrl.toString()); // storage url sini database tarafına bastk
                                    newPostElectronics.child("uid").setValue(mCurrentUser.getUid());
                                    newPostElectronics.child("username").setValue(nameSurname);
                                    break;
                                case "gear":
                                    newPostGear.child("category").setValue(postCategory);
                                    newPostGear.child("title").setValue(title_val); // title düğümü değerini title_val olarak aldık
                                    newPostGear.child("description").setValue(desc_val);//  benzer işlemler
                                    newPostGear.child("image").setValue(downloadUrl.toString()); // storage url sini database tarafına bastk
                                    newPostGear.child("uid").setValue(mCurrentUser.getUid());
                                    newPostGear.child("username").setValue(nameSurname);
                                    break;
                                case "other":
                                    newPostOthers.child("category").setValue(postCategory);
                                    newPostOthers.child("title").setValue(title_val); // title düğümü değerini title_val olarak aldık
                                    newPostOthers.child("description").setValue(desc_val);//  benzer işlemler
                                    newPostOthers.child("image").setValue(downloadUrl.toString()); // storage url sini database tarafına bastk
                                    newPostOthers.child("uid").setValue(mCurrentUser.getUid());
                                    newPostOthers.child("username").setValue(nameSurname);
                                    break;
                                default:
                                    break;


                            }

                            //----------------------Kullanıcının Gönderileri--------------------------------------
                            newUserPost.child("category").setValue(postCategory);
                            newUserPost.child("title").setValue(title_val); // title düğümü değerini title_val olarak aldık
                            newUserPost.child("description").setValue(desc_val);//  benzer işlemler
                            newUserPost.child("image").setValue(downloadUrl.toString()); // storage url sini database tarafına bastk
                            newUserPost.child("uid").setValue(mCurrentUser.getUid());
                            newUserPost.child("username").setValue(nameSurname);


                            //-------------Genel Gönderiler--------------------------------------------------------------
                            newPostPosts.child("category").setValue(postCategory);
                            //newPost.child("date").setValue(date);
                            newPostPosts.child("title").setValue(title_val); // title düğümü değerini title_val olarak aldık
                            newPostPosts.child("description").setValue(desc_val);//  benzer işlemler
                            newPostPosts.child("image").setValue(downloadUrl.toString()); // storage url sini database tarafına bastk
                            newPostPosts.child("uid").setValue(mCurrentUser.getUid());
                            newPostPosts.child("username").setValue(nameSurname)// dataSnapshot refere edilen obje içindeki bütün
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {//verileri döndürürür. Bu yüzden bunun üzerinden sorgulama yaptık.
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getActivity(), "Dosya yüklendi !", Toast.LENGTH_LONG).show();
                                                etProductTitle.getText().clear();
                                                etProductDescription.getText().clear();
                                                imgBtnGallery.setImageResource(R.drawable.ic_add_a_photo_black_48px);
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mProgress.dismiss(); // progressDialog sona erdi işlemle beraber

                }
            });
        }// end of the if statement
    } //end of startPosting

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){ // galeri kodu 1 ve sonuç kodu onaylandıysa aç parantez işlem yap
            imageUri = data.getData(); // intent sınıfından ürettiğimiz data'nın değerini değişkene atadık
            imgBtnGallery.setImageURI(imageUri);  // ekrandaki image buttonuna resmi bas
        }
    }

}

/* Calendar cal = Calendar.getInstance();
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH);
                            int day = cal.get(Calendar.DAY_OF_MONTH);*/

//String date = day+"."+month+"."+year;