package com.exnovation.wishster.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exnovation.wishster.Models.LoginModel;
import com.exnovation.wishster.Models.ProfileModel;
import com.exnovation.wishster.Models.ReviewQuestion;
import com.exnovation.wishster.Models.SignUpModel;
import com.exnovation.wishster.Models.UpdateProfileImgModel;
import com.exnovation.wishster.Models.ValueSet;
import com.exnovation.wishster.Network.ApiManager;
import com.exnovation.wishster.Network.Constants;
import com.exnovation.wishster.R;
import com.exnovation.wishster.Utilities.App;
import com.exnovation.wishster.Utilities.CommonUtils;
import com.exnovation.wishster.Utilities.Loader;
import com.exnovation.wishster.Utilities.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class Profile extends AppCompatActivity {
    TextView tv_update;
    RadioButton m,f,t;
    Call<ProfileModel> coll=null;
    Call<SignUpModel> updateProfile = null;
    ApiManager apiManager=new ApiManager();
    static Loader loader;
    DatePickerDialog datepicker;
    EditText name,user_name,email,mobile_no;
    TextView dob;
    LinearLayout ll_submit,cont1;
    ImageView iv_prf_edit,iv_img_edit, back;
    private static final int REQUEST_GALLERY_CODE = 200;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;
    private Uri uri;
    static  String filePath="";
    MultipartBody.Part fileToUpload;
    CircleImageView profile;
    private String Document_img1="";
    RequestBody bodyRequest;
    JSONObject updateUser=new JSONObject();
    Call<LoginModel> fileUpload = null;
    Prefs prefs;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;

    private static final int PICK_IMAGE_REQUEST = 125;
    private File pickedImage=null;
    private String realPath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loader=new Loader();
        prefs = new Prefs(this);
        pickedImage = new File(getApplicationContext().getFilesDir(), Constants.URL);

        (findViewById(R.id.btn_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });

        init();
        action();
        Log.d("tTokenPrint",""+ App.token);
    }

    private static MultipartBody.Part prepareFilePart(String partName, File file) {
        if (file == null)
            return null;
        else {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            return MultipartBody.Part.createFormData(partName, file.getAbsolutePath(), requestFile);
        }
    }

    private void init() {
        tv_update=(TextView)findViewById(R.id.tv_update);
        profile=findViewById(R.id.profile);
        ll_submit=findViewById(R.id.ll_submit);
        cont1=findViewById(R.id.cont1);
        m=(RadioButton)findViewById(R.id.radioMale);
        f=(RadioButton)findViewById(R.id.radiofeMale);
        t=(RadioButton)findViewById(R.id.radiotrans);
        name=(EditText) findViewById(R.id.name);
        user_name=(EditText) findViewById(R.id.user_name);
        email=(EditText) findViewById(R.id.email);
        mobile_no=(EditText) findViewById(R.id.mobile_no);
        dob=(TextView) findViewById(R.id.dob);
        iv_prf_edit= findViewById(R.id.iv_prf_edit);
        iv_img_edit= findViewById(R.id.iv_img_edit);
        back= findViewById(R.id.back_iv);
    }

    private void action() {
        if (tv_update!=null)
        tv_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    updateUser.put("name",name.getText().toString());
                    updateUser.put("email",email.getText().toString());
                    updateUser.put("mobile",mobile_no.getText().toString());
                    updateUser.put("username",user_name.getText().toString());
                    updateUser.put("address","");
                    updateUser.put("birthday",dob.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                UpdateProfile();
            }
        });

        m.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                m.setChecked(true);
                f.setChecked(false);
                t.setChecked(false);
                try {
                    updateUser.put("gender",m.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        f.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                m.setChecked(false);
                f.setChecked(true);
                t.setChecked(false);
                try {
                    updateUser.put("gender",f.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                m.setChecked(false);
                f.setChecked(false);
                t.setChecked(true);
                try {
                    updateUser.put("gender",t.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        getProfileDtaFromServer();
        dob.setEnabled(false);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(Profile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = String.format("%02d" , (monthOfYear + 1));
                                String day = String.format("%02d" , dayOfMonth);
                                dob.setText( month +"-"+day+ "-" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });
        iv_prf_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewStatus(true);
                ll_submit.setVisibility(View.VISIBLE);
                iv_prf_edit.setVisibility(View.GONE);

            }
        });
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewStatus(false);
                ll_submit.setVisibility(View.GONE);
                iv_prf_edit.setVisibility(View.VISIBLE);
            }
        });
        /*cont1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                new ValueSet(Profile.this);
            }
        });*/
        iv_img_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectImage();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))
                {

                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {


                final Bitmap photo = (Bitmap) data.getExtras().get("data");
                File file = savebitmap(photo);

                MultipartBody.Part filePart = MultipartBody.Part.createFormData("profile_img", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                loader.showDialog(this);
                apiManager.service.UpdatePrfImage(filePart).enqueue(new Callback<UpdateProfileImgModel>() {
                    @Override
                    public void onResponse(Call<UpdateProfileImgModel> call, Response<UpdateProfileImgModel> response) {
                        if (response.isSuccessful()){
                            loader.hideDialog();
                            if (response.body().status){
                                Toast.makeText(Profile.this, response.body().message, Toast.LENGTH_SHORT).show();

                                if(response.body().data.profile_img != null) {

                                    Glide.with(Profile.this).load(response.body().data.profile_img)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true).into(profile);
                                    prefs.saveData("prf_pic", response.body().data.profile_img);

                                }
                            }else {
                                Toast.makeText(Profile.this, response.body().message, Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            loader.hideDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileImgModel> call, Throwable t) {
                        loader.hideDialog();
                        Toast.makeText(Profile.this, "No Internet!", Toast.LENGTH_SHORT).show();

                    }
                });



            } else if (requestCode == 2) {

                if (Build.VERSION.SDK_INT < 11) {
                    realPath = RealPathUtils.getRealPathFromURI_BelowAPI11(Profile.this, data.getData());
                   // upload_file.setHint("Image selected");
                    uploadImg(realPath);
                }

                // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19) {
                    realPath = RealPathUtils.getRealPathFromURI_API11to18(Profile.this, data.getData());
                    //upload_file.setHint("Image selected");
                    uploadImg(realPath);
                }

                // SDK > 19 (Android 4.4)
                else {
                    realPath = RealPathUtils.getRealPathFromURI_API19(Profile.this, data.getData());
                    //upload_file.setHint("Image selected");
                    uploadImg(realPath);
                }
                System.out.println("Image Path : " + realPath);

            }
        }

    }

    private File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");

        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public static class RealPathUtils {

        public static String getRealPath(Context context, Uri fileUri) {
            String realPath;
            // SDK >= 11 && SDK < 19
            if (Build.VERSION.SDK_INT < 19) {
                realPath = getRealPathFromURI_API11to18(context, fileUri);
            }
            // SDK > 19 (Android 4.4) and up
            else {
                realPath = getRealPathFromURI_API19(context, fileUri);
            }
            return realPath;
        }

        public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
            String[] proj = {MediaStore.MediaColumns.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
                cursor.close();
            }
            return result;
        }

        public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        /**
         * Get a file path from a Uri. This will get the the path for Storage Access
         * Framework Documents, as well as the _data field for the MediaStore and
         * other file-based ContentProviders.
         *
         * @param context The context.
         * @param uri     The Uri to query.
         */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public static String getRealPathFromURI_API19(final Context context, final Uri uri) {
            // DocumentProvider
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    // non-primary
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);

                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "");
                        }
                        try {
                            final Uri contentUri = ContentUris.withAppendedId(
                                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                            return getDataColumn(context, contentUri, null, null);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        private static String getDataColumn(Context context, Uri uri, String selection,
                                            String[] selectionArgs) {

            Cursor cursor = null;
            final String column = MediaStore.MediaColumns.DATA;
            final String[] projection = {column};

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        private static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        private static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        private static boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }
    }


    /*private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }*/


    private void uploadImg(String result) {
        loader.showDialog(Profile.this);
        //MultipartBody.Part imageBody = prepareFilePart("profile_img", pickedImage);
        MultipartBody.Part part = null;
        try {
            File file = new File(result);
            // Create a request body with file and image media type
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("*/*"), file);
            // Create MultipartBody.Part using file request-body,file name and part name
            part = MultipartBody.Part.createFormData("profile_img", file.getName(), fileReqBody);
        }catch (Exception e){
            e.printStackTrace();
        }
        apiManager.service.UpdatePrfImage(part).enqueue(new Callback<UpdateProfileImgModel>() {
            @Override
            public void onResponse(Call<UpdateProfileImgModel> call, Response<UpdateProfileImgModel> response) {
                if (response.isSuccessful()){
                    loader.hideDialog();
                    if (response.body().status){
                        Toast.makeText(Profile.this, response.body().message, Toast.LENGTH_SHORT).show();
                        if(response.body().data.profile_img != null) {

                            Glide.with(Profile.this).load(response.body().data.profile_img)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).into(profile);
                            prefs.saveData("prf_pic", response.body().data.profile_img);
                        }
                    }else {
                        Toast.makeText(Profile.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }

                }else{
                    loader.hideDialog();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileImgModel> call, Throwable t) {
                loader.hideDialog();
                Toast.makeText(Profile.this, "No Internet!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getProfileDtaFromServer()
    {
        loader.showDialog(this);
        coll=apiManager.service.getProfile();
        coll.enqueue(new Callback<ProfileModel>()
        {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {

                loader.hideDialog();
                if (response.body().isStatus())
                {

                   new  ValueSet(name,response.body().getUserData().getName());
                   new  ValueSet(user_name,chkUserName(response.body().getUserData().getUsername()));
                   new  ValueSet(email,response.body().getUserData().getEmail());
                   new  ValueSet(mobile_no,response.body().getUserData().getMobile());
                   new  ValueSet(dob,response.body().getUserData().getBirthday());
                   new  ValueSet(m,f,t,response.body().getUserData().getGender());

                   if(response.body().getUserData().getProfile_img() != null) {

                       prefs.saveData("prf_pic", response.body().getUserData().getProfile_img());
                       Log.d("AAA", response.body().getUserData().getProfile_img());

                       Glide.with(Profile.this).load(response.body().getUserData().getProfile_img())
                               .diskCacheStrategy(DiskCacheStrategy.NONE)
                               .skipMemoryCache(true).into(profile);
                   }


                }
                else
                    Toast.makeText(Profile.this, ""+response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t)
            {
                Log.d("errMsg",""+t.getMessage());
            }
        });

    }

    private String chkUserName(String user_name) {
        String s="";
        if (user_name.length()>18)
        {
            s=user_name.substring(0,18)+"...";

        }
        else
            return user_name;
        return s;
    }

    void setViewStatus(Boolean bool){
        name.setEnabled(bool);
        user_name.setEnabled(bool);
        email.setEnabled(bool);
        mobile_no.setEnabled(bool);
        dob.setEnabled(bool);
        m.setEnabled(bool);
        f.setEnabled(bool);
        t.setEnabled(bool);
    }
    private void UpdateProfile()
    {
        Log.d("dataAbcd",""+updateUser.toString());
        loader=new Loader();
        loader.showDialog(this);
        bodyRequest = RequestBody.create(MediaType.parse("application/json"), updateUser.toString());
        updateProfile=apiManager.service.Update_profile(bodyRequest);
        updateProfile.enqueue(new Callback<SignUpModel>()
        {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response)
            {


                loader.hideDialog();
                if (response.body().isStatus())
                {
                    Toast.makeText(Profile.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();

                }
                }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t)
            {
                loader.hideDialog();
                Log.d("errMsg",""+t.getMessage());
            }
        });

    }


}
