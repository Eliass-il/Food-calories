package com.example.finy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;

import com.example.finy.ml.Model2;


public class MainActivity extends AppCompatActivity {

    Button camera, gallery;
    ImageView imageView;
    TextView result, textView2;
    EditText input;

    int imageSize = 224;
    private String strResult;
    public static final String FOOD_CALORIES = "Food_calories";
    public static final String FOOD_NAME = "Food name";
    public static final String CALORIES = "Calories";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        textView2 = findViewById(R.id.result2);

        File dbFile = getDatabasePath("Food_calories.db");

        if (!dbFile.exists()) {
            try {
                copyDatabaseFromAssets();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }

    public void classifyImage(Bitmap image){
        try {
            Model2 model = Model2.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model2.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {
                    "Apple pie",
                    "Baby back ribs",
                    "Baklava",
                    "Beef carpaccio",
                    "Beef tartare",
                    "Beet salad",
                    "Beignets",
                    "Bibimbap",
                    "Bread pudding",
                    "Breakfast burrito",
                    "Bruschetta",
                    "Caesar salad",
                    "Cannoli",
                    "Caprese salad",
                    "Carrot cake",
                    "Ceviche",
                    "Cheese plate",
                    "Cheesecake",
                    "Chicken curry",
                    "Chicken quesadilla",
                    "Chicken wings",
                    "Chocolate cake",
                    "Chocolate mousse",
                    "Churros",
                    "Clam chowder",
                    "Club sandwich",
                    "Crab cakes",
                    "Creme brulee",
                    "Croque madame",
                    "Cup cakes",
                    "Deviled eggs",
                    "Donuts",
                    "Dumplings",
                    "Edamame",
                    "Eggs benedict",
                    "Escargots",
                    "Falafel",
                    "Filet mignon",
                    "Fish and chips",
                    "Foie gras",
                    "French fries",
                    "French onion soup",
                    "French toast",
                    "Fried calamari",
                    "Fried rice",
                    "Frozen yogurt",
                    "Garlic bread",
                    "Gnocchi",
                    "Greek salad",
                    "Grilled cheese sandwich",
                    "Grilled salmon",
                    "Guacamole",
                    "Gyoza",
                    "Hamburger",
                    "Hot and sour soup",
                    "Hot dog",
                    "Huevos rancheros",
                    "Hummus",
                    "Ice cream",
                    "Lasagna",
                    "Lobster bisque",
                    "Lobster roll sandwich",
                    "Macaroni and cheese",
                    "Macarons",
                    "Miso soup",
                    "Mussels",
                    "Nachos",
                    "Omelette",
                    "Onion rings",
                    "Oysters",
                    "Pad thai",
                    "Paella",
                    "Pancakes",
                    "Panna cotta",
                    "Peking duck",
                    "Pho",
                    "Pizza",
                    "Pork chop",
                    "Poutine",
                    "Prime rib",
                    "Pulled pork sandwich",
                    "Ramen",
                    "Ravioli",
                    "Red velvet cake",
                    "Risotto",
                    "Samosa",
                    "Sashimi",
                    "Scallops",
                    "Seaweed salad",
                    "Shrimp and grits",
                    "Spaghetti bolognese",
                    "Spaghetti carbonara",
                    "Spring rolls",
                    "Steak",
                    "Strawberry shortcake",
                    "Sushi",
                    "Tacos",
                    "Takoyaki",
                    "Tiramisu",
                    "Tuna tartare",
                    "Waffles"
            };
            strResult = classes[maxPos];
            result.setText(strResult);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String databasePath = "/data/data/com.example.finy/databases/Food_calories.db";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT Calories FROM Food_calories WHERE \"" + FOOD_NAME + "\" = ?";
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);

                String[] selectionArgs = new String[]{strResult};
                Cursor cursor = db.rawQuery(query, selectionArgs);
                if (cursor.moveToFirst()) {
                    int caloriesColumnIndex = cursor.getColumnIndexOrThrow("Calories");
                    String calories = cursor.getString(caloriesColumnIndex);
                    // Use the calories value as needed
                    textView2.setText(calories);
                }
                cursor.close();
                db.close();

            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
                input = findViewById(R.id.input);
                String str = input.getText().toString();
                String[] selectionArgs = new String[]{strResult};
                Cursor cursor = db.rawQuery(query, selectionArgs);
                if (cursor.moveToFirst()) {
                    int caloriesColumnIndex = cursor.getColumnIndexOrThrow("Calories");
                    String calories = cursor.getString(caloriesColumnIndex);
                    // Use the calories value as needed
                    int numer = Integer.parseInt(calories) * Integer.parseInt(str)/100;
                    String result = Integer.toString(numer);
                    textView2.setText(result);
                }
                cursor.close();
                db.close();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void copyDatabaseFromAssets() throws IOException {
        InputStream inputStream = getAssets().open("Food_calories.db");
        OutputStream outputStream = Files.newOutputStream(getDatabasePath("Food_calories.db").toPath());

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}